package P_2;

import java.io.IOException;
import java.util.Random;

import P_2.ABankingSimulator.Account;

public class Withdrawl implements Runnable {
	
	private static int MAX_WITHDRAWL = 99;
	private static Random generator = new Random();
	private static Random sleeptime = new Random();
	private Account sharedLocation;
	
	String threadname;
	
	public Withdrawl(Account shared, String name)
	{
		sharedLocation = shared;
		threadname = name;
	}
	
	public void run()
	{
		
		while(true)
		{
			try {
				sharedLocation.withdraw(generator.nextInt(MAX_WITHDRAWL-1+1)+1, threadname);
				Thread.sleep(generator.nextInt(100));
				Thread.sleep(sleeptime.nextInt(100-1+1)+1);
				Thread.sleep(500);
		        }
		     catch (Exception exception) {
		        exception.printStackTrace();
		      }		
		}
				
	}
}
