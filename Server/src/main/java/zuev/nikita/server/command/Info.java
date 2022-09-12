package zuev.nikita.server.command;

import zuev.nikita.AuthorizationData;
import zuev.nikita.structure.Organization;

import java.util.Map;

/**
 * Returns information about the collection
 */
public class Info extends Command {
    public Info(Map<String, Organization> collection) {
        super(collection);
    }

    @Override
    public String execute(String arg,  Organization organization, AuthorizationData authorizationData) {
        if (arg != null) return "Команда не нуждается в аргументе.";
        return "Коллекция представляет из себя HashTable, хранящий объекты типа Organization. В данный момент в коллекции " + collection.size() + " элементов.";
    }

    @Override
    public String getHelp() {
        return "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }
}
