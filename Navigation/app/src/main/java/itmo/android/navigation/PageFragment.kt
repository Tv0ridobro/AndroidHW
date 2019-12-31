package itmo.android.navigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

class PageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val level = arguments!!.getInt("level")
        val text = view.findViewById(R.id.tab_text) as TextView
        text.text = level.toString()
        val button = view.findViewById(R.id.tab_button) as TextView
        button.setOnClickListener {
            fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.start, 0)
                .replace(R.id.container, PageFragment().apply {
                    arguments = Bundle().apply {
                        putInt("level", level + 1)
                    }
                })
                .addToBackStack(null)
                .commit()
        }
        return view
    }

}