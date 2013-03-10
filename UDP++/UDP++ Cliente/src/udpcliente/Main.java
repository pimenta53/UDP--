/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package udpcliente;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author miguel
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, InterruptedException, UnknownHostException, IOException {

        DatagramSocket cliente = new DatagramSocket();

        Dados dados = new Dados("localhost", 5000, cliente);

        ConfirmPackets confs = new ConfirmPackets(dados);
        Thread thConf = new Thread(confs);
        thConf.start();

        ManageCon manCon = new ManageCon(dados);
        Thread thMan = new Thread(manCon);
        thMan.start();

        RecPackets recPacks = new RecPackets(dados);
        Thread th = new Thread(recPacks);
        th.start();

        thMan.join();

        System.out.println("Pacotes recebidos: " + dados.getRec());
        System.out.println("Pacotes confirmados: " + dados.getConf());
        System.out.println("Pacotes RT: " + dados.getNRT());

    }

}
