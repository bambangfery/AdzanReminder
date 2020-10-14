package com.test.adzanalarm

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.test.adzanalarm.adapter.ViewPagerAdapter
import com.test.adzanalarm.databinding.ActivityMainBinding
import com.test.adzanalarm.fragment.HomeFragment
import com.test.adzanalarm.fragment.ProfileFragment
import com.test.adzanalarm.fragment.QuranFragment
import com.test.adzanalarm.fragment.ShalatFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        BottomNavigationViewHelper.removeShiftMode(binding.navigation)
        binding.navigation.itemIconTintList = null
        binding.viewpager.offscreenPageLimit = 4
        binding.viewpager.setSwipeable(false)
        setupViewPager(binding.viewpager)
        bottomNavClick()
        binding.viewpager.currentItem = 1
        binding.navigation.selectedItemId = R.id.action_item2

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frame_layout, ShalatFragment.newInstance())
        transaction.commit()

    }

    private fun setupViewPager(viewPager: ViewPager) {
        pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        pagerAdapter.addFragment(HomeFragment(), "Home")
        pagerAdapter.addFragment(ShalatFragment(), "Shalat")
        pagerAdapter.addFragment(QuranFragment(), "Quran");
        pagerAdapter.addFragment(ProfileFragment(), "Profile")
        viewPager.adapter = pagerAdapter
    }

    private fun bottomNavClick() {
        binding.navigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_item1 -> {
                    binding.viewpager.currentItem = 0
                    resetIcon()
                    item.setIcon(R.mipmap.home2)
                    item.isChecked = true
                }
                R.id.action_item2 -> {
                    binding.viewpager.currentItem = 1
                    resetIcon()
                    item.setIcon(R.mipmap.shalat2)
                    item.isChecked = true
                }
                R.id.action_item3 -> {
                    binding.viewpager.currentItem = 2
                    resetIcon()
                    item.setIcon(R.mipmap.quran2)
                    item.isChecked = true
                }
                R.id.action_item4 -> {
                    binding.viewpager.currentItem = 3
                    resetIcon()
                    item.setIcon(R.mipmap.profile2)
                    item.isChecked = true
                }
            }
            false
        })
    }

    private fun resetIcon() {
        binding.navigation.menu.getItem(0).setIcon(R.mipmap.home2_unselected)
        binding.navigation.menu.getItem(1).setIcon(R.mipmap.shalat2_unselected)
        binding.navigation.menu.getItem(2).setIcon(R.mipmap.quran2_unselected);
        binding.navigation.menu.getItem(3).setIcon(R.mipmap.profile2_unselected)
    }

    internal object BottomNavigationViewHelper {
        @SuppressLint("RestrictedApi")
        fun removeShiftMode(view: BottomNavigationView) {
            val menuView = view.getChildAt(0) as BottomNavigationMenuView
            try {
                val shiftingMode =
                    menuView.javaClass.getDeclaredField("mShiftingMode")
                shiftingMode.isAccessible = true
                shiftingMode.setBoolean(menuView, false)
                shiftingMode.isAccessible = false
                for (i in 0 until menuView.childCount) {
                    val item =
                        menuView.getChildAt(i) as BottomNavigationItemView
                    //                    item.setShiftingMode(false);
                    item.setShifting(false)
                    // set once again checked value, so view will be updated
                    item.setChecked(item.itemData.isChecked)
                }
            } catch (e: NoSuchFieldException) {
                Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field")
            } catch (e: IllegalAccessException) {
                Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode")
            }
        }
    }
}