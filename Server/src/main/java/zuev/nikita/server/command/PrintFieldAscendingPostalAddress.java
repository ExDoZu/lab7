package zuev.nikita.server.command;

import zuev.nikita.AuthorizationData;
import zuev.nikita.structure.Address;
import zuev.nikita.structure.Organization;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Returns the values of the postalAddreess field of all elements in ascending order as a string.
 */
public class PrintFieldAscendingPostalAddress extends Command {
    public PrintFieldAscendingPostalAddress(Map<String, Organization> collection) {
        super(collection);
    }

    @Override
    public String execute(String arg, Organization organization, AuthorizationData authorizationData) {
        if(arg!=null)return "Команда не нуждается в аргументе.";
        return collection.values().stream().map(Organization::getPostalAddress).sorted().map(Address::getZipCode).collect(Collectors.joining("\n"));
    }

    @Override
    public String getHelp() {
        return "print_field_ascending_postal_address : вывести значения поля postalAddress всех элементов в порядке возрастания.";
    }
}
