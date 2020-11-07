package com.example.myblue;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FirstActivity  extends AppCompatActivity {
    private final static String TAG="FirstActivity";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (requestCode==RESULT_OK){
                String msg=data.getStringExtra("msg");
                Log.d(TAG,"msg=" +msg);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         Log.d("msg","");
        setContentView(R.layout.first_layout);

        Button button1=findViewById(R.id.Button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FirstActivity.this,SecondActivity.class);
                startActivityForResult(intent,1);

            }
            
        });
    }
       /* Bundle bundle=new Bundle();
        String myname="FHYJY";
        String mynumber="20191615";
        bundle.putString("name",myname);
        bundle.putString("number",mynumber);
        Intent intent=new Intent(packgeContext:FirstActivity.this,SecondActivity.class);
        intent.putExtra(name:"Message",bundle);
        startActivity(intent);
    }*/

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
