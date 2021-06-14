package com.ubi.boardgamecollector

import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream

class XmlPullParserHandler {
    private val boardGames = ArrayList<BoardGame>()
    private var boardGame: BoardGame? = null
    private var text: String? = null

    fun parse(inputStream: InputStream): List<BoardGame> {
        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(inputStream, null)
            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagname = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> if (tagname.equals("item", ignoreCase = true)) {
                        // create a new instance of boardGame
                        boardGame = BoardGame()
                        boardGame?.bggID = (parser.getAttributeValue(null, "id")).toLong()
                    }
                    XmlPullParser.END_TAG -> if (tagname.equals("item", ignoreCase = true)) {
                        // add BoardGame object to list
                        boardGame?.let { boardGames.add(it) }
                    } else if (tagname.equals("name", ignoreCase = true)) {
                        boardGame!!.title = parser.getAttributeValue(null, "value")
                    } else if (tagname.equals("yearpublished", ignoreCase = true)) {
                        boardGame!!.year = Integer.parseInt(parser.getAttributeValue(null, "value"))
                    }
                    else -> {
                    }
                }
                eventType = parser.next()
            }

        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return boardGames
    }

    fun parseGameDetails(inputStream: InputStream, bGame: BoardGame) {
        try {
            val gAuthors = StringBuilder()
            val gGraphics = StringBuilder()
            var isBase = true

            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(inputStream, null)
            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagname = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG ->  {}
                    XmlPullParser.TEXT -> text = parser.text
                    XmlPullParser.END_TAG ->
                        if (tagname.equals("name", ignoreCase = true)) {
                            if (parser.getAttributeValue(null, "type") == "primary") {
                                bGame.originalTitle = parser.getAttributeValue(null, "value")
                            }
                        } else if (tagname.equals("image", ignoreCase = true)) {
                            bGame.image = text
                        } else if (tagname.equals("thumbnail", ignoreCase = true)) {
                            bGame.miniImage = text
                        } else if (tagname.equals("yearpublished", ignoreCase = true)) {
                            bGame.year = Integer.parseInt(parser.getAttributeValue(null, "value"))
                        } else if (tagname.equals("description", ignoreCase = true)) {
                            text = text?.replace("&mdash;", "\n")
                            text = text?.replace("&#10;&#10;", "\n")
                            bGame.description = text
                        } else if (tagname.equals("link", ignoreCase = true)) {
                            if (parser.getAttributeValue(null, "type") == "boardgamedesigner") {
                                gAuthors.append(parser.getAttributeValue(null, "value"))
                                    .append("\n")
                            }
                            else if (parser.getAttributeValue(null, "type") == "boardgameartist") {
                                gGraphics.append(parser.getAttributeValue(null, "value"))
                                    .append("\n")
                            }
                            else if (parser.getAttributeValue(null, "type") == "boardgamecategory") {
                                if (parser.getAttributeValue(null, "value").contains("Expansion")) {
                                    isBase = false
                                }
                            }
                        } else if (tagname.equals("rank", ignoreCase = true)) {
                            if (parser.getAttributeValue(null, "type") == "subtype" &&
                                parser.getAttributeValue(null, "name") == "boardgame") {

                                bGame.rank = parser.getAttributeValue(null, "value")
                            }
                        }
                        else -> {
                        }
                }
                eventType = parser.next()
            }

            bGame.authors = gAuthors.toString()
            bGame.graphics = gGraphics.toString()
            if (isBase) {
                bGame.baseOrDLC = "Base Game"
            } else {
                bGame.baseOrDLC = "Expansion"
            }

        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun parseForCollection(inputStream: InputStream): List<BoardGame> {
        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(inputStream, null)
            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagname = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> if (tagname.equals("item", ignoreCase = true)) {
                        // create a new instance of Board Game
                        boardGame = BoardGame()
                        boardGame?.bggID = (parser.getAttributeValue(null, "objectid")).toLong()
                        boardGame?.comment = ""
                    }
                    XmlPullParser.TEXT -> text = parser.text
                    XmlPullParser.END_TAG -> if (tagname.equals("item", ignoreCase = true)) {
                        // add BoardGame object to list
                        boardGame?.let { boardGames.add(it) }
                    } else if (tagname.equals("name", ignoreCase = true)) {
                        boardGame!!.title = text
                    } else if (tagname.equals("yearpublished", ignoreCase = true)) {
                        boardGame!!.year = text?.toInt()
                    } else if (tagname.equals("thumbnail", ignoreCase = true)) {
                        boardGame!!.miniImage = text
                    } else if (tagname.equals("rank", ignoreCase = true)) {
                        if (parser.getAttributeValue(null, "type") == "subtype" &&
                            parser.getAttributeValue(null, "name") == "boardgame") {

                            boardGame!!.rank = parser.getAttributeValue(null, "value")
                        }
                    } else if (tagname.equals("comment", ignoreCase = true)) {
                        boardGame!!.comment = text
                    }
                    else -> {
                    }
                }
                eventType = parser.next()
            }

        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return boardGames
    }
}