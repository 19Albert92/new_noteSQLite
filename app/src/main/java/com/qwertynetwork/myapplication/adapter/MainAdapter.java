package com.qwertynetwork.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qwertynetwork.myapplication.EditActivity;
import com.qwertynetwork.myapplication.R;
import com.qwertynetwork.myapplication.SetOnMyClick;
import com.qwertynetwork.myapplication.db.DBHelper;
import com.qwertynetwork.myapplication.db.MyConstants;
import com.qwertynetwork.myapplication.model.ListItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    private List<ListItem> mainArray;
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
        return new MyViewHolder(view, context, mainArray);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textTitle.setText(mainArray.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mainArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textTitle;
        private Context context;
        private List<ListItem> mainArray;

        public MyViewHolder(@NonNull View itemView, Context context, List<ListItem> mainArray) {
            super(itemView);
            this.mainArray = mainArray;
            this.context = context;
            textTitle = itemView.findViewById(R.id.textTitleForAdapter);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_Edit = new Intent(context, EditActivity.class);
                    intent_Edit.putExtra(MyConstants.LIST_ITEM_INTENT, mainArray.get(getAdapterPosition()));
                    intent_Edit.putExtra(MyConstants.EDIT_STATE, false);
                    context.startActivity(intent_Edit);
                }
            });
        }
    }

    public void updateAdapter(List<ListItem> newList) {
        mainArray.clear();
        mainArray.addAll(newList);
        notifyDataSetChanged();
    }

    public void removeItem(int position, DBHelper dbHelper) {
        dbHelper.deleteDBForId(mainArray.get(position).getId());
        mainArray.remove(position);
        notifyItemRangeChanged(position, mainArray.size());
        notifyItemRemoved(position);
    }
}
