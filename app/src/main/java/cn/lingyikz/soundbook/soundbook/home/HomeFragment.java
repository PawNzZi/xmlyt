package cn.lingyikz.soundbook.soundbook.home;



import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.liys.onclickme.LOnClickMe;
import com.liys.onclickme_annotations.AClick;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import cn.lingyikz.soundbook.soundbook.R;
import cn.lingyikz.soundbook.soundbook.api.RequestService;
import cn.lingyikz.soundbook.soundbook.databinding.FragmentHomeBinding;
import cn.lingyikz.soundbook.soundbook.home.activity.SearchActivity;
import cn.lingyikz.soundbook.soundbook.home.adapter.HomeAdapter;
import cn.lingyikz.soundbook.soundbook.modle.Album;
import cn.lingyikz.soundbook.soundbook.service.AudioService;
import cn.lingyikz.soundbook.soundbook.utils.Constans;
import cn.lingyikz.soundbook.soundbook.utils.DataBaseHelper;
import cn.lingyikz.soundbook.soundbook.utils.IntentAction;
import cn.lingyikz.soundbook.soundbook.utils.MediaPlayer;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class HomeFragment extends Fragment implements HomeAdapter.ItemOperaCallBack {

    private FragmentHomeBinding binding;
    private HomeAdapter homeAdapter = null;
    private SearchFragment searchFragment ;
    private List<Album.DataDTO.ListDTO> mList  = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        LOnClickMe.init(this,binding.getRoot());
        binding.titleBar.title.setText("首页");
        binding.titleBar.goBacK.setVisibility(View.GONE);
        binding.titleBar.goSearch.setVisibility(View.VISIBLE);
        searchFragment = SearchFragment.newInstance();
        searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
            @Override
            public void OnSearchClick(String keyword) {
                //这里处理逻辑
//                Toast.makeText(getActivity(), keyword, Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("keyword",keyword);
                IntentAction.setValueActivity(getActivity(), SearchActivity.class,bundle);
            }
        });
        this.initData(view);
        return view ;
    }
    public void initData(View view){

        Observable<Album> observable  = RequestService.getInstance().getApi().getPostInfo(1,Constans.PAGE_SIZE,"");
        observable.subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                .observeOn(AndroidSchedulers.mainThread()) // UI线程处理返回接口
                .subscribe(new Observer<Album>() { // 订阅

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Album postInfo) {
//                        Log.i("http返回：", postInfo.toString() + "");
                        if(postInfo.getCode() == 200 && postInfo.getData().getList().size() > 0){
                            if(mList == null){
                                mList = new ArrayList<>();
                            }
                            mList.clear();
                            List<Album.DataDTO.ListDTO> newList = postInfo.getData().getList();
                            mList.addAll(newList) ;
                            if(homeAdapter == null){
                                homeAdapter = new HomeAdapter(mList,getContext(),0,HomeFragment.this);
                                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                binding.recyclerView.setAdapter(homeAdapter);
                                DividerItemDecoration divider = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
                                binding.recyclerView.addItemDecoration(divider);
                            }
                            homeAdapter.notifyDataSetChanged();

                        }else{
                            Toast.makeText(getContext(), Constans.EMPTY_TOAST, Toast.LENGTH_SHORT).show();
                        }



                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
//        Log.i("Tag","onStart");
//        Log.i("Tag","onStart:"+ MediaPlayer.getInstance().isPlay());
        if(MediaPlayer.getInstance().isPlay()){
            binding.titleBar.goPlay.setImageDrawable(getResources().getDrawable(R.mipmap.title_play, getResources().newTheme()));
        }else {
            binding.titleBar.goPlay.setImageDrawable(getResources().getDrawable(R.mipmap.title_pause, getResources().newTheme()));
        }

    }

    @Override
    public void onStop() {
        super.onStop();
//        Log.i("Tag","onStop");
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.i("Tag","onResume");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @AClick({R.id.go_search, R.id.go_play})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.go_search:
//                Log.i("TAG","onclick");
                searchFragment.showFragment(getActivity().getSupportFragmentManager(),SearchFragment.TAG);
                break;
            case R.id.go_play:
                IntentAction.startService(getActivity(), AudioService.class, (ImageView) view, DataBaseHelper.getInstance(getActivity()));
                break;
        }
    }

    @Override
    public void deleteItem(int albumId) {

    }
}
