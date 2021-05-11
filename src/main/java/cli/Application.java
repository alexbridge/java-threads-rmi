package cli;

import messages.Message;
import messages.MessageQueue;
import players.Player;

import java.io.IOException;
import java.nio.file.*;

/**
 * Example for 2 messages
 * player 1: "message 0" -> player2
 * player 1 <- player2: "message 0 1"
 * player 1: "message 0 1 1" -> player2
 * player 1 <- player2: "message 0 1 1 2"
 */
public class Application {
    private static final MessageQueue requests = new MessageQueue("requests");
    private static final MessageQueue replies = new MessageQueue("replies");

    public static void main(String[] args) throws InterruptedException, IOException {
        int initiatorMessagesLimit = 2;
        if (args.length == 1) {
            try {
                initiatorMessagesLimit = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Input malformed. Fallback player 1 messages limit to: 2");
            }
        }

        Player initiator = new Player("Player 1", requests, replies);
        Player collocutor = new Player("Player 2", replies, requests);

        registerInOutWatcher();

        collocutor.listenMessages();
        initiator.sendMessages("Player 2", initiatorMessagesLimit);
    }

    static void registerInOutWatcher() {
        Thread fileWatcher = new Thread(() -> {
            try {
                WatchService watchService = FileSystems.getDefault().newWatchService();
                Path path = Paths.get("messages");
                path.register(
                        watchService,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_MODIFY
                );

                WatchKey key;
                while ((key = watchService.take()) != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        String bucket = event.context().toString();

                        Message message = null;
                        try {
                            message = MessageQueue.receiveNetworkMessage(
                                    event.context().toString()
                            );
                            if (requests.getBucketId().equals(bucket)) {
                                requests.put(message);
                            }
                            if (replies.getBucketId().equals(bucket)) {
                                replies.put(message);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    key.reset();
                }

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        });
        fileWatcher.setPriority(Thread.MAX_PRIORITY);
        fileWatcher.start();
    }
}