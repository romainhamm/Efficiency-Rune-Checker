package me.romainhamm.efficiencyrunechecker.parsing.outputmodel

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = false)
@Parcelize
data class RuneOutput(
    @Json(name = "rune_id") val runeId: Long,
    @Json(name = "slot_no") val slot: Int,
    @Json(name = "set_id") val setType: Int,
    @Json(name = "upgrade_curr") val upgradeCurrent: Int,
    @Json(name = "pri_eff") val mainStatEffect: List<Int>,
    @Json(name = "prefix_eff") val innateStatEffect: List<Int>,
    @Json(name = "sec_eff") val secondaryStatEffect: List<List<Int>>,
    @Json(name = "rank") val rank: Int,
    @Json(name = "extra") val baseRank: Int,
    @Json(name = "class") val stars: Int
) : Parcelable
