package com.background.video.recorder.camera.recorder.PhotoEditor.text

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.background.video.recorder.camera.recorder.R
import com.flask.colorpicker.ColorPickerView


class ColorFragment : Fragment() {
    private var textColor = Color.BLACK

    private var myCallback: addTextInterface? = null

    private fun setInterfaceForEditText(callback: addTextInterface?) {
        myCallback = callback
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.color_fragment, container, false)
        val colorPickerView: ColorPickerView = view.findViewById(R.id.color_picker_view)

        setInterfaceForEditText(activity as addTextInterface)
        colorPickerView.addOnColorChangedListener { selectedColor -> // Handle on color change
            textColor = selectedColor
            myCallback?.updateMyTextColor(textColor)
            hideKeyboard(colorPickerView)
        }
        colorPickerView.addOnColorSelectedListener { selectedColor ->
            textColor = selectedColor
            myCallback?.updateMyTextColor(textColor)
            hideKeyboard(colorPickerView)
        }
        return view
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