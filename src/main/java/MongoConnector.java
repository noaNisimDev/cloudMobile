import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoConnector implements IDBConector {
    protected MongoDatabase database;
    protected MongoCollection collection;

    public MongoConnector() {
        ConnectionString connectionString = new ConnectionString("mongodb+srv://noa:noa@cluster0.pwmrt.mongodb.net/?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase("Users");
        collection = database.getCollection("platinumUsers");
    }

    @Override
    public void create(String name, int id) {
        Document document = new Document();
        document.put("id", id);
        document.put("name", name);
        collection.insertOne(document);
    }

    @Override
    public Person read(int id) {
        Document document = new Document("id", id);
        String name = collection.find(document).first().toString();
        return new Person(name, id, PeopleManager.Clubs.platinum);
    }

    @Override
    public List<Person> readAll() {
        List<Person> personList = new ArrayList<>();
        FindIterable<Document> iterable = collection.find();
        MongoCursor<Document> cursor = iterable.iterator();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            personList.add(new Person(document.getString("name"), document.getInteger("id"), PeopleManager.Clubs.platinum));
        }

        return personList;
    }

    @Override
    public void delete(int id) {
        Document document = new Document("id", id);
        collection.findOneAndDelete(document);
    }
}
