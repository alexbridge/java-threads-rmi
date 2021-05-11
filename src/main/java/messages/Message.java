package messages;

/**
 * Simple POJO (message data container) used in players communications
 */
public class Message {
    private final String playerId;
    private final String message;

    /**
     * Predefined Message to gracefully stop conversation
     */
    public static final Message BYE = new Message("Bye!");

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
