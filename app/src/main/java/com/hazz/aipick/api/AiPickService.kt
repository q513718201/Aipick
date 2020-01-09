package com.hazz.aipick.api

import com.hazz.aipick.mvp.model.bean.*
import com.hazz.aipick.mvp.model.bean.Collection
import com.hazz.aipick.net.BaseResult
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*



interface AiPickService{

    /**
     * 注册
     */
    @POST("user/register")
    fun regist(@Body request: RequestBody): Observable<BaseResult<Any>>

    /**
     * 未登录发送验证码
     */
    @POST("msg_send/reset_msg")
    fun sendMsg(@Body request: RequestBody): Observable<BaseResult<Any>>

    /**
     * 登录发送验证码
     */
    @POST("msg_send/modify_msg")
    fun sendMsglogin(@Body request: RequestBody): Observable<BaseResult<Any>>

    /**
     * 重置登录密码
     */
    @POST("user/reset/login_password")
    fun resetPwd(@Body request: RequestBody): Observable<BaseResult<Any>>
    /**
     * 登陆
     */
    @POST("user/login ")
    fun login(@Body request: RequestBody): Observable<BaseResult<LoginBean>>


    /**
     * 用户信息
     */
    @POST("user/info")
    fun userInfo(): Observable<BaseResult<UserInfo>>

    /**
     * 用户更新
     */
    @POST("user/update")
    fun updata(@Body request: RequestBody): Observable<BaseResult<Any>>

    /**
     * 主页列表
     */
    @POST("user/subee/list")
    fun homeList(@Body request: RequestBody): Observable<BaseResult<List<Home>>>


    /**
     * 生成订阅id
     */
    @POST("sub/entity")
    fun createId(@Body request: RequestBody): Observable<BaseResult<CreateId>>

    /**
     * 订阅支付
     */
    @POST("sub/pay")
    fun pay(@Body request: RequestBody): Observable<BaseResult<PayResultMine>>
    /**
     * 钱包信息
     */
    @POST("wallet/info")
    fun walet(): Observable<BaseResult<Walet>>
    /**
     * 提现
     */
    @POST("wallet/withdraw")
    fun tixian(@Body request: RequestBody): Observable<BaseResult<Any>>


    /**
     * 提现记录
     */
    @POST(" wallet/record/withdraw")
    fun tixianRecord(@Body request: RequestBody): Observable<BaseResult<List<TixianRecord>>>

    /**
     * 问题反馈
     */
    @POST("manage/feedback")
    fun feedBack(@Body body: RequestBody): Observable<BaseResult<Any>>//问题反馈


    /**
     * 收藏记录
     */
    @POST("collection/list")
    fun getCollection(@Body request: RequestBody): Observable<BaseResult<List<Collection>>>
    /**
     * 删除收藏
     */
    @POST("collection/delete")
    fun deleteCollection(@Body request: RequestBody): Observable<BaseResult<Any>>


    /**
     * 绑定银行卡
     */
    @POST("pay/bankcard")
    fun bindCard(@Body request: RequestBody): Observable<BaseResult<Any>>

    /**
     * 交易员认证
     */
    @POST("broker/apply")
    fun traderAuth(@Body request: RequestBody): Observable<BaseResult<Any>>


    /**
     * 我的交易所列表
     */
    @GET("exchange/list")
    fun coinHouseList(): Observable<BaseResult<List<CoinHouse>>>

    /**
     * 增加交易所
     */
    @POST("exchange/add")
    fun addCoinHouse(@Body request: RequestBody): Observable<BaseResult<Any>>

    /**
     * 绑定交易所
     */
    @POST("exchange/bind")
    fun bindCoinHouse(@Body request: RequestBody): Observable<BaseResult<Any>>

    /**
     * 交易所详情
     */
    @POST("exchange/detail")
    fun coinHouseDesc(@Body request: RequestBody): Observable<BaseResult<CoinHouseDesc>>
    /**
     * 消息列表
     */
    @POST("message/list")
    fun messageList(@Body request: RequestBody): Observable<BaseResult<List<Message>>>

    /**
     * 标记阅读
     */
    @POST("message/read")
    fun messageRead(@Body request: RequestBody): Observable<BaseResult<Any>>

    /**
     * 我的账户
     */
    @POST("user/profile")
    fun myAccount(@Body request: RequestBody): Observable<BaseResult<MyAccount>>


    /**
     * 机器人策略支持的交易所列表
     */
    @POST("sub/exchanges")
    fun coinList(@Body request: RequestBody): Observable<BaseResult<BindCoinHouse>>


    /**
     * 可订阅选项价格列
     */
    @POST("sub/prices")
    fun getPrice(@Body request: RequestBody): Observable<BaseResult<ChooseTime>>

    /**
     * 设置跟随方式
     */
    @POST("sub/modify")
    fun setFollow(@Body request: RequestBody): Observable<BaseResult<Any>>
    /**
     * 订阅记录
     */
    @POST("sub/list")
    fun mySubscribe(@Body request: RequestBody): Observable<BaseResult<List<MySubscribe>>>

    /**
     * 订阅详情
     */
    @POST("sub/detail")
    fun mySubscribeDesc(@Body request: RequestBody): Observable<BaseResult<SubscribeDesc>>

    /**
     * 订阅开关
     */
    @POST("sub/switch")
    fun mySubscribeSwitch(@Body request: RequestBody): Observable<BaseResult<Any>>
}