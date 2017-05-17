/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.*;
import java.math.*;

/**
 *
 * @author AlbertWu
 */

abstract class Worker implements Comparable<Worker> { //Abstract interface, using Comparable to implement the default comparation
    protected String name;
    protected double salary_rate;
    protected double total_salary;
    
    public Worker() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //Prepared to input
        String s; //Get the input
        while(true) {
            System.out.print("Please input the name of the worker: ");
            name = br.readLine(); //Input
            if(name.matches("[A-Z]{1}"+"[a-z]+") || name.matches("[A-Z]{1}"+"[a-z]*"+"[ ]{1}"+"[A-Z]{1}"+"[a-z]+")) break; //The input should meet the format of names, like Mike or Mike Ken or M
            System.out.println("The name should meet its format!");
        }
        
        while(true) {
            System.out.print("Please input the salary rate of the worker: ");
            s = br.readLine(); //Input
            if(s.matches("[0-9]+") || s.matches("[0-9]+"+".{1}"+"[0-9]+")) break; //The input should be like 5.8 or 5
            System.out.println("Please input the correct format of salary rate!");
        }
        salary_rate = Double.parseDouble(s); //Change the type from String to double
        
        total_salary = 0; //Initial the total salary with 0
    }
    
    abstract public double computePay()  throws IOException; //Expected to implement in derived class
    abstract public String toString(); //Expected to implement in derived class

    @Override
    public int compareTo(Worker t) { //default comparation by name
        return this.name.compareTo(t.name); //bigger than 0 means this is bigger than t
    }
}


class HourlyWorker extends Worker { //Inherit from the abstract base class
    private double pay; //The weekly pay
    
    public HourlyWorker() throws IOException { } //Constuctor
    
    @Override
    public double computePay() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //Prepared to input
        String s; //Get the input
        int hours; //The hours one works a week
        while(true) {
            System.out.print("Please input how many hours does the worker " + name + " work in this week: ");
            s = br.readLine(); //Input
            if(s.matches("[0-9]+")) break; //The input should be like 20 not 20.5
            System.out.println("Please make sure of your choice!");
        }
        hours = Integer.parseInt(s); //Change the type from String to int
        if(hours <= 40) { //An hourly worker get paid the the hourly wage for the actual number of hours worked, of hours is at most 40.
            pay = hours * salary_rate;
        }
        if(hours > 40) { //If the hourly worker worked more than 40 hours, the excess is paid at double rate
            pay = hours * salary_rate + (hours - 40) * salary_rate;
        }
        total_salary += pay;
        BigDecimal b = new BigDecimal(pay); //round-off
        //two decimal places
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    @Override
    public String toString() { //In order to implement the output order using System.out.println(worker)
        BigDecimal b1 = new BigDecimal(salary_rate); //round-off
        BigDecimal b2 = new BigDecimal(total_salary); //round-off
        //two decimal places
        return "HourlyWorker\t" + name + "\t" + b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "\t\t" + b2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
}


class SalariedWorker extends Worker { //Inherit from the abstract base class
    private double pay; //The weekly pay
    
    public SalariedWorker() throws IOException { } //Constructor
    
    @Override
    public double computePay()  throws IOException{
        pay = 40 * salary_rate; //The salaried worker gets paid the hourly wage for 40 hours, no matter what the actual number of hours is
        total_salary += pay;
        BigDecimal b = new BigDecimal(pay); //round-off
        //two decimal places
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    @Override
    public String toString() { //In order to implement the output order using System.out.println(worker)
        BigDecimal b1 = new BigDecimal(salary_rate); //round-off
        BigDecimal b2 = new BigDecimal(total_salary); //round-off
        //two decimal places
        return "SalariedWorker\t" + name + "\t" + b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "\t\t" + b2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}


class SalaryRateComparator implements Comparator<Worker> { //using Comparator to implement the comparation

    @Override
    public int compare(Worker t1, Worker t2) { //Campared by SalaryRate
        return (int)Math.ceil(t1.salary_rate - t2.salary_rate); //bigger than 0 means t1 is bigger than t2
    }

}


class TotalSalaryComparator implements Comparator<Worker> { //using Comparator to implement the comparation

    @Override
    public int compare(Worker t1, Worker t2) { //Campared by TotalSalary
        return (int)Math.ceil(t1.total_salary - t2.total_salary); //bigger than 0 means t1 is bigger than t2
    }

}


public class Project3 { //Main method

    /**
     * @param args the command line arguments
     */    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        String s; //Get the input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //Prepared to input
        while(true) {
            System.out.print("How many workers?: ");
            s = br.readLine(); //Input
            if(s.matches("[1-9]{1}") || s.matches("[1-9]{1}" + "[0-9]+")) break; //The input should be like 5 not 0.4 or 0
            System.out.println("Please make sure of your choice!");
        }
        int num; //The number of how many workers, that is the size of the array
        num = Integer.parseInt(s); //Change the type from String to int
        Worker[] w = new Worker[num]; //Declaration
        for(int i=0; i<num; i++) { //Input the information of all the workers
            while(true) {
                System.out.print("Please input the type of the worker (1-HourlyWorker; 2-SalariedWorker): ");
                s = br.readLine(); //Input
                if(s.matches("[1-2]{1}")) break; //The input should be 1 or 2
                System.out.println("Please make sure of your choice!");
            }
            if("1".equals(s)) w[i] = new HourlyWorker(); //Instantiate as object of HourlyWorker
            else w[i] = new SalariedWorker(); //Instantiate as object of SalariedWorker
        }
        
        while(true) { //Get in the system
            System.out.print("\n1:Get the weekly salary\n2:Get the worker's information\n"
                    + "3:Find a worker\n4:Sort in order\n0:exit\n");
            System.out.print("Your choice: ");
            while(true) {
                s = br.readLine(); //Input
                if(s.matches("[0-4]{1}")) break; //The input should be 0-4
                System.out.print("Please make sure of your choice: ");
            }
            if("1".equals(s)) { //Get the weekly salary
                for(int i=0; i<num; i++) { //Compute in turns
                    System.out.println(w[i].name + " : " + w[i].computePay() + "\n");
                }
            }
            else if("2".equals(s)) { //Get the worker's information
                System.out.println("Type\t\tName\tSalary Rate\tTotal Salary");
                for(int i=0; i<num; i++) { //Use String toString() in each class
                    System.out.println(w[i]);
                }
            }
            else if("3".equals(s)) { //Find a worker
                while(true) {
                    System.out.print("What's the worker's name: ");
                    s = br.readLine(); //Input
                    if(s.matches("[A-Z]{1}"+"[a-z]+") || s.matches("[A-Z]{1}"+"[a-z]*"+"[ ]{1}"+"[A-Z]{1}"+"[a-z]+")) break; //The input should meet the format of names, like Mike or Mike Ken or M
                    System.out.println("The name should meet its format!");
                }
                boolean flag = true; //Mark whether this man exists
                for(int i=0; i<num; i++) {
                    if(s.equals(w[i].name)) { //Match
                        if(flag) System.out.println("Type\t\tName\tSalary Rate\tTotal Salary");
                        flag = false;
                        System.out.println(w[i]); //Use String toString() in each class
                    }
                }
                if(flag) System.out.println(s + " is not your worker!"); //Not existing
            }
            else if("4".equals(s)) {
                System.out.print("\n1:Sort by name\n2:Sort by salary rate\n"
                        + "3:Sort by total salary\n");
                System.out.print("Your choice: ");
                while(true) {
                    s = br.readLine(); //Input
                    if(s.matches("[1-3]{1}")) break; //The input should be 1-3
                    System.out.print("Please make sure of your choice: ");
                }
                if("1".equals(s)) {
                    Arrays.sort(w);
                }
                if("2".equals(s)) {
                    Arrays.sort(w, new SalaryRateComparator());
                }
                if("3".equals(s)) {
                    Arrays.sort(w, new TotalSalaryComparator());
                }
                System.out.println("Type\t\tName\tSalary Rate\tTotal Salary");
                for(int i=0; i<num; i++) { //Use String toString() in each class
                    System.out.println(w[i]);
                }
            }
            else break; //Exit the system
        }
    }

}
