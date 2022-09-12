package zuev.nikita.server.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zuev.nikita.AuthorizationData;
import zuev.nikita.server.net.Connection;
import zuev.nikita.structure.Organization;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;


/**
 * Saves the collection to the file.
 */
public class Save extends Command {
    private final static Logger log = LoggerFactory.getLogger(Save.class);

    public Save(Map<String, Organization> collection){super(collection);}

    @Override
    public String execute(String arg, Organization organization, AuthorizationData authorizationData) {
        return "Save doesn't work for clients";
    }

    @Override
    public String serverExecute(String arg, Statement statement) {
        try {
            statement.executeUpdate("DELETE FROM collection;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        log.info("Collection size: " + collection.size());
        for (String key : collection.keySet()) {

            Organization organization = collection.get(key);

            Integer id = organization.getId();
            String name = organization.getName();
            long x = organization.getCoordinates().getX();
            Double y = organization.getCoordinates().getY();
            long creationDate = organization.getCreationDate().getTime();
            Double annualTurnover = organization.getAnnualTurnover();
            String type = organization.getType().name();
            String address = organization.getPostalAddress().getZipCode();
            String author = organization.getAuthor();
            log.info("Try to save Organization: "+organization);
            try {
                statement.executeUpdate("INSERT INTO collection " +
                        "VALUES (" + id + ",'" + key + "', '" + name + "', " + x + ", " + y + "," + creationDate +
                        ", " + annualTurnover + ", '" + type + "', '"+ address+"', '"+author+"');");
            log.info("Organization must be saved.");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        return "The collection was successfully saved.";
    }

    @Override
    public String getHelp() {
        return "save : save collection to the file";
    }
}
