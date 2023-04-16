package org.example.ada.spacialDay

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.example.ada.R

class ContentAdapter(val context: Context, private val dataset: MutableList<Content>): BaseAdapter(){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.content_recyclerview_item, null)

        val color = view.findViewById<TextView>(R.id.currentIcon)
        val contentTitle = view.findViewById<TextView>(R.id.contentTitle)

        val content = dataset[position]

        if(content.Color == "pink") {
            color.setBackgroundResource(R.drawable.pinkcircle)
        } else if(content.Color == "violet") {
            color.setBackgroundResource(R.drawable.less_important)
        } else if(content.Color == "gray") {
            color.setBackgroundResource(R.drawable.gray)
        }
        contentTitle.text = content.ContentTitle
        return view
    }

    override fun getCount(): Int {
        return dataset.size
    }

    override fun getItem(position: Int): Any {
        return dataset[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }
}