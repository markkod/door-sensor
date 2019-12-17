package ut.ee.door_sensor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    var doorsList: MutableList<Door> = mutableListOf()
    lateinit var myCustomAdapter: CustomAdapter
    var notificationsEnabled: Boolean = true
    private lateinit var db: FirebaseDatabase
    private lateinit var doorsDb: DatabaseReference
    private lateinit var notificationsDb: FirebaseFirestore
    private lateinit var broadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //requestPermissions?
        initDb()
        fetchDbData()
        initBroadcastReceiver()
        initDoorsList()
    }

    override fun onDestroy() {
        LocalBroadcastManager
            .getInstance(this)
            .unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings, menu)

        val settings = menu?.findItem(R.id.menu_settings)
        settings?.setOnMenuItemClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("notifications_enabled", notificationsEnabled)
            startActivityForResult(intent, 1)
            true
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            val newNotificationState = data!!.getBooleanExtra("notifications_enabled", true)

            if (newNotificationState != notificationsEnabled) {
                Log.i(TAG, "New notifications state: $newNotificationState")
                updateNotificationStateInDb(newNotificationState)
            }
            Log.i(TAG, "Notifications enabled: $notificationsEnabled")

        }
    }

    private fun updateNotificationStateInDb(newNotificationState: Boolean) {
        Log.i(TAG, "Updating notifications state in DB to: $newNotificationState")
        notificationsEnabled = newNotificationState

        if (newNotificationState) {
            notificationsDb
                .collection("notifications")
                .document("state")
                .set(mapOf("enabled" to "true"))
        } else {
            notificationsDb
                .collection("notifications")
                .document("state")
                .set(mapOf("enabled" to "false"))
        }
    }


    private fun initDb() {
        db = FirebaseDatabase.getInstance()
        doorsDb = db.getReference("doors")
        notificationsDb = FirebaseFirestore.getInstance()
    }

    private fun initBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Log.i(TAG, "RECEIVED BROADCAST")
                if (notificationsEnabled) {
                    val notificationText = intent?.getStringExtra("notification_text")
                    Toast.makeText(applicationContext, notificationText, Toast.LENGTH_SHORT).show()
                }
            }

        }
        LocalBroadcastManager
            .getInstance(this)
            .registerReceiver(broadcastReceiver, IntentFilter("INTENT_FILTER"))
    }


    private fun initDoorsList() {
        myCustomAdapter = CustomAdapter(doorsList, this)
        doors_listview.adapter = myCustomAdapter
    }

    private fun fetchDbData() {
        fetchDoors()
        fetchNotificationsState()
    }

    private fun fetchDoors() {
        // Read from the database
        doorsDb.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.i(TAG, "onDataChange!")

                for (ds in dataSnapshot.children) {
                    val door = ds.getValue(Door::class.java)
                    doorsList.add(door as Door)
                }
                myCustomAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Error", "Failed to read value.", error.toException())
            }
        })
    }

    private fun fetchNotificationsState() {
        val documentReference = notificationsDb.collection("notifications")
            .document("state")
       documentReference.get().addOnSuccessListener { document ->
           setNotificationsState(document.data!!["enabled"].toString())
       }
    }

    private fun setNotificationsState(state: String) {
        Log.i(TAG, "Notifications state from DB: $state")
        notificationsEnabled = state == "true"
    }



    fun startDetailsActivity(door: Door, position: Int) {
        val intent = Intent(this, DoorDetailsActivity::class.java)
        val doorJson = Gson().toJson(door)
        Log.i(TAG, "Sending door info to details activity: $doorJson")
        intent.putExtra("door", doorJson)
        intent.putExtra("position", position)
        startActivity(intent)
    }

}
