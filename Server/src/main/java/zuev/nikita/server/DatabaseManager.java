package zuev.nikita.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zuev.nikita.structure.Address;
import zuev.nikita.structure.Coordinates;
import zuev.nikita.structure.Organization;
import zuev.nikita.structure.OrganizationType;

import java.io.Console;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.NoSuchElementException;

public class DatabaseManager {
    private  java.sql.Connection connection;
    private  final Logger log = LoggerFactory.getLogger(ServerMain.class);

   public DatabaseManager(){
       String userName = "s334420";
       String connectionURL = "jdbc:postgresql://pg:5432/studs";
       System.out.println("Enter Nikita Zuev's (s334420) password to the database.");
       Console console = System.console();
       while (true) {
           try {
               System.out.print("Password: ");
               String password = new String(console.readPassword());
               try {
                   Class.forName("org.postgresql.Driver");
               }catch (ClassNotFoundException ignored){continue;}
               connection = DriverManager.getConnection(connectionURL, userName, password);
               log.info("Connected to the database.");
               break;
           }catch (SQLException e){
               System.out.println("Connecting to the database was failed.");
           }catch (NullPointerException e) {
               throw new NoSuchElementException();
           }
       }
   }

    public  Statement getStatement()  {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    Map<String, Organization> getCollection() throws SQLException {

        Map<String, Organization> collection =  java.util.Collections.synchronizedMap(new Hashtable<>());

        Statement statement = getStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM collection;");
        while(resultSet.next()){
            String key = resultSet.getString("key");
            Integer id =resultSet.getInt("id");
            String name = resultSet.getString("name");

            long x = resultSet.getLong("x");
            Double y = resultSet.getDouble("y");
            Coordinates coordinates = new Coordinates(x, y);

            Date creationDate = new Date(resultSet.getLong("creationDate"));
            Double annualTurnover = resultSet.getDouble("annualTurnover");
            OrganizationType organizationType = OrganizationType.valueOf(resultSet.getString("organizationType"));
            Address postalAddress = new Address(resultSet.getString("postalAddress"));
            String author = resultSet.getString("author");
            Organization organization = new Organization(id, name, coordinates, creationDate, annualTurnover, organizationType, postalAddress);
            organization.setAuthor(author);
            collection.put(key, organization);
        }
        statement.close();
        return collection;
    }
    public  void closeConnecton() throws SQLException {
        connection.close();
    }
}
