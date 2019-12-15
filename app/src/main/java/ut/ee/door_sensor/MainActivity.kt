package ut.ee.door_sensor

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    var doorsList: MutableList<Door> = mutableListOf()
    lateinit var myCustomAdapter: CustomAdapter
    var notificationsEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //requestPermissions?

        initialiseDoorsList()
        getDoors()
        Log.i(TAG, "Notifications enabled: $notificationsEnabled")

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
            notificationsEnabled = data!!.getBooleanExtra("notifications_enabled", true)
            Log.i(TAG, "Notifications enabled: $notificationsEnabled")
        }
    }


    private fun initialiseDoorsList() {
        myCustomAdapter = CustomAdapter(doorsList, this)
        doors_listview.adapter = myCustomAdapter
    }


    fun addDoorToList(door: Door) {
        // TODO: add to firestore as well -> how do we "connect" the new adapter?
        doorsList.add(door)
        myCustomAdapter.notifyDataSetChanged()
    }


    private fun getDoors() {
        // TODO: fetch door list from repo -> how do we add new doors? do we implement this or
        // just add it to the report as a potential future development thing

        // For developing create some dummy door components
        // TODO: Delete later
        for (i in 0..10) {
            val door = Door()

            val date = Calendar.getInstance().time
            val formatter = SimpleDateFormat.getDateTimeInstance()
            val formatedDate = formatter.format(date)


            val dateTime: String = formatedDate.toString()
            var lastOpenedArray: Array<String> = arrayOf(dateTime)

            door.id = i.toLong()
            door.isOpenState = true
            door.name = "Door".plus(i.toString())
            door.lastOpened = lastOpenedArray

            Log.i("SIIN", "DOOR: ${door.name}")

            addDoorToList(door)
        }
    }

    fun startDetailsActivity(door: Door, position: Int) {
        val intent = Intent(this, DoorDetailsActivity::class.java)
        val doorJson = Gson().toJson(door)
        Log.i(TAG, "Sending door info to details activity: $doorJson")
        intent.putExtra("door", doorJson)
        intent.putExtra("position", position)
        startActivity(intent)
    }

    /**
     * Dummy function containing Firestore code snippets from setup
     */
    fun firestore() {
        val db = FirebaseFirestore.getInstance()
        // Create a new user with a first and last name
        val user1 = hashMapOf<String, Any>()
        user1["first"] = "Ada"
        user1["last"] = "Lovelace"
        user1["born"] = 1815


        /**
         * Adding data
         */

        // Add a new document with a generated ID
        db.collection("users")
            .add(user1)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    TAG,
                    "DocumentSnapshot added with ID: " + documentReference.id
                )
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }

        // Create a new user with a first, middle, and last name
        val user2 = hashMapOf<String, Any>()
        user2["first"] = "Alan"
        user2["middle"] = "Mathison"
        user2["last"] = "Turing"
        user2["born"] = 1912

        // Add a new document with a generated ID
        db.collection("users")
            .add(user2)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    TAG,
                    "DocumentSnapshot added with ID: " + documentReference.id
                )
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }

        /**
         * Read data
         */
        db.collection("users")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d(TAG, document.id + " => " + document.data)
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }
}
