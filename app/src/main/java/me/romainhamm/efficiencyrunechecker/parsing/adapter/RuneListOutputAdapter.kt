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
    private val firstLevelOptions: JsonReader.Options,
    private val mobsOptions: JsonReader.Options,
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
                firstLevelOptions = JsonReader.Options.of("unit_list", "runes"),
                mobsOptions = JsonReader.Options.of("runes")
            )
        }
    }

    override fun fromJson(reader: JsonReader): List<RuneOutput> {
        val sectionContent = mutableListOf<RuneOutput>()
        reader.readObject {
            when (it.selectName(firstLevelOptions)) {
                0 -> { // unit_list
                    reader.readArray {
                        reader.readObject {
                            when (reader.selectName(mobsOptions)) {
                                0 -> { // runes
                                    reader.readArray {
                                        sectionContent.add(elementAdapter.fromJson(reader) ?: throw Util.unexpectedNull("runes", "runes", reader))
                                    }
                                }
                                -1 -> reader.skipNameAndValue()
                            }
                        }
                    }
                }
                1 -> { // runes
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
