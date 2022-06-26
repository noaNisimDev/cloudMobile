public class Person {
    private String name;
    private int id;
    private PeopleManager.Clubs club;

    public Person(String name, int id, PeopleManager.Clubs club) {
        this.name = name;
        this.id = id;
        this.club = club;
    }

    public Person(Person p) {
        this.name = p.getName();
        this.id = p.getId();
        this.club = p.getClub();
    }

    public Person() {

    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public PeopleManager.Clubs getClub() {
        return club;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setClub(PeopleManager.Clubs club) {
        this.club = club;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", club=" + club +
                '}';
    }
}
