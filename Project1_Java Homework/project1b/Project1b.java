/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package project1;

import java.io.*;
import java.util.*; //use the class Calendar and Scanner

/**
 *
 * @author AlbertWu
 */
class MyDate {
    public int year, month, day; //class data
    
    public MyDate() {} //Construct method
    
    public void setDate() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //Prepared to input
        String s; //accept the input
        while(true) { //If the format is not like a date, you have to reinput once again
            System.out.print("The date should be like the example 9/15/2004:");
            s = br.readLine();
            String s1[] = s.split("/"); //Devide the string into 3 parts by the '/' that stand for month, day, year; 
                                        //If there are not 3 parts, you have to reinput, which will be determined in the next line.
            if(s1.length != 3) continue;
            if(s1[0].matches("[0-9]{1,2}") && s1[1].matches("[0-9]{1,2}") && s1[2].matches("-?"+"[0-9]+")) { //Judge if the input is OK with the format of date for the first time
                month = Integer.parseInt(s1[0]); //Change the string to int
                day = Integer.parseInt(s1[1]);
                year = Integer.parseInt(s1[2]);
                if(month>=1 && month<=12) { //Judge if the format is right further.
                    if(month==1 || month==3 || month==5 || month== 7 || month==8 || month==10 || month==12) {
                        if(day<=31 && day>=1) break;
                    }
                    else if(month == 2) {
                        if(day<=28 && day>=1) break;
                    }
                    else {
                        if(day<=30 && day>=1) break;
                    }
                }
            }
        }
    }
    
    public boolean laterThan(MyDate dateB) { //Compare from year to day
        if(year > dateB.year) return true;
        else if(year == dateB.year) {
            if(month > dateB.month) return true;
            else if(month == dateB.month) {
                if(day > dateB.day) return true;
            }
        }
        return false;
    }
    
    public static long dayDifference() throws IOException {
        MyDate start = new MyDate(); //Only a declaration
        MyDate end = new MyDate(); //Only a declaration
        System.out.print("Please enter the start date! ");
        start.setDate(); //Get the start date
        System.out.print("Please enter the end date that is later than the start! ");
        end.setDate(); //Get the end date
        while(!(end.laterThan(start))) { //If the end date is earlier than the start, reinput!
            System.out.println("The end date should be later than the start!");
            end.setDate();
        }
        Calendar startDate = Calendar.getInstance(); //Make a object of class Calendar
        Calendar endDate = Calendar.getInstance(); ////Make a object of class Calendar
        startDate.set(start.year, start.month, start.day, 0, 0, 0); //Init the start date
        endDate.set(end.year, end.month, end.day, 0, 0, 0); //Init the end date
        long daysBetween = 0;
        while(startDate.before(endDate)) { //Calculate the days between the two days by loop
            startDate.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween-1;
    }
    
}

public class Project1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in); //Prepared to input
        MyDate date = new MyDate(); //Only a declaration
        int a;
        while(true) { //Keep functioning
            while(true) { //Input the demand to decide which function to use
                System.out.print("1 stands for judging if date A is later than date B\n2 is calculating the days between date A and B\n0 means to exit\nChoose a function:");
                a = in.nextInt();
                if(a>=0 && a<=2) break;
                else System.out.println("Please input a number between 0 and 2!");
            }
            if(a == 1) { //Function: Judging if date A is later than date B
                System.out.print("Please enter the first date DateA! ");
                date.setDate(); //Get the date A
                MyDate date2 = new MyDate(); //Only a declaration
                System.out.print("Please enter the second date DateB! ");
                date2.setDate(); //Get the date B
                if(date.laterThan(date2)) System.out.println("Date1 is later than date2!"); //Result
                else System.out.println("Date1 is NOT later than date2!");
            }
            else if(a == 2) System.out.println(MyDate.dayDifference()); //Function: calculate the days between date A and B
            else break; //exit the date program
        }
    }
}
