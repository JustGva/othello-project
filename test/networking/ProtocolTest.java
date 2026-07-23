package networking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A class to test some Protocol commands.
 */
public class ProtocolTest {

    /**
     * A test to ensure the ERROR command is handled correctly.
     */
    @Test
    public void testErrorMessage() {
        assertEquals("ERROR~something unexpected happened",
                Protocol.printError("something unexpected happened"));
    }

    /**
     * A test to ensure the HELLO command is handled correctly.
     */
    @Test
    public void testHelloMessage() {
        assertEquals("HELLO~welcome", Protocol.helloMessage("welcome"));
    }

    /**
     * A test to ensure LOGIN command for the user is handled correctly.
     */
    @Test
    public void testLoginMessage() {
        assertEquals("LOGIN~username", Protocol.loginMessage("username"));
    }

    /**
     * A test to ensure that LOGIN command sent by the server is handled correctly.
     */
    @Test
    public void testSuccessfulLogin() {
        assertEquals("LOGIN", Protocol.successfulLogin());
    }

    /**
     * A test to ensure that QUEUE command sent by the client is handled correctly.
     */
    @Test
    public void testJoinQueue() {
        assertEquals("QUEUE", Protocol.joinQueue());
    }

    /**
     * A test to ensure that NEWGAME command is handled correctly.
     */
    @Test
    public void testMakeNewGame() {
        assertEquals("NEWGAME~Justas~Jordan", Protocol.makeNewGame("Justas", "Jordan"));
    }

    /**
     * A test to ensure that MOVE command sent by the client is handled correctly.
     */
    @Test
    public void testSendMove() {
        assertEquals("MOVE~7", Protocol.sendMove("7"));
    }
    /**
     * A test to ensure that MOVE command sent by the server is handled correctly.
     */
    @Test
    public void testSendServerMove() {
        assertEquals("MOVE~59", Protocol.sendServerMove("59"));
    }

}
