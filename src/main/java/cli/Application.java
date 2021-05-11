package cli;

import messages.MessageQueue;
import players.Player;

/**
 * Example outcome for 2 messages conversation
 * <pre>{@code
 *     player 1: "message 0" -> player2
 *     player 1 <- player2: "message 0 1"
 *     player 1: "message 0 1 1" -> player2
 *     player 1 <- player2: "message 0 1 1 2"
 * }</pre>
 */
public class Application {
    private static final MessageQueue requests = new MessageQueue();
    private static final MessageQueue replies = new MessageQueue();

    public static void main(String[] args) {
        int initiatorMessagesLimit = 2;
        if (args.length == 1) {
            try {
                initiatorMessagesLimit = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Input malformed. Fallback player 1 messages limit to: 2");
            }
        }

        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");

        player2.listenMessages(replies, requests);
        player1.sendMessages("Player 2", requests, replies, initiatorMessagesLimit);
    }
}