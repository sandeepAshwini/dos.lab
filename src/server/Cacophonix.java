package server;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Cacophonix implements Server{

	@Override
	public String sayHello() throws RemoteException {
		return "Hello from Cacophonix";
	}
	

public static void main(String args[]) {
        
        try {
            Cacophonix obj = new Cacophonix();
            Server stub = (Server) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Hello", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
	
}
