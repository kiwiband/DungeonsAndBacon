package kiwiband.dnb

import org.json.JSONObject

interface JSONSerializable {
    fun toJSON(): JSONObject
}