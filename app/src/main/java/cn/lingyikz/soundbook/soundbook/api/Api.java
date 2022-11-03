package cn.lingyikz.soundbook.soundbook.api;

import cn.lingyikz.soundbook.soundbook.modle.Album;
import cn.lingyikz.soundbook.soundbook.modle.AlbumCategoryBean;
import cn.lingyikz.soundbook.soundbook.modle.AlbumCount;
import cn.lingyikz.soundbook.soundbook.modle.AlbumDetail;
import cn.lingyikz.soundbook.soundbook.modle.Banner;
import cn.lingyikz.soundbook.soundbook.modle.Category;
import cn.lingyikz.soundbook.soundbook.modle.Version;
import cn.lingyikz.soundbook.soundbook.modle.XmlyNextPaly;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface Api {


      //专辑列表查询
      @FormUrlEncoded
      @POST("album/list")
      Observable<Album> getPostInfo(@Field("page") int page , @Field("size") int size, @Field("keyword") String keyword) ;

      //专辑详情查询
      @FormUrlEncoded
      @POST("xmly/list")
      Observable<AlbumDetail> getAlbumDetail(@Field("page") int page , @Field("size") int size, @Field("albumId") int albumId) ;

      //查询下一集
      @FormUrlEncoded
      @POST("xmly/nextPlay")
      Observable<XmlyNextPaly> getNextPlay(@Field("albumId") int albumId , @Field("episodes") int episodes);

      //查询下一集
      @FormUrlEncoded
      @POST("xmly/episodesCount")
      Observable<AlbumCount> episodesCount(@Field("albumId") int albumId);

      //检查版本
      @GET("version/getVersion")
      Observable<Version> getVersion(@Query("versonCode") int versonCode);

      //查询分类
      @POST("zmlmcategory/list")
      Observable<Category> getCategory();

      //查询分类下的专辑
      @FormUrlEncoded
      @POST("albumcategory/list")
      Observable<AlbumCategoryBean> getAlbumCategory(@Field("categoryId") int categoryId,@Field("page") int page,@Field("size") int size);

      //查询banner
      @GET("dictdata/banner")
      Observable<Banner> getBanner(@Query("typeId") int typeId);
}
