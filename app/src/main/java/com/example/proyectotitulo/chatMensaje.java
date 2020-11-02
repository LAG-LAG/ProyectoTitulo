package com.example.proyectotitulo;
/*
esta clase corresponde a cada mensaje del chat. contiene los atributos de estos que son: nombre, mensaje, hora del mensaje y el contenedor del chat.
 */
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class chatMensaje extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mMessage;
    public TextView mNombre,mHoraMensaje;
    public LinearLayout mContainer;
    public chatMensaje(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mMessage = itemView.findViewById(R.id.message);
        mContainer = itemView.findViewById(R.id.container);
        mNombre = itemView.findViewById(R.id.nombreUserMensaje);
        mHoraMensaje = itemView.findViewById(R.id.horaMensaje);
    }

    @Override
    public void onClick(View view) {
    }
}