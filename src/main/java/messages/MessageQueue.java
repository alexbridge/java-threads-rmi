package messages;

import java.io.*;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageQueue extends LinkedBlockingQueue<Message> {

    private String bucketId;

    public MessageQueue(String bucketId) {
        super();
        this.bucketId = bucketId;
    }

    public String getBucketId() {
        return bucketId;
    }

    public static synchronized void sendNetworkMessage(String bucket, Message message) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("messages" + File.separator + bucket)
        )) {
            out.writeObject(message);
        }
    }

    public static synchronized Message receiveNetworkMessage(String bucket) throws IOException {
        File file = new File("messages" + File.separator + bucket);
        Message message = null;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            message = (Message) in.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("Can not receive message: " + e.getMessage());
        }

        return message;
    }
}
