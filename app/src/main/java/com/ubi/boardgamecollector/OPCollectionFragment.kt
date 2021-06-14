package com.ubi.boardgamecollector

import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
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
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.net.MalformedURLException

class OPCollectionFragment : Fragment() {

    var collection :MutableList<BoardGame> = mutableListOf()
    lateinit var collectionTable: TableLayout
    var containerCollection: ViewGroup? = null
    lateinit var manager: FragmentManager
    lateinit var transaction: FragmentTransaction


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        containerCollection = container
        return inflater.inflate(R.layout.fragment_o_p_collection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val findButton = view.findViewById<Button>(R.id.opFindButton)
        collectionTable = view.findViewById(R.id.opCollectionTable)

        view.findViewById<EditText>(R.id.opPlayerNameEdit).addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.isNotEmpty() and (!findButton.isEnabled)) {
                        findButton.isEnabled = true
                    } else if (s.isEmpty() and (findButton.isEnabled)) {
                        findButton.isEnabled = false
                    }
                }
            }
        })

        findButton.setOnClickListener {
            val nameToFind : String = view.findViewById<EditText>(R.id.opPlayerNameEdit).text.toString()
            view.findViewById<TextView>(R.id.opPlayerNameInfo).text = "Loading $nameToFind's Collection"
            GlobalScope.launch {
                withContext(Dispatchers.Default) {
                    makeRequestForPlayerCollection(view, nameToFind)
                }
            }
        }

        view.findViewById<Button>(R.id.opBackButton).setOnClickListener {
            findNavController().navigate(R.id.action_OPCollectionManagerFragment_to_SecondFragment)
        }

    }

    private suspend fun makeRequestForPlayerCollection(v: View, nameToFind: String){
        try {
            val queue = Volley.newRequestQueue(context)
            val url = "https://www.boardgamegeek.com/xmlapi2/collection?username=$nameToFind&stats=1"

            val request = StringRequest(
                Request.Method.GET, url,
                { response ->
                    makeXMLFile(response)
                    loadCollection()
                    showCollection()
                    v.findViewById<TextView>(R.id.opPlayerNameInfo).text = "$nameToFind's Collection"
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

    private fun makeXMLFile(text :String){
        val path = (activity as MainActivity).filesDir
        val testDirectory = File("$path/XML")
        if (!testDirectory.exists()) testDirectory.mkdir()
        val fos = File("$testDirectory/collection.xml")
        fos.writeText(text)
    }

    private fun loadCollection() {
        collection = mutableListOf()
        val filename = "collection.xml"
        val path = (activity as MainActivity).filesDir
        val inDir = File(path, "XML")

        if (inDir.exists()) {
            val file = File(inDir, filename)
            if (file.exists()) {
                val parser = XmlPullParserHandler()
                val inStream = file.inputStream()
                collection = parser.parseForCollection(inStream) as MutableList
            }
        }
    }

    private fun showCollection() {
        for (game in collection) {

            val gameRankText = TextView(context)
            if (!game.rank.isNullOrBlank()) {
                gameRankText.text = game.rank
            } else {
                gameRankText.text = "Not Ranked"
            }
            gameRankText.layoutParams = TableRow.LayoutParams(150,
                TableRow.LayoutParams.MATCH_PARENT)
            gameRankText.gravity = Gravity.CENTER
            gameRankText.setTypeface(gameRankText.typeface, Typeface.BOLD)

            val gameMiniImage = ImageView(context)
            gameMiniImage.layoutParams = TableRow.LayoutParams(270,
                TableRow.LayoutParams.MATCH_PARENT)
            gameMiniImage.setPadding(5, 5, 5, 5)
            if (!game.miniImage.isNullOrEmpty()) {
                Picasso.with(context).load(Uri.parse(game.miniImage))
                    .into(gameMiniImage)
            } else {
                Picasso.with(context).load(
                    Uri.parse(
                    "https://ecsmedia.pl/c/remember-gra-planszowa-indyjska-patschisi-w-iext53821653.jpg"))
                    .into(gameMiniImage)
            }

            val linLayout = LinearLayout(context)
            linLayout.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT)
            linLayout.orientation = LinearLayout.VERTICAL

            val linLayoutHor = LinearLayout(context)
            linLayoutHor.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT)
            linLayoutHor.orientation = LinearLayout.HORIZONTAL

            val gameTitleText = TextView(context)
            linLayoutHor.addView(gameTitleText)
            gameTitleText.text = game.title
            gameTitleText.layoutParams = TableRow.LayoutParams(400,
                TableRow.LayoutParams.WRAP_CONTENT)
            gameTitleText.gravity = Gravity.CENTER
            gameTitleText.setTypeface(gameTitleText.typeface, Typeface.BOLD)
            gameTitleText.setPadding(10, 0, 0, 0)
            gameTitleText.setTextColor(Color.parseColor("#0E84E1"))

            val gameYearText = TextView(context)
            linLayoutHor.addView(gameYearText)
            gameYearText.text = "(${game.year.toString()})"
            gameYearText.layoutParams = TableRow.LayoutParams(180,
                TableRow.LayoutParams.WRAP_CONTENT)
            gameYearText.gravity = Gravity.START
            gameYearText.setTextColor(Color.parseColor("#FF0000"))

            val gameCommentText = TextView(context)
            if (game.comment?.length!! > 120) {
                gameCommentText.text = game.comment?.subSequence(0, 120).toString() + "..."
            } else {
                gameCommentText.text = game.comment
            }
            gameCommentText.layoutParams = TableRow.LayoutParams(620,
                TableRow.LayoutParams.WRAP_CONTENT)
            gameCommentText.setPadding(10, 0, 0, 10)

            linLayout.addView(linLayoutHor)
            linLayout.addView(gameCommentText)

            val gameRow = TableRow(context)
            val gameRowParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT)
            gameRowParams.setMargins(0, 1, 0, 1)
            gameRow.isClickable = true
            gameRow.layoutParams = gameRowParams
            gameRow.setBackgroundColor(Color.WHITE)

            gameRow.addView(gameRankText)
            gameRow.addView(gameMiniImage)
            gameRow.addView(linLayout)

            gameRow.setOnClickListener {
                it.setBackgroundColor(Color.parseColor("#CBEAF5"))
                val gID = game.bggID
                val gTitle = game.title
                val bundle = bundleOf("GameID" to gID, "GameTitle" to gTitle)

                manager = activity?.supportFragmentManager!!
                transaction = manager.beginTransaction()
                if (containerCollection != null) {
                    transaction.hide(this)
                    val gameDetailsFragment = GameDetailsFragment(bundle, this)
                    transaction.add(containerCollection!!.id, gameDetailsFragment)
                    transaction.commit()
                }
            }

            collectionTable.addView(gameRow)
        }
    }
}