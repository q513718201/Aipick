package com.hazz.aipick.utils

import com.hazz.aipick.net.RetrofitManager
import com.vector.update_app.HttpManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.math.BigDecimal


/**
 * Created by Vector
 * on 2017/6/19 0019.
 */
class UpdateAppHttpUtil : HttpManager {


    override fun download(url: String, path: String, fileName: String, callback: HttpManager.FileCallback) {
        val call = RetrofitManager.resetService.down(url)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>) {
                response.body()?.let {
                    writeResponseBodyToDisk(it, path, fileName, callback)
                }
            }

            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
                callback?.onError(t?.message)
            }
        })

    }

    /**
     * 写文件入磁盘
     *
     * @param body 请求结果
     * @return boolean 是否下载写入成功
     */
    private fun writeResponseBodyToDisk(body: ResponseBody, savePath: String, fileName: String, callback: HttpManager.FileCallback): Boolean {
//        savePath = StorageUtil.getDownloadPath()
        callback.onBefore()
        val apkFile = File(savePath, fileName)
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        return try {
            val fileReader = ByteArray(4096)
            // 获取文件大小
            val fileSize = body.contentLength()
            var fileSizeDownloaded: Long = 0
            inputStream = body.byteStream()
            outputStream = FileOutputStream(apkFile)

            // byte转Kbyte
            val bd1024 = BigDecimal(1024)
            var totalByte = BigDecimal(fileSize).divide(bd1024, BigDecimal.ROUND_HALF_UP).setScale(0).toLong()

            // 只要没有取消就一直下载数据
            while (true) {
                val read: Int = inputStream.read(fileReader)
                if (read == -1) {
                    // 下载完成
                    callback.onResponse(apkFile)
                    break
                }
                outputStream.write(fileReader, 0, read)
                fileSizeDownloaded += read.toLong()
                // 计算进度
                var progress = (fileSizeDownloaded * 100.0 / fileSize).toFloat()
//                downByte = BigDecimal(fileSizeDownloaded).divide(bd1024, BigDecimal.ROUND_HALF_UP).setScale(0).toInt()
                // 子线程中，借助handler更新界面
                callback.onProgress(progress, totalByte)
            }
            outputStream.flush()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 异步get
     *
     * @param url      get请求地址
     * @param params   get参数
     * @param callBack 回调
     */
    override fun asyncGet(url: String, params: Map<String, String>, callBack: HttpManager.Callback) {
        val call = RetrofitManager.resetService.get(url, params = params)

        call?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>) {

                response?.let {
                    if (it.code() == 200) {
                        callBack.onResponse(it.body().toString())
                    } else {
                        callBack.onError(it.message())
                    }
                }

            }

            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
                callBack.onError(t?.message)
            }
        })
    }

    /**
     * 异步post
     *
     * @param url      post请求地址
     * @param params   post请求参数
     * @param callBack 回调
     */
    override fun asyncPost(url: String, params: Map<String, String>, callBack: HttpManager.Callback) {
        val call = RetrofitManager.resetService.post(url, params = params)

        call?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>) {

                response?.let {
                    if (it.code() == 200) {
                        callBack.onResponse(it.body().toString())
                    } else {
                        callBack.onError(it.message())
                    }
                }

            }

            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
                callBack.onError(t?.message)
            }
        })
    }

}