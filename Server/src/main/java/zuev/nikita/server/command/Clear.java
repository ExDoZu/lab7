package zuev.nikita.server.command;

import zuev.nikita.AuthorizationData;
import zuev.nikita.structure.Organization;

import java.sql.Statement;
import java.util.Map;

/**
 * Clears the collection.
 */
public class Clear extends Command {


    public Clear(Map<String, Organization> collection) {
        super(collection);
    }

    @Override
    public synchronized String execute(String arg, Organization organization, AuthorizationData authorizationData) {
        int oldSize = collection.size();
        if (arg != null) return "Команда не нуждается в аргументе.";
        collection.entrySet().removeIf(e -> e.getValue().getAuthor().equals(authorizationData.getLogin()));
        if(oldSize==collection.size())return "В коллекции нет элементов, принадлежащих вам.";
        else return "Ваши элементы удалены.";
    }

    @Override
    public String getHelp() {
        return "clear : очистить коллекцию";
    }
}
