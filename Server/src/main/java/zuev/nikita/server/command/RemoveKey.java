package zuev.nikita.server.command;

import zuev.nikita.AuthorizationData;
import zuev.nikita.structure.Organization;

import java.util.Map;
import java.util.Objects;

/**
 * Removes an element from the collection by its key.
 */
public class RemoveKey extends Command {
    public RemoveKey(Map<String, Organization> collection) {
        super(collection);
    }

    @Override
    public synchronized String execute(String arg, Organization organization, AuthorizationData authorizationData) {
        if (arg == null) return "Не был указан Ключ.";
        if (collection.containsKey(arg)  ) {
            if(collection.get(arg).getAuthor().equals(authorizationData.getLogin())) {
                collection.remove(arg);
                return "Команда выполнена.";
            }else{
                return "Объект с этим ключом вам не принадлежит.";
            }
        } else {
            return "Нет элемента с таким ключом.";
        }
    }

    @Override
    public String getHelp() {
        return "remove_key null : удалить элемент из коллекции по его ключу.";
    }
}
