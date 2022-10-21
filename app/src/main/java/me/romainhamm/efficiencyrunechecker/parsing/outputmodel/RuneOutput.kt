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

/**
"rune_id": 26338494183,
"wizard_id": 13183017, // summoner id
"occupied_type": 2,
"occupied_id": 0,
"slot_no": 1,
"rank": 5,
"set_id": 1,
"upgrade_limit": 15,
"upgrade_curr": 15,
"base_value": 361380,
"sell_value": 27019,
"pri_eff": [ 3, 160 ], // 3 is atk flat, 160 is value
"prefix_eff": [ 0, 0 ], no innate
"sec_eff": [
[ 10, 11, 0, 0 ], // First number is effect so 10 = crit dmg, second one is base, third is and last is grind bonus
[ 4, 8, 0, 6 ],
[ 8, 17, 0, 3 ],
[ 2, 4, 1, 0 ]
],
"extra": 4
}
 **/
