package com.qwertynetwork.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qwertynetwork.myapplication.R;
import com.qwertynetwork.myapplication.SetOnMyClick;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    private List<String> mainArray;
    private Context context;

    public MainAdapter(SetOnMyClick setOnMyClick) {
        this.setOnMyClick = setOnMyClick;
    }

    public SetOnMyClick setOnMyClick;

    public MainAdapter(Context context) {
        this.context = context;
        mainArray = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textTitle.setText(mainArray.get(position));
        /*holder.textTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnMyClick.onMyClick(v);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mainArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitleForAdapter);
        }
    }

    public void updateAdapter(List<String> newList) {
        mainArray.clear();
        mainArray.addAll(newList);
        notifyDataSetChanged();
    }
}
