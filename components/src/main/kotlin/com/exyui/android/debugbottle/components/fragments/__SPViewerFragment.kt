package com.exyui.android.debugbottle.components.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.support.annotation.IdRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.TextView
import com.exyui.android.debugbottle.components.DialogsCollection
import com.exyui.android.debugbottle.components.R
import com.exyui.android.debugbottle.components.spviewer.SPViewer

/**
 * Created by yuriel on 9/3/16.
 */
class __SPViewerFragment: __ContentFragment(), DialogsCollection.SPDialogAction {

    override val TAG: String = "__SPViewerFragment"

    companion object {
        val TAG = "__SPViewerFragment"
    }

    private var rootView: View? = null
    private val listView by lazy { findViewById(R.id.list_view) as ExpandableListView }
    private var adapter: ListAdapter? = null
    private val groupList by lazy { mutableMapOf<String, SharedPreferences>() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.__activity_spviewer, container, false)
        this.rootView = rootView
        initSP()
        return rootView
    }

    private fun findViewById(@IdRes id: Int): View? {
        return rootView?.findViewById(id)
    }

    private fun initSP() {
        val viewer = SPViewer(context?.applicationContext?: return)
        groupList.clear()
        groupList.putAll(viewer.getAll())
        adapter = ListAdapter()
        listView.setAdapter(adapter)
    }

    override fun updateSPViews() {
        initSP()
    }



    inner class ListAdapter: BaseExpandableListAdapter() {

        private val clickListener = View.OnClickListener { v ->
            val p = v.tag as ChildHolder
            if (null == p.position) return@OnClickListener
            val sp = getGroup(p.position!!.first)?.second?: return@OnClickListener
            val key = getChild(p.position!!.first, p.position!!.second)?.first?: return@OnClickListener
            val dialog = DialogsCollection.EditSPDialogFragment.newInstance(key, sp)
            dialog.show(context?.fragmentManager, "dialog")
        }

        override fun isChildSelectable(groupPosition: Int, childPosition: Int) = true

        override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View? {
            var v = convertView
            val holder: ChildHolder
            if (null == v) {
                v = LayoutInflater.from(context).inflate(R.layout.__child_sp_values, parent, false)
                holder = ChildHolder()
                holder.titleView = v.findViewById(R.id.__child_title) as TextView
                holder.contentView = v.findViewById(R.id.__child_content) as TextView
                v.tag = holder
            } else {
                holder = v.tag as ChildHolder
            }

            val content = getChild(groupPosition, childPosition)
            holder.titleView?.text = content?.first
            holder.contentView?.text = content?.second.toString()
            holder.position = Pair(groupPosition, childPosition)

            v?.setOnClickListener(clickListener)

            return v
        }

        override fun getChildId(groupPosition: Int, childPosition: Int): Long = groupPosition * 100L + childPosition

        override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View? {
            var v = convertView
            val holder: GroupHolder
            if (null == v) {
                v = LayoutInflater.from(context).inflate(R.layout.__group_sp_files, parent, false)
                holder = GroupHolder()
                holder.titleView = v.findViewById(R.id.group_title) as TextView
                v.tag = holder
            } else {
                holder = v.tag as GroupHolder
            }

            val content = getGroup(groupPosition)
            holder.titleView?.text = content?.first
            return v
        }

        override fun getGroupCount(): Int = groupList.size

        override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

        override fun getChild(groupPosition: Int, childPosition: Int): Pair<String, Any?>? {
            val group = getGroup(groupPosition)
            var i = 0
            val sp = group?.second?: return null
            for ((k, v) in sp.all) {
                if (i == childPosition)
                    return Pair(k, v)
                i ++
            }
            return null
        }

        override fun hasStableIds(): Boolean = false

        override fun getChildrenCount(groupPosition: Int): Int {
            val group = getGroup(groupPosition)
            return group?.second?.all?.size?: 0
        }

        override fun getGroup(groupPosition: Int): Pair<String, SharedPreferences>? {
            var i = 0
            for ((k, v) in groupList) {
                if (i == groupPosition)
                    return Pair(k, v)
                i ++
            }
            return null
        }
    }

    inner class GroupHolder {
        var titleView: TextView? = null
    }

    inner class ChildHolder {
        var titleView: TextView? = null
        var contentView: TextView? = null
        var position: Pair<Int, Int>? = null
    }
}