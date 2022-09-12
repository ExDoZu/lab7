package zuev.nikita.server.command;

import zuev.nikita.AuthorizationData;
import zuev.nikita.structure.Organization;

import java.util.Date;
import java.util.Map;

/**
 * Updates the collection element by its ID.
 */
public class Update extends Command {
    public Update(Map<String, Organization> collection) {
        super(collection);
    }

    @Override
    public synchronized String execute(String arg, Organization organization, AuthorizationData authorizationData) {
        int id = Integer.parseInt(arg);
        boolean foundID = false;
        for (String key : collection.keySet())
            if (collection.get(key).getId() == id && collection.get(key).getAuthor().equals(authorizationData.getLogin())) {
                foundID = true;
                organization.setId(id);
                organization.setCreationDate(new Date());
                collection.replace(key, organization);
                break;
            }
        if (foundID) return "Данные успешно обновлены.";
        else return "Нет принадлежащего вам элемента коллекции с таким ID.";
    }

    @Override
    public String getHelp() {
        return "update id {element} : обновить значение элемента коллекции, id которого равен заданному";
    }
}
