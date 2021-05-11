package messages;

import java.io.Serializable;

public class Message implements Serializable {
    private final String playerId;
    private final String message;

    public static final Message BYE = new Message("0");

    public Message(String playerId, String message) {
        this.playerId = playerId;
        this.message = message;
    }

    public Message(String message) {
        this(null, message);
    }

    public String getMessage() {
        return message;
    }

    public String getPlayerId() {
        return playerId;
    }
}
