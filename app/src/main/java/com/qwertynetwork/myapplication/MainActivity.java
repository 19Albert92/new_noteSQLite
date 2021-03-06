package com.qwertynetwork.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qwertynetwork.myapplication.adapter.MainAdapter;
import com.qwertynetwork.myapplication.db.DBHelper;
import com.qwertynetwork.myapplication.db.OnDataReceived;
import com.qwertynetwork.myapplication.model.ListItem;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDataReceived {

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
        getItemTouchHelper().attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(mainAdapter);
    }

    //?????????????????? ???????? search ?? ?????????????????? ??????????????????
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        MenuItem item = menu.findItem(R.id.item_search);
        SearchView sv = (SearchView) item.getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("myLog", "Query " + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                Log.d("myLog", "newText " + newText);
                readFromDB(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onResume() {
        super.onResume();
        dbHelper.openDB();
        readFromDB("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.closeDB();
    }

    //??????????
    private ItemTouchHelper getItemTouchHelper() {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mainAdapter.removeItem(viewHolder.getAdapterPosition(), dbHelper);
                Log.d("myLog", "Touch to " + direction);
            }
        });
    }

    private void readFromDB(final String text) {
        AppExecuter.getInstance().getSubIO().execute(new Runnable() {
            @Override
            public void run() {
                dbHelper.getFromDB(text, MainActivity.this);
            }
        });
    }

    public void onReceived(List<ListItem> list) {
        AppExecuter.getInstance().getMainIO().execute(new Runnable() {
            @Override
            public void run() {
                mainAdapter.updateAdapter(list);
            }
        });
    }

}