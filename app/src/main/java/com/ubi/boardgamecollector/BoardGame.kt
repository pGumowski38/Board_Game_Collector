package com.ubi.boardgamecollector

import android.media.Image
import java.sql.Blob
import java.util.*

class BoardGame() {
    var id: Int = 0
    var title: String? = null
    var originalTitle: String? = null
    var year: Int? = 0
    var authors: String? = null
    var graphics: String? = null
    var description: String? = null
    var orderDate: String? = null
    var ownDate: String? = null
    var cost: String? = null
    var scd: String? = null
    var codeEAN_UPC: String? = null
    var bggID: Long? = 0
    var productionCode: String? = null
    var rank: String? = null
    var baseOrDLC: String? = null
    var comment: String? = null
    var image: String? = null
    var miniImage: String? = null
    var localization: String? = null

    override fun toString(): String {
        return "$title ($year)\n"
    }
}