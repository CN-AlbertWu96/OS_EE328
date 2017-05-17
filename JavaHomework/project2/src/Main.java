/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package project2;
import java.io.*;
import java.math.*; //round-off

/**
 *
 * @author AlbertWu
 */
abstract class BankAccount {
    private static int accountNumber = 0; //static viariable shared by all the objects
    private int accountNum; //Account Number, unique for each user
    double balance; //the residue of the account

    public BankAccount() {
        accountNum = ++accountNumber; //get the newest account number
        balance = 0;
    }

    public double checkBalance() {
        return balance;
    }

    public long checkAccountNum() {
        return accountNum;
    }

    public static long checkUsers() {
        return accountNumber;
    }

    abstract public void deposit(double money); //wait to be implement by the sons
    abstract public boolean withdraw(double money);
    abstract public void endMonth();
    abstract public void print();
}


class SavingAccount extends BankAccount{ //need to implement some abstract functions
    private int transactionNum; //The number of the operations, such as deposit or withdraw

    public SavingAccount(double money) {
        balance = money;
        transactionNum = 0;
    }

    @Override //override the abstract function
    public void deposit(double money) {
        balance += money;
        transactionNum++;
    }

    @Override
    public boolean withdraw(double money) {
        if(transactionNum > 3) { //After withdrawing, the balance must afford the fee
            if(money > ((balance-money)*1.05-5)) {
                System.out.println("Sorry! Your saving account don't have enough money!");
                return false;
            }
        }
        else { //The balance should be enough to withdraw
            if(money > balance) {
                System.out.println("Sorry! Your saving account don't have enough money!");
                return false;
            }
        }
        balance -= money;
        transactionNum++;
        return true;
    }

    @Override
    public void endMonth() {
        balance += 0.05 * balance; //get the interest
        if(transactionNum > 3) balance -= 5; // pay the fee
        transactionNum = 0; //get to the next month
    }

    @Override
    public void print() {
        BigDecimal b = new BigDecimal(checkBalance()); //round-off
        //two decimal places
        System.out.println("my saving number is " + checkAccountNum() + ", my balance is " + b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
    }
}

class CheckingAccount extends BankAccount {
    public CheckingAccount(double money) {
        balance = money;
    }

    @Override
    public void deposit(double money) {
        balance += money;
    }

    @Override
    public boolean withdraw(double money) {
        if(balance <=50) { //the balance must afford the fee
            if(money > balance-2) {
                System.out.println("Sorry! Your saving account don't have enough money!");
                return false;
            }
        }
        else { //The balance should be enough to withdraw
            if(money > balance) {
                System.out.println("Sorry! Your saving account don't have enough money!");
                return false;
            }
        }
        balance -= money;
        return true;
    }

    @Override
    public void endMonth() {
        if(balance < 50) balance -= 2; //pay the fee
    }

    @Override
    public void print() {
        BigDecimal b = new BigDecimal(checkBalance()); //round-off
        //two decimal places
        System.out.println("my checking number is " + checkAccountNum() + ", my balance is " + b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
    }
}

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //Prepared to input
        String s; //accept the input

        BankAccount[] bank = new BankAccount[10]; //base class definition
        for(int i=0;i<5;i++)
            bank[i]=new CheckingAccount(50); //derived class definition
        for(int i=5;i<10;i++)
            bank[i]=new SavingAccount(100); //derived class definition

        for(int i=0;i<10;i++){
            bank[i].withdraw(20);
            bank[i].deposit(10);
            //bank[i].withdraw(10);
        }

        for(int i=0;i<10;i++){
            bank[i].endMonth();
            bank[i].print();
        }

        while(true) { //get into the bank system
            System.out.println("Welcome to the bank system in the next month! Please choose what you want to do!");
            System.out.println("1: choose a bank account\n0: exit");
            System.out.print("Your choice:");
            while(true) { //Judge whether the input is valid
                s = br.readLine();
                if(s.matches("[0-1]{1}")) break; //Only one zero-or-one number
                System.out.print("Please input a number between 0 and 1!\nYour choice:");
            }
            if("0".equals(s)) break; //exit the bank system
            else { //do more
                int accNum; // the account number
                double money; //the money of deposit or withdraw
                System.out.print("Please input the bank account:");
                while(true) { //Judge whether the input is valid
                    s = br.readLine();
                    if(s.matches("[0-9]+")) { //A few from-zero-to-nine numbers
                        accNum = Integer.parseInt(s); //change the String to Int
                        if(accNum > BankAccount.checkUsers()) { //account not exist
                            System.out.print("No such account!\nPlease check and input again:");
                            continue;
                        }
                        break;
                    }
                    System.out.print("Please input numbers!\nInput again:");
                }
                while(true) { //the bottom menu
                    System.out.print("What's next?\n1: Deposit\n2: Withdraw\n3: Balance\n0: back\nYour choice:");
                    while(true) { //Judge whether the input is valid
                        s = br.readLine();
                        if(s.matches("[0-3]{1}")) break; //Only one from-zero-to-three number
                        System.out.print("Please input a number between 0 and 2!\nYour choice:");
                    }
                    if("0".equals(s)) { //back to the previous menu, and the end of month
                        for(int i=0;i<10;i++){
                            bank[i].endMonth();
                            bank[i].print();
                        }
                        break;
                    }
                    else if("3".equals(s)) { //tell the balance of the current account
                        bank[accNum-1].print();
                    }
                    else if("1".equals(s)) { //deposit
                        while(true) { //Judge whether the input is valid
                            System.out.print("How much do you want to deposit:");
                            s = br.readLine();
                            if(s.matches("[0-9]+" + ".?" + "[0-9]{0,2}")) { //must be decimal, up to two decimal places, or interger
                                money = Double.parseDouble(s); //change the String to Double
                                break;
                            }
                            System.out.print("Input invalid! Please input the money again!\nInput again:");
                        }
                        bank[accNum-1].deposit(money); //deposit-ing
                    }
                    else {
                        while(true) { //Judge whether the input is valid
                            System.out.print("How much do you want to withdraw:");
                            s = br.readLine();
                            if(s.matches("[0-9]+" + ".?" + "[0-9]{0,2}")) { //must be decimal, up to two decimal places, or interger
                                money = Double.parseDouble(s); //change the String to Double
                                break;
                            }
                            System.out.print("Input invalid! Please input the money again!\nInput again:");
                        }
                        bank[accNum-1].withdraw(money); //withdraw-ing
                    }
                }
            }

        }

    }
}
