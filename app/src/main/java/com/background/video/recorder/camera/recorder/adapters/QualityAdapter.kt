package com.background.video.recorder.camera.recorder.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.background.video.recorder.camera.recorder.R
import com.background.video.recorder.camera.recorder.model.QualityModel

class QualityAdapter(val context: Context , val list :List<QualityModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(i: Int): QualityModel {
        return list[i]
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View? {
        var convertView = view
        val holder: ViewHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(context)!!.inflate(R.layout.quality_item_view, null)
            holder = ViewHolder(convertView)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        holder.name.text = getItem(position).text
        return convertView
    }

    internal class ViewHolder(view: View?) {
        var name: TextView
        init {
            name = view!!.findViewById(R.id.qualityTxt)
        }
    }
}