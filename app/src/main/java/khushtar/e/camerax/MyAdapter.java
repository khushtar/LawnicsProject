package khushtar.e.camerax;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    ArrayList<PhotoHandler> data;
    Context mCtx;

    public MyAdapter(ArrayList<PhotoHandler> data, Context mCtx) {
        this.data = data;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_photo,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            PhotoHandler photoHandler=data.get(position);
//            Log.d("url",photoHandler.getUrl());
//            Log.d("name",photoHandler.getImageName());
            Glide.with(mCtx).load(photoHandler.getUrl()).into(holder.img);
            holder.tv_name.setText(photoHandler.getImageName());
            holder.tv_date.setText(photoHandler.getDate());
            holder.tv_time.setText(photoHandler.getTime());
            holder.tv_type.setText(photoHandler.getType());
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView tv_name,tv_time,tv_date,tv_type;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.imageView);
            tv_name=itemView.findViewById(R.id.txt_imageName_view);
            tv_date=itemView.findViewById(R.id.txt_imageDate_view);
            tv_time=itemView.findViewById(R.id.txt_time_view);
            tv_type=itemView.findViewById(R.id.txt_type_view);
        }
    }
}
