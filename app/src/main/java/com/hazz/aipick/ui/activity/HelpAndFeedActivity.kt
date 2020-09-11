package com.hazz.aipick.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.Toolbar
import com.hazz.aipick.R
import com.hazz.aipick.base.BaseActivity
import com.hazz.aipick.mvp.contract.WaletContract
import com.hazz.aipick.mvp.presenter.FeedBackresenter
import com.hazz.aipick.ui.adapter.PhotoAdapter
import com.hazz.aipick.utils.PicUtil
import com.hazz.aipick.utils.ToastUtils
import com.hazz.aipick.utils.ToolBarCustom
import com.werb.pickphotoview.PickPhotoView
import com.werb.pickphotoview.util.PickConfig
import kotlinx.android.synthetic.main.activity_help_feed.*
import java.util.*


class HelpAndFeedActivity : BaseActivity(), WaletContract.feedAndHelp {
    override fun completeResult(msg: String) {
        ToastUtils.showToast(this, msg)
        finish()
    }


    override fun layoutId(): Int = R.layout.activity_help_feed


    override fun initData() {

    }


    private var mFeedBackresenter: FeedBackresenter = FeedBackresenter(this)

    private var iv1: String? = ""
    private var iv2: String? = ""
    private var iv3: String? = ""
    private var iv4: String? = ""
    private var photoAdapter: PhotoAdapter? = null
    private val selectedPhotos = ArrayList<String>()

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ToolBarCustom.newBuilder(mToolbar as Toolbar)
                .setLeftIcon(R.mipmap.back_white)
                .setTitle(getString(R.string.mine_help_feed))
                .setTitleColor(resources.getColor(R.color.color_white))
                .setToolBarBg(Color.parseColor("#1E2742"))
                .setOnLeftIconClickListener { finish() }
                .setRightTextIsShow(true)
                .setRightText(getString(R.string.send))
                .setRightTextColor(resources.getColor(R.color.dilaog_btn_color))
                .setOnRightClickListener {

                    val toString = et_phone.text.toString()
                    if (toString.isNullOrBlank()) {
                        ToastUtils.showToast(this, "请填写您的联系方式")
                        return@setOnRightClickListener
                    }
                    if (selectedPhotos.size != 0) {
                        for (i in selectedPhotos.indices) {
                            if (i == 0) {
                                iv1 = PicUtil.compressImage(selectedPhotos[0], "png")
                            }
                            if (i == 1) {
                                iv2 = PicUtil.compressImage(selectedPhotos[1], "png")
                            }
                            if (i == 2) {
                                iv3 = PicUtil.compressImage(selectedPhotos[2], "png")
                            }
                            if (i == 3) {
                                iv3 = PicUtil.compressImage(selectedPhotos[2], "png")
                            }
                        }
                    }
                    mFeedBackresenter.feedBack(et.text.toString(), et_phone.text.toString(), iv1!!, iv2!!, iv3!!, iv4!!)
                }

        tv_img_head.text = getString(R.string.text_img_head, "0")
        photoAdapter = PhotoAdapter(this, selectedPhotos)
        recyclerView.layoutManager = StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL)
        recyclerView.adapter = photoAdapter
        photoAdapter!!.setOnMyClick { position ->
            if (photoAdapter!!.getItemViewType(position) == PhotoAdapter.TYPE_ADD) {

                permissionsnew!!.request(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )!!.subscribe { aBoolean ->
                    if (aBoolean!!) {
                        PickPhotoView.Builder(this)
                                .setPickPhotoSize(4)                  // select image size
                                .setClickSelectable(true)             // click one image immediately close and return image
                                .setShowCamera(true)                  // is show camera
                                .setSpanCount(3)                      // span count
                                .setLightStatusBar(true)              // lightStatusBar used in Android M or higher
                                .setStatusBarColor(R.color.color_white)     // statusBar color
                                .setToolbarColor(R.color.color_white)       // toolbar color
                                .setToolbarTextColor(R.color.color_black)   // toolbar text color// select icon color
                                .setShowGif(false)                    // is show gif
                                .start()
                    } else {
                        showMissingPermissionDialog()
                    }
                }


            }

        }


    }

    override fun start() {

    }


    @SuppressLint("StringFormatMatches")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        if (requestCode == PickConfig.PICK_PHOTO_DATA) {
            val paths = data.getSerializableExtra(PickConfig.INTENT_IMG_LIST_SELECT) as ArrayList<String>
            selectedPhotos.addAll(paths)
            photoAdapter?.notifyDataSetChanged()
            tv_img_head.text = getString(R.string.text_img_head, "${paths.size}")

        }
    }

}
