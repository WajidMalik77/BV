package com.background.video.recorder.camera.recorder.adapters

import android.content.Context
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.background.video.recorder.camera.recorder.R
import com.background.video.recorder.camera.recorder.model.LanguageModel
import java.util.ArrayList

class LanguageAdapter(
    private val languageList: ArrayList<LanguageModel>,
    private val context: Context
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {
    var sel = 0
    private var listener: OnItemClickListener? = null
    fun setListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.language_single_row, parent, false)
        return LanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.tv.text = languageList[position].language
        holder.radio_btn.isSelected = languageList[position].check == true
        if (languageList[position].check == true) sel = position
        holder.itemView.setOnClickListener { view: View? -> listener!!.onClick(view, position) }
    }

    override fun getItemCount(): Int {
        return languageList.size
    }

    interface OnItemClickListener {
        fun onClick(view: View?, position: Int)
    }

    inner class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var radio_btn: ImageView
        var tv: TextView
        override fun onClick(view: View) {
            listener!!.onClick(view, adapterPosition)
        }

        init {
            radio_btn = itemView.findViewById<View>(R.id.language_radio) as ImageView
            tv = itemView.findViewById<View>(R.id.language_tv) as TextView
            itemView.setOnClickListener(this)
        }
    }
}