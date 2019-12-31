package itmo.android.pictures

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class UserViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
    val picDescription: TextView = root.description
    val preview: ImageView = root.prewiew
}