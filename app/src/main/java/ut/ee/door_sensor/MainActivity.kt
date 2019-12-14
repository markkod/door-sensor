package ut.ee.door_sensor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var doorsList: MutableList<Door> = mutableListOf()

    lateinit var myCustomAdapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialiseDoorsList()
        getDoors()
    }

    fun initialiseDoorsList() {
        myCustomAdapter = CustomAdapter(doorsList, this)
        doors_listview.adapter = myCustomAdapter
    }


    fun addDoorToList(door: Door) {

        doorsList.add(door)
        myCustomAdapter.notifyDataSetChanged()
    }


    fun getDoors() {

        // For developing create some dummy door components
        // TODO: Delete later
        for (i in 0..10) {
            val door = Door()

            val date = Calendar.getInstance().time
            val formatter = SimpleDateFormat.getDateTimeInstance()
            val formatedDate = formatter.format(date)


            val dateTime: String = formatedDate.toString()
            var lastOpenedArray: Array<String> = arrayOf(dateTime)

            door.id = i.toString()
            door.isOpenState = true
            door.name = "Door".plus(i.toString())
            door.lastOpened = lastOpenedArray

            Log.i("SIIN", "DOOR: ${door.name}")

            addDoorToList(door)
        }
    }
}
