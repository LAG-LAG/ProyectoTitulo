package com.example.proyectotitulo;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mMessage;
    public TextView mUser;
    public LinearLayout mContainer;
    public ChatViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mUser = itemView.findViewById(R.id.nombreUserMensaje);
        mMessage = itemView.findViewById(R.id.message);
        mContainer = itemView.findViewById(R.id.container);
        mNombre = itemView.findViewById(R.id.nombreUserMensaje);
        mHoraMensaje = itemView.findViewById(R.id.horaMensaje);
    }

    @Override
    public void onClick(View view) {
    }
}