package messages;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Messages blocking queue to transfer only {@code Message} objects
 */
public class MessageQueue extends LinkedBlockingQueue<Message> {}
