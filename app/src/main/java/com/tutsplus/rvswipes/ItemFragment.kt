package com.tutsplus.rvswipes

import android.content.Context
import android.graphics.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.tutsplus.rvswipes.dummy.DummyContent
import com.tutsplus.rvswipes.dummy.DummyContent.DummyItem
import kotlin.math.roundToInt

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ItemFragment.OnListFragmentInteractionListener] interface.
 */
class ItemFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val srLayout: SwipeRefreshLayout = inflater.inflate(R.layout.fragment_item_list, container, false)
                                                    as SwipeRefreshLayout
        val view = srLayout.findViewById<RecyclerView>(R.id.list)

        val trashBinIcon = resources.getDrawable(R.drawable.ic_delete_black_24dp, null)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyItemRecyclerViewAdapter(DummyContent.ITEMS, listener)

                val myCallback = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean = false

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        DummyContent.ITEMS.removeAt(viewHolder.adapterPosition)
                        adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                    }

                    override fun onChildDraw(
                        c: Canvas,
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        dX: Float,
                        dY: Float,
                        actionState: Int,
                        isCurrentlyActive: Boolean
                    ) {
                        c.clipRect(0f, viewHolder.itemView.top.toFloat(),
                            dX, viewHolder.itemView.bottom.toFloat())

                        if(dX < width / 3)
                            c.drawColor(Color.GRAY)
                        else
                            c.drawColor(Color.RED)

                        val textMargin = resources.getDimension(R.dimen.text_margin).roundToInt()

                        trashBinIcon.bounds = Rect(
                                    textMargin,
                                    viewHolder.itemView.top + textMargin,
                                    textMargin + trashBinIcon.intrinsicWidth,
                                    viewHolder.itemView.top + trashBinIcon.intrinsicHeight + textMargin
                        )

                        trashBinIcon.draw(c)
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    }
                }

                val myHelper = ItemTouchHelper(myCallback)
                myHelper.attachToRecyclerView(this)
            }
        }

        srLayout.setOnRefreshListener {
            DummyContent.ITEMS.clear()
            for(i in 1..25) {
                DummyContent.ITEMS.add(
                    DummyContent.DummyItem("$i",
                        "Item $i",
                        "")
                )
            }
            view.adapter?.notifyDataSetChanged()
            srLayout.isRefreshing = false
        }

        return srLayout
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: DummyItem?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            ItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
