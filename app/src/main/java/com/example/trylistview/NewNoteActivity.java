package com.example.trylistview;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
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

public class NewNoteActivity extends AppCompatActivity {
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        SQLiteOpenHelper helper = new SQLiteOpenHelper(getBaseContext(), "note_db", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            }
        };
        database = helper.getWritableDatabase();

        SimpleDateFormat format = new SimpleDateFormat("yyyy MMM dd, HH:mm aaa", Locale.getDefault());
        String date = format.format(Calendar.getInstance().getTime());

        Button back = findViewById(R.id.btn_new_note_back);
        back.setOnClickListener(view -> finish());

        int note_id = id();
        EditText note_title = findViewById(R.id.edt_new_note_title);
        TextView note_date = findViewById(R.id.edt_new_note_date);
        TextView note_edited_date = findViewById(R.id.edt_new_note_edited_date);
        EditText note_content = findViewById(R.id.edt_new_note_content);

        note_date.setText(String.format("Created at: %s", date));
        note_edited_date.setText(String.format("Edited at: %s", date));

        Button btn_new_note_save = findViewById(R.id.btn_new_note_save);
        btn_new_note_save.setOnClickListener(view -> {
            ContentValues values = new ContentValues();
            values.put("id",note_id);
            values.put("title",note_title.getText().toString());
            values.put("date",date);
            values.put("edited_date",date);
            values.put("content",note_content.getText().toString());
            database.insert("note",null,values);
            //database.update("note", values, "id='" + note_id + "'", null);
            Toast.makeText(getBaseContext(),"Saved",Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @SuppressLint("Range")
    private int id() {
        int id;
        Cursor cursor = database.rawQuery("select * from note", null);
        if (cursor.getCount() == 0) {
            id = 1;
        } else {
            cursor.moveToLast();
            id = 1 + cursor.getInt(cursor.getColumnIndex("id"));
        }
        cursor.close();
        return id;
    }
}
