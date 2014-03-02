package sim;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.List;

import base.OlympicException;
import client.Tablet;

public class TabletSimulator {
	
	private static List<Tablet> createTablets(int n, String obelixHost) throws OlympicException
	{
		List<Tablet> tablets = new ArrayList<Tablet>();
		for(int i = 0; i < n; i++)
		{
			tablets.add(Tablet.deployTablet(new String[]{obelixHost}));
		}
		return tablets;
	}
	
	
	private static void test1(List<Tablet> tablets) throws InterruptedException, IOException{
		List<Thread> threads = new ArrayList<Thread>();
		
		for(Tablet tablet : tablets){
			TabletTester tester = new TabletTester(tablet);
			Thread thread = new Thread(tester);
			thread.start();
			threads.add(thread);
		}
	
		for(Thread thread : threads){
			thread.join();
		}
		
	}
	
	public static void main(String[] args) throws OlympicException, IOException, NotBoundException{
		String obelixHost = (args.length < 1) ? null : args[0];
		int numTablets = (args.length < 2) ? 3 : Integer.parseInt(args[1]);
		List<Tablet> tablets = createTablets(numTablets, obelixHost);
		try {
			test1(tablets);
			for(Tablet tablet : tablets) {
				tablet.shutDown();
			}
			System.exit(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
