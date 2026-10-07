package it.unimib.socialmesh.util;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
public class FireBaseUtil {
    public static FirebaseUser currentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }
    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }
    public static StorageReference  getCurrentProfilePicStorageRef(String userId){
        return FirebaseStorage.getInstance().getReference().child("pictures").child(userId);
    }
   public static DatabaseReference getUserRef(String userId){
        if(userId != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(userId);
            return userRef;
        }
       return null;
   }

    public static String adjustPath(String path) {
        char[] pathArray;
        pathArray = path.toCharArray();
        for(int i = 0; i < pathArray.length; i++) {
            if(pathArray[i] == '.')
                pathArray[i] = ',';
        }
        return new String(pathArray);
    }

}
