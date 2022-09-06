package cn.lingyikz.soundbook.soundbook.category;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liys.onclickme.LOnClickMe;
import com.liys.onclickme_annotations.AClick;
import cn.lingyikz.soundbook.soundbook.R;
import cn.lingyikz.soundbook.soundbook.databinding.FragmentCategoryBinding;
import cn.lingyikz.soundbook.soundbook.main.BaseFragment;

public class CategoryFragment extends BaseFragment {

    private FragmentCategoryBinding binding ;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    protected View setLayout(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentCategoryBinding.inflate(LayoutInflater.from(getActivity()),container,false);
        LOnClickMe.init(this,binding.getRoot());
        return binding.getRoot();
    }

    @Override
    protected void setView() {

    }

    @Override
    protected void setData() {

    }

    @AClick({R.id.copyQQGruop})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.copyQQGruop:
//                Toast.makeText(getActivity(), "复制成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null ;
    }
}
