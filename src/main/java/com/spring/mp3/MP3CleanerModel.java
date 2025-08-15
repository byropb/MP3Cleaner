package com.spring.mp3;

import java.util.ArrayList;

public class MP3CleanerModel {

	private String path = "";// "C:\\Data\\Music_Books\\OZ_History\\audio";
	private String author = "";//"Strugatsky";
	private String reader = "";//"Kokshov";
	private String bookName = "";//"OZ_Story";
	private String title = "";//"chapter";
	private String newFileName = "";
	private String startCount = "1";
	private String endCount = "0";
	private String year = "1970";
	private String imageName = "";
	private String appContext = "";
	private String filetype = "";
	private String dropDownTypes = "";
	
	private ArrayList<String> attributesList = new ArrayList<String>();

	private ArrayList<String> extensions = new ArrayList<String>();
	private StringBuffer resultBuffer = new StringBuffer("");

	public MP3CleanerModel() {
		extensions.add("all");
		extensions.add(".mp3");
		extensions.add(".mp4");
		extensions.add(".jpg");
		extensions.add(".png");
		extensions.add(".wav");
		extensions.add(".avi");
		extensions.add(".mov");
		extensions.add(".pdf");
		extensions.add(".docx");
		extensions.add(".xls");
		extensions.add(".xml");
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getReader() {
		return reader;
	}

	public void setReader(String reader) {
		this.reader = reader;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNewFileName() {
		return newFileName;
	}

	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}

	public String getStartCount() {
		return startCount;
	}

	public void setStartCount(String startCount) {
		this.startCount = startCount;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public ArrayList<String> getExtensions() {
		return extensions;
	}

	public void setExtensions(ArrayList<String> extensions) {
		this.extensions = extensions;
	}

	public StringBuffer getResultBuffer() {
		return resultBuffer;
	}

	public void setResultBuffer(StringBuffer resultBuffer) {
		this.resultBuffer = resultBuffer;
	}

	public String getAppContext() {
		return appContext;
	}

	public void setAppContext(String appContext) {
		this.appContext = appContext;
	}

	public String getEndCount() {
		return endCount;
	}

	public void setEndCount(String endCount) {
		this.endCount = endCount;
	}

	public ArrayList<String> getAttributesList() {
		return attributesList;
	}

	public void setAttributesList(ArrayList<String> attributesList) {
		this.attributesList = attributesList;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public String getDropDownTypes() {
		return dropDownTypes;
	}

	public void setDropDownTypes(String dropDownTypes) {
		this.dropDownTypes = dropDownTypes;
	}

}
