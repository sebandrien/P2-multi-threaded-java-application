package P_2;

import java.io.IOException;
import java.util.Random;

import P_2.ABankingSimulator.Account;

public class Auditor implements Runnable {
	
	private static int n = 0;
	private static Random generator = new Random();
	private static Random sleeptime = new Random();
	private Account sharedLocation;
	String threadname;
	
	
	public Auditor(Account shared, String name)
	{
		sharedLocation = shared;
		threadname = name;
	}
	
	public void run()
	{
		
		while(true)
		{
			try {
				sharedLocation.audit(n,threadname);
				Thread.sleep(10000);
		        }
		     catch (Exception exception) {
		        exception.printStackTrace();
		      }     	
		}
			
	}
}
