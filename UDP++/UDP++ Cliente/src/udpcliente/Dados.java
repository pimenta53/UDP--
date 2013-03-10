/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package udpcliente;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class Dados {


    private String ip;
    private Boolean portaAlterada;
    private int port;
    private int nRec;
    private int nConf;
    private Boolean die;
    private Boolean wDie;
    private Boolean timeTripe;
    private DatagramSocket clientSocket;
    private Boolean tConf;
    private Boolean retryCon;

    private int nRT;

    private DatagramPacket receivePacket;
    private Boolean ler;

    public Dados(String ip, int p, DatagramSocket cl){
        this.ip=ip;
        this.port=p;
        nRec=0;
        nConf=0;
        die=false;
        timeTripe=false;
        portaAlterada=false;
        clientSocket = cl;
        ler=false;
        tConf=false;
        nRT=0;
        retryCon=true;
    }

    //tentar reconecção
    public Boolean getRetryCon(){return retryCon;}
    public void setRetryCon(Boolean b){retryCon=b;}
    public synchronized void retryCon(){
        retryCon=true;
        notifyAll();
    }

    //Testar conecção



    public String getIp(){return ip;}
    public int getPort(){return port;}

    public void setIp(String ip){this.ip=ip;}
    public void setPort(int port){this.port=port;}

    public synchronized void encRec(){
        nRec++;
        tConf=false;
        notifyAll();
    }

    public synchronized void acordarTodos(){
        notifyAll();
    }

    public synchronized void espera(){
        try {
            wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(Dados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public synchronized void encConf(){
        if(nConf>=nRec) {
            tConf=true;
            if(wDie==true) {die=true; notifyAll();}
        }
        else{nConf++;}
    }

    public synchronized int getRec(){return nRec;}
    public synchronized int getConf(){return nConf;}

    public void decRec(){nRec--;}
    public void decConf(){nConf--;}

    public synchronized Boolean getDie(){return die;}

    public synchronized void endCon(){
        wDie=true;
    }

    public synchronized Boolean getTimeTrip(){return timeTripe;}
    public synchronized void recTimeTrip(){
        timeTripe=true;
        nRT++;
        notifyAll();
    }
    public void timeTripRespond(){
        timeTripe=false;
    }

    public Boolean getPorteAlterada(){return portaAlterada;}
    public void newPort(){portaAlterada=true;}

    public DatagramSocket getClienteSocket(){return clientSocket;}

    public void escrever(DatagramPacket receivePacket){
        this.receivePacket = receivePacket;
        ler = true;
    }

    public DatagramPacket ler(){
        ler=false;
        return receivePacket;
    }
    public Boolean getLer(){return ler;}

    public Boolean getTConf(){return tConf;}

    public int getNRT(){return nRT;}

}