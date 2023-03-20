package ru.everyday.schedule.dataBase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.everyday.schedule.model.DayOfWeek
import ru.everyday.schedule.model.Event
import java.sql.Date
import java.sql.Time

class dataBaseAdapter (context: Context) {
    private val dbHelper: dataBaseHelper
    private var database: SQLiteDatabase? = null

    init {
        dbHelper = dataBaseHelper(context.applicationContext)
    }

    fun open(): dataBaseAdapter {
        database = dbHelper.writableDatabase
        return this
    }

    fun close() {
        dbHelper.close()
    }

    private val allEntriesEvent: Cursor
        get() {
            val columns = arrayOf(
                dataBaseHelper.COLUMN_ID,dataBaseHelper.COLUMN_TITLE,dataBaseHelper.COLUMN_TIME_START,dataBaseHelper.COLUMN_TIME_STOP,dataBaseHelper.COLUMN_PLACE,
                dataBaseHelper.COLUMN_DESCRIPTION, dataBaseHelper.COLUMN_DAY_ID
            )
            return database!!.query(
                dataBaseHelper.TABLE_EVENT,
                columns,
                null,
                null,
                null,
                null,
                dataBaseHelper.COLUMN_ID + " ASC"
            )
        }
    private val allEntriesDays: Cursor
        get() {
            val columns = arrayOf(
                dataBaseHelper.COLUMN_ID,dataBaseHelper.COLUMN_DATE
            )
            return database!!.query(
                dataBaseHelper.TABLE_DAY,
                columns,
                null,
                null,
                null,
                null,
                dataBaseHelper.COLUMN_ID + " ASC"
            )
        }
    fun getEvent(id: Int): Event? {
        val query = String.format(
            "SELECT * FROM %s WHERE %s=?",
            dataBaseHelper.TABLE_EVENT,
            dataBaseHelper.COLUMN_ID
        )
        val cursor = database!!.rawQuery(query, arrayOf(id.toString()))
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") val title =
                cursor.getString(cursor.getColumnIndex(dataBaseHelper.COLUMN_TITLE))
            @SuppressLint("Range") val idDay =
                cursor.getInt(cursor.getColumnIndex(dataBaseHelper.COLUMN_DAY_ID))
            @SuppressLint("Range") val place =
                cursor.getString(cursor.getColumnIndex(dataBaseHelper.COLUMN_PLACE))
            @SuppressLint("Range") val description =
                cursor.getString(cursor.getColumnIndex(dataBaseHelper.COLUMN_DESCRIPTION))
            @SuppressLint("Range") val timeStart =
                cursor.getLong(cursor.getColumnIndex(dataBaseHelper.COLUMN_TIME_START))
            @SuppressLint("Range") val timeStop =
                cursor.getLong(cursor.getColumnIndex(dataBaseHelper.COLUMN_TIME_STOP))
            return Event(id,idDay, Time(timeStart),Time(timeStop),title,place,description)
        }
        cursor.close()
        return null
    }
    fun getEvents(idDay: Int): List<Event> {
        val events = ArrayList<Event>()
        val query = String.format(
            "SELECT * FROM %s WHERE %s=?",
            dataBaseHelper.TABLE_EVENT,
            dataBaseHelper.COLUMN_DAY_ID
        )
        val cursor = database!!.rawQuery(query, arrayOf(idDay.toString()))
        while (cursor.moveToNext()) {
            @SuppressLint("Range") val id =
                cursor.getInt(cursor.getColumnIndex(dataBaseHelper.COLUMN_ID))
            @SuppressLint("Range") val title =
                cursor.getString(cursor.getColumnIndex(dataBaseHelper.COLUMN_TITLE))
            @SuppressLint("Range") val place =
                cursor.getString(cursor.getColumnIndex(dataBaseHelper.COLUMN_PLACE))
            @SuppressLint("Range") val description =
                cursor.getString(cursor.getColumnIndex(dataBaseHelper.COLUMN_DESCRIPTION))
            @SuppressLint("Range") val timeStart =
                cursor.getLong(cursor.getColumnIndex(dataBaseHelper.COLUMN_TIME_START))
            @SuppressLint("Range") val timeStop =
                cursor.getLong(cursor.getColumnIndex(dataBaseHelper.COLUMN_TIME_STOP))
            events.add(Event(id,idDay,Time(timeStart),Time(timeStop),title,place,description))
        }
        cursor.close()
        return events
    }
    val events: ArrayList<Event>
        get() {
            val events = ArrayList<Event>()
            val cursor = allEntriesEvent
            while (cursor.moveToNext()) {
                @SuppressLint("Range") val id =
                    cursor.getInt(cursor.getColumnIndex(dataBaseHelper.COLUMN_ID))
                @SuppressLint("Range") val idDay =
                    cursor.getInt(cursor.getColumnIndex(dataBaseHelper.COLUMN_DAY_ID))
                @SuppressLint("Range") val title =
                    cursor.getString(cursor.getColumnIndex(dataBaseHelper.COLUMN_TITLE))
                @SuppressLint("Range") val place =
                    cursor.getString(cursor.getColumnIndex(dataBaseHelper.COLUMN_PLACE))
                @SuppressLint("Range") val description =
                    cursor.getString(cursor.getColumnIndex(dataBaseHelper.COLUMN_DESCRIPTION))
                @SuppressLint("Range") val timeStart =
                    cursor.getLong(cursor.getColumnIndex(dataBaseHelper.COLUMN_TIME_START))
                @SuppressLint("Range") val timeStop =
                    cursor.getLong(cursor.getColumnIndex(dataBaseHelper.COLUMN_TIME_STOP))
                events.add(Event(id,idDay,Time(timeStart),Time(timeStop),title,place,description))
            }
            cursor.close()
            return events
        }

    fun getDay(date: Long):DayOfWeek?{
        var dayOfWeek: DayOfWeek? = null
        val query = String.format(
            "SELECT * FROM %s WHERE %s=?",
            dataBaseHelper.TABLE_DAY,
            dataBaseHelper.COLUMN_DATE
        )
        val cursor = database!!.rawQuery(query, arrayOf(date.toString()))
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") val id =
                cursor.getInt(cursor.getColumnIndex(dataBaseHelper.COLUMN_ID))
            dayOfWeek = DayOfWeek(id,getEvents(id),Date(date))
        }
        cursor.close()
        return dayOfWeek
    }
    fun getDay(id: Int):DayOfWeek?{
        var dayOfWeek: DayOfWeek? = null
        val query = String.format(
            "SELECT * FROM %s WHERE %s=?",
            dataBaseHelper.TABLE_DAY,
            dataBaseHelper.COLUMN_ID
        )
        val cursor = database!!.rawQuery(query, arrayOf(id.toString()))
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") val date =
                cursor.getLong(cursor.getColumnIndex(dataBaseHelper.COLUMN_DATE))
            dayOfWeek = DayOfWeek(id,getEvents(id),Date(date))
        }
        cursor.close()
        return dayOfWeek
    }
    val days: ArrayList<DayOfWeek>
        get() {
            val days = ArrayList<DayOfWeek>()
            val cursor = allEntriesDays
            while (cursor.moveToNext()) {
                @SuppressLint("Range") val id =
                    cursor.getInt(cursor.getColumnIndex(dataBaseHelper.COLUMN_ID))
                @SuppressLint("Range") val date =
                    cursor.getLong(cursor.getColumnIndex(dataBaseHelper.COLUMN_DATE))
                days.add(DayOfWeek(id,getEvents(id),Date(date)))
            }
            cursor.close()
            return days
        }
    fun deleteEvent(id: Int): Int {
        val whereClause = "id = ?"
        val whereArgs = arrayOf(id.toString())
        return database!!.delete(dataBaseHelper.TABLE_EVENT, whereClause, whereArgs)
    }
    fun insertEvent(event: Event): Int {
        val cv = ContentValues()
        cv.put(dataBaseHelper.COLUMN_TITLE, event.title)
        cv.put(dataBaseHelper.COLUMN_PLACE, event.place)
        cv.put(dataBaseHelper.COLUMN_DAY_ID, event.idDay)
        cv.put(dataBaseHelper.COLUMN_DESCRIPTION, event.description)
        cv.put(dataBaseHelper.COLUMN_TIME_START, event.timeStart.time)
        cv.put(dataBaseHelper.COLUMN_TIME_STOP, event.timeStop.time)
        return database!!.insert(dataBaseHelper.TABLE_EVENT, null, cv).toInt()
    }
    fun updateEvent(event: Event): Int {
        val whereClause = dataBaseHelper.COLUMN_ID + "=" + event.id
        val cv = ContentValues()
        cv.put(dataBaseHelper.COLUMN_TITLE, event.title)
        cv.put(dataBaseHelper.COLUMN_PLACE, event.place)
        cv.put(dataBaseHelper.COLUMN_DAY_ID, event.idDay)
        cv.put(dataBaseHelper.COLUMN_DESCRIPTION, event.description)
        cv.put(dataBaseHelper.COLUMN_TIME_START, event.timeStart.time)
        cv.put(dataBaseHelper.COLUMN_TIME_STOP, event.timeStop.time)
        return database!!.update(dataBaseHelper.TABLE_EVENT, cv, whereClause, null)
    }
    fun deleteDay(ID: Int): Int {
        val whereClause = "id = ?"
        val whereArgs = arrayOf(ID.toString())
        return database!!.delete(dataBaseHelper.TABLE_DAY, whereClause, whereArgs)
    }
    fun insertDay(day: DayOfWeek): Int {
        val cv = ContentValues()
        cv.put(dataBaseHelper.COLUMN_DATE, day.date.time)
        return database!!.insert(dataBaseHelper.TABLE_DAY, null, cv).toInt()
    }

}