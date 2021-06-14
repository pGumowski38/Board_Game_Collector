package com.ubi.boardgamecollector

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.content.ContentValues
import androidx.core.database.getStringOrNull

class MyDBHandler(context: Context, name: String?,
                  factory: SQLiteDatabase.CursorFactory?, version: Int)
    :SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION){

        companion object {
            private const val DATABASE_VERSION = 1
            private const val DATABASE_NAME = "boardGamesDB.db"
            const val TABLE_BOARDGAMES = "board_games"
            const val COLUMN_ID = "_id"
            const val COLUMN_TITLE = "title"
            const val COLUMN_ORIGINAL = "original_title"
            const val COLUMN_YEAR = "publish_year"
            const val COLUMN_AUTHORS = "authors"
            const val COLUMN_GRAPHICS = "graphics"
            const val COLUMN_DESCRIPTION = "description"
            const val COLUMN_ORDER = "order_date"
            const val COLUMN_OWN = "own_date"
            const val COLUMN_COST = "cost"
            const val COLUMN_SCD = "scd"
            const val COLUMN_EANUPC = "eanupc_code"
            const val COLUMN_BGG_ID = "bgg_id"
            const val COLUMN_PRODUCTION_CODE = "production_code"
            const val COLUMN_RANK = "bgg_rank"
            const val COLUMN_BASEDLC = "base_dlc"
            const val COLUMN_COMMENT = "comment"
            const val COLUMN_IMAGE = "image"
            const val COLUMN_MINI_IMAGE = "mini_image"
            const val COLUMN_LOCALIZATION = "localization"
        }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_BOARD_GAMES_TABLE = ("CREATE TABLE $TABLE_BOARDGAMES (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_TITLE TEXT," +
                "$COLUMN_ORIGINAL TEXT," +
                "$COLUMN_YEAR INTEGER," +
                "$COLUMN_AUTHORS TEXT," +
                "$COLUMN_GRAPHICS TEXT," +
                "$COLUMN_DESCRIPTION TEXT," +
                "$COLUMN_ORDER TEXT," +
                "$COLUMN_OWN TEXT," +
                "$COLUMN_COST TEXT," +
                "$COLUMN_SCD TEXT," +
                "$COLUMN_EANUPC TEXT," +
                "$COLUMN_BGG_ID INTEGER," +
                "$COLUMN_PRODUCTION_CODE TEXT," +
                "$COLUMN_RANK TEXT," +
                "$COLUMN_BASEDLC TEXT," +
                "$COLUMN_COMMENT TEXT," +
                "$COLUMN_LOCALIZATION TEXT," +
                "$COLUMN_IMAGE TEXT," +
                "$COLUMN_MINI_IMAGE TEXT)")

        db?.execSQL(CREATE_BOARD_GAMES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_BOARDGAMES")
        onCreate(db)
    }

    fun addNewGame(boardGame: BoardGame) {
        val values = ContentValues()
        values.put(COLUMN_TITLE, boardGame.title)
        values.put(COLUMN_ORIGINAL, boardGame.originalTitle)
        values.put(COLUMN_YEAR, boardGame.year)
        values.put(COLUMN_AUTHORS, boardGame.authors)
        values.put(COLUMN_GRAPHICS, boardGame.graphics)
        values.put(COLUMN_DESCRIPTION, boardGame.description)
        values.put(COLUMN_ORDER, boardGame.orderDate)
        values.put(COLUMN_OWN, boardGame.ownDate)
        values.put(COLUMN_COST, boardGame.cost)
        values.put(COLUMN_SCD, boardGame.scd)
        values.put(COLUMN_EANUPC, boardGame.codeEAN_UPC)
        values.put(COLUMN_BGG_ID, boardGame.bggID)
        values.put(COLUMN_PRODUCTION_CODE, boardGame.productionCode)
        values.put(COLUMN_RANK, boardGame.rank)
        values.put(COLUMN_BASEDLC, boardGame.baseOrDLC)
        values.put(COLUMN_COMMENT, boardGame.comment)
        values.put(COLUMN_LOCALIZATION, boardGame.localization)
        values.put(COLUMN_IMAGE, boardGame.image)
        values.put(COLUMN_MINI_IMAGE, boardGame.miniImage)

        val db = this.writableDatabase
        db.insert(TABLE_BOARDGAMES, null, values)
        db.close()
    }

    fun gameDetails(id: Int): BoardGame {
        val yourGame = BoardGame()
        val query = "SELECT * FROM $TABLE_BOARDGAMES WHERE $COLUMN_ID = $id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            yourGame.id = cursor.getInt(0)
            yourGame.title = cursor.getStringOrNull(1)
            yourGame.originalTitle = cursor.getStringOrNull(2)
            yourGame.year = cursor.getInt(3)
            yourGame.authors = cursor.getStringOrNull(4)
            yourGame.graphics = cursor.getStringOrNull(5)
            yourGame.description = cursor.getStringOrNull(6)
            yourGame.orderDate = cursor.getStringOrNull(7)
            yourGame.ownDate = cursor.getStringOrNull(8)
            yourGame.cost = cursor.getStringOrNull(9)
            yourGame.scd = cursor.getStringOrNull(10)
            yourGame.codeEAN_UPC = cursor.getStringOrNull(11)
            yourGame.bggID = cursor.getLong(12)
            yourGame.productionCode = cursor.getStringOrNull(13)
            yourGame.rank = cursor.getStringOrNull(14)
            yourGame.baseOrDLC = cursor.getStringOrNull(15)
            yourGame.comment = cursor.getStringOrNull(16)
            yourGame.localization = cursor.getStringOrNull(17)
            yourGame.image = cursor.getStringOrNull(18)
            yourGame.miniImage = cursor.getStringOrNull(19)
        }
        cursor.close()
        db.close()

        return yourGame
    }

    fun editGame(id: Int, boardGame: BoardGame) {
        val cv = ContentValues()

        if (!boardGame.originalTitle.isNullOrEmpty()) {
            cv.put(COLUMN_ORIGINAL, boardGame.originalTitle)
        }
        if (boardGame.year != null) {
            cv.put(COLUMN_YEAR, boardGame.year)
        }
        if (!boardGame.authors.isNullOrEmpty()) {
            cv.put(COLUMN_AUTHORS, boardGame.authors)
        }
        if (!boardGame.graphics.isNullOrEmpty()) {
            cv.put(COLUMN_GRAPHICS, boardGame.graphics)
        }
        if (!boardGame.description.isNullOrEmpty()) {
            cv.put(COLUMN_DESCRIPTION, boardGame.description)
        }
        if (!boardGame.baseOrDLC.isNullOrEmpty()) {
            cv.put(COLUMN_BASEDLC, boardGame.baseOrDLC)
        }
        if (!boardGame.comment.isNullOrEmpty()) {
            cv.put(COLUMN_COMMENT, boardGame.comment)
        }
        if (!boardGame.localization.isNullOrEmpty()) {
            cv.put(COLUMN_LOCALIZATION, boardGame.localization)
        }
        if (!boardGame.orderDate.isNullOrEmpty()) {
            cv.put(COLUMN_ORDER, boardGame.orderDate)
        }
        if (!boardGame.ownDate.isNullOrEmpty()) {
            cv.put(COLUMN_OWN, boardGame.ownDate)
        }
        if (!boardGame.cost.isNullOrEmpty()) {
            cv.put(COLUMN_COST, boardGame.cost)
        }
        if (!boardGame.scd.isNullOrEmpty()) {
            cv.put(COLUMN_SCD, boardGame.scd)
        }
        if (!boardGame.codeEAN_UPC.isNullOrEmpty()) {
            cv.put(COLUMN_EANUPC, boardGame.codeEAN_UPC)
        }
        if (!boardGame.productionCode.isNullOrEmpty()) {
            cv.put(COLUMN_PRODUCTION_CODE, boardGame.productionCode)
        }

        val db = this.writableDatabase
        db.update(TABLE_BOARDGAMES, cv, "$COLUMN_ID = ?", arrayOf(id.toString()))

        db.close()
    }

    fun getYourGames(): ArrayList<BoardGame> {
        val yourGames = ArrayList<BoardGame>()
        val query = "SELECT * FROM $TABLE_BOARDGAMES"

        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val bGame = BoardGame()
            bGame.id = cursor.getInt(0)
            bGame.title = cursor.getStringOrNull(1)
            bGame.originalTitle = cursor.getStringOrNull(2)
            bGame.year = cursor.getInt(3)
            bGame.authors = cursor.getStringOrNull(4)
            bGame.graphics = cursor.getStringOrNull(5)
            bGame.description = cursor.getStringOrNull(6)
            bGame.orderDate = cursor.getStringOrNull(7)
            bGame.ownDate = cursor.getStringOrNull(8)
            bGame.cost = cursor.getStringOrNull(9)
            bGame.scd = cursor.getStringOrNull(10)
            bGame.codeEAN_UPC = cursor.getStringOrNull(11)
            bGame.bggID = cursor.getLong(12)
            bGame.productionCode = cursor.getStringOrNull(13)
            bGame.rank = cursor.getStringOrNull(14)
            bGame.baseOrDLC = cursor.getStringOrNull(15)
            bGame.comment = cursor.getStringOrNull(16)
            bGame.localization = cursor.getStringOrNull(17)
            bGame.image = cursor.getStringOrNull(18)
            bGame.miniImage = cursor.getStringOrNull(19)

            yourGames.add(bGame)
        }
        cursor.close()
        db.close()

        return yourGames
    }

    fun deleteGame(id: Int): Boolean {
        var result = false
        val query = "SELECT * FROM $TABLE_BOARDGAMES WHERE $COLUMN_ID = $id"

        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            db.delete(TABLE_BOARDGAMES, "$COLUMN_ID = ?", arrayOf(id.toString()))
            cursor.close()
            result = true
        }
        db.close()
        return result
    }

}