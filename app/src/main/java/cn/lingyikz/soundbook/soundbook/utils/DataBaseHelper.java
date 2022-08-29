package cn.lingyikz.soundbook.soundbook.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import cn.lingyikz.soundbook.soundbook.modle.Album;
import cn.lingyikz.soundbook.soundbook.modle.AlbumDetail;

public class DataBaseHelper extends SQLiteOpenHelper {

    public final static int DB_VERSION = 1;
    public final static String DB_NAME = "audio";
    private volatile static DataBaseHelper dataBaseHelper ;
    private SQLiteDatabase db = null ;

    private DataBaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
//        Log.i("TAG","DataBaseHelper");
    }
    public static DataBaseHelper getInstance(Context context) {
        if(dataBaseHelper == null){
            synchronized (DataBaseHelper.class){
                if(dataBaseHelper == null){
                    dataBaseHelper = new DataBaseHelper(context);
                }
            }
        }
        return dataBaseHelper;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        Log.i("TAG","SQLonCreate");
        sqLiteDatabase.execSQL("CREATE TABLE collection(_id INTEGER PRIMARY KEY AUTOINCREMENT, albumId INTEGER, albumName TEXT,albumDes TEXT,albumThumb TEXT," +
                "uuid TEXT);");
        sqLiteDatabase.execSQL("CREATE TABLE playhistory(_id INTEGER PRIMARY KEY AUTOINCREMENT, albumId INTEGER, albumName TEXT,episodes INTEGER,audioTitle TEXT,audioDes TEXT," +
                "audioCreated TEXT,audioDuration TEXT,audioSrc TEXT ,audioId INTEGER,uuid TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

//        Log.i("TAG","onUpgrade");
    }

    public SQLiteDatabase getReadLink(){
        if(db == null || !db.isOpen()){
            db = dataBaseHelper.getReadableDatabase();
        }
        return db ;
    }
    public SQLiteDatabase getWriteLink(){
        if(db == null || !db.isOpen()){
            db = dataBaseHelper.getWritableDatabase();
        }
        return db ;
    }
    public void close(){
        db.close();
    }

    /**
     * 查询改专辑是否被收藏
     * @param albumId
     * @return
     */
    public int queryCollectionCount(int albumId){
        db = getReadLink();
        Cursor cursor = db.rawQuery("select * from collection where albumId = ? ",new String[]{String.valueOf(albumId)});
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    /**
     * 删除收藏历史
     * @param albumId
     */
    public void cancleCollection(int albumId){
        db = getWriteLink();
        String sql = "delete from collection where albumId = "+ albumId;
        db.execSQL(sql);

    }

    /**
     * 插入收藏历史
     * @param albumDetail
     */
    public void addCollection(Album.DataDTO.ListDTO albumDetail){
        db = getWriteLink();
        ContentValues contentValues = new ContentValues();
        contentValues.put("albumId",albumDetail.getId());
        contentValues.put("albumName",albumDetail.getName());
        contentValues.put("albumDes",albumDetail.getDescription());
        contentValues.put("albumThumb",albumDetail.getThumb().toString());

        db.insert("collection",null,contentValues);

    }

    /**
     * 查询收藏历史列表
     * @return
     */
    public List<Album.DataDTO.ListDTO> queryCollectionAll(){
        db = getReadLink();
        List<Album.DataDTO.ListDTO> mList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from collection ",new String[]{});
//        Log.i("TAG","有:"+cursor.getCount());
        if(cursor.moveToFirst()){
            for (int i = 0; i < cursor.getCount(); i++) {
                Album.DataDTO.ListDTO listDTO = new Album.DataDTO.ListDTO();
                listDTO.setId(cursor.getInt(cursor.getColumnIndex("albumId")));
                listDTO.setName(cursor.getString(cursor.getColumnIndex("albumName")));
                listDTO.setDescription(cursor.getString(cursor.getColumnIndex("albumDes")));
                listDTO.setThumb(cursor.getString(cursor.getColumnIndex("albumThumb")));
                mList.add(listDTO);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return mList ;
    }

    /**
     * 插入或修改播放历史
     * @param map
     */
    public void addPlayHistory(Map<String,Object> map){
//        Log.i("TAG","日期:"+map.get("audioCreated").toString());
        db = getWriteLink();
        String querySql = "select * from playhistory where albumId = ? ";
        Cursor cursor = db.rawQuery(querySql,new String[]{String.valueOf(map.get("albumId"))});
        int count = cursor.getCount();
//        Log.i("TAG","COUNT:"+cursor.getCount());
        if(count == 0){
            //
            ContentValues contentValues = new ContentValues();
            contentValues.put("albumId",(Integer) map.get("albumId"));
            contentValues.put("episodes",(Integer) map.get("episodes"));
            contentValues.put("audioTitle",String.valueOf(map.get("title")));
//            contentValues.put("uuid",uuid);
            contentValues.put("audioDes",String.valueOf(map.get("audioDes")));
            contentValues.put("audioDuration",String.valueOf(map.get("audioDuration")));
            contentValues.put("audioCreated",String.valueOf(map.get("audioCreated")));
            contentValues.put("audioSrc",String.valueOf(map.get("src")));
            contentValues.put("audioId",(Integer)map.get("audioId"));

            db.insert("playhistory",null,contentValues);

        }
//        else if(count == 1){
//            //
//
//            if(cursor.moveToFirst()){
//                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
//                String updateSql = "update playhistory set audioDuration = '"+ String.valueOf(map.get("audioDuration")) +"' where _id = "+_id;
//                db.execSQL(updateSql);
//            }
//        }
        else if(count > 0){
            String deleteSql = "delete from playhistory where albumId = "+ map.get("albumId");
            db.execSQL(deleteSql);
            ContentValues contentValues = new ContentValues();
            contentValues.put("albumId",(Integer) map.get("albumId"));
            contentValues.put("episodes",(Integer) map.get("episodes"));
            contentValues.put("audioTitle",String.valueOf(map.get("title")));
//            contentValues.put("uuid",uuid);
            contentValues.put("audioDes",String.valueOf(map.get("audioDes")));
            contentValues.put("audioDuration",String.valueOf(map.get("audioDuration")));
            contentValues.put("audioCreated",String.valueOf(map.get("audioCreated")));
            contentValues.put("audioSrc",String.valueOf(map.get("src")));
            contentValues.put("audioId",(Integer)map.get("audioId"));

            db.insert("playhistory",null,contentValues);
        }
        cursor.close();

    }
    public void deletePlayHistory(int albumId){
        db = getWriteLink();
        String deleteSql = "delete from playhistory where albumId = "+ albumId;
        db.execSQL(deleteSql);

//        return queryPlayHistoryAll();
    }
    /**
     * 查询播放历史列表
     * @return
     */
    public List<AlbumDetail.DataDTO.ListDTO> queryPlayHistoryAll(){
        db = getReadLink();
        List<AlbumDetail.DataDTO.ListDTO> mList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from playhistory order by audioCreated desc",new String[]{});
//        Log.i("TAG","有:"+cursor.getCount());
        if(cursor.moveToFirst()){
            for (int i = 0; i < cursor.getCount(); i++) {

//                Log.i("TAG",i+"");
                AlbumDetail.DataDTO.ListDTO listDTO = new AlbumDetail.DataDTO.ListDTO();
                listDTO.setId(cursor.getInt(cursor.getColumnIndex("audioId")));
                listDTO.setName(cursor.getString(cursor.getColumnIndex("audioTitle")));
                listDTO.setDescription(cursor.getString(cursor.getColumnIndex("audioDes")));
                listDTO.setAlbumId(cursor.getInt(cursor.getColumnIndex("albumId")));
                listDTO.setEpisodes(cursor.getInt(cursor.getColumnIndex("episodes")));
                listDTO.setCreated(Long.parseLong(cursor.getString(cursor.getColumnIndex("audioCreated"))));
//                Log.i("TAG","x:"+cursor.getString(cursor.getColumnIndex("audioCreated")));
                listDTO.setUrl(cursor.getString(cursor.getColumnIndex("audioSrc")));
                mList.add(listDTO);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return mList ;
    }

    /**
     * 查询当前播放进度
     * @param albumId
     * @param audioId
     * @return
     */
    public long queryPlayHistory(int albumId,int audioId){
        db = getReadLink();
        String querySql = "select audioDuration from playhistory where albumId = ? and audioId = ?";
        Cursor cursor = db.rawQuery(querySql,new String[]{String.valueOf(albumId),String.valueOf(audioId)});

//        Log.i("TAG","bofanglishi:"+cursor.getCount());
        if(cursor.moveToFirst()){
            Long result = Long.parseLong(cursor.getString(cursor.getColumnIndex("audioDuration")));
            cursor.close();

            return result;

        }else{
            cursor.close();
            return 0;
        }


    }

}
