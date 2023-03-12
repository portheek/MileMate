package com.example.milemate

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.NumberPicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.Writer
import java.nio.file.Files.createFile
import java.text.DateFormat.getDateTimeInstance
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CheckUpFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_check_up, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CheckUpPage_DatePicker_Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CheckUpFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val numPickerYear = view.findViewById<NumberPicker>(R.id.numberPickerYear)
        val numPickerMonth = view.findViewById<NumberPicker>(R.id.numberPickerMonth)
        val numPickerDay = view.findViewById<NumberPicker>(R.id.numberPickerDay)



        val checkUpSaveBtn = view.findViewById<Button>(R.id.CheckUpSaveBtn)
        val datePicker = view.findViewById<DatePicker>(R.id.CheckUpDatePicker)

        val calendar = Calendar.getInstance()
        datePicker.minDate = calendar.timeInMillis+24*60*60*1000//set min available date to tomorrow in datePicker


        //dynamic adjustment of numPickers. This ensures that negative date can't be set for reminder.
        datePicker.setOnDateChangedListener { datePicker, Year, Month, Day ->

            //subtract current date from selected date to get time difference.
            val year = Year - calendar.get(Calendar.YEAR)
            val month = Month - calendar.get(Calendar.MONTH)
            val day = Day - calendar.get(Calendar.DAY_OF_MONTH)

            numPickerYear.minValue = 0
            numPickerYear.maxValue = year

            numPickerMonth.minValue = 0
            numPickerMonth.maxValue = month

            numPickerDay.minValue = 0
            numPickerDay.maxValue = day

            //TODO Fix numPickers. This is shit implementation. if check-up date is exactly year later, user has to select year in numPickerYear, numPickerMonth will have max num as 0, even tho it should have it as 12.
        }

        checkUpSaveBtn.setOnClickListener{

            calendar.set(datePicker.year, datePicker.month, datePicker.dayOfMonth)//set calendar object to selected date in datePicker

            //subtraction from selected date in date picker
            calendar.add(Calendar.YEAR, -numPickerYear.value)//Current year - numpicker.Year
            calendar.add(Calendar.MONTH, -numPickerMonth.value)
            calendar.add(Calendar.DAY_OF_MONTH, -numPickerDay.value)


            val dateFormat = getDateTimeInstance()
            val reminderDate = dateFormat.format(calendar.time)

            //val reminderObject = Reminder(calendar, calendarSet)


            //val output1 = Gson().toJson(reminderObject)
/*
            val output: Writer
            val file = createFile(getApplicationInfo().dataDir)
            output = BufferedWriter(FileWriter(file))
            output.write(output1)
            output.close()

            it.write(output)
*/


            Toast.makeText(activity, "Reminder set for $reminderDate", Toast.LENGTH_SHORT).show()
        }

    }
}