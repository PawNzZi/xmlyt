package cn.lingyikz.soundbook.soundbook.user.fragment;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import cn.lingyikz.soundbook.soundbook.databinding.FragmentCollectionBinding;
import cn.lingyikz.soundbook.soundbook.home.adapter.HomeAdapter;
import cn.lingyikz.soundbook.soundbook.modle.Album;
import cn.lingyikz.soundbook.soundbook.utils.DataBaseHelper;

public class CollectionFragment extends Fragment implements HomeAdapter.ItemOperaCallBack {

    private FragmentCollectionBinding binding ;
    private HomeAdapter adapter = null;
    private List<Album.DataDTO.ListDTO> mList = new ArrayList<>();
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
//        Log.i("TAG","initData");
        dataBaseHelper = DataBaseHelper.getInstance(getActivity());
        if(mList == null){
            mList = new ArrayList<>();
        }
        mList.clear();
        List<Album.DataDTO.ListDTO> newList = dataBaseHelper.queryCollectionAll();
        mList.addAll(newList);
        dataBaseHelper.close();
//        Log.i("TAG","mList.size:"+mList.size());
        if(adapter == null){
            binding.swipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new HomeAdapter(mList,getContext(),1,this);
            binding.swipeRecyclerView.setAdapter(adapter);
            DividerItemDecoration divider = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
            binding.swipeRecyclerView.addItemDecoration(divider);
        }
        adapter.notifyDataSetChanged();


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null ;
    }

    @Override
    public void deleteItem(int albumId) {
        dataBaseHelper = DataBaseHelper.getInstance(getContext());
        dataBaseHelper.cancleCollection(albumId);
        dataBaseHelper.close();
        initData();
    }
}
