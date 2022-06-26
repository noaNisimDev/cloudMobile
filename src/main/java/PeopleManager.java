import java.util.ArrayList;
import java.util.List;

public class PeopleManager {
    private IDBConector[] dbs;

    public enum Clubs {silver, gold, platinum};

    public PeopleManager(IDBConector[] dbs) {
        this.dbs = dbs;
    }

    public void addNewSilverMember(Person person) {
        dbs[Clubs.silver.ordinal()].create(person.getName(), person.getId());
    }

    public void addNewGoldMember(Person person) {
        dbs[Clubs.gold.ordinal()].create(person.getName(), person.getId());
    }

    public void addNewPlatinumMember(Person person) {
        dbs[Clubs.platinum.ordinal()].create(person.getName(), person.getId());
    }

    public void moveBetweenClubs(Person personToMove, Clubs to) {
        dbs[personToMove.getClub().ordinal()].delete(personToMove.getId());
        dbs[to.ordinal()].create(personToMove.getName(), personToMove.getId());
    }

    public List<Person> getAllPersons() {
        List<Person> personList = new ArrayList<>();
        personList.addAll(dbs[Clubs.platinum.ordinal()].readAll());
        personList.addAll(dbs[Clubs.gold.ordinal()].readAll());
        personList.addAll(dbs[Clubs.silver.ordinal()].readAll());
        return personList;
    }

    public void removeAll() {
        dbs[Clubs.platinum.ordinal()].delete(1);
        dbs[Clubs.platinum.ordinal()].delete(2);
        dbs[Clubs.platinum.ordinal()].delete(3);
        dbs[Clubs.platinum.ordinal()].delete(4);
        dbs[Clubs.gold.ordinal()].delete(100);
        dbs[Clubs.gold.ordinal()].delete(200);
        dbs[Clubs.gold.ordinal()].delete(300);
        dbs[Clubs.gold.ordinal()].delete(400);
        dbs[Clubs.silver.ordinal()].delete(10);
        dbs[Clubs.silver.ordinal()].delete(20);
        dbs[Clubs.silver.ordinal()].delete(30);
        dbs[Clubs.silver.ordinal()].delete(40);
    }


}
