package players;

import messages.Message;
import messages.MessageQueue;

import java.io.IOException;

public class Player {
    private final String playerId;
    private final MessageQueue messageIn;
    private final MessageQueue messageOut;

    private int messagesToSend;

    public Player(String playerId, MessageQueue messageIn, MessageQueue messageOut) {
        this.playerId = playerId;
        this.messageIn = messageIn;
        this.messageOut = messageOut;
    }

    public void sendMessages(String toPlayerId, final int messagesToSend) {
        new Thread(() -> {
            int messagesSent = 0;
            int leftMessages = messagesToSend;
            Message message = new Message(this.playerId, "message");
            try {
                do {
                    if (Thread.interrupted()) {
                        // Interrupted outside of a thread
                        break;
                    }
                    /*messageOut.put(
                            new Message(this.playerId, message.getMessage() + " " +  messagesSent++)
                    );*/
                    MessageQueue.sendNetworkMessage(
                            messageOut.getBucketId(),
                            new Message(this.playerId, message.getMessage() + " " +  messagesSent++)
                    );
                    leftMessages--;

                    // block until a request arrives
                    message = messageIn.take();

                    System.out.println(
                            this.playerId + " <- " + toPlayerId + ": \"" + message.getMessage() + "\""
                    );
                } while (leftMessages > 0);

                messageOut.put(Message.BYE);
            } catch (InterruptedException | IOException ie) {
                ie.printStackTrace();
            }
        }).start();
    }

    public void listenMessages() {
        new Thread(() -> {
            Message message;
            int messagesSent = 0;
            while (true) {
                if (Thread.interrupted()) {
                    // Interrupted outside of a thread
                    break;
                }

                try {
                    // block until a request arrives
                    message = messageIn.take();
                    if (message == Message.BYE) {
                        break;
                    }

                    System.out.println(
                            message.getPlayerId() + ": \"" + message.getMessage() +  "\" -> " + this.playerId
                    );

                    /*messageOut.put(
                            new Message(message.getMessage() + " " + ++messagesSent)
                    );*/
                    MessageQueue.sendNetworkMessage(
                            messageOut.getBucketId(),
                            new Message(message.getMessage() + " " + ++messagesSent)
                    );
                } catch (InterruptedException | IOException ie) {
                    ie.printStackTrace();
                }
            }
        }).start();
    }
}
