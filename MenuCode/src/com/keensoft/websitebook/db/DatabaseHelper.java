/* 
 * Copyright (C) 2009 Roman Masek
 * 
 * This file is part of OpenSudoku.
 * 
 * OpenSudoku is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * OpenSudoku is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with OpenSudoku.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package com.keensoft.websitebook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class helps open, create, and upgrade the database file.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String TAG = "DatabaseHelper";

	public static final int DATABASE_VERSION = 8;

	private Context mContext;

	DatabaseHelper(Context context) {
		super(context, FaiCodeDatabase.DATABASE_NAME, null, DATABASE_VERSION);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + FaiCodeDatabase.MENU_TABLE_NAME + " ("
				+ ColumnsMenu._ID + " INTEGER PRIMARY KEY," + ColumnsMenu.NAME
				+ " TEXT," + ColumnsMenu.NUM + " INTEGER" + ");");

		db.execSQL("CREATE TABLE " + FaiCodeDatabase.FILE_TABLE_NAME + " ("
				+ ColumnsCodeItem._ID + " INTEGER PRIMARY KEY,"
				+ ColumnsCodeItem.MENU_ID + " INTEGER," + ColumnsCodeItem.STATE
				+ " INTEGER," + ColumnsCodeItem.NAME + " Text,"
				+ ColumnsCodeItem.PATH + " Text" + ");");

		insertFolder(db, 1, "folder1", 2);
		insertFileItem(db, 1, 1, "baidu", "https://www.baidu.com/");

		insertFolder(db, 2, "folder2", 2);
		insertFileItem(db, 2, 2, "126", "http://mail.126.com/");

		insertFolder(db, 3, "folder3", 2);
		insertFileItem(db, 3, 3, "xiaomi", "http://bbs.xiaomi.cn/");

		createIndexes(db);
	}

	private void insertFolder(SQLiteDatabase db, int folderID,
			String folderName, int num) {
		long now = System.currentTimeMillis();
		db.execSQL("INSERT INTO " + FaiCodeDatabase.MENU_TABLE_NAME
				+ " VALUES (" + folderID + ", '" + folderName + "', " + num
				+ ");");
	}

	// TODO: sudokuName is not used
	private void insertFileItem(SQLiteDatabase db, int folderID, int fileID,
			String name, String path) {
		String sql = "INSERT INTO " + FaiCodeDatabase.FILE_TABLE_NAME
				+ " VALUES (" + fileID + ", " + folderID + ", 0, '" + name
				+ "',  '" + path + "');";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ".");

		// createIndexes(db);
	}

	private void createIndexes(SQLiteDatabase db) {
		db.execSQL("create index " + FaiCodeDatabase.FILE_TABLE_NAME
				+ "_idx1 on " + FaiCodeDatabase.FILE_TABLE_NAME + " ("
				+ ColumnsCodeItem.MENU_ID + ");");
	}
}
