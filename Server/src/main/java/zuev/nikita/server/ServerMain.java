package zuev.nikita.server;

import java.util.NoSuchElementException;

public class ServerMain {
    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.start(args);
        } catch (NoSuchElementException ignored) {}
    }
}
