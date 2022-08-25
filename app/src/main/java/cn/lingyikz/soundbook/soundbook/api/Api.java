package cn.lingyikz.soundbook.soundbook.api;

import cn.lingyikz.soundbook.soundbook.modle.Album;
import cn.lingyikz.soundbook.soundbook.modle.AlbumDetail;
import cn.lingyikz.soundbook.soundbook.pojo.PostInfo;
import cn.lingyikz.soundbook.soundbook.pojo.Test;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface Api {


      @FormUrlEncoded
      @POST("album/list")
      Observable<Album> getPostInfo(@Field("page") int page , @Field("size") int size, @Field("keyword") String keyword) ;

      @FormUrlEncoded
      @POST("xmly/list")
      Observable<AlbumDetail> getAlbumDetail(@Field("page") int page , @Field("size") int size, @Field("albumId") int albumId) ;


}
