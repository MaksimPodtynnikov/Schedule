package ru.everyday.schedule

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.everyday.schedule.adapters.DayAdapter
import ru.everyday.schedule.dataBase.dataBaseAdapter
import ru.everyday.schedule.databinding.ActivityMainBinding
import ru.everyday.schedule.model.DayOfWeek

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fab =findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this,EventActivity::class.java)
            startActivity(intent)
        }
    }
}