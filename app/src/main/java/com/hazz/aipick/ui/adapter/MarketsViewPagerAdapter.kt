package com.hazz.aipick.ui.adapter

import android.support.v4.view.PagerAdapter
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup


class MarketsViewPagerAdapter(private var viewList: List<RecyclerView>?, private val mTitleList: Array<String>?) : PagerAdapter() {
    private var mChildCount = 0

    override fun getCount(): Int {
        return viewList!!.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    //这个方法用来实例化页卡
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        //添加页卡
        container.addView(viewList!![position])
        return viewList!![position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(viewList!![position])//删除页卡
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitleList!![position]
    }

    //    @Override
    //    public CharSequence getPageTitle(int position) {
    //        return viewList.get(position).getTag() + "";
    //    }

    fun getItem(position: Int): RecyclerView {
        return viewList!![position]
    }

    fun updateList(recyclerViewList: List<RecyclerView>) {
        this.viewList = recyclerViewList
        notifyDataSetChanged()
    }

    override fun notifyDataSetChanged() {
        mChildCount = count
        super.notifyDataSetChanged()
    }

    //    @Override
    //    public int getItemPosition(Object object)   {
    //        if ( mChildCount > 0) {
    //            mChildCount --;
    //            return POSITION_NONE;
    //        }
    //        return super.getItemPosition(object);
    //    }
}
