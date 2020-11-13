/*
 * MonitorThread.java
 * Created on 20-mar-2006
 *
 * author: Jacopo Penazzi
 */
package rmi;

import gamedata.Player;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Jacopo
 * 
 * 
 */
public class MonitorThread extends Thread {

    private GameCreator gameCreator;
    private IController remoteController;
    private String currentIP;
   
    
    public MonitorThread(GameCreator gc, String currentIP){
       // setDaemon(true);
        super();
        this.currentIP = currentIP;
        gameCreator = gc;
    } 
    
           
    public String getCurrentIP(){
        return currentIP;
    }
    
    
    public void run(){
        System.setSecurityManager(new RMISecurityManager());
        try {
            remoteController = (IController)Naming.lookup("rmi://"+currentIP+"/Controller");
            
            while(true){
                sleep(5000);
                remoteController.ping();
            }
         } catch (RemoteException e) {
             gameCreator.alarmDeadPlayer(currentIP);    
             Thread.currentThread().interrupt();
             e.printStackTrace();
         } catch (InterruptedException e) {
             e.printStackTrace();
         } catch (MalformedURLException e) {
             System.err.println("MALFORMED URL EXCEPTION in MONITORTHREAD");
             e.printStackTrace();
         } catch (NotBoundException e) {
             gameCreator.alarmDeadPlayer(currentIP);
             Thread.currentThread().interrupt();
             e.printStackTrace();
         }
         
    }
    
    
    
    
    
}
