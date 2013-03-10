/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package udpcliente;

/**
 *
 * @author miguel
 */
public class Protocol {

    public static int MAX_CLIENTS=2;
    public static int FIRST_INDEX_PORT=3000;
    public static int MAX_PDU_SIZE=1024;
    public static byte connect = 1,
                        disconnect = 2,
                        data = 3,
                        timeRoundTrip = 4,
                        ackEndConnection = 5,
                        ackData = 6,
                        ackTimeRoundTrip = 7;
}
