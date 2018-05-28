package com.ats.barstockexchangeuser.util;

import com.ats.barstockexchangeuser.bean.AllCategoryAndItemsData;
import com.ats.barstockexchangeuser.bean.BillData;
import com.ats.barstockexchangeuser.bean.CallWaiter;
import com.ats.barstockexchangeuser.bean.ErrorMessage;
import com.ats.barstockexchangeuser.bean.ItemListData;
import com.ats.barstockexchangeuser.bean.LoginData;
import com.ats.barstockexchangeuser.bean.NewsData;
import com.ats.barstockexchangeuser.bean.OrderEntry;
import com.ats.barstockexchangeuser.bean.TableData;
import com.ats.barstockexchangeuser.bean.UserBean;
import com.ats.barstockexchangeuser.bean.UserOrderHistory;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by maxadmin on 25/11/17.
 */

public interface InterfaceApi {

    public static final String URL = "http://132.148.143.124:8080/BSEWebApis/";
    // public static final String URL = "http://192.168.2.6:8061/";
//public static final String URL = "http://192.168.43.245:8060/";

    public static final String MY_PREF = "BSE_User";
    public static final String IMAGE_PATH = "http://132.148.143.124:8080/BSEWebApis/images/";

    @GET("user/getAllCategoryAndItems")
    Call<AllCategoryAndItemsData> getAllCatAndItemsByQuery();

    @POST("getItemBycatId")
    Call<ItemListData> getItemsByCategory(@Query("catId") int catId);

    //------OTP-------
    @GET("sendhttp.php")
    Call<JsonObject> sendSMS(@Query("authkey") String authkey, @Query("mobiles") String mobiles, @Query("message") String message, @Query("sender") String sender, @Query("route") String route, @Query("country") String country, @Query("response") String response);

    //http://sms.sminfomedia.in/vendorsms/pushsms.aspx?user=lavazza&password=lavazza&msisdn=9881168357&sid=BARSHL&msg=New Message&fl=0&gwid=2
    @POST("pushsms.aspx")
    Call<JsonObject> sendSMSAPI(@Query("user") String user, @Query("password") String password, @Query("msisdn") String msisdn, @Query("sid") String sid, @Query("msg") String msg, @Query("fl") int fl, @Query("gwid") int gwid);

    @POST("insertuser")
    Call<ErrorMessage> registerUser(@Body UserBean userBean);

    @GET("userLogin")
    Call<LoginData> doLogin(@Query("username") String username, @Query("password") String password);

    @POST("user/placeOrder")
    Call<ErrorMessage> placeUserOrder(@Body OrderEntry orderEntry);

    @GET("getAllTable")
    Call<TableData> getAllTables();

    @GET("getOrderHistoryForUser")
    Call<UserOrderHistory> orderHistory(@Query("userId") int userId);

    @GET("getBillByUser")
    Call<BillData> getBillDetails(@Query("userId") int userId);

    @GET("getAllNews")
    Call<NewsData> getAllNews();

    @GET("updateusertoken")
    Call<ErrorMessage> updateToken(@Query("userId") int userId, @Query("token") String token);

    @GET("findUserExist")
    Call<ErrorMessage> checkUserExist(@Query("mobile") String mobile);

    @GET("changePassword")
    Call<ErrorMessage> changePassword(@Query("mobile") String mobile, @Query("pass") String pass);

    @POST("user/callCaptain")
    Call<ErrorMessage> callWaiter(@Body CallWaiter callWaiter);


}
