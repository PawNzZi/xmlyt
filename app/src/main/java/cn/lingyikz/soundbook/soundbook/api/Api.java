package cn.lingyikz.soundbook.soundbook.api;


import cn.lingyikz.soundbook.soundbook.modle.v2.BaseModel;
import cn.lingyikz.soundbook.soundbook.modle.v2.Category;
import cn.lingyikz.soundbook.soundbook.modle.v2.AlbumSound;
import cn.lingyikz.soundbook.soundbook.modle.v2.CategoryAlbum;
import cn.lingyikz.soundbook.soundbook.modle.v2.Collection;
import cn.lingyikz.soundbook.soundbook.modle.v2.CollectionHistory;
import cn.lingyikz.soundbook.soundbook.modle.v2.Sound;
import cn.lingyikz.soundbook.soundbook.modle.v2.User;
import cn.lingyikz.soundbook.soundbook.modle.v2.UserInfo;
import cn.lingyikz.soundbook.soundbook.modle.v2.Version;
import cn.lingyikz.soundbook.soundbook.modle.v2.Album;
import cn.lingyikz.soundbook.soundbook.modle.v2.HomeBanner;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface Api {


      //专辑列表查询
      @GET("/zmlmAlbum/app/home/list")
      Observable<Album> getPostInfo(@Query("pageNo") int page , @Query("pageSize") int size, @Query("keyword") String keyword) ;

      //专辑详情查询
      @GET("/zmlmSound/app/list")
      Observable<AlbumSound> getAlbumDetail(@Query("pageNo") int page , @Query("pageSize") int size, @Query("albumId") Long albumId) ;

      //查询下一集
      @GET("/zmlmSound/app/nextEpisodes")
      Observable<Sound> getNextPlay(@Query("albumId") Long albumId , @Query("episodes") int episodes);


      //检查版本
      @GET("/zmlmVersion/app/checkVersion")
      Observable<Version> getVersion(@Query("versonCode") int versonCode);

      //查询分类
      @GET("/zmlmCategory/app/getList")
      Observable<Category> getCategory();

      //查询分类下的专辑
      @GET("/zmlmMidCategory/app/list")
      Observable<CategoryAlbum> getAlbumCategory(@Query("categoryId") Long categoryId, @Query("pageNo") int page, @Query("pageSize") int size);

      //查询banner
      @GET("/zmlmConfig/app/config")
      Observable<HomeBanner> getHomeBanner(@Query("code") String code);

      //用户注册
      @POST("/zmlmUser/app/register")
      Observable<BaseModel> userRegister(@Body User user);
      //用户登录
      @POST("/zmlmUser/app/login")
      Observable<UserInfo> login(@Body User user);
      //是否收藏
      @GET("/zmlmCollectionHistory/app/isCollection")
      Observable<BaseModel> isCollection(@Query("userId") Long userId,@Query("albumId") Long albumId);
      //修改收藏
      @GET("/zmlmCollectionHistory/app/changeCollection")
      Observable<BaseModel> changeCollection(@Query("userId") Long userId,@Query("albumId") Long albumId);
      //收藏历史列表
      @GET("/zmlmCollectionHistory/app/list")
      Observable<CollectionHistory> collectionList(@Query("userId") Long userId, @Query("pageNo") int page, @Query("pageSize") int size);
}
