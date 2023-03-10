package com.example.android.metercollectionapp.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class SpinnerTextViewAdapter<T : Any> (
    private val context: Context,
    private val resId: Int,
    private val items: List<T>,
    private val itemToString: (T) -> String
) : BaseAdapter() {

    val currentList: List<T>
        get() = items

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return convertView ?: (LayoutInflater.from(context).inflate(resId, null, false) as TextView)
            .apply { text = itemToString(items[position]) }
    }
}
