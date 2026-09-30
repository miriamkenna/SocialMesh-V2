package it.unimib.socialmesh.ui.main.match;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.unimib.socialmesh.adapter.PartecipantsAdapter;
import it.unimib.socialmesh.databinding.FragmentEventPartecipantsBinding;
import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.model.User;
import it.unimib.socialmesh.util.FireBaseUtil;

public class EventPartecipantsFragment extends Fragment {
    private static final String TAG = EventPartecipantsFragment.class.getSimpleName();

    private FragmentEventPartecipantsBinding fragmentEventPartecipantsBinding;
    private PartecipantsAdapter usersAdapter;
    private List<String> participantsList;
    private List<String> idList;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentEventPartecipantsBinding = FragmentEventPartecipantsBinding.inflate(inflater, container, false);
        return fragmentEventPartecipantsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        participantsList = new ArrayList<>();
        idList = new ArrayList<>();
        usersAdapter = new PartecipantsAdapter(idList, participantsList, userId -> {
            EventPartecipantsFragmentDirections.ActionEventPartecipantsFragmentToUserDetailsFragment action =
                    EventPartecipantsFragmentDirections.actionEventPartecipantsFragmentToUserDetailsFragment(userId);
            Navigation.findNavController(requireView()).navigate(action);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());

        fragmentEventPartecipantsBinding.RecyclerviewPartecipants.setLayoutManager(layoutManager);
        fragmentEventPartecipantsBinding.RecyclerviewPartecipants.setAdapter(usersAdapter);

        fragmentEventPartecipantsBinding.backButton.setOnClickListener(CloseView -> {
            getParentFragmentManager().popBackStack();
        });


        if (getArguments() != null) {

            Event commonEvent = EventPartecipantsFragmentArgs.fromBundle(getArguments()).getCommonEvent();
            Log.d(TAG, "Evento ricevuto con successo");
            retrieveParticipantsFromFirebase(commonEvent);

        } else {
            Log.d(TAG, "Errore, evento passato da Match a Partecipants vuoto");
        }

    }

    private void retrieveParticipantsFromFirebase(Event commonEvent) {

        String eventId = commonEvent.getRemoteId();
        DatabaseReference eventParticipantsRef = FirebaseDatabase.getInstance().getReference()
                .child("events").child(eventId).child("participants");

        eventParticipantsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                participantsList.clear();

                for (DataSnapshot participantSnapshot : snapshot.getChildren()) {
                    String userId = participantSnapshot.getKey();
                    retrieveUserDetailsFromFirebase(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Errore nel recupero dei partecipanti", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrieveUserDetailsFromFirebase(String userId) {
        DatabaseReference userRef = FireBaseUtil.getUserRef(userId);
        String currentUser = FireBaseUtil.currentUserId();

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null && !userId.equals(currentUser)) {
                        String currentId = snapshot.getKey();
                        participantsList.add(user.getName());
                        idList.add(currentId);
                        Log.d(TAG, currentId);
                        usersAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Errore nel recupero dei dettagli utente", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

