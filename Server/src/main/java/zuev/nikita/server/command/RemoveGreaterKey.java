package zuev.nikita.server.command;

import zuev.nikita.AuthorizationData;
import zuev.nikita.structure.Organization;

import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Removes from the collection all elements whose key is greater than the specified value.
 */
public class RemoveGreaterKey extends Command {
    public RemoveGreaterKey(Map<String, Organization> collection) {
        super(collection);
    }

    /**
     * @param arg Given key
     */
    @Override
    public synchronized String execute(String arg, Organization organization, AuthorizationData authorizationData) {
        if (arg == null) return "Не был указан Ключ.";
        int oldSize = collection.size();
        collection.entrySet().removeIf(x -> (x.getKey().compareTo(arg) > 0 && x.getValue().getAuthor().equals(authorizationData.getLogin())));
        if (oldSize == collection.size()) {
            return "Нет элементов с ключом больше заданного.";
        } else {
            return "Команда выполнена.";
        }
    }

    @Override
    public String getHelp() {
        return "remove_greater_key null : удалить из коллекции все элементы, ключ которых превышает заданный";
    }
}
