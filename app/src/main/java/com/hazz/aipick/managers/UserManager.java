package com.hazz.aipick.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.hazz.aipick.MyApplication;
import com.hazz.aipick.mvp.model.bean.Coin;
import com.hazz.aipick.mvp.model.bean.UserInfo;
import com.hazz.aipick.utils.ACache;
import com.hazz.aipick.utils.BigDecimalUtil;
import com.orhanobut.logger.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserManager {

    private static final String TAG = "UserManager";
    private static final String COMMON_FILE = "common_file";
    private static final String USER_INFO_S = "user_info_s";
    private static final String AUTH_INFO_S = "auth_info_s";


    public static final String SP_ACCESS_TOKEN_TIMESTAMP = "access_token_timestamp";
    public static final String SP_REFRESH_TOKEN_TIMESTAMP = "refresh_token_timestamp";

    private static UserManager sInstance;

    private String mTempPhoneNumber;
    private String mAreaCode = "+86";
    private List<Coin> mCoins = new ArrayList<>();

    private HashMap<String, Coin> mCoinHashMap = new HashMap<>();

//    private Api mApi = Singleton.get().dataSource.api();

    private int uid;
    private String mobile;
    private String email;
    private String nickname;
    private String refreshToken;
    private String socketToken;
    private String mTempEmail;
    private String mTempCode;

    private UserManager() {
    }

    public static UserManager getInstance() {
        if (sInstance == null) {
            synchronized (UserManager.class) {
                if (sInstance == null) {
                    sInstance = new UserManager();
                }
            }
        }
        return sInstance;
    }


    public void setTempPhoneNumber(String tempPhoneNumber) {
        mTempPhoneNumber = tempPhoneNumber;
    }

    public void setAreaCode(String areaCode) {
        mAreaCode = areaCode;
    }

    public String getAreaCode() {
        return mAreaCode;
    }


    public String getTempPhoneNumber() {
        return mTempPhoneNumber;
    }


//    public void loggedIn(Context context, LoginResult data) {
//        nickname = data.getNickname();
//        mobile = data.getMobile();
//        email = data.getEmail();
//        saveRefreshToken(context, data.getRefreshToken());
//        saveSocketToken(context, data.getSocketToken());
//        saveAccessToken(context, data.getAccessToken());
//        WebSocket.getInstance().requestLogin();
//        startQueryUserCoins(BaseApp.shared());
//    }


    private void saveSocketToken(Context context, String socketToken) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        defaultSharedPreferences.edit().putString("socket_token", socketToken).apply();
    }

    public void saveRefreshToken(Context context, String refreshToken) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        long timestamp = System.currentTimeMillis() / 1000;
        defaultSharedPreferences.edit().putLong(SP_REFRESH_TOKEN_TIMESTAMP, timestamp).apply();
        defaultSharedPreferences.edit().putString("refresh_token", refreshToken).apply();
    }

    public void saveAccessToken(Context context, String token) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        long timestamp = System.currentTimeMillis() / 1000;
        defaultSharedPreferences.edit().putLong(SP_ACCESS_TOKEN_TIMESTAMP, timestamp).apply();
        defaultSharedPreferences.edit().putString("access_token", token).apply();
    }

    public String getRefreshToken(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getString("refresh_token", refreshToken);
    }

    public String getAccessToken(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getString("access_token", "");
    }


    public void saveUserInfo(Context context, UserInfo userInfo) {
        ACache aCache = ACache.get(context, COMMON_FILE);
        if (userInfo != null) {
            aCache.put(USER_INFO_S, userInfo);
        }
    }

    public boolean isLogin(Context context) {
        String refreshToken = UserManager.getInstance().getRefreshToken(context);
        if (TextUtils.isEmpty(refreshToken) || UserManager.getInstance().isTokenExpired()) {
            return false;
        }
        return true;
    }


    public UserInfo getUserInfo(Context context) {
        ACache aCache = ACache.get(context, COMMON_FILE);
        if (aCache != null) {
            return (UserInfo) aCache.getAsObject(USER_INFO_S);
        }
        return null;
    }


    //
//    public float getCoinFree(String coinName) {
//        if (mCoinHashMap.get(coinName) == null) {
//            return -1;
//        }
//        return mCoinHashMap.get(coinName).free.floatValue();
//    }
//
    public BigDecimal getCoinFreeDecimal(String coinName) {
        if (mCoinHashMap.get(coinName) == null) {
            return null;
        }
        return /*mCoinHashMap.get(coinName).free*/BigDecimalUtil.mulByDecimal("1", "1");// TODO: 2020/8/20
    }


    public void setTempEmail(String tempEmail) {
        mTempEmail = tempEmail;
    }

    public String getTempEmail() {
        return mTempEmail;
    }

//    public void checkIfRefreshToken() {
//        String refreshToken = getRefreshToken(BaseApp.shared());
//        if (TextUtils.isEmpty(refreshToken)) {
//            Logger.d("no refresh token");
//            return;
//        }
//        if (isBetterToRefresh()) {
//            mApi.refreshToken(new RefreshTokenBody(refreshToken))
//                    .compose(RxResultHelper.handleResult())
//                    .compose(ScheduleCompat.apply())
//                    .subscribe(new ByexSubscriber<RefreshTokenResult>() {
//                        @Override
//                        protected void success(RefreshTokenResult data) {
//                            Logger.d(data.access_token);
//                            saveAccessToken(BaseApp.shared(), data.getAccess_token());
//                            saveRefreshToken(BaseApp.shared(), data.getRefresh_token());
//                        }
//
//                        @Override
//                        protected void failure(HLError error) {
//                            if (error.code == ReplyCode.refreshExpired) {
//                                //refresh token expired, need to login again
//                                saveRefreshToken(BaseApp.shared(), "");
//                            }
//                        }
//                    });
//        }
//    }

    public void setTempCode(String tempCode) {
        mTempCode = tempCode;
    }

    public String getTempCode() {
        return mTempCode;
    }

//    public boolean isBetterToRefresh() {
//        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApp.shared());
//        long timestamp = defaultSharedPreferences.getLong(SP_ACCESS_TOKEN_TIMESTAMP, 0);
//        long duration = System.currentTimeMillis() / 1000 - timestamp;
//        boolean result = duration > 60 * 60 * 3 + 50 * 60; //beyond 3h : 50m
//        Logger.d(result);
//        return result;
//    }

    public boolean isJustRefreshed() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.Companion.getContext());
        long timestamp = defaultSharedPreferences.getLong(SP_ACCESS_TOKEN_TIMESTAMP, 0);
        long duration = System.currentTimeMillis() / 1000 - timestamp;
        boolean result = duration < 60; //within 60 seconds
        Logger.d(result);
        return result;
    }

    public boolean isTokenExpired() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.Companion.getContext());
        long timestamp = defaultSharedPreferences.getLong(SP_REFRESH_TOKEN_TIMESTAMP, 0);
        long duration = System.currentTimeMillis() / 1000 - timestamp;
        //expire time is 60 * 60 * 8 = 28800 seconds
        boolean result = duration > 28000; //beyond 7 hours
        Logger.d(result);
        return result;
    }


    public void saveUserSelectToken(List<String> customTokenPair) {

    }


    /**
     * 存储公钥
     *
     * @param publicKey
     */
    public void saveUserPublicKey(String publicKey) {
        if (TextUtils.isEmpty(publicKey)) {
            return;
        }
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.Companion.getContext());
        defaultSharedPreferences.edit().putString("pb_key", publicKey);
    }


    public String getUserPublicKey() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.Companion.getContext());
        String pb_key = defaultSharedPreferences.getString("pb_key", "");
        return pb_key;
    }


}
