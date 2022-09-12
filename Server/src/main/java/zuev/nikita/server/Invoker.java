package zuev.nikita.server;

import zuev.nikita.AuthorizationData;
import zuev.nikita.message.ServerResponse;
import zuev.nikita.server.command.Command;
import zuev.nikita.structure.Organization;

import java.util.HashMap;

/**
 * Invokes commands.
 */
public class Invoker {

    /**
     * Contains current executing scripts.
     */
    private final HashMap<String, Command> registeredCommands;

    public Invoker() {
        registeredCommands = new HashMap<>();
    }

    public HashMap<String, Command> getRegisteredCommands() {
        return registeredCommands;
    }

    /**
     * Registers a new command
     */
    public void register(String commandName, Command command) {
        registeredCommands.put(commandName, command);
    }

    /**
     * @param fullCommand command with/without argument
     */
    public String invoke(String[] fullCommand, Organization organization, AuthorizationData authorizationData){
        if (registeredCommands.containsKey(fullCommand[0])) {
            if (fullCommand.length == 1)
                return registeredCommands.get(fullCommand[0]).execute(null, organization, authorizationData);
            else
                return registeredCommands.get(fullCommand[0]).execute(fullCommand[1],organization, authorizationData);
        } else {
            return String.valueOf(ServerResponse.WRONG_COMMAND);
        }
    }
}

