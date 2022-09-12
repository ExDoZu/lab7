package zuev.nikita.message;

import zuev.nikita.AuthorizationData;
import zuev.nikita.structure.Organization;

import java.io.Serializable;

public class ClientRequest implements Serializable {
    private final String[] fullCommand;
    private final Organization organization;
    private final AuthorizationData authorizationData;

    public ClientRequest(String[] fullCommand, Organization organization, AuthorizationData authorizationData) {
        this.fullCommand = fullCommand;
        this.organization = organization;
        this.authorizationData=authorizationData;

    }

    public Organization getOrganization() {
        return organization;
    }

    public String[] getFullCommand() {
        return fullCommand;
    }

    public AuthorizationData getAuthorizationData() {
        return authorizationData;
    }
}
