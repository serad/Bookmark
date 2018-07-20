package com.aplication.sergio.bookmark.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import com.aplication.sergio.bookmark.model.db.UserPreference

class SQLHelper(context: Context, name: String?,
                  factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " +
                TABLE_PREFERENCES + "("
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_PREFERENCE_ID + " INTEGER ,"
                + "PRIMARY KEY ($COLUMN_USER_ID, $COLUMN_PREFERENCE_ID)" +
                ")"
                )
        db.execSQL(CREATE_PRODUCTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int,
                           newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PREFERENCES)
        onCreate(db)
    }

    fun addUserPreference(userPreference: UserPreference) {

        val values = ContentValues()
        values.put(COLUMN_USER_ID, userPreference.userId)
        values.put(COLUMN_PREFERENCE_ID, userPreference.userPreferenceId)

        val db = this.writableDatabase

        db.insert(TABLE_PREFERENCES, null, values)
        db.close()
    }

    fun listUserPreferences(userId: String): MutableList<UserPreference>? {
        val userPreferences: MutableList<UserPreference> = arrayListOf()
        val query =
                "SELECT * FROM $TABLE_PREFERENCES WHERE $COLUMN_USER_ID =  \"$userId\""

        val db = this.writableDatabase


        val cursor = db.rawQuery(query, null)

        try {
            while (cursor.moveToNext()) {

                var userPreference: UserPreference? = null
                val userId = cursor.getString(0)
                val userPreferenceId = Integer.parseInt(cursor.getString(1))

                userPreference = UserPreference(userId, userPreferenceId)
                userPreferences.add(userPreference)
            }
        } finally {
            cursor.close()
        }

        db.close()
        return userPreferences
    }

    fun deleteUserPreference(userPreference: UserPreference): Boolean {

        var result = false

        val query =
                "SELECT * FROM $TABLE_PREFERENCES WHERE $COLUMN_USER_ID = \"" + userPreference.userId + "\" AND $COLUMN_PREFERENCE_ID = \"" + userPreference.userPreferenceId + "\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val id = cursor.getString(0)
            db.delete(TABLE_PREFERENCES, "$COLUMN_USER_ID = ? AND $COLUMN_PREFERENCE_ID = ?",
                    arrayOf(id,  userPreference.userPreferenceId.toString()))
            cursor.close()
            result = true
        }
        db.close()
        return result
    }

    companion object {

        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "productDB.db"
        val TABLE_PREFERENCES = "user"

        val COLUMN_USER_ID = "user_id"
        val COLUMN_PREFERENCE_ID = "preference_id"
    }

}