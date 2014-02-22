package server;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import base.Event;


public class Cacophonix implements CacophonixInterface {
	
//	private Obelix obelixInstance;
	
	public Cacophonix() {
//		obelixInstance = new Obelix();
	}

	public void updateScoresAndTallies(Event simulatedEvent) throws RemoteException {
//		obelixInstance.updateScoresAndTallies(simulatedEvent);
	}
	
	public static void main(String args[])throws Exception {
        
		// Bind the remote object's stub in the registry
    	Registry registry = null;
    	String SERVER_NAME = "Cacophonix";
    
        CacophonixInterface stub = (CacophonixInterface) UnicastRemoteObject.exportObject(new Cacophonix(), 0);
        
        try {        	
            registry = LocateRegistry.getRegistry();
            registry.bind(SERVER_NAME, stub);
            System.err.println("Cacophonix ready");
        } catch (Exception e) {
            System.err.println("Cacophonix exception: " + e.toString());
            e.printStackTrace();
            if(registry != null)
        	{
            	registry.rebind(SERVER_NAME, stub);
        	}            
        }
	}	
}
