package zuev.nikita.server;

import zuev.nikita.server.command.Command;
import zuev.nikita.server.net.Connection;

import java.nio.channels.SocketChannel;
import java.sql.Statement;
import java.util.HashMap;

public class ServersCommandsInvoker {
    private final HashMap<String, Command> registeredCommands;
    private final Statement statement;
    public ServersCommandsInvoker(Statement statement) {
        this.statement=statement;
        registeredCommands = new HashMap<>();
    }
    public HashMap<String, Command> getRegisteredCommands() {
        return registeredCommands;
    }
    public void register(String commandName, Command command) {
        registeredCommands.put(commandName, command);
    }

    public String invoke(String[] fullCommand) {
        if (registeredCommands.containsKey(fullCommand[0])) {
            if (fullCommand.length == 1)
                return registeredCommands.get(fullCommand[0]).serverExecute(null, statement);
            else
                return registeredCommands.get(fullCommand[0]).serverExecute(fullCommand[1], statement);
        } else {
            return "Unknown command '" + fullCommand[0] + "'";
        }
    }
}
