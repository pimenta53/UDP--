/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package udpcliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class ConfirmPackets implements Runnable{
        
    private Dados dadosPacks;
    private DatagramSocket servidor;    
    private InetAddress IPAddress;

    public ConfirmPackets(Dados dados) throws UnknownHostException{
        this.dadosPacks = dados;
        servidor=dadosPacks.getClienteSocket();
        IPAddress = InetAddress.getByName(dadosPacks.getIp());
    }

    public void sendPacket(byte[] sendData) throws IOException{
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,IPAddress, dadosPacks.getPort());
	servidor.send(sendPacket);
    }
    
    public synchronized void manageCon() throws IOException{
        byte[] sendData = null;

        if(dadosPacks.getRetryCon()){
            dadosPacks.setRetryCon(false);
            sendData = new byte[]{Protocol.connect}; //enviar pacotes ACK roundTrip
            sendPacket(sendData);
            System.out.println("Try connection...");
            return;
        }
        
        if(!dadosPacks.getTConf()){ //enviar pacotes de ACK
            dadosPacks.encConf();
            sendData = new byte[]{Protocol.ackData}; //enviar pacotes de ACK
            sendPacket(sendData);
            //System.out.println("Confirmado");
        }

        if(dadosPacks.getTimeTrip()){ 
            dadosPacks.timeTripRespond();
            sendData = new byte[]{Protocol.ackTimeRoundTrip}; //enviar pacotes ACK roundTrip
            sendPacket(sendData);
            System.out.println("RoundTripAckSend");
        }
    
    }

    public void startCon(){
        byte[] sendData;

        sendData = new byte[]{Protocol.connect};
        try {
            sendPacket(sendData); //iniciar ligação
        } catch (IOException ex) {
            Logger.getLogger(ConfirmPackets.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void run() {
        //startCon();
        //System.out.println("Connected");
        
        while(!dadosPacks.getDie()){
            if(!dadosPacks.getTConf() || dadosPacks.getTimeTrip() || dadosPacks.getRetryCon()) try {
                manageCon();
            } catch (IOException ex) {
                Logger.getLogger(ConfirmPackets.class.getName()).log(Level.SEVERE, null, ex);
            }
            else dadosPacks.espera();
        }
        System.out.print("morreu");
    }

}
