package com.keensoft.websitebook.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {
	private File currentDirectory = new File("/sdcard/engineeringcode");
	private static FileHelper fh = new FileHelper();

	private FileHelper() {
		if (!currentDirectory.exists()) {
			currentDirectory.mkdirs();
		}
		init();
	}

	private void init() {
		String folder[] = { "ff", "fg", "fg", "fi", "fj", };
		File t;
		// if (!sdcardPath.exists()) {
		// }
		for (String string : folder) {
			t = new File("/sdcard/engineeringcode/" + string);
			if (!t.exists()) {
				t.mkdirs();
			}
		}
	}

	public static FileHelper getInstance() {
		return fh;
	}

	public File getCurrentDirectory() {
		return currentDirectory;
	}

	public void setCurrentDirectory(File currentDirectory) {
		this.currentDirectory = currentDirectory;
	}

	public List<File> getSubFilesList() {
		File f[] = currentDirectory.listFiles();
		List<File> list = new ArrayList<File>();
		for (File file : f) {
			list.add(file);
		}
		return list;
	}

}
