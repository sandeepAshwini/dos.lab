package server;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import base.OlympicException;

public class RegistryService {
	
	public RegistryService(){}
	
	public void startRegistryService() throws IOException{
		Runtime.getRuntime().exec(new String[]{"rmiregistry", "-J-Djava.rmi.server.useCodebaseOnly=false"});
	}
	
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
	
	public void setupLocalRegistry() throws IOException{
		this.startRegistryService();
		String ipAddress = this.getLocalIPAddress();
		try{
			Thread.currentThread().sleep(200);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
    	System.out.println("Registry Service started at : " + ipAddress);
		
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
