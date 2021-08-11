package com.qwertynetwork.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qwertynetwork.myapplication.adapter.MainAdapter;
import com.qwertynetwork.myapplication.db.DBHelper;
import com.qwertynetwork.myapplication.db.MyConstants;

public class EditActivity extends AppCompatActivity {

    private EditText edTitleSave, edTextSave;
    private FloatingActionButton buttonSave ,buttonGet;

    private DBHelper dbHelper;
    private MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initialView();
    }

    private void initialView() {
        dbHelper = new DBHelper(this);

        edTitleSave = (EditText) findViewById(R.id.edTitleSave);
        edTextSave = (EditText) findViewById(R.id.edTextSave);

        buttonSave = (FloatingActionButton) findViewById(R.id.button_save);
        createClickForButtonSave();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbHelper.openDB();
    }

    private void createClickForButtonSave() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edTitleSave.getText().toString();
                String text = edTextSave.getText().toString();

                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(text)) {
                    Toast.makeText(EditActivity.this, "Вы не заполнили поля", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.insertToDB(title, text);
                    Toast.makeText(EditActivity.this, "Все успешно сохранено", Toast.LENGTH_SHORT).show();
                    finish();
                    dbHelper.closeDB();
                }
            }
        });
    }
}