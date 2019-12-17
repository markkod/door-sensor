package ut.ee.door_sensor

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.door_statistics.*
import java.util.*
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.AnyChart
import java.text.SimpleDateFormat


class DoorStatisticsActivity : AppCompatActivity() {

    private val TAG = "DoorStatisticsActivity"

    private var door: Door? = null
    private var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.door_statistics)

        initTargetDoor()
        addDoorDetails(door!!)

        statistics_back_button.setOnClickListener {
            finish()
        }
    }

    private fun initTargetDoor() {
        val doorJson = intent.getStringExtra("door")
        position = intent.getIntExtra("position", -1)
        door = Gson().fromJson(doorJson, Door::class.java)
        Log.i(TAG,"Received door: $door with position: $position")
    }


    private fun addDoorDetails(door: Door) {
        if (door != null) {
            statistics_door_name.text = door.name
            loadChart()
        }
    }

    private fun loadChart() {
        val column = AnyChart.column()
        val data: MutableList<DataEntry> = mutableListOf()

        var temporaryArray: MutableList<Any> = mutableListOf()


        var timestampArray: MutableList<Long> = mutableListOf()
        timestampArray.addAll(door?.lastOpened!!.values)

        for (ts in timestampArray) {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date = Date(ts)
            val formattedDate = sdf.format(date)



            temporaryArray.add(formattedDate)
        }

        for (date in temporaryArray) {
            val count = temporaryArray.count { it == date }
            Log.i(TAG, "Formatted date, count: $date + $count")
            data.add(ValueDataEntry(date.toString(), count))
        }


        column.data(data)
        statistics_chart.setChart(column)
    }


}