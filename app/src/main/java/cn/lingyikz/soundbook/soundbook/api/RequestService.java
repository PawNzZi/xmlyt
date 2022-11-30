package cn.lingyikz.soundbook.soundbook.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestService {
    private static Retrofit retrofit ;
    private volatile static RequestService requestService ;
//    public static final String BASE_URL = "http://lingyikz.qicp.vip" ;//本地测试域名
    public static final String BASE_URL = "https://zmlm-api.lingyikz.cn" ;//

    private RequestService(){

        retrofit =  new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 支持RxJava
                .build();
    }

    public static RequestService getInstance() {

        if(requestService == null){
            synchronized (RequestService.class){
                if(requestService == null){
                    requestService = new RequestService();
                }
            }
        }

        return requestService;
    }

    public Api getApi(){

       return retrofit.create(Api.class);
    }
}
