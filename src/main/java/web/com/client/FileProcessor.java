package web.com.client;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileProcessor {

	private static final Logger logger = LoggerFactory.getLogger(FileProcessor.class);

	protected static boolean locDelSource = false;
	public static final String FILE_DATE_MMddyyyy24 = "MM_dd_yyyy_HH_mm_ss";
	public static final String DATE_yyyyMMdd = "yyyy-MM-dd";

	public enum FileType {
		FILE, FOLDER, ALL
	}

	public enum FilesRoot {
		SHARED, USER, APP
	}

	public void uploadFilesToDB(String dirToRead, String tableName, boolean delSourceFile) {
		File fileDir = new File(dirToRead);
		File[] filesList = fileDir.listFiles();
		String fileFullName = "";
		// String dateFrom =
		// CommonUtils.getCurrentDate("".DATE_FORMAT1);

		for (int i = 0; i < filesList.length; i++) {
			if (filesList[i].isDirectory()) {
			} else if (filesList[i].isFile()) {
				fileFullName = filesList[i].getAbsolutePath();
				if (delSourceFile) {
					File fileSrc = new File(fileFullName);

					if (fileSrc.delete()) {
						StringBuffer buffer = new StringBuffer(this.getClass().getName());
						buffer.append("File deleted: ");
						buffer.append(fileFullName);

					}
				}

			}
		}
	}

	public static boolean buildDirectory(String dirPath) {

		String path = dirPath.replace("\\", "/");
		File directory = new File(path);
		// String part = "".BLANK;
		StringBuffer part = new StringBuffer("");
		if (!directory.exists()) {

			String[] arTemp = path.split("/");
			for (int i = 0; i < arTemp.length; i++) {
				if (null != (arTemp[i])) {
					part.append(arTemp[i]);
					part.append("/");
					directory = new File(part.toString());
					if (!directory.exists()) {
						if (directory.mkdir()) {
							StringBuffer buffer = new StringBuffer("");
							buffer.append("Directory created: ");
							buffer.append(directory);

						}

					}
				}
			}
		}
		return true;
	}

	public static void saveProperiesToFile(String shortFileName, HashMap<String, String> properties) {

		Properties prop = new Properties();
		boolean hasNext = false;
		FileOutputStream outStream = null;
		try {
			if (null != properties) {
				// Iterator<?> iterator = properties.keySet().iterator();
				Iterator<?> iterator = properties.entrySet().iterator();
				do {
					Object iter = iterator.next();
					prop.setProperty(iter.toString(), properties.get(iter.toString()));
					hasNext = iterator.hasNext();
				} while (hasNext);
				// save properties to project root folder
			}
			outStream = new FileOutputStream(shortFileName);
			prop.store(outStream, null);

		} catch (IOException ex) {
			logger.error(ex.toString());
		} finally {
			try {
				if (null != outStream) {
					outStream.close();
				}
			} catch (IOException e) {
				logger.error(e.toString());

			}
		}
	}

	public static String writeInputStreamToFile(InputStream inStream, String destFile) {

		File fileDest = null;
		String retval = "";
		FileOutputStream fileOutStream = null;
		try {
			fileDest = new File(destFile);
			retval = fileDest.getAbsolutePath().replace("\\", "/");
			fileOutStream = new FileOutputStream(fileDest);
			byte[] buf = new byte[1024];
			int len = inStream.read(buf);
			do {
				fileOutStream.write(buf, 0, len);
				len = inStream.read(buf);
			} while (len > 0);
			logger.info("FileProcessor: File copied to: " + fileDest);

		} catch (FileNotFoundException e) {
			logger.error(e.toString());

		} catch (IOException e) {
			logger.error(e.toString());

		} finally {
			try {
				if (null != fileOutStream) {
					fileOutStream.close();
				}
			} catch (IOException e1) {
				logger.error(e1.toString());
			}

			try {
				if (null != inStream) {
					inStream.close();
				}
			} catch (IOException e1) {
				logger.error(e1.toString());
			}
		}
		return retval;
	}

	public static void copyFile(String sourceFile, String destFile, boolean move) {

		File fileSrc = null;
		File fileDest = null;
		InputStream in = null;
		// OutputStream out = null;
		FileOutputStream fileOutStream = null;
		try {
			fileSrc = new File(sourceFile);
			fileDest = new File(destFile);
			in = new FileInputStream(fileSrc);
			fileOutStream = new FileOutputStream(fileDest);
			byte[] buf = new byte[1024];
			int len = in.read(buf);
			do {
				fileOutStream.write(buf, 0, len);
				len = in.read(buf);
			} while (len > 0);
			if (move) {
				in.close();
				fileOutStream.close();
				if (fileSrc.delete()) {
					// logger.info("Source deleted: : ".concat(sourceFile));
				}
			}

		} catch (FileNotFoundException e) {

			StringBuffer buffer = new StringBuffer("");
			buffer.append("FileProcessor");
			buffer.append(".copyFile(String sourceFile, String destFile, boolean move)");
			buffer.append(" \n");
			buffer.append(e.toString());
			logger.info(buffer.toString());

		} catch (IOException e) {

			StringBuffer buffer = new StringBuffer("");
			buffer.append("FileProcessor");
			buffer.append(".copyFile(String sourceFile, String destFile, boolean move)");
			buffer.append(" \n");
			buffer.append(e.toString());
			logger.info(buffer.toString());
		} finally {

			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				StringBuffer buffer = new StringBuffer("");
				buffer.append("FileProcessor");
				buffer.append(".copyFile(String sourceFile, String destFile, boolean move)");
				buffer.append(" \n");
				buffer.append(e.toString());
				logger.info(buffer.toString());
			}

			try {
				if (null != fileOutStream) {
					fileOutStream.close();
				}
			} catch (IOException e) {
				StringBuffer buffer = new StringBuffer("");
				buffer.append("FileProcessor");
				buffer.append(".copyFile(String sourceFile, String destFile, boolean move)");
				buffer.append(" \n");
				buffer.append(e.toString());
				logger.info(buffer.toString());

			}

		}
	}

	public InputStream openFile(String uri) throws FileNotFoundException {
		InputStream in = null;
		File fileSrc = new File(uri);
		in = new FileInputStream(fileSrc);
		return in;
	}

	public static boolean deleteFile(String uri) throws FileNotFoundException {
		File fileSrc = new File(uri);
		return fileSrc.delete();
	}

	public static InputStream openInputStream(String uri) throws FileNotFoundException {
		final File fileSrc = new File(uri);
		InputStream in = new FileInputStream(fileSrc);
		return in;
	}

	public static File writeFileStream(String uri, String streamBody, boolean create, boolean append)
			throws IOException {

		writeTextToFile(uri, streamBody, create, append);
		// InputStream in = new FileInputStream(uri);
		File file = null;
		BufferedReader inf = null;
		try {
			inf = new BufferedReader(new FileReader(uri));
			String str;
			str = inf.readLine();
			do {
				StringBuffer temp = new StringBuffer("FileProcessor: writeFileStream:");
				temp.append(str);
				str = inf.readLine();
			} while (str != null);
			file = new File(uri);
		} catch (FileNotFoundException e) {
			StringBuffer buffer = new StringBuffer("");
			buffer.append("FileProcessor");
			buffer.append(".writeFileStream(String uri, String streamBody, boolean create, boolean append)");
			buffer.append(" \n");
			buffer.append(e.toString());
			logger.info(buffer.toString());

		} catch (IOException e) {
			logger.error(e.toString());

		} finally {

			try {
				if (null != inf) {
					inf.close();
				}
			} catch (IOException e) {
				StringBuffer buffer = new StringBuffer("");
				buffer.append("FileProcessor");
				buffer.append(".writeFileStream(String uri, String streamBody, boolean create, boolean append)");
				buffer.append(" \n");
				buffer.append(e.toString());
				logger.info(buffer.toString());

			}
		}
		return file;
	}

	public static boolean writeTextToFile(String uri, String text, boolean create, boolean append) throws IOException {

		File fileSrc = new File(uri);
		FileWriter fileWriter = null;
		boolean bcreated = false;
		boolean retval = false;
		try {

			if (!fileSrc.exists()) {
				if (create) {
					bcreated = fileSrc.createNewFile();
					if (bcreated) {
						logger.info("FileProcessor: writeToFile(): file created: ".concat(uri));

						fileWriter = new FileWriter(fileSrc, append);
						fileWriter.write(text);
						fileWriter.flush();
						retval = true;
					} else {
						logger.info("FileProcessor: writeToFile(): file created: ".concat(uri));

					}

				} else {
					logger.info("FileProcessor: writeToFile(): file created: ".concat(uri));

				}

			} else {
				fileWriter = new FileWriter(fileSrc, append);
				fileWriter.write(text);
				fileWriter.flush();
				retval = true;
			}
		} catch (IOException e) {
			StringBuffer buffer = new StringBuffer("");
			buffer.append("FileProcessor");
			buffer.append(".writeTextToFile(String uri, String streamBody, boolean create, boolean append)");
			buffer.append(" \n");
			buffer.append(e.toString());

			logger.info(buffer.toString());

			throw e;
		} finally {
			if (null != fileWriter) {
				fileWriter.close();
			}
		}
		return retval;
	}

	public void copyFormFileToFile(String sourceFile, String destFile, boolean move) {

		InputStream in = null;
		OutputStream out = null;

		try {
			File fileSrc = new File(sourceFile);
			File fileDest = new File(destFile);
			in = new FileInputStream(fileSrc);
			out = new FileOutputStream(fileDest);

			byte[] buf = new byte[1024];
			int len = in.read(buf);
			do {
				out.write(buf, 0, len);
				len = in.read(buf);
			} while (len > 0);
			/*
			 * while ((len) > 0) { out.write(buf, 0, len); len = in.read(buf); }
			 */
			if (move) {
				in.close();
				out.close();
				if (fileSrc.delete()) {
					logger.info("source file deleted: " + sourceFile);

				}
			}

			StringBuilder builder = new StringBuilder();
			builder.append(this.getClass().getName());
			builder.append("; copyFormFileToFile(); FileProcessor: File copied as: ");
			builder.append(fileDest);
			logger.info(builder.toString());

		} catch (FileNotFoundException e) {
			StringBuilder builder = new StringBuilder();
			builder.append(this.getClass().getName());
			builder.append("; copyFormFileToFile(); ");
			builder.append(e.toString());
			logger.info(builder.toString());

		} catch (IOException e) {
			StringBuilder builder = new StringBuilder();
			builder.append(this.getClass().getName());
			builder.append("; copyFormFileToFile(); ");
			builder.append(e.toString());
			logger.info(builder.toString());
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				StringBuffer buffer = new StringBuffer("");
				buffer.append("FileProcessor");
				buffer.append(".copyFormFileToFile(String uri, String streamBody, boolean create, boolean append)");
				buffer.append(" \n");
				buffer.append(e.toString());
				logger.info(buffer.toString());

			}
			try {
				if (null != out) {
					out.close();
				}

			} catch (IOException e) {
				StringBuffer buffer = new StringBuffer("");
				buffer.append("FileProcessor");
				buffer.append(".writeTextToFile(String uri, String streamBody, boolean create, boolean append)");
				buffer.append(" \n");
				buffer.append(e.toString());
				logger.info(buffer.toString());

			}
		}
	}

	public ArrayList<String> listFileNames(String dirPath, boolean filesOnly) {

		// Directory path here
		// String path = ".";

		ArrayList<String> filesNames = new ArrayList<String>(1);
		File folder = new File(dirPath);

		if (!folder.exists()) {
			buildDirectory(dirPath);
		}
		File[] listOfFiles = folder.listFiles();
		int icount = listOfFiles.length;
		for (int i = 0; i < icount; i++) {
			if (filesOnly) {
				if (listOfFiles[i].isFile()) {
					filesNames.add(listOfFiles[i].getName());
				}
			} else {
				filesNames.add(listOfFiles[i].getName());
			}
		}
		return filesNames;
	}

	public static ArrayList<File> sortFiles(ArrayList<File> arrayFiles, String sortOrder) {

		ArrayList<File> arFolders = new ArrayList<File>(1);
		ArrayList<File> arFiles = new ArrayList<File>(1);
		ArrayList<File> arrayFilesSorted = new ArrayList<File>(1);

		int icount = arrayFiles.size();
		Collections.sort(arrayFiles);

		if ("desc".equalsIgnoreCase(sortOrder)) {
			// --
			for (int i = icount - 1; i > -1; i--) {

				if (arrayFiles.get(i).isDirectory()) {
					arFolders.add(arrayFiles.get(i));
				}
				if (arrayFiles.get(i).isFile()) {
					arFiles.add(arrayFiles.get(i));
				}
			}
			int ifilesSize = arFiles.size();
			for (int i = 0; i < ifilesSize; i++) {
				arrayFilesSorted.add(arFiles.get(i));
			}
			int ifolderSize = arFolders.size();
			for (int i = 0; i < ifolderSize; i++) {
				arrayFilesSorted.add(arFolders.get(i));
			}

		} else {

			for (int i = 0; i < icount; i++) {

				if (arrayFiles.get(i).isDirectory()) {
					arFolders.add(arrayFiles.get(i));
				}
				if (arrayFiles.get(i).isFile()) {
					arFiles.add(arrayFiles.get(i));
				}
			}
			int ifolderSize = arFolders.size();
			for (int i = 0; i < ifolderSize; i++) {
				arrayFilesSorted.add(arFolders.get(i));
			}
			int ifilesSize = arFiles.size();
			for (int i = 0; i < ifilesSize; i++) {
				arrayFilesSorted.add(arFiles.get(i));
			}

		}
		return arrayFilesSorted;
	}

	// function support sorting with numeric format where 1 to 9 dont have 0 in front 
	public static ArrayList<File> bubbleSort(ArrayList<File> arFiles) {
		int n = arFiles.size();

		File fileTemp = null;
		int lastIndex = 0;
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - i - 1; j++) {
				String currentName = arFiles.get(j).getName();
				String nextName = arFiles.get(j + 1).getName();
				lastIndex = currentName.lastIndexOf(".");
				currentName = currentName.substring(0, lastIndex);
				lastIndex = nextName.lastIndexOf(".");
				nextName = nextName.substring(0, lastIndex);

				String currentId = currentName.replaceAll("[^0-9]", "");
				String nextId = nextName.replaceAll("[^0-9]", "");
				if (!"".equals(currentId) && !"".equals(nextId)) {
					if (Integer.valueOf(currentId) > Integer.valueOf(nextId)) {
						fileTemp = arFiles.get(j);
						arFiles.set(j, arFiles.get(j + 1));
						arFiles.set(j + 1, fileTemp);
					}
				}
			}
		}
		return arFiles;

	}

	public static InputStream textToInputStream(String text) {
		InputStream inputStream = null;
		inputStream = new ByteArrayInputStream(text.getBytes());
		return inputStream;
	}

	/*
	 * Appends unique timestamp to given file name
	 */

	public static String stripFileName(String fullPath) {
		String fileName = fullPath;
		if (fileName.indexOf("\\") > -1) {
			fileName = fileName.replaceAll("\\\\", "/");
		}
		if (fileName.indexOf("/") > -1) {
			fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
		}
		return fileName;
	}

	public static String readFromTextFile(String fullPath) throws IOException {

		BufferedReader br = null;
		StringBuffer buffer = new StringBuffer("");

		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(fullPath));

			do {
				sCurrentLine = br.readLine();
				if (null != sCurrentLine) {
					buffer.append(sCurrentLine).append(" \n");
				}
			} while (null != sCurrentLine);

		} catch (IOException e) {

			buffer.append("FileProcessor");
			buffer.append(".writeTextToFile(String uri, String streamBody, boolean create, boolean append)");
			buffer.append(" \n");
			buffer.append(e.toString());
			logger.info(buffer.toString());
			throw e;
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {

				buffer.append("FileProcessor");
				buffer.append(".writeTextToFile(String uri, String streamBody, boolean create, boolean append)");
				buffer.append(" \n");
				buffer.append(e.toString());
				logger.info(buffer.toString());
			}
		}
		return buffer.toString();
	}

	// -----------

	@SuppressWarnings("deprecation")
	protected String readFileAsText(DataInputStream dataStream, int startRows) throws IOException {

		if (null == dataStream) {
			return "";
		}
		StringBuffer buffer = new StringBuffer("");
		int skipCount = 1;
		int istream = dataStream.available();
		do { // scroll to startRow position
			if (skipCount <= startRows) {
				break;
			}
			dataStream.readLine();
			skipCount++;
		} while (skipCount < startRows);

		do {
			buffer.append(dataStream.readLine()).append(" \n");
			istream = dataStream.available();
		} while (istream != 0);

		dataStream.close();
		return buffer.toString();
	}

	public String readFileAsText(String fileName, int startRows) throws IOException {

		StringBuffer buffer = new StringBuffer("");
		FileInputStream fileStream = null;
		DataInputStream dataStream = null;
		try {
			fileStream = new FileInputStream(fileName);
			dataStream = new DataInputStream(fileStream);
			buffer.append(this.readFileAsText(dataStream, startRows));
		} catch (IOException e) {
			e.toString();
		} finally {
			if (null != dataStream) {
				dataStream.close();
			}
			if (null != fileStream) {
				fileStream.close();
			}
		}
		return buffer.toString();
	}

	public void writeToFile(String filePath, String textStream, boolean append) {
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(new FileOutputStream(filePath));
			out.writeBytes(textStream);

		} catch (FileNotFoundException e) {

			StringBuilder builder = new StringBuilder();
			builder.append(this.getClass().getName());
			builder.append("; writeToFile();").append(" \n");
			builder.append(e.toString());

			logger.info(builder.toString());

		} catch (IOException e) {
			StringBuilder builder = new StringBuilder();
			builder.append(this.getClass().getName());
			builder.append("; writeToFile();").append(" \n");
			builder.append("\n ");
			builder.append(e.toString());
			logger.info(builder.toString());
		} finally {
			try {
				if (null != out) {
					out.close();
				}
			} catch (IOException e) {
				StringBuilder builder = new StringBuilder();
				builder.append(this.getClass().getName());
				builder.append("; writeToFile()/close stream: ").append(" \n");
				builder.append(e.toString());
				logger.info(builder.toString());
			}
		}
	}

	public ArrayList<HashMap<String, String>> readFileDirtory(String dirToRead, String sorted) {

		ArrayList<HashMap<String, String>> retval = new ArrayList<HashMap<String, String>>();

		File fileDir = new File(dirToRead);
		if (!fileDir.exists()) {
			logger.error("directory does not exist! - ".concat(dirToRead));
			return retval;
		}
		File[] filesList = fileDir.listFiles();
		ArrayList<File> dirList = new ArrayList<File>(0);
		for (int i = 0; i < filesList.length; i++) {
			if (filesList[i].isDirectory()) {
				// --
			} else if (filesList[i].isFile()) {
				dirList.add(filesList[i]);
			}
		}

		// dirList = sortFiles(dirList, sorted); // desc, asc
		dirList = bubbleSort(dirList);
		int icount = dirList.size();

		for (int i = 0; i < icount; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("path", dirList.get(i).getPath());
			map.put("name", dirList.get(i).getName());
			map.put("size", String.valueOf(String.format("%,d", dirList.get(i).length() / 1024)).concat(" KB"));
			map.put("lastModified", String.valueOf(unixTimeToDate(dirList.get(i).lastModified(), "yyyy-MM-dd HH:mm")));
			retval.add(map);
		}
		return retval;
	}

	public ArrayList<File> readFileDirtorySorted(String dirToRead, String sorted) {

		File fileDir = new File(dirToRead);
		ArrayList<File> dirList = new ArrayList<File>(0);
		if (!fileDir.exists()) {
			logger.error("directory does not exist! - ".concat(dirToRead));
			return dirList;
		}
		File[] filesList = fileDir.listFiles();

		for (int i = 0; i < filesList.length; i++) {
			if (filesList[i].isDirectory()) {
				// --
			} else if (filesList[i].isFile()) {
				dirList.add(filesList[i]);
			}
		}
		// dirList = sortFiles(dirList, sorted); // desc, asc
		dirList = bubbleSort(dirList);

		return dirList;
	}

	public static String unixTimeToDate(long unixTime, String format) {

		String datetimeString = "";
		Date d = new Date(unixTime);
		SimpleDateFormat f = new SimpleDateFormat(format);
		// SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy,HH:mm");
		datetimeString = f.format(d);
		return datetimeString;
	}

	@SuppressWarnings("deprecation")
	public static URL getUrl(String resource, Class<?> resourceClass) {
		URL retval = null;
		try {
			if (resourceClass == null || resource == null) {
				return retval;
			}

			File f = new File(resource);
			if (f.exists() && f.canRead()) {
				return f.toURL();
			}
			URL rs = resourceClass.getResource(resource);
			if (rs == null) {
				rs = Object.class.getResource(resource);
			}
			return rs;
		} catch (MalformedURLException e) {
			return retval;
		}
	}

	public static String nullToBlank(Object input) {
		String retval = "";
		if (null != input) {
			retval = String.valueOf(input);
		}
		return retval;
	}

	@SuppressWarnings("unused")
	private String parseMp3link(File file) {

		StringBuffer buffer = new StringBuffer("");
		buffer.append("<a href='file:\\\\").append(file.getPath()).append("' target='_blank'>").append(file.getName())
				.append("</a>");
		return buffer.toString();

	}
	// -----injected

	// ---
}
