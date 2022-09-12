package zuev.nikita.client;

import zuev.nikita.AuthorizationData;
import zuev.nikita.client.net.Connection;
import zuev.nikita.client.net.SocketIO;
import zuev.nikita.message.ServerResponse;

import java.io.Console;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

/**
 * Base class of program. Prepares port, host, file path and start a session.
 */
public class Client {

    private AuthorizationData authorizationData=null;


    /**
     * Trys to get a port from program arguments.
     * @param args Port should be the second argument
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




    /**
     * Trys to get a connection using the host and the port.
     * @param host host
     * @param port port
     * @return Connection or null
     */
    private Connection tryToConnect(String host, int port) {
        Scanner inputScanner = new Scanner(System.in);
        while (true) {
            try {
                return new Connection(host, port);
            } catch (IOException e) {
                System.out.println("Сервер выключен или порт " + port + " недоступен.\n" +
                        "Укажите порт сервера.");
                while (true) {
                    try {
                        String inp = inputScanner.nextLine().trim();
                        if (inp.equals("exit")) {
                            return null;
                        }
                        port = Integer.parseInt(inp);
                        break;
                    } catch (NumberFormatException numberFormatException) {
                        System.out.println("Укажите корректный порт.");
                    } catch (NoSuchElementException noSuchElementException) {
                        return null;
                    }
                }
            }
        }
    }

    public void start(String[] args) {
        Scanner inputScanner = new Scanner(System.in);
        SocketIO socketIO = null;
        int port = tryToGetPort(args);
        String host = "localhost";
        Connection connection = tryToConnect(host, port);
        boolean flag = false;
        if (connection != null) {
            try {
                socketIO = new SocketIO(connection);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            port = connection.getSocket().getPort();
            flag = true;
            System.out.println("Соединение установлено.");

        }
        while (flag) {
            try {
                if(authorizationData==null)
                    authorizationData = Authorization.authorize(socketIO);
                InvokerLauncher programLauncher = new InvokerLauncher();
                programLauncher.launch(authorizationData, socketIO);
                flag= false;
            } catch (NoSuchElementException e) {
                break;
            } catch (IOException | ClassNotFoundException e) {
                authorizationData=null;
                System.out.println("Сервер стал не доступен.\n" +
                        "Введите 'exit' чтобы выйти. Или что-нибудь, чтобы попробовать подключиться снова.");
                if (inputScanner.nextLine().trim().equals("exit")) {
                    break;
                } else {
                    connection = tryToConnect(host, port);
                    if (connection != null) {
                        try {
                            socketIO = new SocketIO(connection);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        port = connection.getSocket().getPort();
                        System.out.println("Соединение восстановлено.");
                    }
                }
            }
        }
        if (connection != null)
            try {
                connection.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
}
