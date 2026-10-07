package it.unimib.socialmesh.data.source.user;

import it.unimib.socialmesh.data.repository.user.UserResponseCallback;
import it.unimib.socialmesh.model.User;

public abstract class BaseUserDataRemoteDataSource {
    protected UserResponseCallback userResponseCallback;

    public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }

    public abstract void saveUserData(User user);
}