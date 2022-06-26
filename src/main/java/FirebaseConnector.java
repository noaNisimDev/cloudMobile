import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class FirebaseConnector implements IDBConector {
    private Firestore db;

    public FirebaseConnector() throws FileNotFoundException {
        // Use the application default credentials
        ClassLoader classLoader = FirebaseConnector.class.getClassLoader();
        File file = new File((String) Objects.requireNonNull(classLoader.getResource("serviceAccount.json")).getFile());
        FileInputStream serviceAccount = new FileInputStream(file.getAbsolutePath());
        FirebaseOptions options = null;
        try {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://couldfinal-696e1-default-rtdb.europe-west1.firebasedatabase.app")
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FirebaseApp.initializeApp(options);

        db = FirestoreClient.getFirestore();
    }

    @Override
    public void create(String name, int id) {
        // Add a new document (asynchronously) in collection
        ApiFuture<WriteResult> future = db.collection("silverUsers").document(name).set(new Person(name, id, PeopleManager.Clubs.silver));
    }

    @Override
    public Person read(int id) {
        DocumentReference documentReference = db.collection("silverUsers").document(String.valueOf(id));
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot documentSnapshot;
        try {
            documentSnapshot = future.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        Person person;
        if (documentSnapshot.exists()) {
            person = new Person(documentSnapshot.toObject(Person.class));
            return person;
        }

        return null;
    }

    @Override
    public List<Person> readAll() {
        List<Person> personList = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = db.collection("silverUsers").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        for (QueryDocumentSnapshot document : documents) {
            personList.add(new Person(document.toObject(Person.class)));
        }


        return personList;
    }

    @Override
    public void delete(int id) {
        ApiFuture<WriteResult> future = db.collection("silverUsers").document(String.valueOf(id)).delete();
    }
}
