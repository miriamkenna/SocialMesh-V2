package it.unimib.socialmesh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.model.Message;
import it.unimib.socialmesh.util.FireBaseUtil;

public class MessageAdapter extends RecyclerView.Adapter {

    private static final int ITEM_RECEIVE = 1;
    private static final int ITEM_SENT = 2;
    private Context context;
    private ArrayList<Message> messageList;

    public MessageAdapter(Context context, ArrayList<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 1){
            View view = LayoutInflater.from(context).inflate(R.layout.receive, parent, false);
            return new ReceiveViewHolder(view);

        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.sent, parent, false);
            return new SentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message currentMessage = messageList.get(position);
        if (holder instanceof SentViewHolder) {

            SentViewHolder viewHolder = (SentViewHolder) holder;
            ((SentViewHolder) holder).sentMessage.setText(currentMessage.getMessage());

        } else if (holder instanceof ReceiveViewHolder) {

            ReceiveViewHolder viewHolder = (ReceiveViewHolder) holder;
            ((ReceiveViewHolder) holder).receiveMessage.setText(currentMessage.getMessage());

        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message currentMessage = messageList.get(position);
        String currentUserEmail = FireBaseUtil.currentUser().getEmail();
        if (currentUserEmail.equals(currentMessage.getSenderEmail())) {
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVE;
        }
    }

    private static class SentViewHolder extends RecyclerView.ViewHolder {
        SentViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        TextView sentMessage = itemView.findViewById(R.id.txt_sent_message);
    }

    private static class ReceiveViewHolder extends RecyclerView.ViewHolder {
        ReceiveViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        TextView receiveMessage = itemView.findViewById(R.id.txt_receive_message);
    }
}