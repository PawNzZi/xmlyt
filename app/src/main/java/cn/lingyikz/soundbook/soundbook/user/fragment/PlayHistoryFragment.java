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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import cn.lingyikz.soundbook.soundbook.databinding.FragmentPalyhistoryBinding;
import cn.lingyikz.soundbook.soundbook.home.adapter.HomeAdapter;
import cn.lingyikz.soundbook.soundbook.modle.Album;
import cn.lingyikz.soundbook.soundbook.utils.DataBaseHelper;
import cn.lingyikz.soundbook.soundbook.utils.SharedPreferences;

public class PlayHistoryFragment extends Fragment {
    private FragmentPalyhistoryBinding binding ;
    private HomeAdapter adapter ;
    private List<Album.DataDTO.ListDTO> mList ;
    private DataBaseHelper dataBaseHelper ;
    private SQLiteDatabase db ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("TAG","onCreate");
    }

    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {
        binding = FragmentPalyhistoryBinding.inflate(LayoutInflater.from(getContext()),container,false);
        View view = binding.getRoot();
        Log.i("TAG","onCreate");
        return view ;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }
    private void initData() {
        Log.i("TAG","initData");
//        dataBaseHelper = new DataBaseHelper(getActivity());
        dataBaseHelper = DataBaseHelper.getInstance(getActivity());
        dataBaseHelper.getReadLink();
        mList = dataBaseHelper.queryCollectionAll(SharedPreferences.getUUID(getActivity()));
        dataBaseHelper.close();
        binding.swipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new HomeAdapter(mList,getContext());
        binding.swipeRecyclerView.setAdapter(adapter);


    }
    @Override
    public void onStart() {
        super.onStart();
        Log.i("TAG","onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("TAG","onStop");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("TAG","onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("TAG","onDestroy");
        binding = null ;
    }
}
