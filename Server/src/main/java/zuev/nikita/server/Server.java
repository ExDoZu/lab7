package zuev.nikita.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zuev.nikita.server.command.Exit;
import zuev.nikita.server.command.Help;
import zuev.nikita.server.command.Save;
import zuev.nikita.server.in_thread.ClientHandler;
import zuev.nikita.structure.Organization;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Base class of Server. Prepares port and handles connections
 */
public class Server {
    private final Logger log = LoggerFactory.getLogger(ServerMain.class);
    /**
     * Trys to get a port from program arguments.
     * @param args Port should be the first argument (0-index)
     * @return port got from program arguments or 52300 as default
     */
    private int tryToGetPort(String[] args) {
        int port = 52300;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception ignored) {
        }
        return port;
    }


    private ServerSocketChannel tryToGetServerSocketChannel( int port) {
        Scanner scanner = new Scanner(System.in);
        ServerSocketChannel serverSocketChannel = null;
        boolean stop = false;
        while (!stop) {
            try {
                serverSocketChannel = ServerSocketChannel.open();
                serverSocketChannel.configureBlocking(false);
                serverSocketChannel.bind(new InetSocketAddress(port));
                break;
            } catch (IOException e) {
                System.out.println("Port " + port + " is not available.\n" +
                        "Input a new port");
                while (true) {
                    try {
                        port = Integer.parseInt(scanner.nextLine().trim());
                        break;
                    } catch (NumberFormatException numberFormatException) {
                        System.out.println("Input a correct port.");
                    } catch (NoSuchElementException noSuchElementException) {
                        stop = true;
                        break;
                    }
                }
            }
        }
        return serverSocketChannel;
    }
    private void serverInvokerRegisterCommands(ServersCommandsInvoker serversCommandsInvoker, Map<String, Organization>collection){
        serversCommandsInvoker.register("exit", new Exit(collection));
        serversCommandsInvoker.register("save", new Save(collection));
        serversCommandsInvoker.register("help", new Help(serversCommandsInvoker.getRegisteredCommands()));

    }


    public void start(String[] args)  {
        System.out.println("Connecting to the database...");
        DatabaseManager databaseManager = new DatabaseManager();

        Statement serverStatement = databaseManager.getStatement();

        Map<String, Organization> collection;
        try {
            collection = databaseManager.getCollection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ServersCommandsInvoker serversCommandsInvoker =new ServersCommandsInvoker(serverStatement);
        serverInvokerRegisterCommands(serversCommandsInvoker, collection);

        log.info("Application started");
        int port = tryToGetPort(args);
        log.info("Port is set as " + port);

        ServerSocketChannel serverSocketChannel = tryToGetServerSocketChannel(port);
        if (serverSocketChannel == null) return;
        log.info("Server started");

        ThreadPoolExecutor executor =
                (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);

        log.debug("Fixed thread pool was created with "+Runtime.getRuntime().availableProcessors()*2+" threads.");
        boolean flag;
        while(true){
            flag = consoleServerRead(serversCommandsInvoker);
            if (!flag)break;
            SocketChannel socketChannel=null;
            try {
                socketChannel = serverSocketChannel.accept();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(socketChannel!=null){
                log.info("New socketChannel");
                Statement newClientStatment = databaseManager.getStatement();
                executor.submit(new ClientHandler(collection, socketChannel, newClientStatment));
            }

        }
        executor.shutdownNow();
        try {
            serverStatement.close();
            serverSocketChannel.close();
            log.info("Server stopped");
            databaseManager.closeConnecton();
        } catch (IOException e) {
            log.error("Unexpected error " + e);
            throw new RuntimeException(e);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * Handles server console input.
     */
    private boolean consoleServerRead(ServersCommandsInvoker serversCommandsInvoker) {
        Scanner scanner = new Scanner(System.in);
        boolean serverIsOn = true;
        try {
            if (System.in.available() > 0) {
                String input = scanner.nextLine().trim();
                String response = serversCommandsInvoker.invoke(input.split("\\s+", 2));
                if (response.equals("exit")) serverIsOn = false;
                else{
                    System.out.println(response);
                }
            }
        } catch (IOException e) {
            serversCommandsInvoker.invoke(new String[]{"exit"});
            throw new RuntimeException(e);

        } catch (NoSuchElementException e) {
            serverIsOn = false;
            serversCommandsInvoker.invoke(new String[]{"exit"});
        }
        return serverIsOn;
    }
}
