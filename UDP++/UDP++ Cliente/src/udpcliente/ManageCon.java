/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package udpcliente;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class ManageCon implements Runnable{
    
    private Dados dados;
    private int pRec;
    private int nTent;

    public ManageCon(Dados d){
        dados=d;
        pRec=0;
        nTent=0;
    }

    public void testCon(){
        if(nTent>2) {
            System.out.println("Conecção falhou...");
            dados.endCon();
            dados.acordarTodos();
        }
        if(dados.getRec()==0) {
            dados.setRetryCon(true);  // voltar a tentar ligar ao servidor
            nTent++;
            return;
        } 
        if(dados.getRec()==pRec){
            //verificar con
            nTent++;
        }
        pRec=dados.getRec();
    }

    public void run() {
        System.out.println("Começou a manutenção");

        while(!dados.getDie()){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ManageCon.class.getName()).log(Level.SEVERE, null, ex);
            }
            testCon();
        }

        System.out.println("morreuMANAGECON");

    }

}
