package cli;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

/**
 * Functional test for Application
 */
public class ApplicationTest
{
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(out));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void shouldRunApplicationAndProduceOutput() throws InterruptedException {
        Application.main(new String[]{ "2" });

        Thread.sleep(100);

        StringBuilder expected = new StringBuilder();
        expected.append("Player 1: \"message 0\" -> Player 2");
        expected.append(System.lineSeparator());
        expected.append("Player 1 <- Player 2: \"message 0 1\"");
        expected.append(System.lineSeparator());
        expected.append("Player 1: \"message 0 1 1\" -> Player 2");
        expected.append(System.lineSeparator());
        expected.append("Player 1 <- Player 2: \"message 0 1 1 2\"");
        expected.append(System.lineSeparator());

        assertEquals(expected.toString(), out.toString());
    }
}
