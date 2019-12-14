package ut.ee.door_sensor

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.door_details.*

class DoorDetailsActivity : AppCompatActivity() {

    private val TAG = "DoorDetailsActivity"

    private var door: Door? = null
    private var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.door_details)

        initTargetDoor()
        addDoorDetails(door!!)

        details_view_stats.setOnClickListener {
            // TODO will the chart just be a histogram of "most active" times of the day
            // i.e. a bar for every two hours of the day
//            startStatisticsActivity()
        }

        back_button.setOnClickListener {
            finish()
        }

    }

    private fun addDoorDetails(door: Door) {
        details_door_name.text = door.name
        details_is_open_state.text = door.deriveDoorStateText()
        details_state_changes.text = "Here are previous state changes times...\n\n\n\n\n\n\n" //TODO
    }

    private fun initTargetDoor() {
        val doorJson = intent.getStringExtra("door")
        position = intent.getIntExtra("position", -1)
        door = Gson().fromJson(doorJson, Door::class.java)
        Log.i(TAG,"Received door: $door with position: $position")
    }
}