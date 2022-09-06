package cn.lingyikz.soundbook.soundbook.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

public abstract class BaseFragment extends Fragment {



    /**
     * 当fragment与activity发生关联时调用
     * @param context  与之相关联的activity
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,Bundle savedInstanceState) {

        return setLayout( inflater, container);
    }

    /**
     * 绑定布局
     * @return
     */
    protected abstract View setLayout(LayoutInflater inflater,  ViewGroup container);

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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
