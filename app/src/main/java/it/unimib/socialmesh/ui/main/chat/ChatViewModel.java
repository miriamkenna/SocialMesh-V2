package it.unimib.socialmesh.ui.main.chat;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.unimib.socialmesh.model.Message;

public class ChatViewModel extends ViewModel {
    private MutableLiveData<List<Message>> messageListLiveData = new MutableLiveData<>();

    public MutableLiveData<List<Message>> getMessageListLiveData() {
        return messageListLiveData;
    }

    public void loadMessages(String senderRoom) {
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("messages");

        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Message> messages = new ArrayList<>();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Message message = postSnapshot.getValue(Message.class);
                    if (message != null) {
                        messages.add(message);
                    }
                }
                messageListLiveData.setValue(messages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestire eventuali errori
            }
        });
    }

    public void sendMessage(String senderRoom, String receiverRoom, Message message) {
        DatabaseReference senderMessagesRef = FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("messages");
        DatabaseReference receiverMessagesRef = FirebaseDatabase.getInstance().getReference().child("chats").child(receiverRoom).child("messages");

        // Push per generare una chiave unica per il messaggio
        DatabaseReference senderNewMessageRef = senderMessagesRef.push();
        DatabaseReference receiverNewMessageRef = receiverMessagesRef.child(senderNewMessageRef.getKey());

        senderNewMessageRef.setValue(message);
        receiverNewMessageRef.setValue(message);
    }
}

