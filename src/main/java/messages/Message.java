package messages;

import java.io.Serializable;

/**
 * Simple POJO (message data container) used in players communications
 */
public class Message implements Serializable {
    private final String playerName;
    private final String request;
    private String response;

    /**
     * Predefined Message to gracefully stop conversation
     */
    public static final Message BYE = new Message(null, "Bye!");

    public Message(String playerName, String request) {
        this.playerName = playerName;
        this.request = request;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getRequest() {
        return request;
    }

    public String getResponse() {
        return response;
    }

    public Message setResponse(String response) {
        this.response = response;
        return this;
    }
}
