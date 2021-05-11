package players;

import messages.Message;
import messages.MessageExchanger;

import java.util.concurrent.atomic.AtomicInteger;

public class Player implements Runnable {
    private final String playerId;
    private final MessageExchanger messageExchanger;
    private final AtomicInteger messagesSent = new AtomicInteger(0);

    private int messagesToSend;

    public Player(String playerId, MessageExchanger messageExchanger, int messagesToSend) {
        this.playerId = playerId;
        this.messageExchanger = messageExchanger;
        this.messagesToSend = messagesToSend;
    }

    public Player(String playerId, MessageExchanger messageExchanger) {
        this.playerId = playerId;
        this.messageExchanger = messageExchanger;
    }

    @Override
    public void run() {

        THREAD_LOOP:
        while (true) {
            if (Thread.interrupted()) {
                // Interrupted outside of a thread
                break;
            }

            Message message = null;

            try {
                if (this.messagesToSend > 0) {
                    // Messages producer
                    message = new Message("message 0");
                    do {
                        message = this.messageExchanger.exchange(message);
                        if (message == Message.ALIVE) {
                            continue;
                        }

                        System.out.println(
                                "Player 1: <- Player 2: " + message.getMessage()
                        );

                        message = new Message(message.getMessage() + " " + this.messagesSent.incrementAndGet());
                        this.messagesToSend--;
                    } while (this.messagesToSend > 0);

                    // Finish conversation by sending silent message
                    this.messageExchanger.exchange(Message.BYE);
                    // Break main thread loop
                    break;
                } else {
                    // Messages consumer
                    message = Message.ALIVE;
                    while (true) {
                        message = this.messageExchanger.exchange(message);
                        if (message == Message.ALIVE) {
                            continue;
                        }

                        if (message == Message.BYE) {
                            break THREAD_LOOP;
                        }

                        System.out.println(
                                "Player 1: " + message.getMessage() + " -> Player 2"
                        );

                        message = new Message(message.getMessage() + " " + this.messagesSent.incrementAndGet());
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
