package ut.ee.door_sensor

import java.util.*
import kotlin.collections.ArrayList

class Door(
    var id: Long? = null,
    var name: String? = null,
    var isOpenState: Boolean? = null,
    var lastOpened: HashMap<String, Long>? = null
) {
    fun deriveDoorStateText(): String {
        return if (this.isOpenState == true) {
            "Open"
        } else "Closed"
    }
}