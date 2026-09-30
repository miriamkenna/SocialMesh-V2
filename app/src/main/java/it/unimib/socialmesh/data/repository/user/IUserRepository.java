package it.unimib.socialmesh.data.repository.user;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import it.unimib.socialmesh.model.Result;
import it.unimib.socialmesh.model.User;

public interface IUserRepository {

    MutableLiveData<Result> getUser(String email, String password, boolean isUserRegistered);
    MutableLiveData<Result> getGoogleUser(String idToken);
    MutableLiveData<Result> logout();
    User getLoggedUser();
    void signUp(String email, String password);
    void signIn(String email, String password);
    void signInWithGoogle(String token);
}
