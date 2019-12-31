package itmo.android.pictures

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class UserAdapter(
    private val pics: MutableList<Pic>,
    val onClick: (Pic) -> Unit
) : RecyclerView.Adapter<UserViewHolder>() {

    override fun getItemCount(): Int = pics.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        if (pics[position].description != "null")
            holder.picDescription.text = pics[position].description
        else holder.picDescription.text = ""
        holder.preview.setImageBitmap(pics[position].lowQ)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        val holder = UserViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item,
                parent,
                false
            )
        )
        holder.root.setOnClickListener {
            onClick(pictures[holder.adapterPosition])
        }
        return holder
    }
}


