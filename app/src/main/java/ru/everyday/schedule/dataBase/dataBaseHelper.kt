package ru.everyday.schedule.dataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.scale
import java.io.ByteArrayOutputStream
import java.sql.Date

class dataBaseHelper(private var context: Context) : SQLiteOpenHelper(
    context, DB_NAME, null, SCHEMA)
{
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE " + TABLE_DAY + " (" + COLUMN_ID
                        + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_DATE
                        + " INTEGER);"
            )
            db.execSQL(
                "CREATE TABLE " + TABLE_EVENT + " (" + COLUMN_ID
                        + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TIME_START
                        + " INTEGER, " + COLUMN_TIME_STOP + " INTEGER, " + COLUMN_TITLE
                        + " TEXT, " + COLUMN_DESCRIPTION + " TEXT, " + COLUMN_PLACE + " TEXT, "
                        + COLUMN_DAY_ID + " INTEGER NULL,"
                        + "FOREIGN KEY($COLUMN_DAY_ID) REFERENCES $TABLE_DAY($COLUMN_ID));"
            )
            // добавление начальных данных
        }

        private fun convertToBlob(bitmap: Bitmap?): ByteArray?
        {
            return if (bitmap != null) {
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()
                bitmap.recycle()
                squeeze(byteArray)
            } else null
        }
        private fun squeeze(image: ByteArray?): ByteArray? {
            var img = image
            return if (img != null) {
                while (img!!.size > 500000) {
                    val bitmap = BitmapFactory.decodeByteArray(img, 0, img.size)
                    val resized = Bitmap.createScaledBitmap(
                        bitmap,
                        (bitmap.width * 0.8).toInt(),
                        (bitmap.height * 0.8).toInt(),
                        true
                    )
                    val stream = ByteArrayOutputStream()
                    resized.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    img = stream.toByteArray()
                }
                img
            } else null
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_EVENT")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_DAY")
            onCreate(db)
        }

        companion object {
            private const val DB_NAME = "schedule.db"
            private const val SCHEMA = 1 // версия базы данных
            const val TABLE_DAY = "day" // название таблицы в бд
            const val TABLE_EVENT = "event"

            // названия столбцов
            const val COLUMN_ID = "id"
            const val COLUMN_DATE = "date"

            const val COLUMN_TITLE = "title"
            const val COLUMN_PLACE = "place"
            const val COLUMN_DAY_ID = "id_day"
            const val COLUMN_DESCRIPTION = "description"
            const val COLUMN_TIME_START = "time_start"
            const val COLUMN_TIME_STOP = "time_stop"
        }
}