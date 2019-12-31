package itmo.android.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class FragmentContainer : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_container, container, false)
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .add(R.id.container, PageFragment().apply {
                    arguments = Bundle().apply {
                        putInt("level", 0)
                    }
                })
                .commit()
        }
        return view
    }
}