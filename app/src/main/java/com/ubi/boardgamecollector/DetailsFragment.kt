package com.ubi.boardgamecollector

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import java.io.File

class DetailsFragment : Fragment() {

    lateinit var yourGame: BoardGame

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gamesID = arguments?.getInt("GameID")
        if (gamesID != null) {
            getYourGameInfo(gamesID)
            showYourGameInfo(view)
        }

        val backButton = view.findViewById<Button>(R.id.detGoBackButton)
        val deleteButton = view.findViewById<Button>(R.id.detDeleteGameButton)
        val editButton = view.findViewById<Button>(R.id.detEditGameButton)


        backButton.setOnClickListener {
            if (backButton.text == "Back") {
                findNavController().navigate(R.id.action_detailsFragment_to_FirstFragment)
            }
            else if (backButton.text == "Cancel") {
                view.findViewById<ScrollView>(R.id.detScrollView).visibility = View.VISIBLE
                view.findViewById<ScrollView>(R.id.detScrollViewEdit).visibility = View.GONE
                editButton.text = "Edit Game"
                backButton.text = "Back"
                deleteButton.isEnabled = true
            }
        }

        deleteButton.setOnClickListener {
            if (gamesID != null) {
                if (deleteYourGame(gamesID)) {
                    findNavController().navigate(R.id.action_detailsFragment_to_FirstFragment)
                } else {
                    Toast.makeText(context, "Something went wrong :(", Toast.LENGTH_SHORT).show()
                }
            }
        }

        editButton.setOnClickListener {
            if (editButton.text == "Edit Game") {
                view.findViewById<ScrollView>(R.id.detScrollView).visibility = View.GONE
                view.findViewById<ScrollView>(R.id.detScrollViewEdit).visibility = View.VISIBLE
                editButton.text = "Save"
                backButton.text = "Cancel"
                deleteButton.isEnabled = false
            }
            else if (editButton.text == "Save") {
                if (gamesID != null) {
                    editYourGame(gamesID)
                    getYourGameInfo(gamesID)
                    showYourGameInfo(view)
                    view.findViewById<ScrollView>(R.id.detScrollView).visibility = View.VISIBLE
                    view.findViewById<ScrollView>(R.id.detScrollViewEdit).visibility = View.GONE
                    editButton.text = "Edit Game"
                    backButton.text = "Back"
                    deleteButton.isEnabled = true
                }
            }
        }

    }

    private fun getYourGameInfo(id: Int){
        val dbHandler = context?.let { MyDBHandler(it, null, null, 1) }
        yourGame = dbHandler?.gameDetails(id)!!
    }

    private fun showYourGameInfo(v: View) {
        v.findViewById<TextView>(R.id.detTitleText).text = yourGame.title
        v.findViewById<TextView>(R.id.detOriginalTitle).text = yourGame.originalTitle
        if (yourGame.image != null && yourGame.image!!.startsWith("https")) {
            Picasso.with(context).load(Uri.parse(yourGame.image))
                .into(v.findViewById<ImageView>(R.id.detImage))
        } else {
            Picasso.with(context).load(Uri.parse(
                "https://ecsmedia.pl/c/remember-gra-planszowa-indyjska-patschisi-w-iext53821653.jpg"))
                .into(v.findViewById<ImageView>(R.id.detImage))
        }
        v.findViewById<TextView>(R.id.detPublishYear).text = yourGame.year.toString()
        v.findViewById<TextView>(R.id.detAuthors).text = yourGame.authors
        v.findViewById<TextView>(R.id.detGraphicAuthors).text = yourGame.graphics
        v.findViewById<TextView>(R.id.detDescription).text = yourGame.description
        v.findViewById<TextView>(R.id.detBaseDLC).text = yourGame.baseOrDLC
        v.findViewById<TextView>(R.id.detBGGRank).text = yourGame.rank
        v.findViewById<TextView>(R.id.detBGGID).text = yourGame.bggID.toString()
        v.findViewById<TextView>(R.id.detComment).text = yourGame.comment
        v.findViewById<TextView>(R.id.detLocalization).text = yourGame.localization
        v.findViewById<TextView>(R.id.detOrderDate).text = yourGame.orderDate
        v.findViewById<TextView>(R.id.detOwnDate).text = yourGame.ownDate
        v.findViewById<TextView>(R.id.detCost).text = yourGame.cost
        v.findViewById<TextView>(R.id.detRecommendedPrice).text = yourGame.scd
        v.findViewById<TextView>(R.id.detEANUPC).text = yourGame.codeEAN_UPC
        v.findViewById<TextView>(R.id.detProductionCode).text = yourGame.productionCode

        v.findViewById<TextView>(R.id.detOriginalTitleEdit).text = yourGame.originalTitle
        v.findViewById<TextView>(R.id.detPublishYearEdit).text = yourGame.year.toString()
        v.findViewById<TextView>(R.id.detAuthorsEdit).text = yourGame.authors
        v.findViewById<TextView>(R.id.detGraphicAuthorsEdit).text = yourGame.graphics
        v.findViewById<TextView>(R.id.detDescriptionEdit).text = yourGame.description
        v.findViewById<TextView>(R.id.detBaseDLCEdit).text = yourGame.baseOrDLC
        v.findViewById<TextView>(R.id.detCommentEdit).text = yourGame.comment
        v.findViewById<TextView>(R.id.detLocalizationEdit).text = yourGame.localization
        v.findViewById<TextView>(R.id.detOrderDateEdit).text = yourGame.orderDate
        v.findViewById<TextView>(R.id.detOwnDateEdit).text = yourGame.ownDate
        v.findViewById<TextView>(R.id.detCostEdit).text = yourGame.cost
        v.findViewById<TextView>(R.id.detRecommendedPriceEdit).text = yourGame.scd
        v.findViewById<TextView>(R.id.detEANUPCEdit).text = yourGame.codeEAN_UPC
        v.findViewById<TextView>(R.id.detProductionCodeEdit).text = yourGame.productionCode
    }

    private fun deleteYourGame(id: Int): Boolean {
        val dbHandler = context?.let { MyDBHandler(it, null, null, 1) }
        return dbHandler?.deleteGame(id)!!
    }

    private fun editYourGame(id: Int) {
        val editedGame = getEditedInfo()

        val dbHandler = context?.let { MyDBHandler(it, null, null, 1) }
        dbHandler?.editGame(id, editedGame)!!
    }

    private fun getEditedInfo(): BoardGame {
        val bGame = BoardGame()
        bGame.originalTitle = view?.findViewById<EditText>(R.id.detOriginalTitleEdit)?.text.toString()
        bGame.year = view?.findViewById<TextView>(R.id.detPublishYearEdit)?.text.toString().toInt()
        bGame.authors = view?.findViewById<TextView>(R.id.detAuthorsEdit)?.text.toString()
        bGame.graphics = view?.findViewById<TextView>(R.id.detGraphicAuthorsEdit)?.text.toString()
        bGame.description = view?.findViewById<TextView>(R.id.detDescriptionEdit)?.text.toString()
        bGame.baseOrDLC = view?.findViewById<TextView>(R.id.detBaseDLCEdit)?.text.toString()
        bGame.comment = view?.findViewById<TextView>(R.id.detCommentEdit)?.text.toString()
        bGame.localization = view?.findViewById<TextView>(R.id.detLocalizationEdit)?.text.toString()
        bGame.orderDate = view?.findViewById<TextView>(R.id.detOrderDateEdit)?.text.toString()
        bGame.ownDate = view?.findViewById<TextView>(R.id.detOwnDateEdit)?.text.toString()
        bGame.cost = view?.findViewById<TextView>(R.id.detCostEdit)?.text.toString()
        bGame.scd = view?.findViewById<TextView>(R.id.detRecommendedPriceEdit)?.text.toString()
        bGame.codeEAN_UPC = view?.findViewById<TextView>(R.id.detEANUPCEdit)?.text.toString()
        bGame.productionCode = view?.findViewById<TextView>(R.id.detProductionCodeEdit)?.text.toString()

        return bGame
    }
}