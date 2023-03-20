package ru.everyday.schedule

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import ru.everyday.schedule.dataBase.dataBaseAdapter
import ru.everyday.schedule.model.DayOfWeek
import ru.everyday.schedule.model.Event
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList


class EventActivity : AppCompatActivity() {
    private lateinit var adapter: dataBaseAdapter
    private lateinit var date:CalendarView
    private lateinit var title:EditText
    private lateinit var desc:EditText
    private lateinit var place:EditText
    private lateinit var timeStart:EditText
    private lateinit var timeFinish:EditText
    private var longtimeStart:Long = 0
    private var longtimeStop:Long = 0
    private var selectedDate:Long=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        val cal = Calendar.getInstance()

        val timeSetListener = OnTimeSetListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            timeStart.setText(SimpleDateFormat("HH:mm",Locale.US).format(cal.time))
            longtimeStart=cal.time.time
        }
        val timeSetListener2 = OnTimeSetListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            timeFinish.setText(SimpleDateFormat("HH:mm",Locale.US).format(cal.time))
            longtimeStop=cal.time.time
        }
        adapter = dataBaseAdapter(this)
        title =findViewById(R.id.eventTitleMain)
        desc =findViewById(R.id.eventDescMain)
        date =findViewById(R.id.eventDateMain)
        place =findViewById(R.id.eventPlaceMain)
        date.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val cal1 = Calendar.getInstance()
            cal1.set(year,month,dayOfMonth)
            selectedDate = cal1.timeInMillis
        }
        timeStart =findViewById(R.id.eventTimeStMain)
        timeStart.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }
        timeStart.setOnClickListener {
            TimePickerDialog(
                this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }
        timeFinish =findViewById(R.id.eventTimeFiMain)
        timeFinish.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                TimePickerDialog(this, timeSetListener2, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }
        timeFinish.setOnClickListener {
            TimePickerDialog(
                this,
                timeSetListener2,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }
    }


    override fun onStart() {
        super.onStart()
        val event= intent.getSerializableExtra("event") as Event?
        if(event!=null)
        {
            title.setText(event.title)
            desc.setText(event.description)
            place.setText(event.place)
            adapter.open()
            date.date = adapter.getDay(event.idDay)?.date?.time!!
            adapter.close()
            timeStart.setText(event.timeStart.toString())
            timeFinish.setText(event.timeStop.toString())
            findViewById<Button>(R.id.buttonAdd).visibility=View.GONE
            findViewById<Button>(R.id.buttonDelete).setOnClickListener{
                adapter.open()
                adapter.deleteEvent(event.id)
                val day = adapter.getDay(event.idDay)
                if (day != null) {
                    if(day.events.isEmpty())
                        adapter.deleteDay(day.id)
                }
                adapter.close()
                finish()
            }
            findViewById<Button>(R.id.buttonEdit).setOnClickListener{
                adapter.open()
                if(adapter.getDay(selectedDate)==null) {
                    adapter.insertDay(DayOfWeek(-1, ArrayList(), Date(selectedDate)))
                }
                adapter.updateEvent(Event(event.id, adapter.getDay(selectedDate)!!.id, Time(longtimeStart),Time(longtimeStop),title.text.toString()
                    ,place.text.toString(),desc.text.toString()))
                val day = adapter.getDay(event.idDay)
                if (day != null) {
                    if(day.events.isEmpty())
                        adapter.deleteDay(day.id)
                }
                adapter.close()
                finish()
            }
        }
        else
        {
            findViewById<Button>(R.id.buttonAdd).setOnClickListener{
                adapter.open()
                val day =adapter.getDay(selectedDate)
                if(day==null) {
                    adapter.insertDay(DayOfWeek(-1, ArrayList(),Date(selectedDate)))
                }
                adapter.insertEvent(Event(-1, adapter.getDay(selectedDate)!!.id, Time(longtimeStart),Time(longtimeStop),title.text.toString()
                    ,place.text.toString(),desc.text.toString()))
                adapter.close()
                finish()
            }
            findViewById<Button>(R.id.buttonDelete).visibility=View.GONE
            findViewById<Button>(R.id.buttonEdit).visibility=View.GONE
        }
    }
}