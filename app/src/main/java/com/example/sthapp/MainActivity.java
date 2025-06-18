package com.example.sthapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sthapp.helper.DatabaseHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText editTextName;
    private ListView listView;

    private int idSelected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        editTextName = findViewById(R.id.editTextName);
        listView = findViewById(R.id.listViewData);

        findViewById(R.id.buttonAdd).setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            if (!name.isEmpty()) {
                databaseHelper.addData(name);
                editTextName.setText("");
                loadData();
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            idSelected = (int) id;
            editTextName.setText(((TextView) view).getText());
        });

        findViewById(R.id.buttonEdit).setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            if (idSelected != -1 && !name.isEmpty()) {
                databaseHelper.updateData(idSelected, name);
                editTextName.setText("");
                loadData();
                idSelected = -1;
            }
        });

        findViewById(R.id.buttonDelete).setOnClickListener(v -> {
            if (idSelected != -1) {
                databaseHelper.deleteData(idSelected);
                editTextName.setText("");
                loadData();
                idSelected = -1;
            }
        });

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    private void loadData() {
        Cursor cursor = databaseHelper.getData();
        ArrayList<String> names = new ArrayList<>();
        while (cursor.moveToNext()) {
            names.add(cursor.getString(1));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        listView.setAdapter(adapter);
    }
}