package com.hazz.aipick.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.utils.SPUtil
import kotlinx.android.synthetic.main.activity_guide.*
import java.util.*


/**
 * Created by xuhao on 2017/12/5.
 * desc: 启动页
 */

class GuideActivity : BaseActivity() {



    override fun layoutId(): Int = R.layout.activity_guide

    override fun initData() {

    }

    private val views = ArrayList<View>()
    private var mIv: ImageView? = null

    @SuppressLint("SetTextI18n")
    override fun initView() {

        val res = intArrayOf(R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3)
        for (i in res.indices) {
            mIv = ImageView(this)
            Glide.with(this).load(res[i]).into(mIv!!)
            mIv!!.layoutParams = ViewGroup.LayoutParams(-1, -1)
            mIv!!.scaleType = ImageView.ScaleType.FIT_XY
            views.add(mIv!!)
        }
        viewPager.adapter = GuideAdapter()
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {

            }

            override fun onPageSelected(i: Int) {
                if (i == views.size - 1) {
                    mIv!!.setOnClickListener { v ->
                        SPUtil.putBoolean("isGuide", true)
                        val string = SPUtil.getString("token")
                        if(TextUtils.isEmpty(string)){
                            val intent = Intent(application, LoginActivity::class.java)
                            startActivity(intent)
                        }else{
                            val intent = Intent(application, MainActivity::class.java)
                            startActivity(intent)
                        }

                       finish()
                    }
                }
            }

            override fun onPageScrollStateChanged(i: Int) {

            }
        })


    }

    override fun start() {

    }


    private inner class GuideAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return views.size
        }

        override fun isViewFromObject(view: View, o: Any): Boolean {
            return view === o
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val v = views[position]
            container.addView(v)
            return v
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}