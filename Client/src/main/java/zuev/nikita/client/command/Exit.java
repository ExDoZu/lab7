package zuev.nikita.client.command;


import zuev.nikita.AuthorizationData;
import zuev.nikita.client.net.SocketIO;

import java.io.File;
import java.util.Set;

/**
 * Gives a command to exit the program.
 */
public class Exit extends Command {
    public Exit(AuthorizationData authorizationData, SocketIO socketIO) {
        super(authorizationData, socketIO);
    }

    @Override
    public String execute(String arg, Set<File> scripts) {
        if (arg != null) return "Команда не нуждается в аргументе.";
        return "exit";
    }

    @Override
    public String getHelp() {
        return "exit : завершить программу";
    }
}
