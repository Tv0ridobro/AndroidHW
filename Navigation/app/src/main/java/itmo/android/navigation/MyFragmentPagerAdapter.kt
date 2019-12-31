package itmo.android.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MyFragmentPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val pageCount = 3

    override fun getItem(position: Int): Fragment {
        return FragmentContainer()
    }

    override fun getCount(): Int {
        return pageCount
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "first"
            1 -> "second"
            else -> "third"
        }
    }

}