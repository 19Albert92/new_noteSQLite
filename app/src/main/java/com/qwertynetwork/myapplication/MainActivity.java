package com.qwertynetwork.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qwertynetwork.myapplication.adapter.MainAdapter;
import com.qwertynetwork.myapplication.db.DBHelper;
import com.qwertynetwork.myapplication.db.MyConstants;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton buttonGet;

    private DBHelper dbHelper;
    private MainAdapter mainAdapter;
    private RecyclerView recyclerView;
    private SetOnMyClick set;

    private String title;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialRecyclerView();
        initialView();
        initialDb();
    }

    private void initialView() {
        buttonGet = (FloatingActionButton) findViewById(R.id.button_go);
        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_create_titleAndText = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent_create_titleAndText);
            }
        });
    }

    private void initialDb() {
        dbHelper = new DBHelper(this);
    }

    private void initialRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        mainAdapter = new MainAdapter(this);
        recyclerView.setAdapter(mainAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbHelper.openDB();
        mainAdapter.updateAdapter(dbHelper.getFromDB());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.closeDB();
    }

}