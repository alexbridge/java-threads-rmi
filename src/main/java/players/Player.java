package players;

import messages.Message;
import messages.MessageQueue;

/**
 * Player class represent bi-directional direct communication between players.
 * Message queues that are used as incoming/outgoing messages of first player
 * should be flipped fo second player (what is outgoing of first player, that is
 * incoming for second user)
 *
 * Player can:
 * - start sending messages to other player
 * - listen for incoming messages from other player
 *
 * <p>
 *  <b>Sample Usage</b>
 *  <pre> {@code
 *      Player player1 = new Player("Player 1");
 *      Player player2 = new Player("Player 2", <MessageQueue>, <MessageQueue>);
 *
 *      player2.listenMessages(<MessageQueue>, <MessageQueue>);
 *      player1.sendMessages("Player 2", <MessageQueue>, <MessageQueue>, 2);
 *  }</pre>
 */
public class Player {
    private final String playerId;

    public Player(String playerId) {
        this.playerId = playerId;
    }

    /**
     * Start communication to given player (initiator player)
     */
    public void sendMessages(String toPlayerId, MessageQueue messageQueue, final int messagesToSend) {
        new Thread(() -> {
            int messagesSent = 0;
            int leftMessages = messagesToSend;

            // Initial message to send
            Message message = new Message(this.playerId, "message");
            try {
                do {
                    if (Thread.interrupted()) {
                        // Interrupted outside of a thread
                        break;
                    }
                    // Send a message to outgoing queue
                    messageQueue.put(
                            new Message(this.playerId, message.getMessage() + " " +  messagesSent++)
                    );
                    leftMessages--;

                    // Wait message in incoming queue
                    message = messageQueue.take();

                    System.out.println(
                            this.playerId + " <- " + message.getPlayerId() + ": \"" + message.getMessage() + "\""
                    );
                } while (leftMessages > 0);

                // Notify other player that communication is done
                messageQueue.put(Message.BYE);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }).start();
    }

    /**
     * Start listening for incoming messages from initiator player
     */
    public void listenMessages(MessageQueue messageQueue) {
        new Thread(() -> {
            Message message;
            int messagesSent = 0;
            while (true) {
                if (Thread.interrupted()) {
                    // Interrupted outside of a thread
                    break;
                }

                try {
                    // Wait a message in incoming queue
                    message = messageQueue.take();
                    if (message == Message.BYE) {
                        // Stop a Thread since communication is finished
                        break;
                    }

                    System.out.println(
                            message.getPlayerId() + ": \"" + message.getMessage() +  "\" -> " + this.playerId
                    );

                    // Send a message to outgoing queue
                    messageQueue.put(
                            new Message(this.playerId, message.getMessage() + " " + ++messagesSent)
                    );
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }).start();
    }
}
