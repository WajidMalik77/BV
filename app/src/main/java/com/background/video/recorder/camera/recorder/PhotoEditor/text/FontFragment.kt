package com.background.video.recorder.camera.recorder.PhotoEditor.text

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.background.video.recorder.camera.recorder.R


class FontFragment : Fragment(), FontClickListner {
    private var mAdapter: FontAdapter? = null
    private val fontID = 40 ///Hardcoded Size Defined for assetFiles

    private var myCallback: addTextInterface? = null

    private fun setInterfaceForEditText(callback: addTextInterface?) {
        myCallback = callback
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.font_fragment, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.font_recycler)
        setInterfaceForEditText(activity as addTextInterface)
        val mLayoutManager = LinearLayoutManager(activity)
        mAdapter = FontAdapter(fontID, requireActivity(), this)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = mLayoutManager
        recyclerView.setHasFixedSize(true)

//        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
//                mLayoutManager.getOrientation());
//        recyclerView.addItemDecoration(mDividerItemDecoration);
        return view
    }

    override fun onItemClickListener(position: Int, view: View?) {
        if (view != null) {
            hideKeyboard(view)
        }
        val fontPath = "font$position"
        try {
            myCallback?.updateMyTextFont("fonts/$fontPath.ttf")
        } catch (e: Exception) {
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isActive) inputMethodManager.hideSoftInputFromWindow(
            view.windowToken,
            0
        )
    }


}