/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.text.DecimalFormat;

/**
 *
 * @author williamwjs
 */
class CalMatrix extends Thread {
    private int row, col; //element in C to be calculated
    
    public CalMatrix(int r, int c) {
        row = r;
        col = c;
    }
    
    @Override
    public void run() { //Will be executed when executing start()
        float tmp = 0; //cal the element
        DecimalFormat df = new DecimalFormat("#.00"); //round-off
        
        for(int i=0; i<Project8.Aj; i++) {
            tmp += Float.parseFloat(Project8.A[row][i]) * Float.parseFloat(Project8.B[i][col]);
        }
        Project8.C[row][col] = df.format(tmp) + ""; //Change into String
    }
}

public class Project8 {

    public static String [] [] A;
    public static String [] [] B;
    public static String [] [] C;
    public static int Aj; //Col of A
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //Prepared to input
        String s; //Get the input
        
        while(true)
        {
            System.out.println("Please input ROW_A, COL_A(The limit is 9*9): ");
            /*while(true) {
                try{
                    s = br.readLine();
                
                    if(s.matches("[0,9]+" + " " + "[0,9]+")) {
                        System.out.println("Wrong Matrix!");
                        s = br.readLine();
                    }
                } catch(IOException ex) {
                    System.out.println("Wrong Matrix!");
                }
            }*/
            s = br.readLine(); //Input
            while(!s.matches("[0-9]+" + " +" + "[0-9]+" + " *")) { //Check: ONLY two elements
                System.out.println("Wrong Matrix!");
                s = br.readLine(); //Input
            }
            String[] tmp = s.split(" +"); //Devide
            int Ai = Integer.parseInt(tmp[0]); //Row of A
            Aj = Integer.parseInt(tmp[1]); //Col of A
            A = new String[Ai][Aj];
            System.out.println("Please input Matrix A: ");
            for(int i=0; i<Ai; i++) {
                s = br.readLine(); //Input
                A[i] = s.split(" +"); //Devide
                if(A[i].length != Aj) { //Check the number of Row elements
                    System.out.println("Wrong Matrix Format!");
                    System.out.println("Re-input:");
                    i = -1;
                }
            }
            
            System.out.println("Please input ROW_B, COL_B(The limit is 9*9): ");
            s = br.readLine(); //Input
            while(!s.matches("[0-9]+" + " +" + "[0-9]+" + " *")) { //Check: ONLY two elements
                System.out.println("Wrong Matrix!");
                s = br.readLine(); //Input
            }
            tmp = s.split(" +"); //Devide
            while(!tmp[0].equals(Aj+"")) { //Check whether the two matrix can times
                System.out.println("ROW_B must be equal to COL_A");
                System.out.println("Re-input:");
                s = br.readLine(); //Input
                while(!s.matches("[0-9]+" + " +" + "[0-9]+")) { //Check: ONLY two elements
                    System.out.println("Wrong Matrix!");
                    s = br.readLine(); //Input
                }
                tmp = s.split(" +"); //Devide
            }
            int Bi = Integer.parseInt(tmp[0]); //Row of B
            int Bj = Integer.parseInt(tmp[1]); //Col of B
            B = new String[Bi][Bj];
            System.out.println("Please input Matrix B: ");
            for(int i=0; i<Bi; i++) {
                s = br.readLine(); //Input
                B[i] = s.split(" +"); //Devide
                if(B[i].length != Bj) { //Check the number of Row elements
                    System.out.println("Wrong Matrix Format!");
                    System.out.println("Re-input:");
                    i = -1;
                }
            }
            
            C = new String[Ai][Bj];
            CalMatrix [] [] cm = new CalMatrix[9][9]; //Declare class object
            
            for(int i = 0; i < Ai; i++)
                for(int j = 0; j < Bj; j++) {
                    try {
                        cm[i][j] = new CalMatrix(i, j); //Create the class object
                        cm[i][j].start(); //Start the thread
                        cm[i][j].join(); //Wait for all the sub-thread to finish the task
                    } catch (InterruptedException ex) { }
                }
            
            System.out.println("With A * B, here is Matrix C: ");
            for(int i = 0; i < Ai; i++) //Output C
                for(int j = 0; ; j++) {
                    if(j == Bj-1) {
                        System.out.println(C[i][j]);
                        break;
                    }
                    System.out.print(C[i][j] + "\t");
                }
            
            do { //Judge whether to continue the program or not
                System.out.println("Continue?(y/n)");
                s = br.readLine();
            } while(!(s.equals("y") || s.equals("n")));
            if(s.equals("n")) break;
        }
    }
}
