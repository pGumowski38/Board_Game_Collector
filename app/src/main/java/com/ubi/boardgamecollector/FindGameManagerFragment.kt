package com.ubi.boardgamecollector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.*

class FindGameManagerFragment : Fragment() {

    lateinit var container: FragmentContainerView
    lateinit var manager: FragmentManager
    lateinit var transaction: FragmentTransaction

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_find_game_manager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        container = view.findViewById(R.id.containerForFindGameView)
        val bggFindGameFragment = BGG_FindGameFragment()
        manager = activity?.supportFragmentManager!!
        transaction = manager.beginTransaction()

        transaction.add(container.id, bggFindGameFragment)
        transaction.commit()
    }
}