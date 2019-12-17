package ut.ee.door_sensor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    var doorsList: MutableList<Door> = mutableListOf()
    lateinit var myCustomAdapter: CustomAdapter
    private var notificationsEnabled = true
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //requestPermissions?
        db = FirebaseDatabase.getInstance().getReference("doors")

        getDoors()
        initialiseDoorsList()
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

    /**
     * TODO MOVE THIS ASYNCTASK, when it is in onCreate, then the response wont come fast enough and the items are not displayed
     */
    private fun getDoors() {
        // Read from the database
        db.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.i(TAG, "onDataChange!")

                for (ds in dataSnapshot.children) {
                    val door = ds.getValue(Door::class.java!!)
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
