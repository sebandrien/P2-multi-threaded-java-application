package P_2;

import java.io.IOException;
import java.util.Random;

import P_2.ABankingSimulator.Account;

public class Depositor implements Runnable {
	
	private static int MAX_DEPOSIT = 500;
	private static Random generator = new Random();
	private static Random sleeptime = new Random();
	private Account sharedLocation;
	
	String threadname;
	
	public Depositor(Account shared, String name)
	{
		sharedLocation = shared;
		threadname = name;
	}
	
	public void run()
	{
		
		while(true)
		{
			try {
				sharedLocation.deposit(generator.nextInt(MAX_DEPOSIT-1+1)+1, threadname);
				Thread.sleep(generator.nextInt(3000));
				Thread.sleep(sleeptime.nextInt(1500-1+1)+1);
				Thread.sleep(100);
		        }
		     catch (Exception exception) {
		        exception.printStackTrace();
		      }
		      		
		}
				
	}
}
