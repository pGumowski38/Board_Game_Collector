package com.ubi.boardgamecollector

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


/**
 * A simple [Fragment] subclass.
 */
class AddGameFragment : Fragment() {

    lateinit var boardGameTitle: EditText
    lateinit var boardGameOriginal: EditText
    lateinit var boardGameYear: EditText
    lateinit var boardGameAuthors: EditText
    lateinit var boardGameGraphics: EditText
    lateinit var boardGameDescription: EditText
    lateinit var boardGameOrder: EditText
    lateinit var boardGameOwn: EditText
    lateinit var boardGameCost: EditText
    lateinit var boardGameSCD: EditText
    lateinit var boardGameEANUPC: EditText
    lateinit var boardGameProductionCode: EditText
    lateinit var boardGameBaseDLC: EditText
    lateinit var boardGameComment: EditText
    lateinit var boardGameImage: Button
    lateinit var boardGameSelectedImage: TextView
    lateinit var boardGameLocalization: EditText
    var imageURI: Uri? = null

    private val PICK_IMAGES_CODE = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.addGoBackButton).setOnClickListener {
            findNavController().navigate(R.id.action_addGameFragment_to_SecondFragment)
        }

        boardGameTitle = view.findViewById(R.id.addTitleEdit)
        boardGameOriginal = view.findViewById(R.id.addOrgTitleEdit)
        boardGameYear = view.findViewById(R.id.addYearEdit)
        boardGameAuthors = view.findViewById(R.id.addProducentsEdit)
        boardGameGraphics = view.findViewById(R.id.addArtistsEdit)
        boardGameDescription = view.findViewById(R.id.addDescriptionEdit)
        boardGameOrder = view.findViewById(R.id.addOrderDateEdit)
        boardGameOwn = view.findViewById(R.id.addOwnDateEdit)
        boardGameCost = view.findViewById(R.id.addCostEdit)
        boardGameSCD = view.findViewById(R.id.addSCDEdit)
        boardGameEANUPC = view.findViewById(R.id.addEAN_UPC_CodeEdit)
        boardGameProductionCode = view.findViewById(R.id.addProductionCodeEdit)
        boardGameBaseDLC = view.findViewById(R.id.addBasicDLCEdit)
        boardGameComment = view.findViewById(R.id.addCommentEdit)
        boardGameImage = view.findViewById(R.id.addSelectImage)
        boardGameLocalization = view.findViewById(R.id.addLocalizationEdit)

        val addGameButton = view.findViewById<Button>(R.id.addAddGameButton)
        var enoughInfoCounter = 0
        var titleCheck = false
        var yearCheck = false
        var descCheck = false

        boardGameTitle.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.isNotEmpty() and (!addGameButton.isEnabled) and (!titleCheck)){
                        enoughInfoCounter++
                        titleCheck = true
                        if (enoughInfoCounter >= 3) {
                            addGameButton.isEnabled = true
                        }
                    } else if (s.isEmpty() and (addGameButton.isEnabled) and (titleCheck)){
                        enoughInfoCounter--
                        titleCheck = false
                        if (enoughInfoCounter < 3) {
                            addGameButton.isEnabled = false
                        }
                    }
                }
            }
        })

        boardGameYear.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.isNotEmpty() and (!addGameButton.isEnabled) and (!yearCheck)){
                        enoughInfoCounter++
                        yearCheck = true
                        if (enoughInfoCounter >= 3) {
                            addGameButton.isEnabled = true
                        }
                    } else if (s.isEmpty() and (addGameButton.isEnabled) and (yearCheck)){
                        enoughInfoCounter--
                        yearCheck = false
                        if (enoughInfoCounter < 3) {
                            addGameButton.isEnabled = false
                        }
                    }
                }
            }
        })

        boardGameDescription.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.isNotEmpty() and (!addGameButton.isEnabled) and (!descCheck)){
                        enoughInfoCounter++
                        descCheck = true
                        if (enoughInfoCounter >= 3) {
                            addGameButton.isEnabled = true
                        }
                    } else if (s.isEmpty() and (addGameButton.isEnabled) and (descCheck)){
                        enoughInfoCounter--
                        descCheck = false
                        if (enoughInfoCounter < 3) {
                            addGameButton.isEnabled = false
                        }
                    }
                }
            }
        })

        boardGameOrder.filters = arrayOf<InputFilter>(LengthFilter(10))
        boardGameOrder.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (s.length == 2 || s.length == 5) {
                        s.append("/")
                    }
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        boardGameOwn.filters = arrayOf<InputFilter>(LengthFilter(10))
        boardGameOwn.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (s.length == 2 || s.length == 5) {
                        s.append("/")
                    }
                }
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        view.findViewById<Button>(R.id.addAddGameButton).setOnClickListener {
            addNewGame()
        }

//        boardGameImage.setOnClickListener {
//            pickImagesIntent()
//        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == PICK_IMAGES_CODE) {
//
//            if (resultCode == RESULT_OK) {
//                imageURI = data?.data
//                print("\nUri: $imageURI")
//                val path = context?.let { getPath(it, imageURI) }
//                print("\nPath: $path")
//                boardGameSelectedImage.text = path
//            }
//        }
//    }

    private fun addNewGame() {
        val dbHandler = context?.let { MyDBHandler(it, null, null, 1) }
        val bGame = BoardGame()
        bGame.title = boardGameTitle.text.toString()
        bGame.originalTitle = boardGameOriginal.text.toString()
        bGame.year = Integer.parseInt(boardGameYear.text.toString())
        bGame.authors = boardGameAuthors.text.toString()
        bGame.graphics = boardGameGraphics.text.toString()
        bGame.description = boardGameDescription.text.toString()
        bGame.orderDate = boardGameOrder.text.toString()
        bGame.ownDate = boardGameOwn.text.toString()
        bGame.cost = boardGameCost.text.toString()
        bGame.scd = boardGameSCD.text.toString()
        bGame.codeEAN_UPC = boardGameEANUPC.text.toString()
        bGame.productionCode = boardGameProductionCode.text.toString()
        bGame.baseOrDLC = boardGameBaseDLC.text.toString()
        bGame.comment = boardGameComment.text.toString()
        bGame.localization = boardGameLocalization.text.toString()

        dbHandler?.addNewGame(bGame)

        boardGameTitle.setText("")
        boardGameOriginal.setText("")
        boardGameYear.setText("")
        boardGameAuthors.setText("")
        boardGameGraphics.setText("")
        boardGameDescription.setText("")
        boardGameOrder.setText("")
        boardGameOwn.setText("")
        boardGameCost.setText("")
        boardGameSCD.setText("")
        boardGameEANUPC.setText("")
        boardGameProductionCode.setText("")
        boardGameBaseDLC.setText("")
        boardGameComment.setText("")
        boardGameLocalization.setText("")

        Toast.makeText(context, "New game has been added to database", Toast.LENGTH_SHORT).show()
    }

//    private fun pickImagesIntent() {
//        val intent = Intent()
//        intent.type = "image/*"
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGES_CODE)
//    }
//
//    fun getPath(context: Context, uri: Uri?): String? {
//        var result: String? = null
//        val proj = arrayOf(MediaStore.Images.Media.DATA)
//        val cursor: Cursor? =
//            uri?.let { context.getContentResolver().query(it, proj, null, null, null) }
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                val column_index = cursor.getColumnIndexOrThrow(proj[0])
//                result = cursor.getString(column_index)
//            }
//            cursor.close()
//        }
//        if (result == null) {
//            result = "Not found"
//        }
//        return result
//    }
}