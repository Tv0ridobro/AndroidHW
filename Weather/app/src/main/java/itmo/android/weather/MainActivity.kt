package itmo.android.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.weekinfo.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val date: Calendar = GregorianCalendar()
        when (date.get(Calendar.DAY_OF_WEEK)) {
            1 -> {
                dayOfWeek.text = "Sunday"
                todayIcon.setImageDrawable(sundayIcon.drawable)
                windToday.append(sundayWind.text)
                humidityToday.append(sundayHumidity.text)
                temperatureToday.append(sundayTemperature.text)
            }
            2 -> {
                dayOfWeek.text = "Monday"
                todayIcon.setImageDrawable(mondayIcon.drawable)
                windToday.append(mondayWind.text)
                humidityToday.append(mondayHumidity.text)
                temperatureToday.append(mondayTemperature.text)
            }
            3 -> {
                dayOfWeek.text = "Tuesday"
                todayIcon.setImageDrawable(tuesdayIcon.drawable)
                windToday.append(tuesdayWind.text)
                humidityToday.append(tuesdayHumidity.text)
                temperatureToday.append(tuesdayTemperature.text)
            }
            4 -> {
                dayOfWeek.text = "Wednesday"
                todayIcon.setImageDrawable(wednesdayIcon.drawable)
                windToday.append(wednesdayWind.text)
                humidityToday.append(wednesdayHumidity.text)
                temperatureToday.append(wednesdayTemperature.text)
            }
            5 -> {
                dayOfWeek.text = "Thursday"
                todayIcon.setImageDrawable(thursdayIcon.drawable)
                windToday.append(thursdayWind.text)
                humidityToday.append(thursdayHumidity.text)
                temperatureToday.append(thursdayTemperature.text)
            }
            6 -> {
                dayOfWeek.text = "Friday"
                todayIcon.setImageDrawable(fridayIcon.drawable)
                windToday.append(fridayWind.text)
                humidityToday.append(fridayHumidity.text)
                temperatureToday.append(fridayTemperature.text)
            }
            7 -> {
                dayOfWeek.text = "Saturday"
                todayIcon.setImageDrawable(saturdayIcon.drawable)
                windToday.append(saturdayWind.text)
                humidityToday.append(saturdayHumidity.text)
                temperatureToday.append(saturdayTemperature.text)
            }
        }
    }
    
}
