package cn.lingyikz.soundbook.soundbook.base;

import android.widget.Toast;

import rx.Observer;

public abstract class BaseObsever<T> implements Observer<T>     {

    
    @Override
    public void onCompleted() {
     
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(T t) {

    }
}
