package com.qwertynetwork.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qwertynetwork.myapplication.db.DBHelper;
import com.qwertynetwork.myapplication.db.MyConstants;

public class EditActivity extends AppCompatActivity {

    private EditText edTitleSave, edTextSave;
    private FloatingActionButton buttonSave, buttonAddImage;
    private ImageButton imageButtonAdd, imageButtonDelete;
    private ImageView image_crete;
    private ConstraintLayout container_for_image;

    private DBHelper dbHelper;

    private String tempUri = "empty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initialView();
    }

    private void initialView() {
        dbHelper = new DBHelper(this);

        imageButtonAdd = (ImageButton) findViewById(R.id.button_image_add);
        searchDisplayImage();
        imageButtonDelete = (ImageButton) findViewById(R.id.button_image_delete);
        deleteDisplayImage();

        image_crete = (ImageView) findViewById(R.id.image_crete);
        image_crete.setImageResource(R.drawable.ic_image_def);

        container_for_image = (ConstraintLayout) findViewById(R.id.container_for_image);

        edTitleSave = (EditText) findViewById(R.id.edTitleSave);
        edTextSave = (EditText) findViewById(R.id.edTextSave);

        buttonSave = (FloatingActionButton) findViewById(R.id.button_save);
        createClickForButtonSave();

        buttonAddImage = (FloatingActionButton) findViewById(R.id.button_search_image);
        addClickForButtonSave();
    }

    private void deleteDisplayImage() {
        imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_crete.setImageResource(R.drawable.ic_image_def);
                container_for_image.setVisibility(View.GONE);
                buttonAddImage.setVisibility(View.VISIBLE);
                tempUri = "empty";
            }
        });
    }

    private void searchDisplayImage() {
        imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search_image_intent = new Intent(Intent.ACTION_PICK);
                search_image_intent.setType("image/*");
                startActivityForResult(search_image_intent, MyConstants.REQUEST_CODE);
            }
        });
    }

    private void addClickForButtonSave() {
        buttonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container_for_image.setVisibility(View.VISIBLE);
                buttonAddImage.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyConstants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            tempUri = data.getData().toString();
            image_crete.setImageURI(data.getData());
        }
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
                    dbHelper.insertToDB(title, text, tempUri);
                    Toast.makeText(EditActivity.this, "Все успешно сохранено", Toast.LENGTH_SHORT).show();
                    finish();
                    dbHelper.closeDB();
                }
            }
        });
    }
}