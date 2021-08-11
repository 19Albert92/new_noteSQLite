package com.qwertynetwork.myapplication;

import android.content.Intent;
import android.net.Uri;
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
import com.qwertynetwork.myapplication.model.ListItem;

public class EditActivity extends AppCompatActivity {

    private EditText edTitleSave, edTextSave;
    private FloatingActionButton buttonSave, buttonAddImage;
    private ImageButton imageButtonAdd, imageButtonDelete;
    private ImageView image_crete;
    private ConstraintLayout container_for_image;
    private ListItem item;

    private DBHelper dbHelper;

    private String tempUri = "empty";
    private boolean isEditState = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initialView();
        getMyIntent();
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

    private void getMyIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            item = (ListItem) intent.getSerializableExtra(MyConstants.LIST_ITEM_INTENT);
            isEditState = intent.getBooleanExtra(MyConstants.EDIT_STATE, true);

            if (!isEditState) {
                edTitleSave.setText(item.getTitle());
                edTextSave.setText(item.getDescription());
                if (!item.getUri().equals("empty")) {
                    tempUri = item.getUri();
                    container_for_image.setVisibility(View.VISIBLE);
                    image_crete.setImageURI(Uri.parse(item.getUri()));
                    imageButtonAdd.setVisibility(View.GONE);
                    imageButtonDelete.setVisibility(View.GONE);
                }
            }
        }
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
                Intent search_image_intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
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

    //Сохранение фотографии на долго
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MyConstants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            tempUri = data.getData().toString();
            image_crete.setImageURI(data.getData());
            getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    //открываем базу данных
    @Override
    protected void onResume() {
        super.onResume();
        dbHelper.openDB();
    }

    private void createClickForButtonSave() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String title = edTitleSave.getText().toString();
                final String text = edTextSave.getText().toString();

                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(text)) {
                    Toast.makeText(EditActivity.this, "Вы не заполнили поля", Toast.LENGTH_SHORT).show();
                } else {
                    if (isEditState) {
                        AppExecuter.getInstance().getSubIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                dbHelper.insertToDB(title, text, tempUri);
                                Toast.makeText(EditActivity.this, "Все успешно сохранено", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        dbHelper.updateItemToId(title, text, tempUri, item.getId());
                    }
                    //закрываем базу
                    dbHelper.closeDB();
                    finish();
                }
            }
        });
    }
}