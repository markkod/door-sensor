package ut.ee.door_sensor

class Door(
    var id: String? = null,
    var name: String? = null, var isOpenState: Boolean? = null,
    var lastOpened: Array<String>? = null
)