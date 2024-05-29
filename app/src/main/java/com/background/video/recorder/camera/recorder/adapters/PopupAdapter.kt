package com.background.video.recorder.camera.recorder.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.background.video.recorder.camera.recorder.R
import com.background.video.recorder.camera.recorder.model.MenuModel


class PopupAdapter(val context: Context , val list : List<MenuModel>) : BaseAdapter() {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(i: Int): MenuModel {
        return list[i]
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View? {
        var convertView = view
        val holder: ViewHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(context)!!.inflate(R.layout.menu_item, null)
            holder = ViewHolder(convertView)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        holder.name.text = getItem(position).name
        holder.img.setImageResource(getItem(position).icon!!)
        return convertView
    }

    internal class ViewHolder(view: View?) {
        var name: TextView
        var img: ImageView

        init {
            name = view!!.findViewById(R.id.menuName)
            img = view.findViewById(R.id.icon)
        }
    }
}