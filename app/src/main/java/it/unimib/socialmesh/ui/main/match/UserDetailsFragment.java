package it.unimib.socialmesh.ui.main.match;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.adapter.InterestsAdapter;
import it.unimib.socialmesh.adapter.PhotosViewPagerAdapter;
import it.unimib.socialmesh.data.repository.user.IUserRepository;
import it.unimib.socialmesh.databinding.UserDetailsFragmentBinding;

import it.unimib.socialmesh.ui.welcome.UserViewModel;
import it.unimib.socialmesh.ui.welcome.UserViewModelFactory;
import it.unimib.socialmesh.util.FireBaseUtil;
import it.unimib.socialmesh.util.ServiceLocator;

public class UserDetailsFragment extends Fragment {
    private AtomicInteger msgId = new AtomicInteger();
    private UserDetailsFragmentBinding userDetailsFragmentBinding;
    private UserViewModel userViewModel;
    private boolean isLiked = false;
    private String otherUserId;
    private InterestsAdapter interestsAdapter;
    private ViewPager2 viewPager;

    public UserDetailsFragment() {}

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userDetailsFragmentBinding = UserDetailsFragmentBinding.inflate(inflater, container, false);
        return userDetailsFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);
        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(this.getActivity().getApplication());
        userViewModel = new ViewModelProvider(
                this, new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        if (getArguments() != null) {
            otherUserId = UserDetailsFragmentArgs.fromBundle(getArguments()).getUserId();
        }
        userViewModel.obtainUserData(otherUserId);
        userViewModel.fetchUserDescription(otherUserId);
        userViewModel.retrieveUserImages(otherUserId);

        String currentUserId = FireBaseUtil.currentUserId();

        DatabaseReference currentUserLikesRef = FireBaseUtil.getUserRef(currentUserId).child("likes");

        userDetailsFragmentBinding.buttonBack.setOnClickListener(CloseView -> {
            getParentFragmentManager().popBackStack();
        });
        userDetailsFragmentBinding.buttonLike.setOnClickListener(v -> {
            if (isLiked) {
                userDetailsFragmentBinding.buttonLike.setImageResource(R.drawable.baseline_favorite_border_black_48dp);
                userViewModel.removeLike(currentUserId, otherUserId);
                userViewModel.getLikeLiveData().observe(getViewLifecycleOwner(), like -> {
                    if (!like) {
                        Snackbar.make(requireView(), "Like rimosso correttamente!", Snackbar.LENGTH_SHORT).show();
                    }
                });
                Snackbar.make(requireActivity().findViewById(android.R.id.content), "Like rimosso correttamente", Snackbar.LENGTH_SHORT).show();
            } else {
                userDetailsFragmentBinding.buttonLike.setImageResource(R.drawable.baseline_favorite_red_48dp);
                userViewModel.addLikeAndCheckForMatch(currentUserId, otherUserId);
                userViewModel.getLikeLiveData().observe(getViewLifecycleOwner(), like -> {
                    if (like) {
                        Snackbar.make(requireView(), "Like aggiunto correttamente!", Snackbar.LENGTH_SHORT).show();
                    }
                });
                userViewModel.getMatchLiveData().observe(getViewLifecycleOwner(), match -> {
                    if (match) {
                        Snackbar.make(requireView(), "MATCH!", Snackbar.LENGTH_SHORT).show();
                    }
                });
                Snackbar.make(requireActivity().findViewById(android.R.id.content), "Like aggiunto correttamente", Snackbar.LENGTH_SHORT).show();
            }
            isLiked = !isLiked;
        });

        currentUserLikesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(otherUserId)) {
                    userDetailsFragmentBinding.buttonLike.setImageResource(R.drawable.baseline_favorite_red_48dp);
                    isLiked = true;
                }else{
                    userDetailsFragmentBinding.buttonLike.setImageResource(R.drawable.baseline_favorite_border_black_48dp);
                    isLiked = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userViewModel.getProfileFullName().observe(getViewLifecycleOwner(), name -> {
            userDetailsFragmentBinding.textviewName.setText(name);
        });

        userViewModel.getProfileBirthDate().observe(getViewLifecycleOwner(), birthDate -> {
            int age = calculateAge(birthDate);
            userDetailsFragmentBinding.textviewAge.setText(String.valueOf(age));
        });

        updateDescription(otherUserId);
        updateInterests(otherUserId, requireActivity().findViewById(android.R.id.content));
        retrieveImagesFromStorage(otherUserId, requireActivity().findViewById(android.R.id.content));
    }



    private void sendMatchNotificationToUsers(String user1ID, String user2ID) {
        DatabaseReference usersRef = FireBaseUtil.getUserRef(user1ID);
        usersRef.child(user1ID).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String tokenUser1 = dataSnapshot.getValue(String.class);
                    sendNotification(tokenUser1, "Match!", "Hai un nuovo match!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        usersRef.child(user2ID).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String tokenUser2 = dataSnapshot.getValue(String.class);
                    sendNotification(tokenUser2, "Match!", "Hai un nuovo match!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void sendNotification(String token, String title, String body) {
        Map<String, String> notificationData = new HashMap<>();
        notificationData.put("title", title);
        notificationData.put("body", body);

        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(token)
                .setMessageId(Integer.toString(msgId.incrementAndGet()))
                .setData(notificationData)
                .build());
    }



    private void updateDescription(String currentUserId) {
        userViewModel.fetchUserDescription(currentUserId);
        userViewModel.getUserDescriptionLiveData().observe(getActivity(), description -> {
            if (description != null) {
                userDetailsFragmentBinding.textviewDescription.setText(description);
            }
        });
    }


    private int calculateAge(String dateOfBirth) {
        int age = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date birthDate = sdf.parse(dateOfBirth);
            Calendar dob = Calendar.getInstance();
            Calendar today = Calendar.getInstance();

            dob.setTime(birthDate);

            int yearDiff = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            int currentMonth = today.get(Calendar.MONTH) + 1;
            int birthMonth = dob.get(Calendar.MONTH) + 1;
            int currentDay = today.get(Calendar.DAY_OF_MONTH);
            int birthDay = dob.get(Calendar.DAY_OF_MONTH);

            if (currentMonth < birthMonth || (currentMonth == birthMonth && currentDay < birthDay)) {
                age = yearDiff - 1;
            } else {
                age = yearDiff;
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return age;
    }
    private void updateInterests(String currentUserId,View view) {
        interestsAdapter = new InterestsAdapter(new ArrayList<>());
        userDetailsFragmentBinding.recyclerInterests.setLayoutManager(new LinearLayoutManager(getContext()));
        userDetailsFragmentBinding.recyclerInterests.setAdapter(interestsAdapter);
        userViewModel.updateInterests(currentUserId);
        userViewModel.getUserInterestsLiveData().observe(getViewLifecycleOwner(), interestsList -> {
            interestsAdapter.setInterestsList(interestsList);
            interestsAdapter.notifyDataSetChanged();
        });
    }

    private void retrieveImagesFromStorage(String currentUserId, View view) {
        userViewModel.retrieveUserImages(currentUserId);
        viewPager = view.findViewById(R.id.imageview_profile);
        userViewModel.getPhotosUrlLiveData().observe(getActivity(), photos -> {
            PhotosViewPagerAdapter adapter = new PhotosViewPagerAdapter(getContext(), photos);
            viewPager.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });
    }
}


