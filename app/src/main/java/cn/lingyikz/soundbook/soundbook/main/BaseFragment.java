package cn.lingyikz.soundbook.soundbook.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public abstract class BaseFragment extends Fragment {



    /**
     * 当fragment与activity发生关联时调用
     * @param context  与之相关联的activity
     */
    @Override
    public void onAttach(@NonNull Context context) {
//        Log.i("TAG","onAttach");
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,Bundle savedInstanceState) {
//        Log.i("TAG","onCreateView");

        return setLayout( inflater, container);
    }

    @Override
    public void onStart() {
//        Log.i("TAG"," base onStart");
//        Constans.user = SharedPreferences.getUser(getContext());
        super.onStart();
    }

    /**
     * 绑定布局
     * @return
     */
    protected abstract View setLayout(LayoutInflater inflater,  ViewGroup container);

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        Log.i("TAG","onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        setView();
        setData();
    }

    /**
     * 初始化组件
     */
    protected abstract void setView();



    /**
     * 设置数据等逻辑代码
     */
    protected abstract void setData();

}
