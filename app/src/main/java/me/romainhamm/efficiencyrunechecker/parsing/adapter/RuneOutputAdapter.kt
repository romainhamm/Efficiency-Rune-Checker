package me.romainhamm.efficiencyrunechecker.parsing.adapter

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.internal.Util
import me.romainhamm.efficiencyrunechecker.parsing.outputmodel.RuneOutput
import me.romainhamm.efficiencyrunechecker.parsing.util.readArray
import me.romainhamm.efficiencyrunechecker.parsing.util.readArrayToList
import me.romainhamm.efficiencyrunechecker.parsing.util.readObject
import me.romainhamm.efficiencyrunechecker.parsing.util.skipNameAndValue
import java.lang.reflect.Type
import javax.inject.Inject

class RuneOutputAdapter(
    private val longAdapter: JsonAdapter<Long>,
    private val intAdapter: JsonAdapter<Int>,
    private val options: JsonReader.Options
) : JsonAdapter<RuneOutput>() {

    class Factory @Inject constructor() : JsonAdapter.Factory {
        override fun create(type: Type, annotations: Set<Annotation>, moshi: Moshi): JsonAdapter<*>? {
            if (annotations.isNotEmpty() || Types.getRawType(type) != RuneOutput::class.java) {
                return null
            }

            val longAdapter: JsonAdapter<Long> = moshi.adapter(Long::class.java)
            val intAdapter: JsonAdapter<Int> = moshi.adapter(Int::class.java)

            val options: JsonReader.Options =
                JsonReader.Options.of("rune_id", "slot_no", "set_id", "upgrade_curr", "pri_eff", "prefix_eff", "sec_eff", "rank", "extra", "class")
            return RuneOutputAdapter(
                longAdapter = longAdapter,
                intAdapter = intAdapter,
                options = options
            )
        }
    }

    override fun toString(): String = buildString(32) {
        append("GeneratedJsonAdapter(").append("RuneOutput").append(')')
    }

    override fun fromJson(reader: JsonReader): RuneOutput {
        var runeId: Long? = null
        var slot: Int? = null
        var setType: Int? = null
        var upgradeCurrent: Int? = null
        var mainStatEffect: List<Int>? = null
        var innateStatEffect: List<Int>? = null
        var secondaryStatEffect: List<List<Int>>? = null
        var quality: Int? = null
        var baseQuality: Int? = null
        var runeClass: Int? = null
        reader.readObject {
            when (it.selectName(options)) {
                0 -> runeId = longAdapter.fromJson(it) ?: throw Util.unexpectedNull("runeId", "rune_id", it)
                1 -> slot = intAdapter.fromJson(it) ?: throw Util.unexpectedNull("slot", "slot_no", it)
                2 -> setType = intAdapter.fromJson(it) ?: throw Util.unexpectedNull("setType", "set_id", it)
                3 -> upgradeCurrent = intAdapter.fromJson(it) ?: throw Util.unexpectedNull("upgradeCurrent", "upgrade_curr", it)
                4 -> mainStatEffect = it.readArrayToList { reader -> reader.nextInt() }
                5 -> innateStatEffect = it.readArrayToList { reader -> reader.nextInt() }
                6 -> {
                    val result = mutableListOf<List<Int>>()
                    it.readArray { reader ->
                        val innerResult = mutableListOf<Int>()
                        reader.readArray { readerInner ->
                            innerResult.add(readerInner.nextInt())
                        }
                        result.add(innerResult)
                    }
                    secondaryStatEffect = result
                }
                7 -> quality = intAdapter.fromJson(it) ?: throw Util.unexpectedNull("quality", "rank", it)
                8 -> baseQuality = intAdapter.fromJson(it) ?: throw Util.unexpectedNull("baseQuality", "extra", it)
                9 -> runeClass = intAdapter.fromJson(it) ?: throw Util.unexpectedNull("runeClass", "class", it)
                -1 -> {
                    // Unknown name, skip it.
                    it.skipNameAndValue()
                }
            }
        }

        return RuneOutput(
            runeId = runeId ?: throw Util.missingProperty("runeId", "rune_id", reader),
            slot = slot ?: throw Util.missingProperty("slot", "slot_no", reader),
            setType = setType ?: throw Util.missingProperty("setType", "set_id", reader),
            upgradeCurrent = upgradeCurrent ?: throw Util.missingProperty("upgradeCurrent", "upgrade_curr", reader),
            mainStatEffect = mainStatEffect ?: throw Util.missingProperty("mainStatEffect", "pri_eff", reader),
            innateStatEffect = innateStatEffect ?: throw Util.missingProperty("innateStatEffect", "prefix_eff", reader),
            secondaryStatEffect = secondaryStatEffect ?: throw Util.missingProperty("secondaryStatEffect", "sec_eff", reader),
            quality = quality ?: throw Util.missingProperty("quality", "rank", reader),
            baseQuality = baseQuality ?: throw Util.missingProperty("baseQuality", "extra", reader),
            runeClass = runeClass ?: throw Util.missingProperty("runeClass", "class", reader),
        )
    }

    override fun toJson(writer: JsonWriter, value_: RuneOutput?) {
        throw NullPointerException("ToJson not supported")
    }
}
