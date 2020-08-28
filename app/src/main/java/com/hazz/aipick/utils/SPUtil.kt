package com.hazz.aipick.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.ArrayMap
import com.google.gson.Gson
import com.hazz.aipick.LanguageType
import com.hazz.aipick.mvp.model.bean.UserInfo
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 * 数据存储类　
 * 优先从map 中读取，如果map 中没有，再从文件或者　sharedPreferences 中读取。
 * 写入的时候，用handler 写入
 */
object SPUtil {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var map: ArrayMap<String, Any>
    private val gson = Gson()
    private val APP_CONFIG = "app_config"
    private lateinit var rootFile: File
    private val DAYLY_RATE = "RATE"
    private val TOKEN = "token"
    fun setRate(rate: String) {
        putString(DAYLY_RATE, rate)
    }

    fun getToken(): String {
        return getString(TOKEN)
    }

    fun setToken(token: String) {
        putString(TOKEN, token)
    }

    fun getRate(): String {
        return getString(DAYLY_RATE)
    }

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        map = ArrayMap()
        rootFile = context.cacheDir
    }

    fun putString(key: String, value: String) {
        map[key] = value
        Schedulers.io().run {
            editor.putString(key, value).apply()
        }
    }

    fun getString(key: String): String {
        return getString(key, "")
    }

    fun getString(key: String, def: String): String {
        var s = map[key]
        if (s == null) {
            s = sharedPreferences.getString(key, def)
            if (s != null) {
                map[key] = s
            }
        }
        return s as String
    }

    fun putInt(key: String, value: Int) {
        map[key] = value
        Schedulers.io().run {
            editor.putInt(key, value).apply()
        }
    }

    fun getInt(key: String): Int {
        return getInt(key, 0)
    }

    fun getInt(key: String, def: Int): Int {
        var result = map[key]
        if (result == null) {
            result = sharedPreferences.getInt(key, def)
            map[key] = result
        }
        return result as Int
    }

    fun putBoolean(key: String, value: Boolean) {
        map[key] = value
        Schedulers.io().run {
            editor.putBoolean(key, value).apply()
        }
    }

    fun getBoolean(key: String): Boolean {
        return getBoolean(key, false)
    }

    fun getBoolean(key: String, def: Boolean): Boolean {
        var result = map[key]
        if (result == null) {
            result = sharedPreferences.getBoolean(key, def)
            map[key] = result
        }
        return result as Boolean
    }

    fun putObj(key: String, any: Any) {
        map[key] = any
        Schedulers.io().run {
            var file = File(rootFile, key)
            if (file.exists()) {
                file.delete()
            }
            file.writeText(gson.toJson(any))
        }
    }


    fun <T> getObj(key: String, cls: Class<T>): T {
        var result = map[key]
        if (result == null) {
            val file = File(rootFile, key)
            if (file.exists()) {
                val text = file.readText()
                result = gson.fromJson(text, cls)
                if (result != null) {
                    map[key] = result
                }
            }
        }
        return result as T
    }

    fun removeObj(key: String): Any {
        val file = File(rootFile, key)
        if (file.exists()) {
            file.delete()
        }
        return map.remove(key)!!
    }

    fun saveLanguage(context: Context, language: LanguageType) {
        putString("lang", language.value)
    }

    /**
     * 获取语言，默认中文
     * @return
     */
    fun getLanguage(): String? {
        return getString("lang", LanguageType.LG_SIMPLIFIED_CHINESE.value)
    }

    fun getUser(): UserInfo {
        return getObj("userinfo", UserInfo::class.java)
    }

}