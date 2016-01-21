package com.qihoo.permmgr.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelpser extends SQLiteOpenHelper
{
	private static String DATABASE_NAME = "WxPlugs.db";
	public static final String TABLE_NAME = "wx_plugs_setting";
	private static int DATABASE_VERSION = 2;

	public MyDatabaseHelpser(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("Create table " + TABLE_NAME + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, dice_num INTEGER, morra_num INTEGER);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
}
