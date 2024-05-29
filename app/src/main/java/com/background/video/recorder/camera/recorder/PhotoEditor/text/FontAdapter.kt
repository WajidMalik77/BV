package com.background.video.recorder.camera.recorder.PhotoEditor.text

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.background.video.recorder.camera.recorder.R


class FontAdapter(
    private val fontID: Int,
    private val context: Context,
    private val fontClickListner: FontClickListner?
) : RecyclerView.Adapter<FontAdapter.DataObjectHolder>() {
    private var mSelectedItem = -1

    inner class DataObjectHolder(view: View) : RecyclerView.ViewHolder(view) {
        var font_txt: TextView = view.findViewById(R.id.font_txt)
        var mRadioButton: RadioButton = view.findViewById(R.id.radio_btn)
        fun populateFonts(integer: Int) {
            val s = "font$integer"
            val customFont = Typeface.createFromAsset(
                context.assets, "fonts/$s.ttf"
            )
            font_txt.typeface = customFont

            Log.d("font_custom", "populateFonts: "+customFont)

            font_txt.text = "ABCDEF"
        }

        init {
            val clickListener = View.OnClickListener { v ->
                mSelectedItem = adapterPosition
                notifyDataSetChanged()
                fontClickListner?.onItemClickListener(mSelectedItem, v)
            }
            view.setOnClickListener(clickListener)
            mRadioButton.setOnClickListener(clickListener)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DataObjectHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_font, viewGroup, false)
        return DataObjectHolder(view)
    }

    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {
        holder.populateFonts(position)
        holder.mRadioButton.isChecked = position == mSelectedItem
    }

    override fun getItemCount(): Int {
        return fontID //fonts length is hardcoded
    }
}