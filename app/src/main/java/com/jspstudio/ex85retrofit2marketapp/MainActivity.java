package com.jspstudio.ex85retrofit2marketapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.jspstudio.ex85retrofit2marketapp.databinding.ActivityMainBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    ArrayList<MarketItem> items= new ArrayList<>();

    MarketAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter= new MarketAdapter(this, items);
        binding.recyclerView.setAdapter(adapter);
        // 리사이클러뷰에 구분선 꾸미기 ( ItemDecoration - DividerItemDecoration )
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // 스와이프 리프레시 뷰 드래그하여 서버데이터 갱신
        binding.swipeRefreshLayout.setOnRefreshListener(()-> loadData());


        binding.fabEdit.setOnClickListener(v-> startActivity(new Intent(this, EditActivity.class)));

        // 외부저장소에 대한 퍼미션 - 사진업로드를 위해 필요 - 2개를 요청해도 외부메모리사용 요청은 하나만 요청함
        String[] permissions= new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_MEDIA_LOCATION};
        if( checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_DENIED){
            requestPermissions(permissions, 100); //requestCode는 식별코드라서 아무거나 쓰면됨
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();
    }

    void loadData(){
        // 테스트로 우선 더미데이터를 추가해보기
//        items.add(new MarketItem(1, "aaa", "aaabb", "vvvv", "1000", "", "2022"));
//        items.add(new MarketItem(1, "aaa", "aaabb", "vvvv", "1000", "", "2022"));
//        items.add(new MarketItem(1, "aaa", "aaabb", "vvvv", "1000", "", "2022"));


        // 서버에서 market테이블의 DB정보를 가져와서 대량의 데이터(items)에 추가
        // Retrofit 을 이용하여 데이터 가져오기
        Retrofit retrofit= RetrofitHelper.getRetrofitInstance();
        RetrofitService retrofitService= retrofit.create(RetrofitService.class);
        Call<ArrayList<MarketItem>> call= retrofitService.loadDataFromServer();
        call.enqueue(new Callback<ArrayList<MarketItem>>() {
            @Override
            public void onResponse(Call<ArrayList<MarketItem>> call, Response<ArrayList<MarketItem>> response) {

                // 기존 데이터들 모두 삭제
                items.clear();
                adapter.notifyDataSetChanged();

                // 결과 json array를 곧바로 파싱하여 Arraylist<MarketItem>로 변환된 리스트 받기
                ArrayList<MarketItem> list= response.body();
                for(MarketItem item : list){
                    items.add(0, item);
                    adapter.notifyItemInserted(0);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MarketItem>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.swipeRefreshLayout.setRefreshing(false); // 로딩 아이콘 제거


    }


}















