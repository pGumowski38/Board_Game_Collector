package com.ubi.boardgamecollector

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.core.view.marginBottom
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    lateinit var yourGames: ArrayList<BoardGame>
    lateinit var gamesTable: TableLayout

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.newGameButton).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        getYourGames()

        gamesTable = view.findViewById(R.id.gamesTable)
        showYourGames()
    }

    private fun getYourGames() {
        val dbHandler = context?.let { MyDBHandler(it, null, null, 1) }
        yourGames = dbHandler?.getYourGames()!!
//        Toast.makeText(context, "Your games has been downloaded", Toast.LENGTH_SHORT).show()
    }

    private fun showYourGames() {
        gamesTable.removeView(view?.findViewById(R.id.gamesRow2))
        for (game in yourGames) {

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
                Picasso.with(context).load(Uri.parse(
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

            val gameDescriptionText = TextView(context)
            if (game.description?.length!! > 120) {
                gameDescriptionText.text = game.description?.subSequence(0, 120).toString() + "..."
            } else {
                gameDescriptionText.text = game.description
            }
            gameDescriptionText.layoutParams = TableRow.LayoutParams(620,
                TableRow.LayoutParams.WRAP_CONTENT)
            gameDescriptionText.setPadding(10, 0, 0, 10)

            linLayout.addView(linLayoutHor)
            linLayout.addView(gameDescriptionText)

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
                val gID = game.id
                val bundle = bundleOf("GameID" to gID)
                findNavController().navigate(R.id.action_FirstFragment_to_detailsFragment, bundle)
            }

            gamesTable.addView(gameRow)
        }
    }

}