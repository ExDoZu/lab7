package zuev.nikita.client;

import zuev.nikita.AuthorizationData;
import zuev.nikita.client.net.SocketIO;
import zuev.nikita.message.ServerResponse;

import java.io.Console;
import java.io.IOException;
import java.util.Scanner;

public class Authorization {
    public static AuthorizationData authorize(SocketIO socketIO) throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Желаете войти или зарегистрироваться? [L/R]");
        while (true) {
            char choice = scanner.next().charAt(0);
            if (choice == 'l' || choice=='L') return login(socketIO);
            else if (choice == 'r' || choice=='R') return registration(socketIO);
        }
    }

    private static String inputLogin() {
        String login;
        Scanner scanner =new Scanner(System.in);
        while (true) {
            System.out.print("Логин: ");
            login = scanner.nextLine();
            if (login.charAt(0) == ' ' || login.charAt(login.length() - 1) == ' '
                    || login.charAt(0) == '\t' || login.charAt(login.length() - 1) == '\t') {
                System.out.println("Пожалуйста, не используйте пробелы в начеле и конце логина.");
            } else {
                break;
            }
        }
        return login;
    }

    private static AuthorizationData login(SocketIO socketIO) throws IOException, ClassNotFoundException {
        socketIO.write(new String[]{"login"}, null, null);
        String login;
        String password;
        while (true) {
            login =inputLogin();
            socketIO.write(null, null, new AuthorizationData(login, null));
            ServerResponse serverResponse = socketIO.read();
            if (serverResponse.getStatusCode() == ServerResponse.NEW_USER) {
                System.out.println("Такого пользователя не существует");
            }
            if (serverResponse.getStatusCode() == ServerResponse.OK) break;
        }
        while (true) {
            System.out.print("Пароль: ");
            Console console = System.console();
            password = new String(console.readPassword());
            socketIO.write(null, null, new AuthorizationData(login, password));
            ServerResponse serverResponse = socketIO.read();
            if (serverResponse.getStatusCode() == ServerResponse.WRONG_PASSWORD) System.out.println("Неверный пароль.");
            if (serverResponse.getStatusCode() == ServerResponse.OK) break;
        }
        System.out.println("Вы вошли.");
        return new AuthorizationData(login, password);
    }

    private static AuthorizationData registration(SocketIO socketIO) throws IOException, ClassNotFoundException {

        socketIO.write(new String[]{"registration"}, null, null);
        String login;
        String password;
        while (true) {
            login = inputLogin();
            socketIO.write(null, null, new AuthorizationData(login, null));
            ServerResponse serverResponse = socketIO.read();

            if (serverResponse.getStatusCode() == ServerResponse.OLD_USER) {
                System.out.println("Такой пользователь уже существует.");
            }
            if (serverResponse.getStatusCode() == ServerResponse.OK) break;
        }
        System.out.print("Пароль: ");
        Console console = System.console();
        password = new String(console.readPassword());
        AuthorizationData authorizationData = new AuthorizationData(login, password);
        socketIO.write(null, null, authorizationData);
        System.out.println("Вы зарегистрированы.");
        return authorizationData;
    }
}
