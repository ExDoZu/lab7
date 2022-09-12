package zuev.nikita.server.command;

import zuev.nikita.AuthorizationData;
import zuev.nikita.structure.Organization;

import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Removes all elements from the collection that are less than the specified value.
 */
public class RemoveLower extends Command {
    public RemoveLower(Map<String, Organization> collection) {
        super(collection);
    }

    @Override
    public synchronized String execute(String arg,  Organization organization, AuthorizationData authorizationData){
        int oldSize = collection.size();
        collection.entrySet().removeIf(x -> (x.getValue().compareTo(organization) < 0 && x.getValue().getAuthor().equals(authorizationData.getLogin())));
        if (oldSize == collection.size()) {
            return "Нет элемента меньше заданного.";
        } else {
            return "Команда выполнена.";
        }
    }

    @Override
    public String getHelp() {
        return "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный";
    }
}
