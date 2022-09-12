package zuev.nikita.server.command;

import zuev.nikita.AuthorizationData;
import zuev.nikita.server.net.Connection;
import zuev.nikita.structure.Organization;

import java.nio.channels.SocketChannel;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Super class for commands
 */
public abstract class Command {
    /**
     * Collection processed by a command
     */
    protected Map<String, Organization> collection;
    protected final Map<String, Command> commandList;



    public Command() {
        collection =  java.util.Collections.synchronizedMap(new Hashtable<>());
        commandList = null;
    }

    public Command(Map<String, Organization> collection) {
        this.collection = collection;
        commandList = null;
    }

    public Command(HashMap<String, Command> commandList) {
        this.commandList =commandList;
        collection = java.util.Collections.synchronizedMap(new Hashtable<>());
    }

    /**
     * @param arg Command argument
     * @return result/report of command execution
     */
    public abstract String execute(String arg, Organization organization, AuthorizationData authorizationData);

    public String serverExecute(String arg,Statement statement) {
        return null;
    }

    /**
     * @return Information about the command.
     */
    public abstract String getHelp();
}
