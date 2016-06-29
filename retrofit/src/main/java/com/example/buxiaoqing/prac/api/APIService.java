package com.example.buxiaoqing.prac.api;
/*Retrofit2.0*/


import com.example.buxiaoqing.prac.bean.ApiBean;
import com.example.buxiaoqing.prac.bean.DemoBean;
import com.example.buxiaoqing.prac.bean.IpBean;
import com.example.buxiaoqing.prac.model.WeatherBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Call<T> get();必须是这种形式,这是2.0之后的新形式
 * 如果不需要转换成Json数据,可以用了ResponseBody;
 * 你也可以使用Call<GsonBean> get();这样的话,需要添加Gson转换器
 */
public interface APIService {


    //地址是 传过来的baseUrl+/service/getIpInfo.php?ip ='ip'    Query拼接的是参数
    @GET("/service/getIpInfo.php")
    Call<DemoBean> getIpInfo(@Query("ip") String ip);

    //参数也可以是Json格式,如下
    /*{
        "apiInfo": {
        "apiName": "WuXiaolong",
                "apiKey": "666"
    }
    }*/


    //那么接口这么写
    /*
    public interface APIService {
    @POST("client/shipper/getCarType")
    Call<ResponseBody> getCarType(@Body ApiInfo apiInfo);
    }*/

    /*
    APIService apiService = mRetrofit.create(APIService.class);
    ApiInfo apiInfo = new ApiInfo();
    ApiInfo.ApiInfoBean apiInfoBean = apiInfo.new ApiInfoBean();
    apiInfoBean.setApiKey("666");
    apiInfoBean.setApiName("Sammo");
    apiInfo.setApiInfo(apiInfoBean);
    Call<ResponseBody> call = apiService.getIpInfo(apiInfo);*/


    @GET("/service/getIpInfo.php")
    Call<ApiBean<IpBean>> getIpInfo2(@Query("ip") String ip);


    @GET("/adat/sk/{cityId}.html")
    Call<WeatherBean> getWeather(@Path("cityId") String cityId);
}