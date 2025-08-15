package com.spring.mp3;

import web.com.client.CleanUpAudioBooks;
import web.com.client.RenameAllFilesInDirectory;

public class MP3Processor {

	public StringBuffer formatMP3Directory(MP3CleanerModel model) {
		return CleanUpAudioBooks.cleanupAudioEx(model);
	}

	public StringBuffer renameMP3Directory(MP3CleanerModel model) {
		return RenameAllFilesInDirectory.renameFilesInDirectory(model);
	}

	//	public StringBuffer formatMP3Directory(MP3CleanerModel model) {
	//		return CleanUpAudioBooks.cleanupAudioEx(model.getPath(), model.getAuthor(), model.getBookName(),
	//				model.getYear(), model.getReader(), model.getAuthor(), model.getTitle(), null, model.getStartCount());
	//	}

	//	public StringBuffer renameMP3Directory_bak(MP3CleanerModel model) {
	//		return RenameAllFilesInDirectory.renameFilesInDirectory(model.getPath(), model.getNewFileName(),
	//				model.getExetentions(), model.getStartCount());
	//	}

}
