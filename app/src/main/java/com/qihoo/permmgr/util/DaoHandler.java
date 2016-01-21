package com.qihoo.permmgr.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qihoo.permmgr.HookApplication;

public class DaoHandler
{
	private MyDatabaseHelpser helper;

	private DaoHandler()
	{
		helper = new MyDatabaseHelpser(HookApplication.getInstance());
	}

	private static class SingleHolder
	{
		private static DaoHandler instance = new DaoHandler();
	}

	public static DaoHandler getInstance()
	{
		return SingleHolder.instance;
	}

	public Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		SQLiteDatabase readableDatabase = helper.getReadableDatabase();
		return readableDatabase.query(MyDatabaseHelpser.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
	}

	public void add(ContentValues values)
	{
		SQLiteDatabase writableDatabase = helper.getWritableDatabase();
		int result = writableDatabase.update(MyDatabaseHelpser.TABLE_NAME, values, null, null);
		if(result == 0)
		{
			writableDatabase.insert(MyDatabaseHelpser.TABLE_NAME, null, values);
		}
	}
}
