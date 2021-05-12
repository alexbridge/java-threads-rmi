package messages;

import java.util.concurrent.SynchronousQueue;

/**
 * Messages blocking queue to transfer only {@code Message} objects
 */
public class MessageQueue extends SynchronousQueue<Message> {}
