package com.ubi.boardgamecollector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.*

class OPCollectionManagerFragment : Fragment() {

    lateinit var container: FragmentContainerView
    lateinit var manager: FragmentManager
    lateinit var transaction: FragmentTransaction

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_o_p_collection_manager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        container = view.findViewById(R.id.containerForCollectionView)
        val opCollectionFragment = OPCollectionFragment()
        manager = activity?.supportFragmentManager!!
        transaction = manager.beginTransaction()

        transaction.add(container.id, opCollectionFragment)
        transaction.commit()
    }
}