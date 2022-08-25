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
        Log.i("TAG","DataBaseHelper");
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
        Log.i("TAG","SQLonCreate");
        sqLiteDatabase.execSQL("CREATE TABLE collection(_id INTEGER PRIMARY KEY AUTOINCREMENT, albumId INTEGER, albumName TEXT,albumDes TEXT,albumThumb TEXT," +
                "uuid TEXT);");
        sqLiteDatabase.execSQL("CREATE TABLE playhistory(_id INTEGER PRIMARY KEY AUTOINCREMENT, albumId INTEGER, episodes INTEGER,audioTitle TEXT,audioDes TEXT," +
                "audioCreated TEXT,audioDuration TEXT,audioSrc TEXT ,audioId INTEGER,uuid TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        Log.i("TAG","onUpgrade");
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
    public int queryCollectionCount(int albumId,String uuid){

        Cursor cursor = db.rawQuery("select * from collection where albumId = ? and uuid = ?",new String[]{String.valueOf(albumId),uuid});
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public void cancleCollection(int albumId,String uuid){
        String sql = "delete from collection where albumId = "+ albumId+ " and uuid = '"+uuid+"'";
        db.execSQL(sql);

    }
    public void addCollection(Album.DataDTO.ListDTO albumDetail, String uuid){
        ContentValues contentValues = new ContentValues();
        contentValues.put("albumId",albumDetail.getId());
        contentValues.put("albumName",albumDetail.getName());
        contentValues.put("albumDes",albumDetail.getDescription());
        contentValues.put("uuid",uuid);
        contentValues.put("albumThumb",albumDetail.getThumb().toString());

        db.insert("collection",null,contentValues);

    }
    public List<Album.DataDTO.ListDTO> queryCollectionAll(String uuid ){
        List<Album.DataDTO.ListDTO> mList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from collection where  uuid = ?",new String[]{uuid});
        Log.i("TAG","有:"+cursor.getCount());
        if(cursor.moveToFirst()){
            for (int i = 0; i < cursor.getCount(); i++) {
                Album.DataDTO.ListDTO listDTO = new Album.DataDTO.ListDTO();
                listDTO.setId(cursor.getInt(cursor.getColumnIndex("albumId")));
                listDTO.setName(cursor.getString(cursor.getColumnIndex("albumName")));
                listDTO.setDescription(cursor.getString(cursor.getColumnIndex("albumDes")));
                listDTO.setThumb(cursor.getString(cursor.getColumnIndex("albumThumb")));
                mList.add(listDTO);
            }
        }
        cursor.close();
        return mList ;
    }
    public void addPlayHistory(Map<String,Object> map){
        db = getWritableDatabase();
        String querySql = "select * from collection where albumId = ? and xmlyId = ?";
        Cursor cursor = db.rawQuery(querySql,new String[]{String.valueOf(map.get("albumId"))});
        int count = cursor.getCount();

        if(count == 0){
            //
            ContentValues contentValues = new ContentValues();
            contentValues.put("albumId",(Integer) map.get("albumId"));
            contentValues.put("episodes",(Integer) map.get("episodes"));
            contentValues.put("audioTitle",String.valueOf(map.get("title")));
//            contentValues.put("uuid",uuid);
            contentValues.put("audioDes",String.valueOf(map.get("audioDes")));
            contentValues.put("audioDuration",String.valueOf(map.get("audioDuration")));
            contentValues.put("audioCreated",String.valueOf(map.get("albumId")));
            contentValues.put("audioSrc",String.valueOf(map.get("src")));
            contentValues.put("audioId",(Integer)map.get("audioId"));

            db.insert("playhistory",null,contentValues);

        }else if(count == 1){
            //
            int _id = cursor.getInt(cursor.getColumnIndex("_id"));
            String updateSql = "update playhistory set audioDuration = '"+ String.valueOf(map.get("audioDuration")) +"' where _id = "+_id;
            db.execSQL(updateSql);
        }else {
            String deleteSql = "delete from playhistory where albumId = "+ map.get("albumId")+ " and audioId = "+map.get("audioId");
            db.execSQL(deleteSql);
        }
        cursor.close();

    }
}
