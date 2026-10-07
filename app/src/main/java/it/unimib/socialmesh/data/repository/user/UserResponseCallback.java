package it.unimib.socialmesh.data.repository.user;

import java.util.List;

import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.model.User;

public interface UserResponseCallback {
    void onSuccessFromAuthentication(User user);
    void onFailureFromAuthentication(String message);
    void onSuccessFromRemoteDatabase(User user);
    void onSuccessFromRemoteDatabase(List<Event> eventsList);
    void onFailureFromRemoteDatabase(String message);
    void onSuccessLogout();
}