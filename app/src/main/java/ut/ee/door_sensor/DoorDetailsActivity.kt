package ut.ee.door_sensor

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.door_details.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class DoorDetailsActivity : AppCompatActivity() {

    private val TAG = "DoorDetailsActivity"

    private var door: Door? = null
    private var position: Int? = null

    lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.door_details)

        initTargetDoor()
        addDoorDetails(door!!)

        details_view_stats.setOnClickListener {
            startStatisticsActivity(door, position)
        }

        back_button.setOnClickListener {
            finish()
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun generateDateStrings(): Array<Any> {
        val datePattern = "yyyy-MM-dd HH:mm:ss.SSS"

        var timestampArray: MutableList<Long> = mutableListOf()
        timestampArray.addAll(door?.lastOpened!!.values)

        var stringArray: MutableList<String> = mutableListOf()

        for (ts in timestampArray) {
            val sdf = SimpleDateFormat(datePattern)
            val date = Date(ts)
            val formattedDate = sdf.format(date)

            stringArray.add(formattedDate)
        }

        Collections.sort(stringArray, StringDateComparator())

        return stringArray.toTypedArray()
    }

    private fun initialiseDoorsList() {

        val datesArray = generateDateStrings()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, datesArray)
        details_state_changes_listView.adapter = adapter
        adapter.notifyDataSetChanged()

    }

    private fun startStatisticsActivity(door: Door?, position: Int?) {
        val intent = Intent(this, DoorStatisticsActivity::class.java)
        val doorJson = Gson().toJson(door)
        Log.i(TAG, "Sending door info to statistics activity: $doorJson")

        intent.putExtra("door", doorJson)
        intent.putExtra("position", position)
        startActivity(intent)
    }

    private fun addDoorDetails(door: Door) {
        details_door_name.text = door.name
        details_is_open_state.text = door.deriveDoorStateText()
        initialiseDoorsList()
    }

    private fun initTargetDoor() {
        val doorJson = intent.getStringExtra("door")
        position = intent.getIntExtra("position", -1)
        door = Gson().fromJson(doorJson, Door::class.java)
        Log.i(TAG,"Received door: $door with position: $position")
    }
}

internal class StringDateComparator : Comparator<String> {
    var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    override fun compare(lhs: String, rhs: String): Int {
        return dateFormat.parse(lhs)!!.compareTo(dateFormat.parse(rhs))
    }
}