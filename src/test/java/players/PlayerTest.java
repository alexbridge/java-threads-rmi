package players;

import messages.Message;
import messages.MessageQueue;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

/**
 * Testing Player behavior
 */
public class PlayerTest
{
    /**
     * Test sending messages
     */
    @Test
    public void shouldSendMessages() throws InterruptedException {
        MessageQueue incoming = mock(MessageQueue.class);
        MessageQueue outgoing = mock(MessageQueue.class);

        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);

        when(incoming.take()).thenReturn(
                // Mimic player 2
                new Message("Player 2", "message 0 1")
        ).thenReturn(
                // Mimic player 2
                new Message("Player 2", "message 0 1 1 2")
        );


        Player player = new Player("Player 1");
        player.sendMessages("Player 2", incoming, outgoing, 2);

        Thread.sleep(100);

        verify(outgoing, times(3)).put(captor.capture());

        List<Message> captured = captor.getAllValues();

        assertEquals("Player 1", captured.get(0).getPlayerId());
        assertEquals("message 0", captured.get(0).getMessage());

        assertEquals("Player 1", captured.get(1).getPlayerId());
        assertEquals("message 0 1 1", captured.get(1).getMessage());

        assertSame(captured.get(2), Message.BYE);
    }

    /**
     * Test receiving messages
     */
    @Test
    public void shouldReceiveMessages() throws InterruptedException {
        MessageQueue incoming = mock(MessageQueue.class);
        MessageQueue outgoing = mock(MessageQueue.class);

        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);

        when(incoming.take()).thenReturn(
                // Mimic player 1
                new Message("Player 1", "message 0")
        ).thenReturn(
                // Mimic player 1
                new Message("Player 1", "message 0 1 1")
        ).thenReturn(Message.BYE);

        Player player = new Player("Player 2");
        player.listenMessages(incoming, outgoing);

        Thread.sleep(100);

        verify(incoming, times(3)).take();
        verify(outgoing, times(2)).put(captor.capture());

        List<Message> captured = captor.getAllValues();

        assertEquals("Player 2", captured.get(0).getPlayerId());
        assertEquals("message 0 1", captured.get(0).getMessage());

        assertEquals("Player 2", captured.get(1).getPlayerId());
        assertEquals("message 0 1 1 2", captured.get(1).getMessage());
    }
}
