package com.mrswapdrobe.swapdrobe;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserChatAdapter extends RecyclerView.Adapter<chatMensaje>{
    private List<misChatObject> chatList;
    private Context context;

/*
    public UserChatAdapter(List<ChatObject> matchesList, Context context){
        this.chatList = matchesList;
        this.context = context;
    }
*/
    public UserChatAdapter(List<misChatObject> dataSetChat, ChatUserActivity context) {
        this.chatList = dataSetChat;
        this.context = context;
    }



    @Override
    public chatMensaje onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        chatMensaje rcv = new chatMensaje(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(chatMensaje holder, int position) {
        holder.mMessage.setText(chatList.get(position).getMessage());
        holder.mNombre.setText(chatList.get(position).getNombre());
        holder.mHoraMensaje.setText((DateFormat.format("dd-MM-yyyy (HH:mm:ss)",chatList.get(position).getHora())));

        if(chatList.get(position).getCurrentUser()){
            holder.mMessage.setGravity(Gravity.END);
            holder.mNombre.setGravity(Gravity.END);
            holder.mHoraMensaje.setGravity(Gravity.END);
            holder.mMessage.setTextColor(Color.parseColor("#404040"));
            holder.mHoraMensaje.setTextColor(Color.parseColor("#404040"));
            holder.mContainer.setBackgroundColor(Color.parseColor("#F4F4F4"));
        }else{
            holder.mMessage.setGravity(Gravity.START);
            holder.mNombre.setGravity(Gravity.START);
            holder.mHoraMensaje.setGravity(Gravity.START);
            holder.mMessage.setTextColor(Color.parseColor("#404040"));
            holder.mHoraMensaje.setTextColor(Color.parseColor("#404040"));
            holder.mContainer.setBackgroundColor(Color.parseColor("#6edfff"));
        }

    }

    @Override
    public int getItemCount() {
        return this.chatList.size();
    }
}
