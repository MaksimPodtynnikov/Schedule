package ru.everyday.schedule.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import ru.everyday.schedule.R
import ru.everyday.schedule.model.DayOfWeek

class DayAdapter (context: Context?, private val layout: Int, private val days: List<DayOfWeek>) :
ArrayAdapter<DayOfWeek?>(
context!!, layout, days
) {
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        @SuppressLint("ViewHolder") val view = inflater.inflate(layout, parent, false)
        val dateText = view.findViewById<TextView>(R.id.dateText)
        val listDayEvents = view.findViewById<ListView>(R.id.listDayEvents)
        val day = days[position]
        listDayEvents.adapter=EventAdapter(context,R.layout.element_event,day.events)
        dateText.text = day.date.toString()
        return view
    }
}