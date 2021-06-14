package com.ubi.boardgamecollector

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.net.MalformedURLException

class BGG_FindGameFragment : Fragment() {

    var foundGames:MutableList<BoardGame> = mutableListOf()
    var containerFindGame: ViewGroup? = null
    lateinit var manager: FragmentManager
    lateinit var transaction: FragmentTransaction

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        containerFindGame = container
        return inflater.inflate(R.layout.fragment_b_g_g__find_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listOfFoundGames = view.findViewById<ListView>(R.id.foundGamesListView)

        view.findViewById<Button>(R.id.bggGoBackButton).setOnClickListener {
            findNavController().navigate(R.id.action_findGameManagerFragment_to_SecondFragment)
        }


        val searchButton = view.findViewById<Button>(R.id.bggSearchButton)

        view.findViewById<EditText>(R.id.bggTitleSearchEdit).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.isNotEmpty() and (!searchButton.isEnabled)){
                        searchButton.isEnabled = true
                    } else if (s.isEmpty() and (searchButton.isEnabled)){
                        searchButton.isEnabled = false
                    }
                }
            }
        })



        view.findViewById<Button>(R.id.bggSearchButton).setOnClickListener {
            val query : String = view.findViewById<EditText>(R.id.bggTitleSearchEdit).text.toString()

            try {
                val queue = Volley.newRequestQueue(context)
                val url = "https://www.boardgamegeek.com/xmlapi2/search?type=boardgame&query=$query"

                val request = StringRequest(Request.Method.GET, url,
                    { response ->
                        makeXMLFile(response)
                        loadFoundGames()
                        val adapter = ArrayAdapter(activity as Context, android.R.layout.simple_list_item_1, foundGames)
                        listOfFoundGames.adapter = adapter
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

        var selectedItem: Int = -1
        listOfFoundGames.setOnItemClickListener { parent, v, position, id ->
            selectedItem = id.toInt()
            view.findViewById<Button>(R.id.bggShowDetailsButton).isEnabled = true
        }

        view.findViewById<Button>(R.id.bggShowDetailsButton).setOnClickListener {
            val gID = foundGames[selectedItem].bggID
            val gTitle = foundGames[selectedItem].title
            val bundle = bundleOf("GameID" to gID, "GameTitle" to gTitle)

            manager = activity?.supportFragmentManager!!
            transaction = manager.beginTransaction()
            if (containerFindGame != null) {
                transaction.hide(this)
                val gameDetailsFragment = GameDetailsFragment(bundle, this)
                transaction.add(containerFindGame!!.id, gameDetailsFragment)
                transaction.commit()
            }
        }

    }

    private fun makeXMLFile(text :String){
        val path = (activity as MainActivity).filesDir
        val testDirectory = File("$path/XML")
        if (!testDirectory.exists()) testDirectory.mkdir()
        val fos = File("$testDirectory/foundGames.xml")
        fos.writeText(text)
    }

    private fun loadFoundGames() {
        foundGames = mutableListOf()
        val filename = "foundGames.xml"
        val path = (activity as MainActivity).filesDir
        val inDir = File(path, "XML")

        if (inDir.exists()) {
            val file = File(inDir, filename)
            if (file.exists()) {
                val parser = XmlPullParserHandler()
                val inStream = file.inputStream()
                foundGames = parser.parse(inStream) as MutableList
            }
        }
    }

}