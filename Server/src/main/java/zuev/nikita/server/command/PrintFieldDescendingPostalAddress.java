package zuev.nikita.server.command;

import zuev.nikita.AuthorizationData;
import zuev.nikita.structure.Address;
import zuev.nikita.structure.Organization;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Returns the values of the postalAddress field of all elements in descending order as a string.
 */
public class PrintFieldDescendingPostalAddress extends Command {
    public PrintFieldDescendingPostalAddress(Map<String, Organization> collection) {
        super(collection);
    }

    @Override
    public String execute(String arg,Organization organization, AuthorizationData authorizationData) {
        if (arg != null) return "Команда не нуждается в аргументе.";
        return collection.values().stream().map(Organization::getPostalAddress).sorted(Comparator.reverseOrder()).map(Address::getZipCode).collect(Collectors.joining("\n"));
    }

    @Override
    public String getHelp() {
        return "print_field_descending_postal_address : вывести значения поля postalAddress всех элементов в порядке убывания.";
    }
}
