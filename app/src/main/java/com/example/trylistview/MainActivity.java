package com.example.trylistview;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    noteAdapter noteAdapter;
    SQLiteDatabase database;
    ListView listView;
    EditText edt_search;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        Button btn_add = findViewById(R.id.btn_add);
        Button btn_search = findViewById(R.id.btn_search);

        btn_add.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,NewNoteActivity.class);
            startActivity(intent);
        });
        btn_search.setOnClickListener(view -> note_get());

        ArrayList<note> noteArrayList = new ArrayList<>();
        noteAdapter = new noteAdapter(this, 0, noteArrayList);
        listView.setAdapter(noteAdapter);
        listView.setDividerHeight(0);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            note item = (note) adapterView.getItemAtPosition(i);
            int id = item.getId();
            String date = item.getCreated_date();
            String edited_date = item.getEdited_date();
            String title = item.getTitle();
            String content = item.getContent();
            Intent intent = new Intent(MainActivity.this,NoteActivity.class);
            intent.putExtra("id",id);
            intent.putExtra("date",date);
            intent.putExtra("edited_date",edited_date);
            intent.putExtra("title",title);
            intent.putExtra("content",content);
            startActivity(intent);
        });
        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            note item = (note) adapterView.getItemAtPosition(i);

            builder.setView(R.layout.note_delete);
            builder.setTitle("Delete note");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton(android.R.string.ok, (dialogInterface, i12) -> {
                note_delete(item.getId());
                dialogInterface.dismiss();
            });
            builder.setNegativeButton(android.R.string.cancel, (dialogInterface, i13) -> dialogInterface.dismiss());
            builder.show();
            return false;
        });

        SQLiteOpenHelper helper = new SQLiteOpenHelper(this, "note_db", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            }
        };
        database = helper.getWritableDatabase();
        database.execSQL("create table if not exists note(title TEXT, content TEXT, id INT, date TEXT, edited_date TEXT);");
        //note_get();
    }

    @Override
    protected void onResume() {
        super.onResume();
        note_get();
    }

    public static class noteAdapter extends ArrayAdapter<note> {
        public noteAdapter(Context context, int resource, List<note> objects) {
            super(context, resource, objects);
        }
        @SuppressLint("SetTextI18n")
        public View getView(int position, View convertView, ViewGroup parent) {
            note dtNote = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_note, parent, false);
            }

            TextView txt_date = convertView.findViewById(R.id.txt_date);
            TextView txt_title = convertView.findViewById(R.id.txt_title);
            txt_date.setText(dtNote.getCreated_date());
            txt_title.setText(dtNote.getTitle());

            return convertView;
        }
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

    @NonNull
    public String date() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy MMM dd, HH:mm aaa", Locale.getDefault());
        return format.format(date);
    }

    private void add_item (String title, String content) {
        // Insert to database
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        int id = id();
        values.put("id", id);
        String date = date();
        values.put("date", date);
        database.insert("note", null, values);

        note_insert(title, content, id, date, date);
        note_get();
    }

    private void note_insert(String title, String content, int id, String date, String edited_date) {
        // Insert to list
        note newNote = new note(title, content, id, date, edited_date);
        noteAdapter.add(newNote);
    }

    private void note_delete(int id) {
        database.delete("note","id='" + id + "'",null);
        note_get();
    }

    @SuppressLint("Range")
    private void note_get() {
        edt_search = findViewById(R.id.edt_search);
        noteAdapter.clear();
        Cursor cursor = database.rawQuery("select * from note where title like '%" + edt_search.getText().toString() + "%'",null);
        boolean i = cursor.moveToLast();
        while (i) {
            note_insert(cursor.getString(cursor.getColumnIndex("title")), cursor.getString(cursor.getColumnIndex("content")), cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("date")), cursor.getString(cursor.getColumnIndex("edited_date")));
            i = cursor.moveToPrevious();
        }
        cursor.close();
    }

    @NonNull
    public static List<String> usingSplitMethod(@NonNull String text, int n) {
        String[] results = text.split("(?<=\\G.{" + n + "})");
        return Arrays.asList(results);
    }
}