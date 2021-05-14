package players;

import messages.Message;
import messages.MessageAcceptor;

import java.rmi.RemoteException;

public class Player implements MessageAcceptor {
    private final String playerId;
    private final String playerName;
    private PlayerStatus status = PlayerStatus.ACTIVE;
    private int messagesCount = 0;

    public Player(String playerId, String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public Message acceptMessage(Message message) {
        if (message.getRequest().equals(Message.BYE.getRequest())) {
            this.status = PlayerStatus.IDLE;
            return message;
        }

        message.setResponse(message.getRequest() + " " + ++messagesCount);

        System.out.println(
                message.getPlayerName() + " <- " + this.playerId + ": \"" + message.getResponse() + "\""
        );

        return message;
    }

    public void startConversation(String playerName, MessageAcceptor player, int messagesToSend) throws RemoteException {
        Message message = new Message(this.playerName, "message " + messagesCount);
        for (int i = 0; i < messagesToSend; i++) {
            System.out.println(
                    this.playerName + ": \"" + message.getRequest() +  "\" -> " + playerName
            );

            message = player.acceptMessage(message);

            message = new Message(this.playerName, message.getResponse() + " " + ++messagesCount);
        }

        player.acceptMessage(Message.BYE);

        this.status = PlayerStatus.IDLE;
    }
}
