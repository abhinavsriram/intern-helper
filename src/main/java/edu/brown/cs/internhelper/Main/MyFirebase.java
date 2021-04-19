package edu.brown.cs.internhelper.Main;

import java.io.FileInputStream;
import java.util.List;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import edu.brown.cs.internhelper.Functionality.Experience;
import edu.brown.cs.internhelper.Functionality.Resume;
import edu.brown.cs.internhelper.Functionality.User;

/**
 * The MyFirebase class provides a way of setting up a connection to our Google Firestore which
 * is storing all user data. It then also provides a way to take the data associated with a
 * particular user in Firebase and then set its attributes so that it can be used throughout
 * the other Java classes.
 *
 */
public class MyFirebase {

  /**
   * Returns nothing.
   * <p>
   * This method initializes Firebase app credentials.
   *
   */
  public void setUp() {
    try {
      FileInputStream serviceAccount =
          new FileInputStream("googleCredentials.json");

      FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
          .setCredentials(GoogleCredentials.fromStream(serviceAccount))
          .build();

      FirebaseApp.initializeApp(firebaseOptions);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns an instance of User with all the attributes filled in based on the Firebase data
   * associated with the userID passed in.
   * <p>
   * Accesses all resume data associated with user in Firebase and then iterates through document
   * and collections to set the individual attributes of an instance of the User class based on the
   * data. Need to do this so that can take Firebase data and make it accessible to Java classes
   * rather than having to query Firebase constantly.
   *
   * @param userID unique string that Firebase associates with a user of the application
   * @return an instance of User with all the attributes filled in based on Firebase data
   */
  public User getFirebaseResumeData(String userID) {
    Firestore db = FirestoreClient.getFirestore(); //access our FireStore database
    DocumentReference userDataRef = db.collection("user-data").document(userID); //indexes into
    //collection that contains all data attached to userID
    ApiFuture<DocumentSnapshot> userDataSnapshot = userDataRef.get();

    User user = new User();
    Resume resume = new Resume();

    try {
      DocumentSnapshot userDataDoc = userDataSnapshot.get(); //indexes into document within
      //collection that contains resume-related data on user except for prior experiences
      if (userDataDoc.exists()) {
        user = userDataDoc.toObject(User.class); //rather than individually setting attributes
        //of user, this will infer what attributes to set
        user.setId(userID);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    ApiFuture<QuerySnapshot> experiencesDataSnapshot = db.collection("user-data").document(userID)
        .collection("experiences").get(); //indexes into sub-collection that
    //contains all prior experiences data related to user
    try {
      List<QueryDocumentSnapshot> experienceDocs = experiencesDataSnapshot.get().getDocuments();
      //each experience is a new document
      for (QueryDocumentSnapshot experienceDoc : experienceDocs) { //loops through list of
        //experiences
        if (!(experienceDoc.getId().equals("Experiences List"))) {
          Experience experience = experienceDoc.toObject(Experience.class); //rather than
          //individually setting attributes of experience, this will infer what attributes to set
          experience.setId(experienceDoc.getId());
          resume.addExperience(experience); //resume consists of list of experiences
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    user.setResume(resume);
    return user;
  }

}
