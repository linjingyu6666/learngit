package com.example.week1zy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    /* private HomeAdapter mAdapter;*/
    private TmpAdapter mTmpAdapter;

    private String[] names = { "小猫", "哈士奇", "小黄鸭","小鹿","老虎"};
    private int[]  icons= { R.drawable.cat,R.drawable.siberiankusky,
            R.drawable.yellowduck,R.drawable.fawn, R.drawable.tiger};
    private String[] introduces = {
            "猫，属于猫科动物，分家猫、野猫，是全世界家庭中较为广泛的宠物。",
            "西伯利亚雪橇犬，常见别名哈士奇，昵称为二哈。",
            "鸭的体型相对较小，颈短，一些属的嘴要大些。腿位于身体后方，因而步态蹒跚。",
            "鹿科是哺乳纲偶蹄目下的一科动物。体型大小不等，为有角的反刍类。",
            "虎，大型猫科动物；毛色浅黄或棕黄色，满有黑色横纹；头圆、耳短，耳背面黑色，中央有一白斑甚显著；四肢健壮有力；尾粗长，具黑色环纹，尾端黑色。"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_1);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTmpAdapter = new TmpAdapter();
        mRecyclerView.setAdapter(mTmpAdapter);
               /* mAdapter = new HomeAdapter();
        mRecyclerView.setAdapter(mAdapter);*/
    }

    class TmpAdapter extends RecyclerView.Adapter<TmpAdapter.MyViewHolder> {
        @Override
        public int getItemCount() {
            return names.length;
        }

        @Override
        public void onBindViewHolder(@NonNull TmpAdapter.MyViewHolder holder, int position) {
            holder.name.setText(names[position]);
            holder.introduce.setText(introduces[position]);
            holder.iv.setImageResource(icons[position]);
        }
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            /*return new MyViewHolder(LayoutInflater.from((MainActivity.this).inflate(R.layout.recycler_item,parent,));*/
            TmpAdapter.MyViewHolder holder = new TmpAdapter.MyViewHolder(LayoutInflater.from(MainActivity.
                    this).inflate(R.layout.recycler_item, parent, false));
            return holder;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            ImageView iv;
            TextView introduce;

            public MyViewHolder(View view) {
                super(view);
                iv = view.findViewById(R.id.iv);
                name = view.findViewById(R.id.name);
                introduce = view.findViewById(R.id.introduce);
            }
        }
    }


}
