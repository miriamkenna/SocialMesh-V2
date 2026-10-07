
package it.unimib.socialmesh.ui.main.match;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.unimib.socialmesh.adapter.SimpleEventsAdapter;
import it.unimib.socialmesh.data.repository.user.IUserRepository;
import it.unimib.socialmesh.databinding.FragmentMatchBinding;
import it.unimib.socialmesh.model.Event;

import it.unimib.socialmesh.ui.welcome.UserViewModel;
import it.unimib.socialmesh.ui.welcome.UserViewModelFactory;
import it.unimib.socialmesh.util.FireBaseUtil;
import it.unimib.socialmesh.util.ServiceLocator;

public class MatchFragment extends Fragment {

    private FragmentMatchBinding fragmentMatchBinding;
    private RecyclerView recyclerView;
    private SimpleEventsAdapter myEventsAdapter;
    private UserViewModel userViewModel;
    private List<Event> myEventsList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentMatchBinding = FragmentMatchBinding.inflate(inflater, container, false);
        return fragmentMatchBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);
        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(
                this, new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        fragmentMatchBinding.progressBar.setVisibility(View.VISIBLE);
        myEventsList = new ArrayList<>();
        myEventsAdapter = new SimpleEventsAdapter(myEventsList, new SimpleEventsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                fragmentMatchBinding.progressBar.setVisibility(View.GONE);
                MatchFragmentDirections.ActionMatchFragmentToEventPartecipantsFragment action =
                        MatchFragmentDirections.actionMatchFragmentToEventPartecipantsFragment(event);
                Navigation.findNavController(view).navigate(action);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        fragmentMatchBinding.RecyclerviewMyevents.setLayoutManager(layoutManager);
        fragmentMatchBinding.RecyclerviewMyevents.setAdapter(myEventsAdapter);
        retrieveMyEventsFromFirebase();
                myEventsAdapter.setOnUnsubscribeClickListener(eventId -> unsubscribeFromEvent(eventId));
    }
    private void unsubscribeFromEvent(String eventId) {
        String currentUserId = FireBaseUtil.currentUserId();
       userViewModel.unsubscribeFromEvent(currentUserId, eventId);
    }

    private void retrieveMyEventsFromFirebase() {
        String currentUserId = FireBaseUtil.currentUserId();

        DatabaseReference userEventsRef = FireBaseUtil.getUserRef(currentUserId).child("events");
        userEventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myEventsList.clear();

                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    String eventId = eventSnapshot.getKey();
                    Log.d("MatchFragment", "Event ID: " + eventId);
                    retrieveEventDetailsFromFirebase(eventId);
                }

                fragmentMatchBinding.progressBar.setVisibility(View.GONE);

                if (myEventsList.isEmpty()) {
                    fragmentMatchBinding.noEventsMessage.setVisibility(View.VISIBLE);
                } else {
                    fragmentMatchBinding.noEventsMessage.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void retrieveEventDetailsFromFirebase(String eventId) {
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("events").child(eventId);
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Event event = snapshot.getValue(Event.class);
                    if (event != null) {
                        myEventsList.add(event);
                        myEventsAdapter.notifyDataSetChanged();
                    }
                }
                if (myEventsList.isEmpty()) {
                    fragmentMatchBinding.noEventsMessage.setVisibility(View.VISIBLE);
                } else {
                    fragmentMatchBinding.noEventsMessage.setVisibility(View.GONE);
                }

                if (myEventsList.size() == snapshot.getChildrenCount()) {
                    fragmentMatchBinding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}