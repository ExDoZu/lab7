package zuev.nikita.server.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zuev.nikita.AuthorizationData;
import zuev.nikita.message.ClientRequest;
import zuev.nikita.message.ServerResponse;
import zuev.nikita.server.Invoker;
import zuev.nikita.server.command.*;
import zuev.nikita.server.in_thread.InvokersRunner;
import zuev.nikita.structure.Organization;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;

/**
 * Client connection class
 */
public class Connection {
    private final static Logger log = LoggerFactory.getLogger(Connection.class);
    private final SocketChannelIO socketChannelIO;

    private final Map<String, Organization> collection;
    private final Invoker invoker;



    public Connection(SocketChannel socketChannel, Map<String, Organization> collection) {
        this.collection = collection;
        try {
            socketChannelIO = new SocketChannelIO(socketChannel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        invoker = new Invoker();
        registerInvokerCommands();
    }

    public SocketChannelIO getSocketChannelIO() {
        return socketChannelIO;
    }

    private void registerInvokerCommands() {
        invoker.register("info", new Info(collection));
        invoker.register("clear", new Clear(collection));
        invoker.register("filter_greater_than_postal_address", new FilterGreaterThanPostalAddress(collection));
        invoker.register("help", new Help(invoker.getRegisteredCommands()));
        invoker.register("insert", new Insert(collection));
        invoker.register("print_field_ascending_postal_address", new PrintFieldAscendingPostalAddress(collection));
        invoker.register("print_field_descending_postal_address", new PrintFieldDescendingPostalAddress(collection));
        invoker.register("remove_greater_key", new RemoveGreaterKey(collection));
        invoker.register("remove_key", new RemoveKey(collection));
        invoker.register("remove_lower", new RemoveLower(collection));
        invoker.register("show", new Show(collection));
        invoker.register("update", new Update(collection));
    }

    /**
     * Handles commands by a client using the invoker
     *
     * @return true - continue handling this connection
     */
    public boolean clientHandle() throws IOException, ClassNotFoundException {
        if (collection == null) return false;
        String invokerResponse;

        ClientRequest clientRequest = socketChannelIO.read();

        if (clientRequest == null) return true;
        AuthorizationData authorizationData = clientRequest.getAuthorizationData();
        log.info("Got new request: " + Arrays.toString(clientRequest.getFullCommand()));
        String[] fullCommand = clientRequest.getFullCommand();

        Organization organization = clientRequest.getOrganization();

        if (fullCommand[0].equals("exit")) {
            return false;
        }
        try {

            InvokersRunner invokerRunner = new InvokersRunner(fullCommand, organization, invoker, authorizationData);
            Thread threadInvoker = new Thread(invokerRunner);
            threadInvoker.start();

            threadInvoker.join();
            invokerResponse = invokerRunner.getResponse();

            //invokerResponse = invoker.invoke(fullCommand, organization);
            if (invokerResponse.equals(String.valueOf(ServerResponse.WRONG_COMMAND))) {
                socketChannelIO.write(null, ServerResponse.WRONG_COMMAND);
                log.info("Command was wrong");
            } else {
                socketChannelIO.write(invokerResponse, ServerResponse.OK);
                log.info("Command '" + fullCommand[0] + "' was successfully executed\nInvoker response: "+invokerResponse);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public Map<String, Organization> getCollection() {
        return collection;
    }
}
