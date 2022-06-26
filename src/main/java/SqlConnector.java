import org.postgresql.ds.PGSimpleDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlConnector implements IDBConector {
    PGSimpleDataSource ds;

    public SqlConnector() {
        ds = new PGSimpleDataSource();
        ds.setUrl("jdbc:postgresql://free-tier13.aws-eu-central-1.cockroachlabs.cloud:26257/)}");
        ds.setSslMode("require");
        ds.setUser("noa");
//        ds.setSslHostnameVerifier("free-tier13.aws-eu-central-1.cockroachlabs.cloud");
        ds.setPassword("pmrweEGll3gYR4Xvl7Hzvg");
        ds.setDatabaseName("defaultdb");
//        ds.setPortNumber(26257);
        ds.setOptions("--cluster=goodly-titan-2121");

        try {
            ds.getConnection().createStatement().executeQuery("CREATE TABLE IF NOT EXISTS defaultdb.goldUsers (" +
                    "PersonID INT," +
                    "name STRING" +
                    ");");
        } catch (SQLException e) {
            //throw new RuntimeException(e);
        }
    }

    @Override
    public void create(String name, int id) {
        try (Connection connection = ds.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO goldUsers (PersonID, name) VALUES (?, ?)");

            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.addBatch();

            pstmt.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            System.out.printf("sql state = [%s]\ncause = [%s]\nmessage = [%s]\n",
                    e.getSQLState(), e.getCause(), e.getMessage());
        }
    }

    @Override
    public Person read(int id) {
        try (Connection connection = ds.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT name FROM goldUsers WHERE PersonID=%d", id).toString());
            String name = "";
            while (rs.next()) {
                name = rs.getString(1);
            }
            rs.close();
            return new Person(name, id, PeopleManager.Clubs.gold);
        } catch (SQLException e) {
            System.out.printf("sql state = [%s]\ncause = [%s]\nmessage = [%s]\n",
                    e.getSQLState(), e.getCause(), e.getMessage());
            return null;
        }
    }

    @Override
    public List<Person> readAll() {
        List<Person> personList = new ArrayList<>();
        try (Connection connection = ds.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name, PersonID FROM goldUsers");
            String name = "";
            int id = -1;
            while (rs.next()) {
                name = rs.getString(1);
                id = rs.getInt(2);
                personList.add(new Person(name, id, PeopleManager.Clubs.gold));
            }
            rs.close();
            return personList;
        } catch (SQLException e) {
            System.out.printf("sql state = [%s]\ncause = [%s]\nmessage = [%s]\n",
                    e.getSQLState(), e.getCause(), e.getMessage());
            return null;
        }
    }

    @Override
    public void delete(int id) {
        try (Connection connection = ds.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("DELETE FROM goldUsers WHERE PersonID=%d", id).toString());
            rs.close();
        } catch (SQLException e) {
            System.out.printf("sql state = [%s]\ncause = [%s]\nmessage = [%s]\n",
                    e.getSQLState(), e.getCause(), e.getMessage());
        }

    }
}
