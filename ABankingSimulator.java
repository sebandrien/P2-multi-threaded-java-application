package P_2;

import java.util.concurrent.ExecutorService;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.DateFormat;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;
import java.util.Date;

public class ABankingSimulator {

    public static final int MAX_AGENTS = 16;

    public static class Account {

        int balance;
        int transactionNum = 1;
        int counter = 0;
        int k;
        int n = 0;

        Lock accessLock;
        Condition sufficientFunds;

        public Account() {

            balance = 0;
            accessLock = new ReentrantLock();
            sufficientFunds = accessLock.newCondition();
         
        }

        public static int getRandomNumber(int min, int max) {
            return (int)((Math.random() * (max - min)) + min);
        }

        public void deposit(int i, String threadname) {
            accessLock.lock();
            try {
                if (i > 350) {
                    balance += i; 
                    System.out.printf("%s deposits $%d\t\t\t\t\t\t(+)Balance is $%d\t\t " + transactionNum + "\n", threadname, i, balance);
                    System.out.printf("\n");
                    System.out.printf("***Flagged Transaction - Deposit %s made a deposit in excess of $350.00 USD - See Flagged Transaction Log.", threadname);
                    System.out.printf("\n");

                    printFileDeposit(i, threadname);

                    transactionNum++;
                    sufficientFunds.signalAll();
                    counter++;
                    
                } else {
                    balance += i;
                    System.out.printf("%s deposits $%d\t\t\t\t\t\t(+)Balance is $%d\t\t " + transactionNum + "\n", threadname, i, balance);
                    transactionNum++;
                    counter++;
                    sufficientFunds.signalAll();
                    k = getRandomNumber(1, 5); //generating random token

                }
            } finally {
                accessLock.unlock();
            }

        }

        public void withdraw(int i, String threadname) {
            accessLock.lock();
            try {

                if (balance > i && i < 75) {
                    balance -= i;
                    System.out.printf("\t\t\t\t%s withdrawls $%-3d\t(-)Balance is $%-3d\t\t " + transactionNum + "\n", threadname, i, balance);
                    transactionNum++;
                    counter++;
                } else if (i > 75 && balance > i) {
                    balance -= i; 
                    System.out.printf("\t\t\t\t%s withdrawls $%-3d\t(-)Balance is $%-3d\t\t " + transactionNum + "\n", threadname, i, balance);
                    System.out.printf("\n");
                    System.out.printf("\t\t\t\t***Flagged Transaction - Withdrawl %s made a withdrawl in excess of $75.00 USD - See Flagged Transaction Log.", threadname);
                    System.out.printf("\n");

                    printFileWithdrawal(i, threadname);
                    transactionNum++;
                    counter++;


                } else {
                    while (balance < i) {
                        System.out.printf("\t\t\t\t%s withdrawls $%-3d\t(******)WITHDRAWL BLOCKED - INSUFFICENT FUNDS!!!\n", threadname, i);
                        sufficientFunds.await();

                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                accessLock.unlock();
            }

        }

        private void printFileWithdrawal(double i, String threadname) {
            Date date = new Date();
            DateFormat formatter = new SimpleDateFormat("DD/MM/YY HH:MM:SS z");

            formatter.setTimeZone(TimeZone.getTimeZone("EST"));

            try {
                FileWriter myWriter = new FileWriter("transactions.txt", true);
                myWriter.write(threadname + " issued withdrawal of $" + i + " at: " + formatter.format(date) + " Transaction Number: " + transactionNum);
                myWriter.write("\n");
                myWriter.write("\n");
                myWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }


        }

        private void printFileDeposit(double i, String threadname) {
            
            Date date = new Date();
            DateFormat formatter = new SimpleDateFormat("DD/MM/YY HH:MM:SS z");

            formatter.setTimeZone(TimeZone.getTimeZone("EST"));

            try {
                FileWriter myWriter = new FileWriter("transactions.txt", true);
                myWriter.write(threadname + " issued deposit of $" + i + " at: " + formatter.format(date) + " Transaction Number: " + transactionNum);
                myWriter.write("\n");
                myWriter.write("\n");
                myWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        public void audit(int i, String threadname) throws InterruptedException {
            accessLock.lock();

            try {
                if (k == 3) { 
                    System.out.printf("\n\n");
                    System.out.printf("\t ***************************** Auditor finds current account balance to be: $" + balance + "     Number of transactions since last audit is: " + counter + " ******************************** \t");
                    System.out.printf("\n\n");
                    counter = 0;
                }

            } finally {
                accessLock.unlock();
            }

        }
    }

    public static void main(String[] args) {

        ExecutorService application = Executors.newFixedThreadPool(MAX_AGENTS);
        Account sharedLocation = new Account();

        try {

            System.out.printf("Deposit Agents\t\t\tWithdrawl Agents \t\tBalance \t\t\tTransaction Number\n");
            System.out.printf("---------------\t\t\t------------------\t\t------------------\t\t------------------\n");

            Runnable d1 = new Depositor(sharedLocation, "Agent DT1");
            Runnable d2 = new Depositor(sharedLocation, "Agent DT2");
            Runnable d3 = new Depositor(sharedLocation, "Agent DT3");
            Runnable d4 = new Depositor(sharedLocation, "Agent DT4");
            Runnable d5 = new Depositor(sharedLocation, "Agent DT4");

            Runnable w1 = new Withdrawl(sharedLocation, "Agent WT1");
            Runnable w2 = new Withdrawl(sharedLocation, "Agent WT2");
            Runnable w3 = new Withdrawl(sharedLocation, "Agent WT3");
            Runnable w4 = new Withdrawl(sharedLocation, "Agent WT4");
            Runnable w5 = new Withdrawl(sharedLocation, "Agent WT5");
            Runnable w6 = new Withdrawl(sharedLocation, "Agent WT6");
            Runnable w7 = new Withdrawl(sharedLocation, "Agent WT7");
            Runnable w8 = new Withdrawl(sharedLocation, "Agent WT8");
            Runnable w9 = new Withdrawl(sharedLocation, "Agent WT9");
            Runnable w10 = new Withdrawl(sharedLocation, "Agent WT10");

            Runnable a1 = new Auditor(sharedLocation, "A1");

            application.execute(w1);
            application.execute(d1);
            application.execute(d2);
            application.execute(d4);
            application.execute(d5);

            application.execute(w2);
            application.execute(d3);
            application.execute(w3);
            application.execute(w4);
            application.execute(w5);
            application.execute(w6);
            application.execute(w7);
            application.execute(w8);
            application.execute(w9);
            application.execute(w10);
            application.execute(a1);

        } 
        catch (Exception exception) {
            exception.printStackTrace();
        }

        application.shutdown();
    }

}
