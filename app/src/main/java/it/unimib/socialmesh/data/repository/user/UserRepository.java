package it.unimib.socialmesh.data.repository.user;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import it.unimib.socialmesh.data.source.event.BaseEventsLocalDataSource;
import it.unimib.socialmesh.data.source.user.BaseUserAuthenticationRemoteDataSource;
import it.unimib.socialmesh.data.source.user.BaseUserDataRemoteDataSource;
import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.model.EventApiResponse;
import it.unimib.socialmesh.model.Result;
import it.unimib.socialmesh.model.User;
import it.unimib.socialmesh.util.EventCallback;

public class UserRepository implements IUserRepository, UserResponseCallback, EventCallback {

    private static final String TAG = UserRepository.class.getSimpleName();

    private final BaseUserAuthenticationRemoteDataSource userRemoteDataSource;
    private final BaseUserDataRemoteDataSource userDataRemoteDataSource;
    private final BaseEventsLocalDataSource eventsLocalDataSource;
    private final MutableLiveData<Result> userMutableLiveData;


    public UserRepository(BaseUserAuthenticationRemoteDataSource userRemoteDataSource,
                          BaseUserDataRemoteDataSource userDataRemoteDataSource,
                          BaseEventsLocalDataSource eventsLocalDataSource) {
        this.userRemoteDataSource = userRemoteDataSource;
        this.userDataRemoteDataSource = userDataRemoteDataSource;
        this.eventsLocalDataSource = eventsLocalDataSource;
        this.userMutableLiveData = new MutableLiveData<>();
        this.userRemoteDataSource.setUserResponseCallback(this);
        this.userDataRemoteDataSource.setUserResponseCallback(this);
        this.eventsLocalDataSource.setEventCallback(this);
    }



    @Override
    public MutableLiveData<Result> getUser(String email, String password, boolean isUserRegistered) {
        if (isUserRegistered) {
            signIn(email, password);
        } else {
            signUp(email, password);
        }
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getGoogleUser(String idToken) {
        signInWithGoogle(idToken);
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> logout() {
        userRemoteDataSource.logout();
        return userMutableLiveData;
    }

    @Override
    public User getLoggedUser() { return userRemoteDataSource.getLoggedUser(); }

    @Override
    public void signUp(String email, String password) {
        userRemoteDataSource.signUp(email, password);
    }

    @Override
    public void signIn(String email, String password) {
        userRemoteDataSource.signIn(email, password);
    }

    @Override
    public void signInWithGoogle(String token) { userRemoteDataSource.signInWithGoogle(token); }

    @Override
    public void onSuccessFromAuthentication(User user) {
        if (user != null) {
            userDataRemoteDataSource.saveUserData(user);
        }
    }

    @Override
    public void onFailureFromAuthentication(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromRemoteDatabase(User user) {
        Result.UserResponseSuccess result = new Result.UserResponseSuccess(user);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromRemoteDatabase(List<Event> eventsList) {
        eventsLocalDataSource.insertEvents(eventsList);
    }

    @Override
    public void onFailureFromRemoteDatabase(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessLogout() {}

    @Override
    public void onSuccessFromRemote(EventApiResponse eventApiResponse, long lastUpdate) {}

    @Override
    public void onFailureFromRemote(Exception exception) {}

    @Override
    public void onSuccessFromLocal(List<Event> eventsList) {}

    @Override
    public void onFailureFromLocal(Exception exception) {}
}