package cli;

import messages.MessageExchanger;
import players.Player;

public class Application {
    private static final MessageExchanger messageExchanger = new MessageExchanger();

    public static void main(String[] args) throws InterruptedException {
        int initiatorMessagesLimit = 2;
        if (args.length == 1) {
            try {
                initiatorMessagesLimit = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Input malformed. Fallback player 1 messages limit to: 2");
            }
        }

        Player initiator = new Player("Player 1", messageExchanger, initiatorMessagesLimit);
        Player collocutor = new Player("Player 2", messageExchanger);


        new Thread(initiator).start();
        new Thread(collocutor).start();
        Thread.sleep(1000);

    }
}