package zuev.nikita.server.in_thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zuev.nikita.AuthorizationData;
import zuev.nikita.message.ClientRequest;
import zuev.nikita.message.ServerResponse;
import zuev.nikita.server.Hasher;
import zuev.nikita.server.ServerMain;
import zuev.nikita.server.net.Connection;
import zuev.nikita.server.net.SocketChannelIO;
import zuev.nikita.structure.Organization;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;


/**
 * Class that got connection with new client, authorizes it
 * and launches reader and handler of clients commands.
 */
public class ClientHandler implements Runnable {

    private final Map<String, Organization> collection;
    private final SocketChannel socketChannel;
    private final Statement statement;

    private  final Logger log = LoggerFactory.getLogger(ServerMain.class);

    public ClientHandler(Map<String, Organization> collection, SocketChannel socketChannel, Statement statement) {
        this.collection = collection;
        this.socketChannel = socketChannel;
        this.statement = statement;
    }

    private void authorizeConnectedUser(SocketChannelIO socketChannelIO){
        ClientRequest clientRequest;
        log.info("Authorization is begun.");
        try {
            clientRequest = socketChannelIO.read();
            String s = clientRequest.getFullCommand()[0];
            ResultSet resultSet;
            log.info("Got authorization type: "+ s);
            String login = null;
            String passwordHash = null;
            String password;

            if (s.equals("login")) {
                boolean ok = false;
                while (!ok) {
                    clientRequest = socketChannelIO.read();
                    login = clientRequest.getAuthorizationData().getLogin();
                    resultSet = statement.executeQuery("SELECT * FROM users WHERE login = '"+login+"';");
                    while (resultSet.next()) {
                        String databaseLogin = resultSet.getString("login");
                        log.debug("Comparing of "+login+" and "+databaseLogin);
                        if (databaseLogin.equals(login)) {
                            ok = true;
                            passwordHash = resultSet.getString("password");
                            break;
                        }

                    }
                    log.debug("server response is ok: "+ok);
                    if (ok) socketChannelIO.write(null, ServerResponse.OK);
                    else socketChannelIO.write(null, ServerResponse.NEW_USER);
                }
                while (true) {
                    clientRequest = socketChannelIO.read();
                    password = clientRequest.getAuthorizationData().getPassword();
                    if (passwordHash.equals(Hasher.getHashSHA384(password))) {
                        socketChannelIO.write(null, ServerResponse.OK);
                        break;
                    } else socketChannelIO.write(null, ServerResponse.WRONG_PASSWORD);
                }

            } else {
                boolean newLogin = false;
                while (!newLogin) {
                    newLogin = true;
                    clientRequest = socketChannelIO.read();
                    login = clientRequest.getAuthorizationData().getLogin();
                    resultSet = statement.executeQuery("SELECT * FROM users WHERE login = '"+login+"';");
                    while (resultSet.next()) {
                        String databaseLogin = resultSet.getString("login");
                        if (databaseLogin.equals(login)) {
                            newLogin = false;
                            break;
                        }
                    }
                    if (newLogin) socketChannelIO.write(null, ServerResponse.OK);
                    else socketChannelIO.write(null, ServerResponse.OLD_USER);
                }
                clientRequest = socketChannelIO.read();
                AuthorizationData authorizationData = clientRequest.getAuthorizationData();
                statement.executeUpdate("INSERT INTO users VALUES ('"+login+"', '"+Hasher.getHashSHA384(authorizationData.getPassword())+"')");
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {

        Connection connection = new Connection(socketChannel, collection);
        authorizeConnectedUser(connection.getSocketChannelIO());
        boolean notGotExit = true;
        while (notGotExit) {
            try {
                notGotExit = connection.clientHandle();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
