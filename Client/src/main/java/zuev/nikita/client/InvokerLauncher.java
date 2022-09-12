package zuev.nikita.client;

import zuev.nikita.AuthorizationData;
import zuev.nikita.client.command.*;
import zuev.nikita.client.net.SocketIO;

import java.io.IOException;
import java.util.Scanner;


/**
 * Launch the invoker and starts command processing.
 *
 * @author Nikita Zuev
 */
public class InvokerLauncher {
    /**
     * Launches invoker
     */
    public void launch(AuthorizationData authorizationData, SocketIO socketIO) throws IOException, ClassNotFoundException {
        Scanner inputScanner = new Scanner(System.in);
        Invoker invoker = new Invoker(authorizationData, socketIO);
        invoker.register("execute_script", new ExecuteScript(authorizationData, socketIO, invoker.getRegisteredCommands()));
        invoker.register("help", new Help(authorizationData, socketIO, invoker.getRegisteredCommands()));
        invoker.register("insert", new Insert(authorizationData, socketIO));
        invoker.register("remove_lower", new RemoveLower(authorizationData, socketIO));
        invoker.register("update", new Update(authorizationData, socketIO));
        String[] fullCommand;
        //'while' statement completes after inputting  the 'exit' command
        boolean exitFlag = true;
        String invokerResponse;
        while (exitFlag) {
            fullCommand = inputScanner.nextLine().trim().split("\\s+", 2);
            if (!fullCommand[0].equals(""))

                try {
                    invokerResponse = invoker.invoke(fullCommand);
                    if (invokerResponse.equals("exit")) {
                        exitFlag = false;

                    }
                    System.out.println(invokerResponse);

                } catch (NumberFormatException e) {
                    System.out.println("ID должен быть целым числом.");

                }

        }
    }
}
