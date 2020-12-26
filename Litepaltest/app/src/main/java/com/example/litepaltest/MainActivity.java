package com.example.litepaltest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
      private MyDatabaseHelper dbHelper;
      private SQLiteDatabase db;
      private ContentValues mValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite );
        dbHelper = new MyDatabaseHelper(this,"BookStory.db",null,2);
        Button createDB_btn = findViewById(R.id.creatDB_btn);
        createDB_btn.setOnClickListener(this);
        Button addData_btn = findViewById(R.id.addData_btn);
        addData_btn.setOnClickListener(this);
        Button updateData = findViewById(R.id.updateData_btn);
        updateData.setOnClickListener(this);
        Button deleteData_btn = findViewById(R.id.deleteData_btn);
        deleteData_btn.setOnClickListener(this);
        Button queryData_btn = findViewById(R.id.queryData_btn);
        queryData_btn.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.creatDB_btn:
                dbHelper.getWritableDatabase();
                break;
            case R.id.addData_btn:
                db = dbHelper.getWritableDatabase();
                mValues = new ContentValues();
                mValues .put("name","Seven,I love U");
                mValues .put("author","I'm Seven"); 
                mValues .put("pages","520");
                mValues .put("price","9999");
                db.insert("Book",null,mValues);
                mValues.clear();
                break;
            case R.id.updateData_btn:
                db = dbHelper.getWritableDatabase();
                mValues = new ContentValues();
                mValues.put("price",1);
                db.update("Book",mValues,"name = ?",new String[]{"The I'm Seven"});
                mValues.clear();
                break;
            case R.id.deleteData_btn:
                db = dbHelper.getWritableDatabase();
                db.delete("Book","pages > ?",new String[]{"520"});
                break;
            case R.id.queryData_btn:
                db = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("Book",null,null,
                        null,null,null,null);
                if(cursor.moveToFirst()){
                   do{
                       //遍历Cursor对象，取出数据并打印
                       String name = cursor.getString(cursor.getColumnIndex("nema"));
                       String author =  cursor.getString(cursor.getColumnIndex("author"));
                       int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                       double price = cursor.getDouble(cursor.getColumnIndex("price"));
                       Log.d("SQLiteActivity","《" + name + "》,The author is" + author + "," + pages + "页，价格" + price);
                   }while (cursor.moveToNext());
                }
                cursor.close();
        }

    }
}
