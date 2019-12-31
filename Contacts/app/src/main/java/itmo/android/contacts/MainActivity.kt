package itmo.android.contacts

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*
import android.content.Intent
import android.net.Uri
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val myAdapter = UserAdapter(contacts) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_DIAL
            data = Uri.parse("tel:" + it.number)
        }
        startActivity(sendIntent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.READ_CONTACTS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.READ_CONTACTS),
                myRequestId
            )
        } else {
            contacts.clear()
            contacts.addAll(fetchAllContacts())
        }
        val viewManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            layoutManager = viewManager
            adapter = myAdapter
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            myRequestId -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    contacts.addAll(fetchAllContacts())
                    myAdapter.notifyDataSetChanged()
                    Toast
                        .makeText(
                            this@MainActivity,
                            this.resources.getQuantityString(
                                R.plurals.phone_number,
                                contacts.size, contacts.size
                            ),
                            Toast.LENGTH_SHORT
                        )
                        .show()
                } else {
                    Toast
                        .makeText(
                            this@MainActivity,
                            "No permission granted",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
                return
            }
        }
    }

}

data class Contact(val name: String, val number: String)

val contacts: MutableList<Contact> = mutableListOf()

var myRequestId: Int = 0

class UserViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
    val userNameText = root.user_name
    val userNumberText = root.user_number
}

class UserAdapter(
    private val users: MutableList<Contact>,
    val onClick: (Contact) -> Unit
) : RecyclerView.Adapter<UserViewHolder>() {

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.userNameText.text =
            users[position].name
        holder.userNumberText.text =
            users[position].number
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
            onClick(contacts[holder.adapterPosition])
        }
        return holder
    }
}


fun Context.fetchAllContacts(): MutableList<Contact> {
    contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        null,
        null,
        null
    )
        .use { cursor ->
            if (cursor == null) return mutableListOf()
            val builder = ArrayList<Contact>()
            while (cursor.moveToNext()) {
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                        ?: "N/A"
                val phoneNumber =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        ?: "N/A"
                builder.add(Contact(name, phoneNumber))
            }
            return builder
        }
}

