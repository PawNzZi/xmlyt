package cn.lingyikz.soundbook.soundbook.base;

import android.util.Log;

import rx.Observer;

public abstract class BaseObsever<T> implements Observer<T>     {

    
    @Override
    public void onCompleted() {
     
    }

    @Override
    public void onError(Throwable e) {
        Log.i("TAG：", e.getMessage());
    }

    @Override
    public void onNext(T t) {

    }
}
