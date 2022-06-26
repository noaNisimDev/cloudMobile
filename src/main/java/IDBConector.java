import java.util.List;

public interface IDBConector<T> {
    void create(String name, int id);

    public Person read(int id);

    public List<Person> readAll();

    public void delete(int id);

}
