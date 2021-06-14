package com.ubi.boardgamecollector

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.net.MalformedURLException
import java.net.URI


/**
 * A simple [Fragment] subclass.
 * Use the [GameDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameDetailsFragment(bund: Bundle, prev: Fragment) : Fragment() {

    private var boardGame: BoardGame = BoardGame()
    private var bundle: Bundle = bund
    private var prevFrag: Fragment = prev
    var containerFindGame: ViewGroup? = null
    lateinit var manager: FragmentManager
    lateinit var transaction: FragmentTransaction

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        containerFindGame = container
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.gameDetGoBackButton).setOnClickListener {
            manager = activity?.supportFragmentManager!!
            transaction = manager.beginTransaction()
            if (containerFindGame != null) {
                transaction.remove(this)
                transaction.show(prevFrag)
                transaction.commit()
            }
        }

        boardGame.bggID = bundle.getLong("GameID")
        boardGame.title = bundle.getString("GameTitle")

        if (boardGame.bggID != null) {

            makeRequestForDetails(view)

            view.findViewById<Button>(R.id.gDetAddNewGameButton).setOnClickListener {
                addNewGame(boardGame)
                print("\n\n${boardGame.image}")
                it.isEnabled = false
            }
        }
    }

    private fun addNewGame(bGame: BoardGame) {
        val dbHandler = context?.let { MyDBHandler(it, null, null, 1) }
        dbHandler?.addNewGame(bGame)
        Toast.makeText(context, "New game has been added to database", Toast.LENGTH_SHORT).show()

    }

    fun makeRequestForDetails(v: View) {
        try {
            val queue = Volley.newRequestQueue(context)
            val url = "https://www.boardgamegeek.com/xmlapi2/thing?id=${boardGame.bggID}&stats=1"

            val request = StringRequest(
                Request.Method.GET, url,
                { response ->
                    makeXMLFile(response)
                    loadDataAboutFoundGame()
                    showDataAboutFoundGame(v)
                },
                {
                    Toast.makeText(context, "Nie udało się pobrać danych.\nSpróbuj ponownie.", Toast.LENGTH_SHORT).show()
                })

            queue.add(request)
        }
        catch(e: MalformedURLException) {
            print("Zły URL")
        } catch (e: FileNotFoundException) {
            print("Brak pliku")
        } catch (e: IOException) {
            print("Wyjątek IO")
        }
    }

    fun makeXMLFile(text :String){
        val path = (activity as MainActivity).filesDir
        val testDirectory = File("$path/XML")
        if (!testDirectory.exists()) testDirectory.mkdir()
        val fos = File("$testDirectory/gameDetails.xml")
        fos.writeText(text)
    }

    fun loadDataAboutFoundGame(){
        val filename = "gameDetails.xml"
        val path = (activity as MainActivity).filesDir
        val inDir = File(path, "XML")

        if (inDir.exists()) {
            val file = File(inDir, filename)
            if (file.exists()) {
                val parser = XmlPullParserHandler()
                val inStream = file.inputStream()
                parser.parseGameDetails(inStream, boardGame)
            }
        }
    }

    fun showDataAboutFoundGame(v: View) {
        v.findViewById<TextView>(R.id.gDetTitleText).text = boardGame.title
        v.findViewById<TextView>(R.id.gDetOriginalTitle).text = boardGame.originalTitle
        Picasso.with(context).load(Uri.parse(boardGame.image)).into(v.findViewById<ImageView>(R.id.gDetImage))
        v.findViewById<TextView>(R.id.gDetPublishYear).text = boardGame.year.toString()
        v.findViewById<TextView>(R.id.gDetAuthors).text = boardGame.authors
        v.findViewById<TextView>(R.id.gDetGraphicAuthors).text = boardGame.graphics
        v.findViewById<TextView>(R.id.gDetDescription).text = boardGame.description
        v.findViewById<TextView>(R.id.gDetBGGRank).text = boardGame.rank
        v.findViewById<TextView>(R.id.gDetBaseDLC).text = boardGame.baseOrDLC

        v.findViewById<TextView>(R.id.gDetOrderDate).visibility = View.GONE
        v.findViewById<TextView>(R.id.gDetOrderDateText).visibility = View.GONE

        v.findViewById<TextView>(R.id.gDetOwnDate).visibility = View.GONE
        v.findViewById<TextView>(R.id.gDetOwnDateText).visibility = View.GONE

        v.findViewById<TextView>(R.id.gDetCost).visibility = View.GONE
        v.findViewById<TextView>(R.id.gDetCostText).visibility = View.GONE

        v.findViewById<TextView>(R.id.gDetRecommendedPrice).visibility = View.GONE
        v.findViewById<TextView>(R.id.gDetRecommendedPriceText).visibility = View.GONE

        v.findViewById<TextView>(R.id.gDetEANUPC).visibility = View.GONE
        v.findViewById<TextView>(R.id.gDetEANUPCText).visibility = View.GONE

        v.findViewById<TextView>(R.id.gDetProductionCode).visibility = View.GONE
        v.findViewById<TextView>(R.id.gDetProductionCodeText).visibility = View.GONE

        v.findViewById<TextView>(R.id.gDetComment).visibility = View.GONE
        v.findViewById<TextView>(R.id.gDetCommentText).visibility = View.GONE

        v.findViewById<TextView>(R.id.gDetLocalization).visibility = View.GONE
        v.findViewById<TextView>(R.id.gDetLocalizationText).visibility = View.GONE

        v.findViewById<Button>(R.id.gDetAddNewGameButton).isEnabled = true
    }
}