package cli;

import messages.MessageAcceptor;
import players.PlayerStatus;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Player {
    private static Registry registry;

    public static void main(String[] args) throws AlreadyBoundException, RemoteException {
        try {
            registry = LocateRegistry.getRegistry(2700);
        } catch (RemoteException e) {
            System.out.println("RMI Registry not found on port 2700");
            System.exit(1);
        }

        if (args.length == 0) {
            System.out.println("Please provide at least 1 player to start");
            System.exit(1);
        }

        if (args.length == 1) {
            startPlayerCollocutor(args);
        } else {
            startPlayerInitiator(args);
        }
    }

    private static void startPlayerInitiator(String[] args) throws RemoteException {
        if (args.length < 2) {
            System.out.println("Please provide 2 players: initiator and collocutor");
            System.exit(1);
        }
        if (args[0].equals(args[1])) {
            System.out.println("Player " + args[0] + " can not send messages to himself");
            System.exit(1);
        }

        int messagesToSend = 2;
        if (args.length > 2) {
            try {
                messagesToSend = Integer.parseInt(args[2]);
            } catch (NumberFormatException ignore) {
                System.out.println("Arg 3 should be int format");
                System.exit(1);
            }
        }

        players.Player player = initPlayer(args[0]);

        MessageAcceptor collocutor = null;
        String player2 = playerId(args[1]);
        do {
            try {
                collocutor = (MessageAcceptor) registry.lookup(player2);
            } catch (NotBoundException ignore) {}
        } while (collocutor == null);

        player.startConversation(args[1], collocutor, messagesToSend);

        observePlayer(player);
    }

    private static void startPlayerCollocutor(String[] args) throws RemoteException {
        players.Player player = initPlayer(args[0]);
        observePlayer(player);
    }

    static players.Player initPlayer(String playerName) throws RemoteException {
        players.Player player = new players.Player(
                playerId(playerName),
                playerName
        );
        registry.rebind(player.getPlayerId(), UnicastRemoteObject.exportObject(player, 0));
        return player;
    }

    static void observePlayer(players.Player player) {
        try {
            do {
                Thread.sleep(100);
            } while (player.getStatus() != PlayerStatus.IDLE);

            System.out.println("\n\t" + player.getPlayerName() + " is idle. Shutting down...");

            registry.unbind(player.getPlayerId());
            UnicastRemoteObject.unexportObject(player, true);

        } catch (InterruptedException | RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    static String playerId(String playerName) {
        return playerName
                .toLowerCase()
                .replaceAll("\\W", "");
    }
}