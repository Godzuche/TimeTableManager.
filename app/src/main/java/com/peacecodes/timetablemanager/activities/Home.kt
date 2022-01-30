package com.peacecodes.timetablemanager.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText
import com.peacecodes.timetablemanager.R
import com.peacecodes.timetablemanager.SQLiteHelper
import com.peacecodes.timetablemanager.adapters.ViewPagerAdapter
import com.peacecodes.timetablemanager.databinding.ActivityHomeBinding
import com.peacecodes.timetablemanager.models.TimeTable

class Home : AppCompatActivity() {
    private lateinit var sqliteHelper: SQLiteHelper
private lateinit var binding: ActivityHomeBinding
    var tabTitle =
        arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sqliteHelper = SQLiteHelper(this)

        binding.apply {
            // connecting the viewpager and the adapter class
            viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

            //initializing and setting the title of the fragments
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabTitle[position]
            }.attach()
        }
        addSchedules()

    }
    private fun addSchedules() {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.edit_dialog, null)
        builder.setView(view)

        val spinner = view.findViewById<Spinner>(R.id.select_day)
        val adapter = ArrayAdapter(this@Home,
            android.R.layout.simple_spinner_item, resources.getStringArray(R.array.days))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.onItemSelectedListener
        spinner.adapter

        val saveBtn = view.findViewById<Button>(R.id.save_btn)
        val title = view.findViewById<TextInputEditText>(R.id.course_title)
        val start_time = view.findViewById<TextInputEditText>(R.id.start_time)
        val end_time = view.findViewById<TextInputEditText>(R.id.end_time)
        val code = view.findViewById<TextInputEditText>(R.id.course_code)

        val alertDialog = builder.create()
        saveBtn.setOnClickListener {
           val result = sqliteHelper.insertTimeTable(TimeTable(
               day = spinner.selectedItem.toString(), course_title = title.text.toString(),
               start_time = start_time.text.toString(), end_time = end_time.text.toString(),
               course_code = code.text.toString()))
            if (result == true){
                Toast.makeText(this, "Record Saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Record not saved", Toast.LENGTH_SHORT).show()
            }
           alertDialog.dismiss()
        }

        binding.floatingActionButton.setOnClickListener {
            alertDialog.show()
        }

    }

}

