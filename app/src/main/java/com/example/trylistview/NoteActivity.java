package com.example.trylistview;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        SQLiteOpenHelper helper = new SQLiteOpenHelper(getBaseContext(), "note_db", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            }
        };
        SQLiteDatabase database = helper.getWritableDatabase();

        Button back = findViewById(R.id.btn_note_back);
        back.setOnClickListener(view -> finish());

        int id = getIntent().getExtras().getInt("id");
        String date = getIntent().getExtras().getString("date");
        String edited_date = getIntent().getExtras().getString("edited_date");
        String title = getIntent().getExtras().getString("title");
        String content = getIntent().getExtras().getString("content");

        EditText note_title = findViewById(R.id.edt_note_title);
        TextView note_date = findViewById(R.id.edt_note_date);
        TextView note_edited_date = findViewById(R.id.edt_note_edited_date);
        EditText note_content = findViewById(R.id.edt_note_content);

        note_title.setText(title);
        note_date.setText(String.format("Created at: %s", date));
        note_edited_date.setText(String.format("Edited at: %s", edited_date));
        note_content.setText(content);

        Button btn_note_save = findViewById(R.id.btn_note_save);
        btn_note_save.setOnClickListener(view -> {
            SimpleDateFormat format = new SimpleDateFormat("yyyy MMM dd, HH:mm aaa", Locale.getDefault());
            String new_edited_date = format.format(Calendar.getInstance().getTime());

            ContentValues values = new ContentValues();
            values.put("title", note_title.getText().toString());
            values.put("content", note_content.getText().toString());
            values.put("edited_date", new_edited_date);
            database.update("note", values, "id='" + id + "'", null);

            note_edited_date.setText(String.format("Edited at: %s", new_edited_date));
            Toast.makeText(getBaseContext(),"Saved",Toast.LENGTH_SHORT).show();
        });
    }
}
