package com.tutsplus.rvswipes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tutsplus.rvswipes.dummy.DummyContent

class MainActivity : AppCompatActivity(),
    ItemFragment.OnListFragmentInteractionListener {

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
