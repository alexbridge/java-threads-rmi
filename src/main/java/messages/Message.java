package messages;

public class Message {
    private final String message;

    public static final Message BYE = new Message("0");
    public static final Message ALIVE = new Message("1");

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
