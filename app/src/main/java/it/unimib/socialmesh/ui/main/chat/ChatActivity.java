package it.unimib.socialmesh.ui.main.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.adapter.MessageAdapter;
import it.unimib.socialmesh.data.repository.user.IUserRepository;
import it.unimib.socialmesh.model.Message;
import it.unimib.socialmesh.model.User;
import it.unimib.socialmesh.ui.welcome.UserViewModel;
import it.unimib.socialmesh.ui.welcome.UserViewModelFactory;
import it.unimib.socialmesh.util.FireBaseUtil;
import it.unimib.socialmesh.util.ServiceLocator;

public class ChatActivity extends AppCompatActivity {
    private TextView userNameTextView;
    private EditText messageBox;
    private MessageAdapter messageAdapter;
    private ArrayList<Message> messageList;
    String receiverRoom;
    String senderRoom;
    private UserViewModel userViewModel;
    private ChatViewModel viewModel;
    private ImageView profile_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(getApplication());
        userViewModel = new ViewModelProvider(
                this, new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String receiverEmail = intent.getStringExtra("email");

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        ImageButton closeChatButton = findViewById(R.id.backButton);
        closeChatButton.setOnClickListener(view -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });

        userNameTextView = findViewById(R.id.nomeChat);
        userNameTextView.setText(name);
        profile_pic = findViewById(R.id.profile_picture);

        String senderEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        senderRoom = FireBaseUtil.adjustPath(receiverEmail + senderEmail);
        receiverRoom = FireBaseUtil.adjustPath(senderEmail + receiverEmail);

        Log.d("senderRoom", senderRoom);
        Log.d("receiverRoom", receiverRoom);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(name);
        }

        RecyclerView chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageBox = findViewById(R.id.messageBox);
        ImageView sendButton = findViewById(R.id.sentButton);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(messageAdapter);

        viewModel.getMessageListLiveData().observe(this, messages -> {
            messageList.clear();
            messageList.addAll(messages);
            messageAdapter.notifyDataSetChanged();
        });

        viewModel.loadMessages(senderRoom);

        sendButton.setOnClickListener(v -> {
            String message = messageBox.getText().toString();
            Message messageObject = new Message(message, senderEmail);
            viewModel.sendMessage(senderRoom, receiverRoom, messageObject);
            messageBox.setText("");
        });

        usersRef.orderByChild("email").equalTo(receiverEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        String userId = userSnapshot.getKey();

                        loadProfileImage(userId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
            }
        });
    }



    private void loadProfileImage(String otherUserID) {
        profile_pic = findViewById(R.id.profile_picture);
        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(getApplication());
        userViewModel = new ViewModelProvider(
                this, new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        userViewModel.getProfileImageUrl(otherUserID).observe(this, imageUrl -> {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                CircularProgressDrawable drawable = new CircularProgressDrawable(this);
                drawable.setColorSchemeColors(R.color.md_theme_light_primary, R.color.md_theme_dark_primary, R.color.md_theme_dark_inversePrimary);
                drawable.setCenterRadius(30f);
                drawable.setStrokeWidth(5f);
                drawable.start();
                Glide.with(this)
                        .load(imageUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .placeholder(drawable)
                        .error(drawable)
                        .into(profile_pic);
            } else {
                Glide.with(this)
                        .load(com.facebook.R.drawable.com_facebook_profile_picture_blank_portrait) // Immagine di default
                        .apply(RequestOptions.circleCropTransform())
                        .into(profile_pic);
            }
        });
    }
}
