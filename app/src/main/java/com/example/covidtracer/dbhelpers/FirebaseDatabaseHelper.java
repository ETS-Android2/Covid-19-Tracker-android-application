package com.example.covidtracer.dbhelpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.covidtracer.R;
import com.example.covidtracer.models.Meet;
import com.example.covidtracer.models.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class FirebaseDatabaseHelper {
    private static final String TAG = "FirebaseDatabaseHelper";
    private static FirebaseDatabaseHelper firebaseDatabaseHelper = new FirebaseDatabaseHelper();
    private CollectionReference usersCollection, meetingsCollection;

    private FirebaseDatabaseHelper() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        usersCollection = db.collection("users");
        meetingsCollection = db.collection("users_meetings");
    }

    public static FirebaseDatabaseHelper getInstance() {
        return firebaseDatabaseHelper;
    }

    public interface DataStatus {
        void Success();

        void Fail();
    }

    public void addUser(User user, final Context context, final DataStatus status) {
        final DocumentReference newUserRef = usersCollection.document();
        newUserRef.set(user).addOnSuccessListener(aVoid -> {
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(context.getString(R.string.UID), newUserRef.getId());
            Log.d(TAG, "Added: " + newUserRef.getId());
            editor.apply();
            status.Success();
        }).addOnFailureListener(e -> status.Fail()).addOnCanceledListener(status::Fail);
    }

    public void addMeeting(String myUserUID, String metUserUID, Meet meet, final DataStatus status) {
        meetingsCollection.document(myUserUID).collection("meetings").document(metUserUID).set(meet)
                .addOnSuccessListener(aVoid -> status.Success())
                .addOnFailureListener(e -> status.Fail());
    }

    public void updateMeetingEnding(String myUserID, String metUserUID, FieldValue endingTimestamp, final DataStatus status) {
        DocumentReference meetToUpdate = meetingsCollection.document(myUserID).collection("meetings").document(metUserUID);
        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put("lostTimestamp", endingTimestamp);
        updatedFields.put("status", "ended");
        meetToUpdate.update(updatedFields)
                .addOnSuccessListener(aVoid -> status.Success())
                .addOnFailureListener(e -> status.Fail());
    }

    public void updateUserStatus(String myUserID, String newStatus, final DataStatus status) {
        DocumentReference userToUpdate = usersCollection.document(myUserID);
        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put("status", newStatus);
        userToUpdate.update(updatedFields)
                .addOnSuccessListener(aVoid -> status.Success())
                .addOnFailureListener(e -> status.Fail());
    }

    public void updateDeviceToken(String myUserID, String deviceToken, final DataStatus status) {
        DocumentReference userToUpdate = usersCollection.document(myUserID);
        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put("token", deviceToken);
        userToUpdate.update(updatedFields)
                .addOnSuccessListener(aVoid -> status.Success())
                .addOnFailureListener(e -> status.Fail());

    }

    public void getEncounteredUserInfo(String myUserID, String metUserUID) {

        DocumentReference meetToUpdate = meetingsCollection.document(myUserID).collection("meetings").document(metUserUID);
        Query query = usersCollection.whereEqualTo("users", metUserUID);

        meetToUpdate
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                });
    }
}
