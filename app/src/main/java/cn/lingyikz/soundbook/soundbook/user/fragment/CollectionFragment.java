package cn.lingyikz.soundbook.soundbook.user.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import cn.lingyikz.soundbook.soundbook.R;
import cn.lingyikz.soundbook.soundbook.databinding.FragmentCollectionBinding;
import cn.lingyikz.soundbook.soundbook.home.adapter.HomeAdapter;
import cn.lingyikz.soundbook.soundbook.modle.Album;
import cn.lingyikz.soundbook.soundbook.pojo.ItemHome;
import cn.lingyikz.soundbook.soundbook.utils.DataBaseHelper;
import cn.lingyikz.soundbook.soundbook.utils.SharedPreferences;

public class CollectionFragment extends Fragment {

    private FragmentCollectionBinding binding ;
    private HomeAdapter adapter ;
    private List<Album.DataDTO.ListDTO> mList ;
    private DataBaseHelper dataBaseHelper ;
    @Override
    public void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {
        binding = FragmentCollectionBinding.inflate(LayoutInflater.from(getContext()),container,false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();

    }

    private void initData() {
        Log.i("TAG","initData");
        dataBaseHelper = DataBaseHelper.getInstance(getActivity());
        dataBaseHelper.getReadLink();
        mList = dataBaseHelper.queryCollectionAll(SharedPreferences.getUUID(getActivity()));
        dataBaseHelper.close();
        Log.i("TAG","mList.size:"+mList.size());
        binding.swipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HomeAdapter(mList,getContext());
        binding.swipeRecyclerView.setAdapter(adapter);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null ;
    }
}
