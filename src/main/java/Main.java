import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static MongoConnector mongoConnector = new MongoConnector();
    static SqlConnector sqlConnector = new SqlConnector();
    static FirebaseConnector firebaseConnector;
    static {
        try {
            firebaseConnector = new FirebaseConnector();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    static IDBConector[] dbs = {firebaseConnector, sqlConnector, mongoConnector};
    static PeopleManager peopleManager = new PeopleManager(dbs);


    public static void main(String[] args) {
        //init
        List<Person> personList = new ArrayList<>();
        //remove all persons to init
        //peopleManager.removeAll();
        personList = peopleManager.getAllPersons();
        if (personList.isEmpty()) {
            initPersons();
            personList = peopleManager.getAllPersons();
        }
        //change clubs
        peopleManager.moveBetweenClubs(personList.get(0), PeopleManager.Clubs.gold);
        peopleManager.moveBetweenClubs(personList.get(1), PeopleManager.Clubs.silver);
        peopleManager.moveBetweenClubs(personList.get(5), PeopleManager.Clubs.platinum);

        personList = peopleManager.getAllPersons();

        for (Person p : personList) {
            System.out.println(p.toString());
        }
    }

    public static void initPersons() {
        List<Person> persons = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            peopleManager.addNewPlatinumMember(new Person(String.valueOf(i), i, PeopleManager.Clubs.platinum));
            peopleManager.addNewSilverMember(new Person(String.valueOf(i * 10), i * 10, PeopleManager.Clubs.silver));
            peopleManager.addNewGoldMember(new Person(String.valueOf(i * 100), i * 100, PeopleManager.Clubs.gold));
        }
    }
}
