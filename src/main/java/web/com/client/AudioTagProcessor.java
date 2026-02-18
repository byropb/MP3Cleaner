package web.com.client;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AudioTagProcessor {

	private static final Logger logger = LoggerFactory.getLogger(AudioTagProcessor.class);
	private String mp3Context = "";

	// function used by Ajax call @GetMapping("/api/data_attr")
	public ArrayList<String> getAttribuesValue(String dirToRead) throws Exception {

		ArrayList<String> retval = new ArrayList<String>();
		File fileDir = new File(dirToRead);
		if (!fileDir.exists()) {
			logger.error("directory does not exist! - ".concat(dirToRead));
			// return retval;
		}
		File[] filesList = fileDir.listFiles();
		String audioTagData = "";
		for (int i = 0; i < filesList.length; i++) {
			if (!filesList[i].isDirectory()) {
				File file = filesList[i];
				String fileName = file.getName();
				int lastIndex = fileName.lastIndexOf(".");

				if (-1 < lastIndex) {
					String extention = fileName.substring(lastIndex);
					if (".mp3".equalsIgnoreCase(extention)) {
						AudioFile audioFile = AudioFileIO.read(file);
						Tag audioTag = audioFile.getTag();
						retval.add("\"path=".concat(dirToRead).concat("\""));
						audioTagData = this.nullToBlank((audioTag.getFirst(FieldKey.valueOf("ALBUM"))));
						retval.add("\"bookName=".concat(audioTagData).concat("\""));
						audioTagData = this.nullToBlank((audioTag.getFirst(FieldKey.valueOf("TITLE"))));
						retval.add("\"title=".concat(audioTagData).concat("\""));
						audioTagData = this.nullToBlank((audioTag.getFirst(FieldKey.valueOf("ARTIST"))));
						retval.add("\"author=".concat(audioTagData).concat("\""));
						audioTagData = this.nullToBlank((audioTag.getFirst(FieldKey.valueOf("ALBUM_ARTIST"))));
						retval.add("\"reader=".concat(audioTagData).concat("\""));
						String year = audioTag.getFirst(FieldKey.valueOf("YEAR"));
						if (3 < year.length()) {
							year = year.substring(0, 4);
						}
						retval.add("\"year=".concat(year).concat("\""));
						break; // read only single file's attributes
					}
				}

			}
		}
		return retval;
	}

	public StringBuffer listMP3Dirtory(ArrayList<HashMap<String, String>> directory, ArrayList<String> fieldsList,
			String mp3Context, String filetype) throws Exception {

		StringBuffer buffer = new StringBuffer("<table class='data_table' id='data_table' >\n");
		if (directory.isEmpty()) {
			buffer.append("<tr><td class='req'>Directory does not exist!</td></tr></table>");
			return buffer;
		}
		this.mp3Context = mp3Context;
		int icount = directory.size();
		int ifiles = 0;
		int ifieldsCount = fieldsList.size();
		HashMap<String, String> map = directory.get(0);
		// sortTable
		buffer.append("<tr>");

		for (int n = 0; n < ifieldsCount; n++) {
			String[] header = fieldsList.get(n).split("=");
			buffer.append("<th class='data_header' ").append(this.parseSortClick("data_table", header[1], n))
					.append(" >").append(header[1]).append("</th>");

		}
		buffer.append("</tr>\n");

		for (int i = 0; i < icount; i++) {
			map = directory.get(i);
			try {
				File file = new File(map.get("path"));
				String fileName = file.getName();
				int lastIndex = fileName.lastIndexOf(".");

				if (-1 < lastIndex) {
					String extention = fileName.substring(lastIndex);
					if (filetype.equalsIgnoreCase(extention) || "all".equalsIgnoreCase(filetype)) {
						ifiles++;
						buffer.append("<tr class='data_row' title='click to play'  onclick='get_download(\"");
						buffer.append(map.get("name")).append("\");' ");
						buffer.append(">\n");

						if (".mp3".equalsIgnoreCase(extention)) {
							// mp3 file's attributes
							buffer.append(this.getMP3data(file, ifiles, fieldsList));

						} else {

							// standard file's attribute.
							for (int n = 0; n < ifieldsCount; n++) {

								String[] header = fieldsList.get(n).split("=");
								switch (header[0]) {
								case "#":
									buffer.append(this.parsHTMLData(String.valueOf(icount)));
									break;
								case "NAME":
									buffer.append(this.parsHTMLData(fileName));
									break;
								case "FILE_SIZE":
									String fileLength = String.valueOf(String.format("%,d", file.length() / 1024))
											.concat(" KB");
									buffer.append(this.parsHTMLData(fileLength));
									break;
								case "LAST_MODIFIED":
									String lastModified = String.valueOf(
											FileProcessor.unixTimeToDate(file.lastModified(), "yyyy-MM-dd HH:mm"));
									buffer.append(this.parsHTMLData(lastModified));
									break;
								case "AUDIO_LENGTH": // reserved
									buffer.append(this.parsHTMLData(""));
									break;
								case "YEAR": // reserved
									buffer.append(this.parsHTMLData(""));
									break;

								default:
									buffer.append(this.parsHTMLData(""));
								}
							}
						}
						buffer.append("</tr>\n");
					}
				} /// ---continue
			} catch (Exception e) {
				logger.error(e.toString());
			}
		}
		if (1 > ifiles) {
			buffer.append("<tr><td class='req' colspan='4'>No <b>").append(this.nullToBlank(filetype).toUpperCase())
					.append("</b> files found in current directory!</td>");
			buffer.append("</tr>\n");
		}
		buffer.append("</table>\n");

		return buffer;
	}

	private StringBuffer getMP3data(File file, int icount, ArrayList<String> fieldsList) throws Exception {

		StringBuffer buffer = new StringBuffer("");
		Tag audioTag = null;
		AudioFile audioFile = null;

		int ifieldsCount = fieldsList.size();
		try {
			audioFile = AudioFileIO.read(file);
			if (null != audioFile) {
				audioTag = audioFile.getTag();
			} else {
				logger.error(file.getName().concat(" -> audioFile is null;"));
				buffer.append(this.parsHTMLData(file.getName()));
				buffer.append(this.parsHTMLData("audioFile is null!"));
				return buffer;
			}
			if (null == audioTag) {
				logger.error(file.getName().concat(" -> audioTag is null;"));
				buffer.append(this.parsHTMLData(file.getName()));
				buffer.append(this.parsHTMLData("no audioTags found!"));
				return buffer;
			}
			for (int n = 0; n < ifieldsCount; n++) {

				String[] header = fieldsList.get(n).split("=");
				switch (header[0]) {
				case "#":
					buffer.append(this.parsHTMLData(String.valueOf(icount)));
					// --
					break;
				case "TRACK":
					String audioTrackId = (audioTag.getFirst(FieldKey.valueOf(header[0])));
					buffer.append(this.parsHTMLData(audioTrackId));

					break;
				case "NAME":
					buffer.append(this.parsHTMLData(file.getName()));
					// buffer.append(this.parsLinkData(file.getName()));
					break;
				case "FILE_SIZE":
					String fileLength = String.valueOf(String.format("%,d", file.length() / 1024)).concat(" KB");
					buffer.append(this.parsHTMLData(fileLength));
					break;

				case "LAST_MODIFIED":
					String lastModified = String
							.valueOf(FileProcessor.unixTimeToDate(file.lastModified(), "yyyy-MM-dd HH:mm"));
					buffer.append(this.parsHTMLData(lastModified));
					break;
				case "AUDIO_LENGTH":

					String audioLength = this.formatAudioLength(audioFile);
					buffer.append(this.parsHTMLData(audioLength));
					break;

				case "YEAR":
					String year = audioTag.getFirst(FieldKey.valueOf(header[0]));
					if (3 < year.length()) {
						year = year.substring(0, 4);
					}
					buffer.append(this.parsHTMLData(year));
					break;

				default:
					if (null != audioTag) {
						String audioTagData = (audioTag.getFirst(FieldKey.valueOf(header[0])));
						buffer.append(this.parsHTMLData(audioTagData));

					}
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return buffer;
	}

	public String parsHTMLData(String data) {

		StringBuffer buffer = new StringBuffer("");
		buffer.append("<td class=''>");
		buffer.append(this.nullToBlank(data));
		buffer.append("</td>");

		return buffer.toString();
	}

	public String parsLinkData(String data) {
// use to create <a href=> for click to download
		StringBuffer buffer = new StringBuffer("");
		buffer.append("<td class=''>");
		buffer.append("<a href='").append(this.mp3Context);
		buffer.append("/download/");
		buffer.append(this.nullToBlank(data));
		buffer.append("' >");
		buffer.append(this.nullToBlank(data));
		buffer.append("</a>");
		buffer.append("</td>");

		return buffer.toString();
	}

	private String parseSortClick(String tableName, String columnName, int columnId) {

		StringBuffer buffer = new StringBuffer(""); // tableName, columnId
		buffer.append(" title ='sort by ").append(columnName).append("' ");
		buffer.append("onclick =\"sortTable('").append(tableName).append("',").append(columnId).append("); \" ");

		return buffer.toString();

	}

	public ArrayList<String> cleanAttributesMP3Directory(String dirToRead, HashMap<String, String> updateFields)
			throws Exception {

		ArrayList<String> retval = new ArrayList<String>();
		File fileDir = new File(dirToRead);
		if (!fileDir.exists()) {
			logger.error("directory does not exist! - ".concat(dirToRead));
			// return retval;
		}
		File[] filesList = fileDir.listFiles();
		// String audioTagData = "";
		for (int i = 0; i < filesList.length; i++) {
			if (!filesList[i].isDirectory()) {
				File file = filesList[i];
				String fileName = file.getName();
				int lastIndex = fileName.lastIndexOf(".");

				if (-1 < lastIndex) {
					String extention = fileName.substring(lastIndex);
					if (".mp3".equalsIgnoreCase(extention)) {
						AudioFile audioFile = AudioFileIO.read(file);
						this.updateAudioTags(audioFile, updateFields);

					}
				}

			}
		}
		return retval;
	}

	public boolean updateAudioTags(AudioFile audioFile, HashMap<String, String> updateFields) {

		boolean retval = true;
		Tag audioTag = audioFile.getTag();

		Iterator<?> iterator = updateFields.keySet().iterator();
		boolean hasNext = false;
		try {
			do {
				Object iter = iterator.next();
				// audioTag.setField(FieldKey.valueOf(iter.toString()),
				// updateFields.get(iter).toString());
				String fieldName = iter.toString();
				if (audioTag.hasField(FieldKey.valueOf(fieldName))) {
					audioTag.deleteField(FieldKey.valueOf(fieldName));
				}
				hasNext = iterator.hasNext();
			} while (hasNext);

			audioFile.commit();

		} catch (Exception e) {
			return false;
		}

		return retval;
	}

	public String formatAudioLength(AudioFile audioFile) {

		// -- String audioLength = formatAudioLength(audioFile);
		String retval = "";
		int trackLengthInSeconds = audioFile.getAudioHeader().getTrackLength();
		int hours = trackLengthInSeconds / 3600;
		int minutes = (trackLengthInSeconds % 3600) / 60;
		int seconds = trackLengthInSeconds % 60;
		retval = String.format("%02d:%02d:%02d%n", hours, minutes, seconds);
		return retval;
	}

	private String nullToBlank(Object input) {
		String retval = "";
		if (null != input) {
			retval = String.valueOf(input);
		}
		return retval;
	}

}
