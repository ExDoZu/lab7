package zuev.nikita.server.command;

import zuev.nikita.AuthorizationData;
import zuev.nikita.structure.Address;
import zuev.nikita.structure.Organization;

import java.sql.Statement;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Returns all elements as a string whose postalAddress field value is greater than the given value.
 */
public class FilterGreaterThanPostalAddress extends Command {
    public FilterGreaterThanPostalAddress(Map<String, Organization> collection) {
        super(collection);
    }

    /**
     * @param arg Given address.
     */
    @Override
    public String execute(String arg, Organization organization, AuthorizationData authorizationData){
        if (arg == null) return "Команда нуждается в аргументе.";
        Address userInputPostalAddress = new Address(arg);
        String str = collection.values().stream().filter(v -> v.getPostalAddress().compareTo(userInputPostalAddress) > 0).map(Organization::toString).collect(Collectors.joining("\n\n"));
        if (!str.equals("")) {
            return str;
        } else {
            return "Нет элементов с адресом больше заданного.";
        }
    }

    @Override
    public String getHelp() {
        return "filter_greater_than_postal_address postalAddress : вывести элементы, значение поля postalAddress которых больше заданного";
    }
}
