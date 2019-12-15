package ut.ee.door_sensor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        val switch = findViewById<Switch>(R.id.notifications_switch)
        switch.isChecked = intent.getBooleanExtra("notifications_enabled", true)
        switch.setOnCheckedChangeListener { _, _ ->
            val switchStateText = getSwitchStateChangeText(switch.isChecked)
            Toast.makeText(this, "Notifications turned $switchStateText", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onBackPressed() {
        val intent = Intent()
        val switch = findViewById<Switch>(R.id.notifications_switch)
        intent.putExtra("notifications_enabled", switch.isChecked)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }

    private fun getSwitchStateChangeText(checked: Boolean): String {
        return if (checked) {
            "ON"
        } else "OFF"
    }
}