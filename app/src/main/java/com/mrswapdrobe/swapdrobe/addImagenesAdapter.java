package com.mrswapdrobe.swapdrobe;
/*
esta vista corresponde al adapter utilizado en addPublicaciones. se utiliza al añadir imagenes en una publicacion. esta contiene y añade las publicaciones de la lista de imagenes (de la clase Imagen).
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class addImagenesAdapter extends RecyclerView.Adapter<addImagenesAdapter.MyViewHolder> {

    Context context;
    List<Imagen> mList;

    public addImagenesAdapter(Context context, List<Imagen> mList) {
        this.context = context;
        this.mList = mList;
    }


    @NonNull
    @Override
    public addImagenesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.file, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull addImagenesAdapter.MyViewHolder holder, int position) {
        holder.textView.setText(mList.get(position).getImagename());
        //Picasso.get().setLoggingEnabled(true);
        Glide.with(this.context).load(mList.get(position).getImage()).into(holder.imageView);
        //Picasso.get().load(mList.get(position).getImage()).into(holder.imageView);
        //Picasso.get().load(mList.get(position).getImage()).placeholder(R.drawable.image).fit().centerCrop().into(holder.imageView);



    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.filename);
            imageView=itemView.findViewById(R.id.icon);
        }
    }
}

