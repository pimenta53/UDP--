/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package udpcliente;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class RecPackets implements Runnable{

    private Dados dadosPacks;
    private DatagramSocket cliente;
    private File ficheiro;
    private OutputStream out;

    public RecPackets(Dados dadosPacks) throws FileNotFoundException {
        this.dadosPacks=dadosPacks;
        cliente = dadosPacks.getClienteSocket();
        ficheiro = new File("/home/miguel/teste1recebido.mp3");
        out = new FileOutputStream(ficheiro);
    }

   ///APAGAR

    public void sendPacket() throws IOException{
        byte[] sendData = new byte[]{Protocol.ackData};

        InetAddress IPAddress = InetAddress.getByName(dadosPacks.getIp());;
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,IPAddress, dadosPacks.getPort());
	cliente.send(sendPacket);
    }

    public synchronized void trataPacotes(DatagramPacket packet) throws IOException{
        byte[] receiveData = packet.getData();
        byte flag = receiveData[0];
        dadosPacks.setPort(packet.getPort());


        if(flag == Protocol.data) {
            write(receiveData);
            dadosPacks.encRec();
            //APAGAR
            /*sendPacket();
            dadosPacks.encConf();*/
            return;
        }

        if(flag == Protocol.disconnect){
            System.out.println("Desconnected");
            dadosPacks.endCon();
            return;
        }

        if(flag == Protocol.timeRoundTrip){
            dadosPacks.recTimeTrip();
            return;
        }
        
    }

    public void write(byte[] receiveData) throws IOException{

        out.write(receiveData,1,(receiveData.length)-1);
	out.flush();
    }

    public void run() {

        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        while(!dadosPacks.getDie()){
            try {
                //System.out.println("espera");
                cliente.receive(receivePacket);
                trataPacotes(receivePacket);
                //System.out.println("recebido");
            } catch (IOException ex) {
                Logger.getLogger(RecPackets.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(RecPackets.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("morreuRECPACKETS");

    }

}
