package zuev.nikita.server.command;

import zuev.nikita.AuthorizationData;
import zuev.nikita.server.net.Connection;
import zuev.nikita.structure.Organization;

import java.nio.channels.SocketChannel;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Exit extends Command{

    public Exit(Map<String, Organization>collection){super(collection);}

    @Override
    public String execute(String arg, Organization organization, AuthorizationData authorizationData) {
       return null;
    }

    @Override
    public String serverExecute(String arg, Statement statement) {
        System.out.println(new Save(collection).serverExecute(arg, statement));
        return "exit";
    }

    @Override
    public String getHelp() {
        return "exit: exit from the program saving collections.";
    }
}
