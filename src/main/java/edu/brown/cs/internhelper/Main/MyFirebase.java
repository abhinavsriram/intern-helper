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

  public void connectToApp() throws ExecutionException, InterruptedException {
    Firestore db = FirestoreClient.getFirestore();
    ApiFuture<QuerySnapshot> future = db.collection("user-data").get();
// future.get() blocks on response
    List<QueryDocumentSnapshot> documents = future.get().getDocuments();
    for (QueryDocumentSnapshot document : documents) {
      Map<String, Object> data = document.getData();
      System.out.println(document.getId() + " => " + data.keySet());
      DocumentReference docRef = db.document(document.getId());

      try {
        System.out.println(docRef.collection("experiences"));
      } catch (Exception e) {
        e.printStackTrace();
      }


    }





    /**
    Firestore db = FirestoreClient.getFirestore();
    CollectionReference userData = db.collection("user-data");

    DocumentReference docRef = userData.document("EjI1c27266U9z3qQbVjcTIc5tdh1");
// asynchronously retrieve the document
    ApiFuture<DocumentSnapshot> future = docRef.get();
// ...
// future.get() blocks on response
    DocumentSnapshot document = future.get();
    if (document.exists()) {
      System.out.println("Document data: " + document.getData());
    } else {
      System.out.println("No such document!");
    }
     **/

  }




}
