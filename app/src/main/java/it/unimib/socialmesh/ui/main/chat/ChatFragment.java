package it.unimib.socialmesh.ui.main.chat;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.adapter.MatchesAdapter;
import it.unimib.socialmesh.databinding.FragmentChatBinding;
import it.unimib.socialmesh.model.User;


public class ChatFragment extends Fragment {

    private FragmentChatBinding fragmentChatBinding;
    private ArrayList<User> matchesList;
    private ArrayList<String> usersId;
    private MatchesAdapter adapter;
    private DatabaseReference mDbRef;

    public ChatFragment() {}
    public static ChatFragment newInstance() { return new ChatFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentChatBinding = FragmentChatBinding.inflate(inflater, container, false);
        return fragmentChatBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);
        TextView noMatchesTextView = fragmentChatBinding.noMatchesTextView;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mDbRef = FirebaseDatabase.getInstance().getReference();
        String currentUserId = mAuth.getCurrentUser().getUid();

        usersId = new ArrayList<>();
        matchesList = new ArrayList<>();

        adapter = new MatchesAdapter(getContext(), matchesList, usersId, user -> {
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra("name", user.getName());
            intent.putExtra("email", user.getEmail());
            startActivity(intent);
        });

        fragmentChatBinding.matchesRecyclerView.findViewById(R.id.matchesRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        fragmentChatBinding.matchesRecyclerView.setLayoutManager(linearLayoutManager);
        fragmentChatBinding.matchesRecyclerView.setAdapter(adapter);


        mDbRef.child("users").child(currentUserId).child("matches").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                matchesList.clear();

                if(snapshot.exists()) {
                    for (DataSnapshot matchSnapshot : snapshot.getChildren()) {
                        String matchUserId = matchSnapshot.getKey();
                        mDbRef.child("users").child(matchUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                if (userSnapshot.exists()) {
                                    User user = userSnapshot.getValue(User.class);
                                    matchesList.add(user);
                                    usersId.add(matchUserId);
                                    adapter.notifyDataSetChanged();
                                }
                                noMatchesTextView.setVisibility(View.GONE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Gestisci l'errore
                            }
                        });

                    }
                    adapter.notifyDataSetChanged();
                } else {
                    noMatchesTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}