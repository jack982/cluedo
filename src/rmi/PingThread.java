/*
 * PingThread.java
 * Created on 14-mar-2006
 *
 * author: Jacopo Penazzi
 */
package rmi;


import gamedata.Player;

import interfaces.Configuration;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.Iterator;

import sun.security.krb5.Config;

/**
 * @author Jacopo
 * 
 * 
 */
public class PingThread extends Thread {

    private GameController controller;
    private IController remoteController;
    private String currentIP;
   
    
    public PingThread(GameController gc, String currentIP){
       // setDaemon(true);
        super();
        this.currentIP = currentIP;
        controller = gc;
       
    } 
    
           
    public void setCurrentIP(String ip){
        currentIP = ip;
    }
    
       
    
    public void run(){
        System.setSecurityManager(new RMISecurityManager());
        try {
            remoteController = (IController)Naming.lookup("rmi://"+currentIP+"/"+Configuration.CLIENT_SERVICE_NAME);
            
            while(true){
                sleep(5000);
                remoteController.ping();
            }
         } catch (RemoteException e) {
             // riconfigura lo stato della partita   
             controller.alarmDeadPlayer(currentIP);
             Thread.currentThread().interrupt();
             e.printStackTrace();
         } catch (InterruptedException e) {
             e.printStackTrace();
         } catch (MalformedURLException e) {
             System.err.println("MALFORMED URL EXCEPTION in PINGTHREAD");
             e.printStackTrace();
         } catch (NotBoundException e) {
             // riconfigura lo stato della partita
             controller.alarmDeadPlayer(currentIP);
             Thread.currentThread().interrupt();
             e.printStackTrace();
         }
        
    }
    
    
}
