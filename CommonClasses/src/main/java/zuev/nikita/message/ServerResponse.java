package zuev.nikita.message;

import java.io.Serializable;

/**
 * Client receives an object of this class from the server
 */
public class ServerResponse implements Serializable {
    private final String response;
    private final int statusCode;

    public static final int OK = 0;
    public static final int NEW_USER = 1;
    public static final int WRONG_PASSWORD=2;
    public static final int WRONG_COMMAND = 3;

    public static final int OLD_USER = 4;




    public ServerResponse(String response, int statusCode) {
        this.response = response;
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponse() {
        return response;
    }
}
