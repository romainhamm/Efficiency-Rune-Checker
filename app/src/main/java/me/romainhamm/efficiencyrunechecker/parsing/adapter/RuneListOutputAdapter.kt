package me.romainhamm.efficiencyrunechecker.parsing.adapter

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.internal.Util
import me.romainhamm.efficiencyrunechecker.parsing.outputmodel.RuneOutput
import me.romainhamm.efficiencyrunechecker.parsing.util.readArray
import me.romainhamm.efficiencyrunechecker.parsing.util.readObject
import me.romainhamm.efficiencyrunechecker.parsing.util.skipNameAndValue
import java.lang.reflect.Type
import javax.inject.Inject

class RuneListOutputAdapter(
    private val elementAdapter: JsonAdapter<RuneOutput>,
    private val options: JsonReader.Options
) : JsonAdapter<List<RuneOutput>>() {

    class Factory @Inject constructor() : JsonAdapter.Factory {
        override fun create(type: Type, annotations: Set<Annotation>, moshi: Moshi): JsonAdapter<*>? {
            if (annotations.isNotEmpty() || Types.getRawType(type) != List::class.java) {
                return null
            }

            val elementType = Types.collectionElementType(type, List::class.java)
            val elementAdapter = moshi.adapter<RuneOutput>(elementType)
            return RuneListOutputAdapter(
                elementAdapter = elementAdapter,
                options = JsonReader.Options.of("runes")
            )
        }
    }

    override fun fromJson(reader: JsonReader): List<RuneOutput> {
        val sectionContent = mutableListOf<RuneOutput>()
        reader.readObject {
            when (it.selectName(options)) {
                0 -> {
                    it.readArray { reader ->
                        sectionContent.add(elementAdapter.fromJson(reader) ?: throw Util.unexpectedNull("runes", "runes", reader))
                    }
                }
                -1 -> it.skipNameAndValue()
            }
        }
        return sectionContent
    }

    override fun toJson(writer: JsonWriter, value: List<RuneOutput>?) {
        throw NullPointerException("ToJson not supported")
    }
}
