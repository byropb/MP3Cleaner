package web.com.client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beaglebuddy.id3.enums.PictureType;
import com.beaglebuddy.mp3.MP3;
import com.spring.mp3.MP3CleanerModel;

public class CleanUpAudioBooks {

	private static final Logger logger = LoggerFactory.getLogger(CleanUpAudioBooks.class);

	/**
	 * java UI builder
	 * https://www.google.com/search?q=eclipse+java+project+create+UI&rlz=1C1CHBD_enCA1083CA1084&oq=eclipse+java+project+create+UI&gs_lcrp=EgZjaHJvbWUyBggAEEUYOdIBCTIyMDA3ajBqN6gCALACAA&sourceid=chrome&ie=UTF-8#kpvalbx=_qe3DZZ-tF8Wr0PEPzKKhkAg_51
	 * 
	 * @param args
	 */

	private static FileProcessor fileProcessor = new FileProcessor();

	public static StringBuffer cleanupAudioEx(MP3CleanerModel model) {

		StringBuffer buffer = new StringBuffer("");
		if (!model.getPath().endsWith("\\")) {
			// model.setPath(model.getPath().concat("\\"));
		}
		ArrayList<File> filesList = fileProcessor.readFileDirtorySorted(model.getPath(), "asc");
		if (1 > filesList.size()) {
			buffer.append("Diroctory does not exist: ".concat(model.getPath()));
			logger.error(buffer.toString());
			return buffer;
		}
		int icount = Integer.valueOf(model.getStartCount());
		boolean isAnnotation = false;

		if (icount < 1) {
			icount = 1; // trackId not allowed 0
		}

		for (File file : filesList) {

			if (file.isDirectory()) {
				// reserved
			} else if (file.getName().endsWith(".mp3")) {

				MP3 mp3 = null;
				String title = "";

				try {
					mp3 = new MP3(file);
					mp3.setTrack(icount);
					title = mp3.getTitle();
					title = (null == title ? "" : title);

					if (title.toLowerCase().contains("prologue") || title.toLowerCase().contains("пролог")) {
						isAnnotation = true;
					}
					if (title.toLowerCase().contains("epilogue") || title.toLowerCase().contains("эпилог")) {
						isAnnotation = true;
					}
					if (title.toLowerCase().contains("annotation") || title.toLowerCase().contains("аннотация")) {
						isAnnotation = true;
					}
					if (isAnnotation) {
						icount--;
						isAnnotation = false;
					} else {
						title = formatSequenceToTitle(model.getTitle() + " ", icount);
					}
					mp3.setTitle(title); // no title change//

					mp3.setBand(model.getReader());
					mp3.setAlbum(model.getBookName());
					mp3.setLeadPerformer(model.getAuthor());
					mp3.setYear(Integer.valueOf(model.getYear()));
					mp3.setMusicType("Speech");
					mp3.setComments(model.getReader());
					mp3.setLeadPerformer(model.getAuthor());
					// logger.info(mp3.getPath());
					// mp3.setAudioDuration(312); // 312 seconds == 5 minutes and 12 seconds

					String imageName = ("".equals(model.getImageName())) ? "image.jpg" : model.getImageName();
					String imagePath = model.getPath().concat(imageName);
					if (new File(imagePath).exists() && new File(imagePath).isFile()) {

						mp3.setPicture(PictureType.FRONT_COVER, (new File(imagePath)));
						mp3.setPicture(PictureType.BACK_COVER, (new File(model.getPath().concat(imageName))));
						mp3.setPicture(PictureType.FRONT_COVER, (new File(model.getPath().concat(imageName))));
						mp3.setPicture(PictureType.ILLUSTRATION, (new File(model.getPath().concat(imageName))));
					}
					// save the new information to the .mp3 file
					mp3.save();
					icount++;
					buffer.append(mp3.getPath()).append("\n<br/>");

				} catch (IOException e) {
					logger.error(e.getMessage());
					if (mp3 != null)
						logger.error(mp3.getPath());
				}
			}
		}
		return buffer;
	}

	public static StringBuffer cleanupAudioEx(String dirPath, String authorName, String albumName, String year,
			String comments, String leadPerformer, String titleName, String imageName, String startTrack) {

		StringBuffer buffer = new StringBuffer("");
		if (!dirPath.endsWith("\\")) {
			dirPath = dirPath.concat("\\");
		}
		int icount = Integer.valueOf(startTrack);
		File directory = new File(dirPath);
		boolean isAnnotation = false;
		if (!directory.exists()) {
			buffer.append("Diroctory does not exist: ".concat(directory.toString()));
			logger.error(buffer.toString());
			return buffer;
		}
		if (icount < 1) {
			icount = 1; // trackId not allowed 0
		}
		for (File file : directory.listFiles()) {
			// if this is a sub-directory, then go examine .mp3 files in it
			if (file.isDirectory()) {
			} else if (file.getName().endsWith(".mp3")) {

				MP3 mp3 = null;
				String title = "";

				try {
					mp3 = new MP3(file);
					mp3.setTrack(icount);
					title = mp3.getTitle();
					title = (null == title ? "" : title);

					if (title.toLowerCase().contains("prologue") || title.toLowerCase().contains("пролог")) {
						isAnnotation = true;
					}
					if (title.toLowerCase().contains("annotation") || title.toLowerCase().contains("аннотация")) {
						isAnnotation = true;
					}
					if (isAnnotation) {
						icount--;
						isAnnotation = false;
					} else {
						title = formatSequenceToTitle(titleName + " ", icount);
					}
					mp3.setTitle(title); // no title change//

					mp3.setBand(comments);
					mp3.setAlbum(albumName);
					mp3.setLeadPerformer(authorName);
					mp3.setYear(Integer.valueOf(year));
					mp3.setMusicType("Speech");
					mp3.setComments(comments);
					mp3.setLeadPerformer(leadPerformer);
					// logger.info(mp3.getPath());

					// mp3.setAudioDuration(312); // 312 seconds == 5 minutes and 12 seconds
					// ,john02.jpg,john03.jpg
					// mp3.setLyrics(lyrics);
					imageName = (null == imageName) ? "image.jpg" : imageName;
					String imagePath = dirPath.concat(imageName);
					if (new File(imagePath).exists() && new File(imagePath).isFile()) {

						mp3.setPicture(PictureType.FRONT_COVER, (new File(imagePath)));
						mp3.setPicture(PictureType.BACK_COVER, (new File(dirPath.concat(imageName))));
						mp3.setPicture(PictureType.FRONT_COVER, (new File(dirPath.concat(imageName))));
						mp3.setPicture(PictureType.ILLUSTRATION, (new File(dirPath.concat(imageName))));
					}
					// save the new information to the .mp3 file
					mp3.save();
					icount++;
					buffer.append(mp3.getPath()).append("\n<br/>");

				} catch (IOException e) {
					logger.error(e.getMessage());
					if (mp3 != null)
						logger.error(mp3.getPath());
				}
			}
		}
		return buffer;
	}

	public static void formatTracks(String dirPath) {
		File directory = new File(dirPath);
		MP3 mp3 = null;
		int trackCount = 0;
		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				// ---
			} else {
				try {
					if (file.getName().endsWith(".mp3")) {
						mp3 = new MP3(file);
						trackCount++;
						mp3.setTrack(trackCount);
						mp3.save();
					}
				} catch (Exception e) {
					logger.error(e.toString());
				}
			}
		}
	}

	public static String formatTitleByName(String fileName) {

		String fileNameShort = fileName.replace(".mp3", ""); /// 1561908387_01_stiks_chelovecheskiy_uley 01_-_Arya
		String retval = "";
		String splitter = "_";
		String[] arName = { fileNameShort };
		arName = fileNameShort.split(splitter);
		String title = "";
		// arName = fileName.split(splitter);

		int start = 2;
		int end = 4;
		StringBuffer buffer = new StringBuffer();
		for (int i = start; i <= end; i++) {
			String temp = doTranslation(arName[i]);
			buffer.append(temp).append(" ");
		}
		title = buffer.toString().substring(0, buffer.length() - 1);
		retval = arName[0].concat("-").concat(title);
		return retval;
	}

	public static String formatTitleByFileName(String fileName) {

		String fileNameShort = fileName.replace(".mp3", ""); /// 1561908387_01_stiks_chelovecheskiy_uley
		// String splitter = " ";
		String splitter = "_-_";
		String[] arName = { fileNameShort };
		arName = fileNameShort.split(splitter);

		StringBuffer nameBuffer = new StringBuffer("");
		for (int i = 0; i < arName.length; i++) {
			// arName[i] = getNumericName(arName[i]);
		}
		if (arName[0].matches("[0-9]+") && arName[1].matches("[0-9]+")) {
			if (0 == Integer.parseInt(arName[0])) {
				nameBuffer.append("Пролог ").append(Integer.parseInt(arName[1]));
			} else {
				int part = Integer.parseInt(arName[0]);
				int chapter = Integer.parseInt(arName[1]);
				nameBuffer.append("Часть ").append(part).append("-").append(chapter);
			}
			return nameBuffer.toString();
		}
		nameBuffer.append("Часть ").append(arName[1]);
		return nameBuffer.toString();
	}

	public static String formatTitleByF(String fileName) {

		String fileNameShort = fileName.replace(".mp3", ""); /// 1561908387_01_stiks_chelovecheskiy_uley
		String splitter = "_";
		String[] arName = { fileNameShort };
		arName = fileNameShort.split(splitter);

		StringBuffer nameBuffer = new StringBuffer("");
		for (int i = 0; i < arName.length; i++) {
			// arName[i] = getNumericName(arName[i]);
		}
		if (arName[0].matches("[0-9]+") && arName[1].matches("[0-9]+")) {
			if (0 == Integer.parseInt(arName[0])) {
				nameBuffer.append("Пролог ").append(Integer.parseInt(arName[1]));
			} else {
				int part = Integer.parseInt(arName[0]);
				int chapter = Integer.parseInt(arName[1]);
				nameBuffer.append("Часть ").append(part).append("-").append(chapter);
			}
			return nameBuffer.toString();
		}
		nameBuffer.append("Часть ").append(arName[1]);
		return nameBuffer.toString();
	}

	// Часть 00_00_Уинслоу - Власть пса --> Глава 01-01
	// Уинслоу - Власть пса

	public static String formatTitleParts(String titleName, int startSeq) {

		String splitter = "_";
		String[] arName = { titleName };
		arName = titleName.split(splitter);
		StringBuffer nameBuffer = new StringBuffer("Глава ");
		if (arName[arName.length - 1].toLowerCase().contains("пролог")) {
			nameBuffer = new StringBuffer("");
		}
		if (arName[arName.length - 1].toLowerCase().contains("эпилог")) {
			nameBuffer = new StringBuffer("");
		}

		for (int i = startSeq; i < arName.length - 1; i++) {
			nameBuffer.append(arName[i]).append("-");
		}
		nameBuffer = new StringBuffer(nameBuffer.substring(0, nameBuffer.length() - 1));
		nameBuffer.append(" ").append(arName[arName.length - 1]);
		return nameBuffer.toString();
	}

	public static String formatTitle(String fileName) {

		String fileNameShort = fileName.replace(".mp3", ""); /// 1561908387_01_stiks_chelovecheskiy_uley
		String splitter = "_";
		String[] arName = { fileNameShort };
		arName = fileNameShort.split(splitter);

		StringBuffer nameBuffer = new StringBuffer("");
		for (int i = 0; i < arName.length; i++) {
			// arName[i] = getNumericName(arName[i]);
		}
		if (arName[0].matches("[0-9]+") && arName[1].matches("[0-9]+")) {
			if (0 == Integer.parseInt(arName[0])) {
				nameBuffer.append("Пролог ").append(Integer.parseInt(arName[1]));
			} else {
				int part = Integer.parseInt(arName[0]);
				int chapter = Integer.parseInt(arName[1]);
				nameBuffer.append("Часть ").append(part).append("-").append(chapter);
			}
			return nameBuffer.toString();
		}
		// nameBuffer.append("").append(arName[arName.length-2]).append("
		// ").append(arName[arName.length-1]);
		nameBuffer.append("Часть ").append(arName[1]);
		return nameBuffer.toString();
	}

	public static String[] formatFileNameMusic(String fileName) {
		String[] arName = fileName.replace(".mp3", "").split(" - ");
		return arName;
	}

	public static String formatSequenceToTitle(String titleName, int icounter) {
		// String.format("%02d", Integer.valueOf(icounter));
		String retval = titleName.concat(String.format("%02d", Integer.valueOf(icounter)));
		return retval;
	}

	public static String getNumericName(String fileName, String prefix) {
		String fileNameShort = fileName.replace(".mp3", "");
		StringBuffer buffer = new StringBuffer("");
		for (int i = 0; i < fileNameShort.length(); i++) {
			if (Character.isDigit(fileNameShort.charAt(i))) {
				buffer.append(fileNameShort.charAt(i));
			}
		}
		if (1 > buffer.length()) {
			buffer.append(fileNameShort);
		}
		return prefix.concat(buffer.toString());
	}

	public static String doTranslation(String source) {

		HashMap<String, String> catalog = DictionaryConsts.getMappingTab();
		StringBuffer buffer = new StringBuffer("");
		int counter = source.length();
		for (int i = 0; i < counter; i++) {
			char sourceChar = source.charAt(i);
			buffer.append(catalog.get(String.valueOf(sourceChar)));
		}
		return buffer.toString();
	}

}
