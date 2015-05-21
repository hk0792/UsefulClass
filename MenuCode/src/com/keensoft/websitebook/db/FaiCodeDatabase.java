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

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;

import com.keensoft.websitebook.beans.FileItem;
import com.keensoft.websitebook.beans.FolderItem;

/**
 * 
 * Wrapper around opensudoku's database.
 * 
 * You have to pass application context when creating instance:
 * <code>SudokuDatabase db = new SudokuDatabase(getApplicationContext());</code>
 * 
 * You have to explicitly close connection when you're done with database (see
 * {@link #close()}).
 * 
 * This class supports database transactions using {@link #beginTransaction()},
 * \ {@link #setTransactionSuccessful()} and {@link #endTransaction()}. See
 * {@link SQLiteDatabase} for details on how to use them.
 * 
 * @author romario
 *
 */
public class FaiCodeDatabase {
	public static final String DATABASE_NAME = "faidatabase";

	public static final String FILE_TABLE_NAME = "file";
	public static final String MENU_TABLE_NAME = "folder";

	// private static final String TAG = "SudokuDatabase";

	private DatabaseHelper mOpenHelper;

	private static FaiCodeDatabase mFaiCodeDatabase;

	private FaiCodeDatabase(Context context) {
		mOpenHelper = new DatabaseHelper(context);
	}

	public static FaiCodeDatabase getInstance(Context context) {
		if (mFaiCodeDatabase == null) {
			mFaiCodeDatabase = new FaiCodeDatabase(context);
		}
		return mFaiCodeDatabase;
	}

	/**
	 * Returns list of puzzle folders.
	 * 
	 * @return
	 */
	public Cursor getFolderList() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		qb.setTables(MENU_TABLE_NAME);

		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		return qb.query(db, null, null, null, null, null, "created ASC");
	}

	public List<FolderItem> getFolderItemList() {
		List<FolderItem> list = new ArrayList<FolderItem>();
		Cursor c = getFolderList();
		FolderItem fi;
		while (c.moveToNext()) {
			fi = new FolderItem();
			fi.setName(c.getString(c.getColumnIndex(ColumnsMenu.NAME)));
			fi.setNum(c.getInt(c.getColumnIndex(ColumnsMenu.NUM)));
			fi.setId(c.getInt(c.getColumnIndex(ColumnsMenu._ID)));
			list.add(fi);
		}
		return list;
	}

	/**
	 * Returns the folder info.
	 * 
	 * @param folderID
	 *            Primary key of folder.
	 * @return
	 */
	public FolderItem getFolderInfo(long folderID) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		qb.setTables(MENU_TABLE_NAME);
		qb.appendWhere(ColumnsMenu._ID + "=" + folderID);

		Cursor c = null;

		try {
			SQLiteDatabase db = mOpenHelper.getReadableDatabase();
			c = qb.query(db, null, null, null, null, null, null);

			if (c.moveToFirst()) {
				long id = c.getLong(c.getColumnIndex(ColumnsMenu._ID));
				String name = c.getString(c.getColumnIndex(ColumnsMenu.NAME));

				FolderItem folderInfo = new FolderItem();
				// folderInfo.id = id;
				// folderInfo.name = name;

				return folderInfo;
			} else {
				return null;
			}
		} finally {
			if (c != null)
				c.close();
		}
	}

	private static final String INBOX_FOLDER_NAME = "Inbox";

	/**
	 * Find folder by name. If no folder is found, null is returned.
	 * 
	 * @param folderName
	 * @param db
	 * @return
	 */
	public FolderItem findFolder(String folderName) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		qb.setTables(MENU_TABLE_NAME);
		qb.appendWhere(ColumnsMenu.NAME + " = ?");

		Cursor c = null;

		try {
			SQLiteDatabase db = mOpenHelper.getReadableDatabase();
			c = qb.query(db, null, null, new String[] { folderName }, null,
					null, null);

			if (c.moveToFirst()) {
				long id = c.getLong(c.getColumnIndex(ColumnsMenu._ID));
				String name = c.getString(c.getColumnIndex(ColumnsMenu.NAME));

				FolderItem folderInfo = new FolderItem();
				// folderInfo.id = id;
				// folderInfo.name = name;

				return folderInfo;
			} else {
				return null;
			}
		} finally {
			if (c != null)
				c.close();
		}
	}

	/**
	 * Inserts new puzzle folder into the database.
	 * 
	 * @param name
	 *            Name of the folder.
	 * @param created
	 *            Time of folder creation.
	 * @return
	 */
	public FolderItem insertFolder(String name, int num) {
		ContentValues values = new ContentValues();
		values.put(ColumnsMenu.NUM, num);
		values.put(ColumnsMenu.NAME, name);

		long rowId;
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		rowId = db.insert(MENU_TABLE_NAME, ColumnsMenu._ID, values);

		if (rowId > 0) {
			FolderItem fi = new FolderItem();
			fi.setName(name);
			fi.setNum(num);
			return fi;
		}

		throw new SQLException(String.format("Failed to insert folder '%s'.",
				name));
	}

	/**
	 * Updates folder's information.
	 * 
	 * @param folderID
	 *            Primary key of folder.
	 * @param name
	 *            New name for the folder.
	 */
	public void updateFolder(long folderID, String name) {
		ContentValues values = new ContentValues();
		values.put(ColumnsMenu.NAME, name);

		SQLiteDatabase db = null;
		db = mOpenHelper.getWritableDatabase();
		db.update(MENU_TABLE_NAME, values, ColumnsMenu._ID + "=" + folderID,
				null);
	}

	/**
	 * Deletes given folder.
	 * 
	 * @param folderID
	 *            Primary key of folder.
	 */
	public void deleteFolder(long folderID) {

		// TODO: should run in transaction
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		// delete all puzzles in folder we are going to delete
		db.delete(FILE_TABLE_NAME, ColumnsCodeItem.MENU_ID + "=" + folderID,
				null);
		// delete the folder
		db.delete(MENU_TABLE_NAME, ColumnsMenu._ID + "=" + folderID, null);
	}

	/**
	 * Returns list of puzzles in the given folder.
	 * 
	 * @param folderID
	 *            Primary key of folder.
	 * @return
	 */
	public Cursor getFileList(long folderID) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		qb.setTables(FILE_TABLE_NAME);
		// qb.setProjectionMap(sPlacesProjectionMap);
		qb.appendWhere(ColumnsCodeItem.MENU_ID + "=" + folderID);

		// if (!filter.showStateCompleted) {
		// qb.appendWhere(" and " + SudokuColumns.STATE + "!=" +
		// SudokuGame.GAME_STATE_COMPLETED);
		// }
		// if (!filter.showStateNotStarted) {
		// qb.appendWhere(" and " + SudokuColumns.STATE + "!=" +
		// SudokuGame.GAME_STATE_NOT_STARTED);
		// }
		// if (!filter.showStatePlaying) {
		// qb.appendWhere(" and " + SudokuColumns.STATE + "!=" +
		// SudokuGame.GAME_STATE_PLAYING);
		// }

		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		return qb.query(db, null, null, null, null, null, ColumnsCodeItem.MENU_ID+" DESC");
	}

	public List<FileItem> getFileItemList(int folderID) {
		Cursor c = getFileList(folderID);
		List<FileItem> list = new ArrayList<FileItem>();
		FileItem fi;
		while (c.moveToNext()) {
			fi = new FileItem();
			fi.setName(c.getString(c.getColumnIndex(ColumnsCodeItem.NAME)));
			fi.setPath(c.getString(c.getColumnIndex(ColumnsCodeItem.PATH)));
			list.add(fi);
		}
		return list;
	}

	/**
	 * Returns sudoku game object.
	 * 
	 * @param fileID
	 *            Primary key of folder.
	 * @return
	 */
	public FolderItem getFile(long fileID) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		qb.setTables(FILE_TABLE_NAME);
		qb.appendWhere(ColumnsCodeItem._ID + "=" + fileID);

		SQLiteDatabase db = null;
		Cursor c = null;
		FolderItem fi = null;
		try {
			db = mOpenHelper.getReadableDatabase();
			c = qb.query(db, null, null, null, null, null, null);

			if (c.moveToFirst()) {
				fi = new FolderItem();
				fi.setId(c.getInt(c.getColumnIndex(ColumnsMenu._ID)));
				fi.setName(c.getString(c.getColumnIndex(ColumnsMenu.NAME)));
				fi.setNum(c.getInt(c.getColumnIndex(ColumnsMenu.NUM)));
			}
		} finally {
			if (c != null)
				c.close();
		}
		return fi;
	}

	/**
	 * Inserts new puzzle into the database.
	 * 
	 * @param folderID
	 *            Primary key of the folder in which puzzle should be saved.
	 * @param sudoku
	 * @return
	 */
	public long insertItemMenu(long folderID, FolderItem sudoku) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		// values.put(SudokuColumns.DATA, sudoku.getCells().serialize());
		// values.put(SudokuColumns.CREATED, sudoku.getCreated());
		// values.put(SudokuColumns.LAST_PLAYED, sudoku.getLastPlayed());
		// values.put(SudokuColumns.STATE, sudoku.getState());
		// values.put(SudokuColumns.TIME, sudoku.getTime());
		// values.put(SudokuColumns.PUZZLE_NOTE, sudoku.getNote());
		// values.put(SudokuColumns.FOLDER_ID, folderID);

		long rowId = db.insert(FILE_TABLE_NAME, ColumnsMenu.NAME, values);
		if (rowId > 0) {
			return rowId;
		}

		throw new SQLException("Failed to insert sudoku.");
	}

	private SQLiteStatement mInsertSudokuStatement;

	/**
	 * Returns List of sudokus to export.
	 * 
	 * @param folderID
	 *            Id of folder to export, -1 if all folders will be exported.
	 * @return
	 */
	public Cursor exportFolder(long folderID) {
		String query = "select f._id as folder_id, f.name as folder_name, f.created as folder_created, s.created, s.state, s.time, s.last_played, s.data, s.puzzle_note from folder f left outer join sudoku s on f._id = s.folder_id";
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		if (folderID != -1) {
			query += " where f._id = ?";
		}
		return db.rawQuery(query,
				folderID != -1 ? new String[] { String.valueOf(folderID) }
						: null);
	}

	/**
	 * Returns one concrete sudoku to export. Folder context is not exported in
	 * this case.
	 * 
	 * @param sudokuID
	 * @return
	 */
	public Cursor exportSudoku(long sudokuID) {
		String query = "select f._id as folder_id, f.name as folder_name, f.created as folder_created, s.created, s.state, s.time, s.last_played, s.data, s.puzzle_note from sudoku s inner join folder f on s.folder_id = f._id where s._id = ?";
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		return db.rawQuery(query, new String[] { String.valueOf(sudokuID) });
	}

	/**
	 * Updates sudoku game in the database.
	 * 
	 * @param sudoku
	 */
	public void updateItemMenu(FolderItem sudoku) {
		ContentValues values = new ContentValues();
		// values.put(SudokuColumns.DATA, sudoku.getCells().serialize());
		// values.put(SudokuColumns.LAST_PLAYED, sudoku.getLastPlayed());
		// values.put(SudokuColumns.STATE, sudoku.getState());
		// values.put(SudokuColumns.TIME, sudoku.getTime());
		// values.put(SudokuColumns.PUZZLE_NOTE, sudoku.getNote());

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		// db.update(SUDOKU_TABLE_NAME, values, SudokuColumns._ID + "=" +
		// sudoku.getId(), null);
	}

	/**
	 * Deletes given sudoku from the database.
	 * 
	 * @param sudokuID
	 */
	public void deleteItemMenu(long sudokuID) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		db.delete(FILE_TABLE_NAME, ColumnsCodeItem._ID + "=" + sudokuID, null);
	}

	public void close() {
		if (mInsertSudokuStatement != null) {
			mInsertSudokuStatement.close();
		}

		mOpenHelper.close();
	}

	public void beginTransaction() {
		mOpenHelper.getWritableDatabase().beginTransaction();
	}

	public void setTransactionSuccessful() {
		mOpenHelper.getWritableDatabase().setTransactionSuccessful();
	}

	public void endTransaction() {
		mOpenHelper.getWritableDatabase().endTransaction();
	}
}
