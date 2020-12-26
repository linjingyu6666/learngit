package com.example.newmusicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;



public class LocalMusicAdapter extends RecyclerView.Adapter<LocalMusicAdapter.LocalMusicViewHolder>{
    Context context;
    List<LocalMusicBean>mDatas;

    OnItemClickListener onItemClickListener;
    //新添？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？
    private OnItemLongClickListener mOnItemLongClickListener;
    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }


    public interface OnItemClickListener{
        public void OnItemClick(View view, int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view, int position);
    }

    public LocalMusicAdapter(Context context, List<LocalMusicBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public LocalMusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_local_music_,parent,false);
        LocalMusicViewHolder holder = new LocalMusicViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final LocalMusicViewHolder holder, final int position) {
        RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) holder;
        LocalMusicBean musicBean = mDatas.get(position);
        holder.idTv.setText(musicBean.getId());
        holder.songTv.setText(musicBean.getSong());
        holder.singerTv.setText(musicBean.getSinger());
        holder.albumTv.setText(musicBean.getAlbum());
        holder.timeTv.setText(musicBean.getDuration());

        if(onItemClickListener!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.OnItemClick(view, position);
                }
            });}
            if(mOnItemLongClickListener !=null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = holder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(holder.itemView,position);
                    //返回true 表示消耗了事件 事件不会继续传递
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    class LocalMusicViewHolder extends RecyclerView.ViewHolder{
        TextView idTv,songTv,singerTv,albumTv,timeTv;
        ImageView img_Tv;

        private TextView textView;

        public TextView getTextView(){
            return textView;
        }


        public LocalMusicViewHolder(View itemView) {
            super(itemView);
            idTv=itemView.findViewById(R.id.item_local_music_num);
            songTv=itemView.findViewById(R.id.item_local_music_song);
            singerTv=itemView.findViewById(R.id.item_local_music_singer);
            albumTv=itemView.findViewById(R.id.item_local_music_album);
            timeTv=itemView.findViewById(R.id.item_local_music_duration);
         img_Tv=itemView.findViewById(R.id.icon_insertlove);

           img_Tv=itemView.findViewById(R.id.icon_insertlove);
        }
    }
}
