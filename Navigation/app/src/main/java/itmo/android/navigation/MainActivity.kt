package itmo.android.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: MyFragmentPagerAdapter
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        adapter = MyFragmentPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 2
        tabLayout = findViewById(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)
        if (savedInstanceState != null) {
            tabLayout.getTabAt(savedInstanceState.getInt("position"))!!.select()
        }
    }

    private fun changeTab(index: Int) {
        if (index == 0)
            finish()
        else {
            if (supportFragmentManager.fragments[index - 1].childFragmentManager.backStackEntryCount > 0) {
                tabLayout.getTabAt(index - 1)!!.select()
            } else {
                changeTab(index - 1)
            }
        }
    }

    override fun onBackPressed() {
        val id = tabLayout.selectedTabPosition
        if (supportFragmentManager.fragments[id].childFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.fragments[id].childFragmentManager.popBackStack()
        } else {
            changeTab(id)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("position", tabLayout.selectedTabPosition)
    }
}