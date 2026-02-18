package web.com.client;

import java.io.File;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spring.mp3.MP3CleanerModel;

public class RenameAllFilesInDirectory {

	private static final Logger logger = LoggerFactory.getLogger(RenameAllFilesInDirectory.class);
	private static FileProcessor fileProcessor = new FileProcessor();

	public static StringBuffer renameFilesInDirectory(MP3CleanerModel model) {

		// model.getPath(), model.getNewFileName(),
		// model.getExetentions(), model.getStartCount()
		ArrayList<File> filesList = fileProcessor.readFileDirtorySorted(model.getPath(), "asc");

		StringBuffer log_buffer = new StringBuffer("");
		// File directory = new File(model.getPath());
		String sourceFile = "";
		int istartSeq = Integer.valueOf(model.getStartCount());
		int icount = 1;
		int totalCount = 0;
		if (-1 < istartSeq) {
			icount = istartSeq;
		}
		for (File file : filesList) {
			// for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				// Reserved
			} else {
				try {
					sourceFile = file.getPath();
					int iIndex = sourceFile.lastIndexOf(".");
					String suffix = sourceFile.substring(iIndex);

					if (model.getFiletype().equalsIgnoreCase(suffix) || "all".equalsIgnoreCase(model.getFiletype())) {

						StringBuffer buffer = new StringBuffer("");
						String sequesnceId = String.format("%03d", Integer.valueOf(icount));

						// buffer.append(sequesnceId).append("-").append(model.getNewFileName()).append(suffix);
						buffer.append(model.getNewFileName()).append("-").append(sequesnceId).append(suffix);

						String destPath = sourceFile.substring(0, sourceFile.lastIndexOf("\\") + 1)
								.concat(buffer.toString());
						File tempFile = new File(destPath);
						if (!tempFile.exists()) {
							icount++; // used for files sequence
							totalCount++;
							model.setEndCount(String.valueOf(totalCount));
							FileProcessor.copyFile(sourceFile, destPath, true);
							log_buffer.append(destPath).append("\n");
						} else {
							logger.error("unable to overwrite file - ".concat(destPath));
						}
					}
				} catch (Exception e) {
					log_buffer.append(e.getMessage()).append("\n");
					logger.error(e.getMessage());
				}
			}
		}

		return log_buffer;
	}

	public static StringBuffer renameFilesInDirectory_byIncludeList_getExetentions(MP3CleanerModel model) {

		// model.getPath(), model.getNewFileName(),
		// model.getExetentions(), model.getStartCount()

		StringBuffer log_buffer = new StringBuffer("");
		File directory = new File(model.getPath());
		String sourceFile = "";
		int istartSeq = Integer.valueOf(model.getStartCount());
		int icount = 1;
		int totalCount = 0;
		int ifiles = model.getExtensions().size();
		if (-1 < istartSeq) {
			icount = istartSeq;
		}
		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				// skip directory
			} else {
				try {
					sourceFile = file.getPath();
					int iIndex = sourceFile.lastIndexOf(".");
					String suffix = sourceFile.substring(iIndex);
					for (int i = 0; i < ifiles; i++) {
						if (model.getExtensions().get(i).equalsIgnoreCase(suffix)) { // model.getExtensions().get(i)
																						// provides list of extension to
																						// be processed.

							StringBuffer buffer = new StringBuffer("");
							String sequesnceId = String.format("%03d", Integer.valueOf(icount));
							buffer.append(sequesnceId).append("-").append(model.getNewFileName()).append(suffix);

							String destPath = sourceFile.substring(0, sourceFile.lastIndexOf("\\") + 1)
									.concat(buffer.toString());
							File tempFile = new File(destPath);
							if (!tempFile.exists()) {
								icount++; // used for files sequence
								totalCount++;
								model.setEndCount(String.valueOf(totalCount));
								FileProcessor.copyFile(sourceFile, destPath, true);
								log_buffer.append(destPath).append("\n");
							} else {
								logger.error("unable to overwrite file - ".concat(destPath));
							}
						}
					}
				} catch (Exception e) {
					log_buffer.append(e.getMessage()).append("\n");
					logger.error(e.getMessage());
				}
			}
		}

		return log_buffer;
	}

	// -- renames all files in Directory by sequence and given name
	public static StringBuffer renameFilesInDirectory(String dirPath, String fileName, ArrayList<String> fileTypes,
			String startSequence) {

		StringBuffer log_buffer = new StringBuffer("");
		File directory = new File(dirPath);
		String sourceFile = "";
		int istartSeq = Integer.valueOf(startSequence);
		int icount = 1;
		int ifiles = fileTypes.size();
		if (-1 < istartSeq) {
			icount = istartSeq;
		}
		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
			} else {
				try {
					sourceFile = file.getPath();
					int iIndex = sourceFile.lastIndexOf(".");
					String suffix = sourceFile.substring(iIndex);
					for (int i = 0; i < ifiles; i++) {
						if (fileTypes.get(i).equalsIgnoreCase(suffix)) {

							StringBuffer buffer = new StringBuffer("");
							String sequesnceId = String.format("%03d", Integer.valueOf(icount));
							buffer.append(sequesnceId).append("-").append(fileName).append(suffix);

							String destPath = sourceFile.substring(0, sourceFile.lastIndexOf("\\") + 1)
									.concat(buffer.toString());
							File tempFile = new File(destPath);
							if (!tempFile.exists()) {
								icount++;
								FileProcessor.copyFile(sourceFile, destPath, true);
								log_buffer.append(destPath).append("\n");
							} else {
								logger.error("unable to overwrite file - ".concat(destPath));
							}
						}
					}
				} catch (Exception e) {
					log_buffer.append(e.getMessage()).append("\n");
					logger.error(e.getMessage());
				}
			}
		}
		return log_buffer;
	}

}
