package com.hazz.aipick.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter


open class FragmentAdapter(private var fm: FragmentManager, private var viewList: List<Fragment>?, private val mTitleList: Array<String>?) : FragmentPagerAdapter(fm) {
    override fun getItem(p0: Int): Fragment {
        return viewList?.get(p0)!!
    }

    override fun getCount(): Int {
        return viewList?.size!!
    }


    override fun getPageTitle(position: Int): CharSequence? {
        return mTitleList?.get(position)
    }
}
