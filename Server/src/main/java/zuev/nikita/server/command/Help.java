package zuev.nikita.server.command;


import zuev.nikita.AuthorizationData;
import zuev.nikita.server.net.Connection;
import zuev.nikita.structure.Organization;

import java.nio.channels.SocketChannel;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Return help for available commands.
 */
public class Help extends Command {
    boolean wasUserRequested;
    public Help(HashMap<String, Command> commandList) {
        super(commandList);
    }

    @Override
    public String execute(String arg, Organization organization, AuthorizationData authorizationData) {
        wasUserRequested= authorizationData != null;
        StringBuilder response = new StringBuilder();
        for (String command : commandList.keySet())
            response.append(commandList.get(command).getHelp()).append('\n');
        return response.toString();
    }


    @Override
    public String getHelp() {
        if(wasUserRequested)
            return "help : вывести справку по доступным командам";
        else return "help: get information about commands";
    }

    @Override
    public String serverExecute(String arg, Statement statement) {
        return execute(null,  null, null);
    }
}
