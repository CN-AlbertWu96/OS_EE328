/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package Project1;

import java.util.*;

public class project1 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while(true) {
            System.out.print("Enter table size 1-9, 0 to exit:");
            int a = in.nextInt(); //When user needs to input sth on the keyboard,
            //numbers are OK, while letters and others will lead to the exception.
            if(a == 0) break; //exit the program
            if(a > 9 || a < 0) { //Reinput the numbers
                System.out.println("please enter a number in the range 0-9");
                continue;
            }
            for(int i = 0; i < a; i++) { //print the number square line by line
                if(i % 2 == 0) { //odd-numbered line where the numbers increase from left to right
                    for(int j = 0; j < a-1; j++) {
                        System.out.print(a * i + 1 + j + "\t");
                    }
                    System.out.println(a * i + a);
                }
                else { //even-numbered line where the numbers decrease from left to right
                    for(int j = 0; j < a-1; j++) {
                        System.out.print((i + 1) * a - j + "\t");
                    }
                    System.out.println(a * i + 1);
                }
            }
        }
    }
}
