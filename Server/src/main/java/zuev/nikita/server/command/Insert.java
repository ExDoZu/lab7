package zuev.nikita.server.command;

import zuev.nikita.AuthorizationData;
import zuev.nikita.structure.Organization;

import java.util.*;

/**
 * Inserts Organization entered manually by the user by the given key into the collection.
 */
public class Insert extends Command {
    public Insert(Map<String, Organization> collection) {
        super(collection);
    }

    @Override
    public String execute(String arg,  Organization organization, AuthorizationData authorizationData) {
        if (collection.containsKey(arg)) {
            return "Уже есть элемент с таким ключом.";
        } else {
            Set<Integer> ids = new HashSet<>();
            for (String key : collection.keySet())
                ids.add(collection.get(key).getId());
            int id = 0;
            for (int i = 1; i <= ids.size() + 1; i++)
                if (!ids.contains(i)) id = i;
            organization.setId(id);
            organization.setCreationDate(new Date());
            organization.setAuthor(authorizationData.getLogin());
            collection.put(arg, organization);
            return "Данные успешно введены.";
        }
    }

    @Override
    public String getHelp() {
        return "insert null {element} : добавить новый элемент с заданным ключом";
    }
}
