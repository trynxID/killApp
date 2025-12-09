package com.autoforcestop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView Adapter for displaying list of apps with checkboxes
 */
class AppListAdapter(
    private val apps: List<AppInfo>,
    private val onSelectionChanged: (String, Boolean) -> Unit
) : RecyclerView.Adapter<AppListAdapter.AppViewHolder>() {

    inner class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appIcon: ImageView = itemView.findViewById(R.id.ivAppIcon)
        val appName: TextView = itemView.findViewById(R.id.tvAppName)
        val checkbox: CheckBox = itemView.findViewById(R.id.cbAppSelect)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = apps[position]
        
        holder.appIcon.setImageDrawable(app.icon)
        holder.appName.text = app.appName
        holder.checkbox.isChecked = app.isSelected
        
        // Set checkbox listener
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            app.isSelected = isChecked
            onSelectionChanged(app.packageName, isChecked)
        }
        
        // Make the whole item clickable to toggle checkbox
        holder.itemView.setOnClickListener {
            holder.checkbox.isChecked = !holder.checkbox.isChecked
        }
    }

    override fun getItemCount(): Int = apps.size
}
