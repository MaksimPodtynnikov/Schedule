package ru.everyday.schedule.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.set
import ru.everyday.schedule.EventActivity
import ru.everyday.schedule.R
import ru.everyday.schedule.model.Event

class EventAdapter (context: Context?, private val layout: Int, private val events: List<Event>) :
    ArrayAdapter<Event?>(
        context!!, layout, events
    ) {
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        @SuppressLint("ViewHolder") val view = inflater.inflate(layout, parent, false)
        val timeStart = view.findViewById<TextView>(R.id.timeStart)
        val timeStop = view.findViewById<TextView>(R.id.timeStop)
        val titleText = view.findViewById<TextView>(R.id.titleText)
        val elementEvent =view.findViewById<ConstraintLayout>(R.id.elementEvent)
        val event = events[position]
        elementEvent.setOnClickListener{
            val intent =Intent(context,EventActivity::class.java)
            intent.putExtra("event",event)
            context.startActivity(intent)
        }
        timeStart.text = event.timeStart.toString()
        timeStop.text = event.timeStop.toString()
        titleText.text = event.title
        return view
    }
}