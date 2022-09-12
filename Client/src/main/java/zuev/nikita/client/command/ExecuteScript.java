package zuev.nikita.client.command;

import zuev.nikita.AuthorizationData;
import zuev.nikita.client.Invoker;
import zuev.nikita.client.net.SocketIO;
import zuev.nikita.message.ServerResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

/**
 * Executes a script written in another file.
 */
public class ExecuteScript extends Command {

    public ExecuteScript(AuthorizationData authorizationData, SocketIO socketIO, HashMap<String, Command> commandList) {
        super(authorizationData, socketIO, commandList);
    }

    /**
     * @param arg Path to the script file.
     * @return Script success/failure report.
     */
    @Override
    public String execute(String arg, Set<File> scripts) throws IOException {
        if (arg == null) return "Не указан путь к файлу.";
        File currentScript = new File(arg);
        if (currentScript.exists()) {
            if (currentScript.isFile()) {
                if (currentScript.canRead()) {
                    try (Scanner input = new Scanner(new File(arg))) {
                        if (!scripts.contains(currentScript.getAbsoluteFile())) {
                            scripts.add(currentScript.getAbsoluteFile());

                            Invoker invoker = new Invoker(authorizationData, commandList, scripts, socketIO);
                            String invokerResponse;
                            String[] fullCommand;
                            while (input.hasNextLine()) {
                                fullCommand = input.nextLine().split("\\s+");
                                try {
                                    invokerResponse = invoker.invoke(fullCommand);
                                    if (invokerResponse.equals("exit")) {
                                        break;
                                    }
                                    System.out.println(invokerResponse);
                                } catch (NumberFormatException e) {
                                    System.out.println("ID должен быть целым числом.\n" +
                                            "Команда пропущена.");
                                } catch (ClassNotFoundException | IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            scripts.remove(currentScript.getAbsoluteFile());
                        } else {
                            return "Невозможно выполнить скрипт, который уже выполняется.";
                        }
                        return "Скрипт выполнен успешно.";
                    } catch (FileNotFoundException e) {
                        return "Файл не найден.";
                    }
                } else {
                    return "Нехватка прав доступа к файлу.";
                }
            } else {
                return "Это должен быть файл, а не директория.";
            }
        } else {
            return "Файл не найден.";
        }
    }

    @Override
    public String getHelp() {
        return "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.";
    }
}
