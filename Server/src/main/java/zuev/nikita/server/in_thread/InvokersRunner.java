package zuev.nikita.server.in_thread;

import zuev.nikita.AuthorizationData;
import zuev.nikita.server.Invoker;
import zuev.nikita.structure.Organization;

/**
 * Class for multithreading that launches invoker.
 */
public class InvokersRunner implements Runnable{
    private final String [] fullCommand;
    private final Organization organization;
    private final Invoker invoker;

    private String response;

    private final AuthorizationData authorizationData;

    public InvokersRunner(String [] fullCommand, Organization organization, Invoker invoker, AuthorizationData authorizationData){
        this.fullCommand=fullCommand;
        this.organization=organization;
        this.invoker = invoker;
        this.authorizationData =authorizationData;
    }
    @Override
    public void run(){
        response = invoker.invoke(fullCommand, organization,authorizationData );
    }

    public String getResponse() {
        return response;
    }
}
