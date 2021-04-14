package edu.brown.cs.internhelper.Main;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import edu.brown.cs.internhelper.Functionality.Experience;
import edu.brown.cs.internhelper.Functionality.Resume;
import edu.brown.cs.internhelper.Functionality.User;

public class MyFirebase {

  public void setUp(){
    try {
//      InputStream is = new FileInputStream("firebaseCredentials.json");
//      FirebaseOptions fbOptions = FirebaseOptions.builder()
//        .setCredentials(GoogleCredentials.fromStream(is))
//        .build();
      //FirebaseApp.initializeApp("./firebaseCredentials.json");
      FileInputStream serviceAccount =
          new FileInputStream("googleCredentials.json");

      FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
          .setCredentials(GoogleCredentials.fromStream(serviceAccount))
          .build();

      FirebaseApp.initializeApp(firebaseOptions);

//      FireStore db = firebase.firestore();
//      CollectionReference cities = db.collection("user-data");
//      System.out.println(cities);


    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public User getFirebaseResumeData(String userID){
    Firestore db = FirestoreClient.getFirestore();
    DocumentReference userDataRef = db.collection("user-data").document(userID);
// asynchronously retrieve the document
    ApiFuture<DocumentSnapshot> userDataSnapshot = userDataRef.get();
    User user = new User();
    Resume resume = new Resume();

    try {
      DocumentSnapshot userDataDoc = userDataSnapshot.get();
      if (userDataDoc.exists()) {
        user = userDataDoc.toObject(User.class);
        user.setId(userID);
//        System.out.println("ID " + user.getId());
//        System.out.println("SKILLS " + user.getSkills());
//        System.out.println("MAJOR GPA " + user.getMajor_GPA());
//        System.out.println("MAJOR " + user.getMajor());
//        System.out.println("CUMULATIVE GPA " + user.getCumulative_GPA());
//        System.out.println("UNIVERSITY " + user.getUniversity());
//        System.out.println("DEGREE " + user.getDegree());
//        System.out.println("LAST NAME " + user.getLast_Name());
//        System.out.println("COURSEWORK " + user.getCoursework());
//        System.out.println("FIRST NAME " + user.getFirst_Name());
//        System.out.println("EMAIL " + user.getEmail());
//        System.out.println("PROFILE COMPLETE " + user.getInitial_Profile_Setup_Complete());

      } else {
        System.out.println("No such document!");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    ApiFuture<QuerySnapshot> experiencesDataSnapshot = db.collection("user-data").document(userID)
        .collection("experiences").get();
    try {
      List<QueryDocumentSnapshot> experienceDocs = experiencesDataSnapshot.get().getDocuments();
//      System.out.println("==========================================");
      for (QueryDocumentSnapshot experienceDoc : experienceDocs) {
        if (!(experienceDoc.getId().equals("Experiences List"))) {
          Experience experience = experienceDoc.toObject(Experience.class);
          experience.setId(experienceDoc.getId());
//          System.out.println("ID " + experience.getId());
//          System.out.println("END DATE " + experience.getEnd_Date());
//          System.out.println("DESCRIPTION " + experience.getDescription());
//          System.out.println("COMPANY " + experience.getCompany());
//          System.out.println("TITLE " + experience.getTitle());
//          System.out.println("START DATE " + experience.getStart_Date());
//          System.out.println("==========================================");
          //System.out.println(resume);
          resume.addExperience(experience);

        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    user.setResume(resume);
    return user;
    //System.out.println(resume.numResumeExperiences());

  }




}
