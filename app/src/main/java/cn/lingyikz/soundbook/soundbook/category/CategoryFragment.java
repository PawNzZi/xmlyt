package cn.lingyikz.soundbook.soundbook.category;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.liys.onclickme.LOnClickMe;
import com.liys.onclickme_annotations.AClick;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cn.lingyikz.soundbook.soundbook.R;
import cn.lingyikz.soundbook.soundbook.databinding.FragmentCategoryBinding;

public class CategoryFragment extends Fragment {

    private FragmentCategoryBinding binding ;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(LayoutInflater.from(getActivity()),container,false);
        View view = binding.getRoot();
        LOnClickMe.init(this,binding.getRoot());
        return view;
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
