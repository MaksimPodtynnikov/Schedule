package ru.everyday.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import ru.everyday.schedule.adapters.DayAdapter
import ru.everyday.schedule.dataBase.dataBaseAdapter
import ru.everyday.schedule.databinding.FragmentFirstBinding
import ru.everyday.schedule.model.DayOfWeek

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(R.layout.fragment_first) {
    var days: ArrayList<DayOfWeek> = ArrayList()
    private lateinit var adapter: dataBaseAdapter
    private lateinit var dayAdapter: DayAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = dataBaseAdapter(view.context)
        dayAdapter = DayAdapter(this.activity, R.layout.element_day, days)
        view.findViewById<ListView>(R.id.listEvents)?.adapter = dayAdapter
    }

    override fun onResume() {
        super.onResume()
        adapter.open()
        days.clear()
        days.addAll(adapter.days)
        adapter.close()
        dayAdapter.notifyDataSetChanged()
    }
}