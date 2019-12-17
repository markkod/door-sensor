package ut.ee.door_sensor

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.door_statistics.*

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

            // TODO: will the chart just be a histogram of "most active" times of the day
            // i.e. a bar for every two hours of the day
        }
    }


}