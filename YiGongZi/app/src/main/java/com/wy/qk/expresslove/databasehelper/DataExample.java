package com.wy.qk.expresslove.databasehelper;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.wy.qk.expresslove.R;

public class DataExample extends Activity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_data_example);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this, "test.db", null, 1);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();

        findViewById(R.id.new_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sqLiteDatabase.insert();
            }
        });

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


}
