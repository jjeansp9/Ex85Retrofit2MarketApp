package com.jspstudio.ex85retrofit2marketapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jspstudio.ex85retrofit2marketapp.databinding.RecyclerItemBinding;

import java.util.ArrayList;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.VH> {

    Context context;
    ArrayList<MarketItem> items;

    public MarketAdapter(Context context, ArrayList<MarketItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.recycler_item,parent, false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        MarketItem item= items.get(position);

        holder.binding.tvTitle.setText(item.title);
        holder.binding.tvMsg.setText(item.msg);
        holder.binding.tvPrice.setText(item.price+"원");

        // 이미지연결 - DB안에는 이미지의 경로 주소만 있음. 즉, 서버컴퓨터 도메인주소가 없음.
        String imgUrl= "http://jspstudio.dothome.co.kr/05Retrofit/" + item.file;
        //Glide.with(context).load(item.file).error(R.drawable.paris).into(holder.binding.iv);
        Glide.with(context).load(imgUrl).error(R.drawable.paris).into(holder.binding.iv);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{
        RecyclerItemBinding binding;
        public VH(@NonNull View itemView) {
            super(itemView);
            binding= RecyclerItemBinding.bind(itemView);


        }
    }

}
