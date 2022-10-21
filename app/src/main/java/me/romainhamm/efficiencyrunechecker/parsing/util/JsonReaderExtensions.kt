package me.romainhamm.efficiencyrunechecker.parsing.util

import com.squareup.moshi.JsonReader

fun JsonReader.skipNameAndValue() {
    skipName()
    skipValue()
}

inline fun JsonReader.readObject(body: (reader: JsonReader) -> Unit) {
    beginObject()
    while (hasNext()) {
        body(this)
    }
    endObject()
}

inline fun JsonReader.readArray(body: (reader: JsonReader) -> Unit) {
    beginArray()
    while (hasNext()) {
        body(this)
    }
    endArray()
}

inline fun <T : Any> JsonReader.readArrayToList(body: (reader: JsonReader) -> T?): List<T> {
    val result = mutableListOf<T>()
    beginArray()
    while (hasNext()) {
        body(this)?.let {
            result.add(it)
        }
    }
    endArray()
    return result
}
