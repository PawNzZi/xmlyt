package cn.lingyikz.soundbook.soundbook.interfaces;

public interface ISubcriber<T> {
    void doError(String error);
    void doNext(T t);
    void doSubcribe();
}
