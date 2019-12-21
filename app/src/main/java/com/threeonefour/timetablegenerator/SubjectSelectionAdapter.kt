package com.threeonefour.timetablegenerator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast

class SubjectSelectionAdapter(private val context: Context, subjectArrayList: ArrayList<Model>) : BaseAdapter() {
    private var subjectArrayList: ArrayList<Model>

    init {
        this.subjectArrayList = subjectArrayList
    }

    override fun getViewTypeCount(): Int {
        return count
    }

    override fun getItemViewType(position: Int): Int {

        return position
    }

    override fun getCount(): Int {
        return subjectArrayList.size
    }

    override fun getItem(position: Int): Any {
        return subjectArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            holder = ViewHolder()
            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.subject_check_row, null, true)

            holder.checkBox = convertView!!.findViewById(R.id.subCheckBox) as CheckBox
            holder.tvAnimal = convertView.findViewById(R.id.subNameTextView) as TextView

            convertView.tag = holder
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = convertView.tag as ViewHolder
        }


        holder.tvAnimal!!.setText(subjectArrayList[position].getsubName())

        val subName: String = subjectArrayList[position].getsubName();
        holder.checkBox!!.isChecked = subjectArrayList[position].getSelects()
        holder.checkBox!!.setTag(R.integer.btnplusview, convertView)
        holder.checkBox!!.tag = position

        holder.checkBox!!.setTag(R.integer.btnplusview, convertView)
        holder.checkBox!!.tag = position
        holder.checkBox!!.setOnClickListener {
            val tempview = holder.checkBox!!.getTag(R.integer.btnplusview) as View
            val tv = tempview.findViewById(R.id.subNameTextView) as TextView
            val pos = holder.checkBox!!.tag as Int
//            Toast.makeText(context, "Checkbox $pos clicked! with subCode: $subName", Toast.LENGTH_SHORT).show()


//            public_subjectArrayList = subjectArrayList

            if (subjectArrayList[pos].getSelects()) {
                subjectArrayList[pos].setSelects(false)
                public_subjectArrayList = subjectArrayList
            } else {
                subjectArrayList[pos].setSelects(true)
                public_subjectArrayList = subjectArrayList
            }
        }

        return convertView
    }

    private inner class ViewHolder {

        var checkBox: CheckBox? = null
        var tvAnimal: TextView? = null

    }

    companion object {
//        var public_subjectArrayList: ArrayList<Model> = ArrayList<Model>()
        lateinit var public_subjectArrayList: ArrayList<Model>
    }
}