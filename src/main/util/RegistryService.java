package util;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import base.OlympicException;

/**
 * A util class to create a registry service with the 
 * required configuration parameters to be used by the servers.
 * @author aravind
 *
 */
public class RegistryService {
	
	public RegistryService(){}
	
	/**
	 * Creates the registry service.
	 * @throws IOException
	 */
	public void startRegistryService() throws IOException{
		Runtime.getRuntime().exec(new String[]{"rmiregistry", "-J-Djava.rmi.server.useCodebaseOnly=false"});
	}
	
	/**
	 * Returns the localIPAddress to be used to configure servers and clients.
	 * @return String
	 * @throws SocketException
	 */
	public String getLocalIPAddress() throws SocketException{
		Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
		while(networkInterfaces.hasMoreElements()){
		    NetworkInterface networkInterface = (NetworkInterface) networkInterfaces.nextElement();
		    Enumeration<InetAddress> networkInterfaceAddresses = networkInterface.getInetAddresses();
		    while(networkInterfaceAddresses.hasMoreElements()) {
		        InetAddress networkInterfaceAddress = (InetAddress) networkInterfaceAddresses.nextElement();
		        if (!networkInterfaceAddress.isLoopbackAddress() && !(networkInterfaceAddress instanceof Inet6Address)){
		        	return networkInterfaceAddress.getHostAddress();
		        }
		    }
		 }
		return null;
	}
	
	/**
	 * Sets up the local registry service.
	 * @throws IOException
	 */
	public void setupLocalRegistry() throws IOException{
		this.startRegistryService();
		String ipAddress = this.getLocalIPAddress();
		try{
			Thread.sleep(200);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
    	System.err.println("Registry Service started at : " + ipAddress);
		
	}
	
	
	public static void main(String[] args) throws OlympicException {
		try {
			RegistryService service = new RegistryService();
			service.setupLocalRegistry();
			
		} catch (UnknownHostException e) {
			throw new OlympicException("Registry service could not be instantiated.", e);
		} catch (IOException e) {
			throw new OlympicException("Registry service could not be instantiated.", e);
		} 
	}
}
