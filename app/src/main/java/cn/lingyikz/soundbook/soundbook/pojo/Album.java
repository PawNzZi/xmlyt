package cn.lingyikz.soundbook.soundbook.pojo;

import rx.Observer;

public class Album implements Observer<Album> {
    private AlbumList albumList ;
    public Album(AlbumList albumList){
        this.albumList = albumList ;
    }
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Album album) {
        albumList.getAlbumList(album);
    }

    public interface AlbumList{
        void getAlbumList(Album album);
    }
}
