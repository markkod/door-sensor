package ut.ee.door_sensor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.custom_list_item.view.*

class CustomAdapter(var objects: MutableList<Door>, var mainActivity: MainActivity) :
    BaseAdapter() {

    private val TAG = "Adapter"
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View

        view = if (convertView == null) {
            val layoutInflater = LayoutInflater.from(parent?.context)
            layoutInflater.inflate(R.layout.custom_list_item, parent, false)
        } else {
            convertView
        }

        val door = getItem(position)

        if (door.name !== null) {
            view.door_name_textView.text = door.name
        }

        view.setOnClickListener {
            mainActivity.startDetailsActivity(door, position)
        }

        return view
    }

    override fun getCount(): Int {
        return objects.size
    }

    override fun getItem(position: Int): Door {
        return objects[position]
    }

    override fun getItemId(position: Int): Long {
        return if (objects[position].id != null) {
            objects[position].id!!.toLong()
        } else 0L
    }
}
