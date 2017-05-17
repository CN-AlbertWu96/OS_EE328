/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.ArrayList;
/**
 *
 * @author albert
 */
public class SimpleShell {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String commandLine, pwd; //commandLine is the command user inputs; pwd is the directory
        ArrayList<String> history = new ArrayList<String>(); //record the history of user input
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        pwd = System.getProperty("user.dir"); //Get the current directory
        
        while(true) {
            try {
                System.out.print("wh>");
                commandLine = console.readLine(); //Input the command
                
                if(commandLine.equals("")) continue; //No command
                if(commandLine.equals("exit")) break; //Exit the shell

                if(commandLine.substring(0, 1).equals("!")) { //execute historical command
                	if(commandLine.substring(1, 2).equals("!")) { //Error message
                		if(history.size() == 0) {
                			System.out.println("No history");
                			continue;
                		}
                		commandLine = history.get(history.size()-1)
                				+ commandLine.substring(2, commandLine.length()); //The former and supplement command
                		System.out.println(commandLine);
                	}
                	else if(commandLine.substring(1, commandLine.length()).matches("[0-9]+")) {
                		int i = Integer.parseInt(commandLine.substring(1, commandLine.length()));
                		if(history.size() <= i) { //Error message
                			System.out.println("No such history");
                			continue;
                		}
                		commandLine = history.get(i); //The historical message
                		System.out.println(commandLine);
                	}     		
                }

                if(history.size() == 0 || !commandLine.equals(history.get(history.size()-1)))
                	history.add(commandLine); //if not equal to the last one, record
                
                String[] tokens = commandLine.split(" +"); //divide
                
                if(tokens[0].equals("history")) { //Check the command history
                	for(int i = 0; i < history.size(); i++) {
                            System.out.println(i + " " + history.get(i));
                	}
                	continue;
                }
                
                if(tokens[0].equals("cd")) { //Change the directory
                	if(tokens[1].equals("..")) { //Get back
                		int index = pwd.lastIndexOf("/"); //Get the last occurrence of "/"
                		if(index == 0) { //When in the root directory, keep the directory
                			pwd = "/";
                			continue;
                		}
                		pwd = pwd.substring(0, index); //substring from 0 to index-1
                		continue;
                	}
                	if(tokens[1].startsWith("/")) {//Change the total directory
                		String pwd1; //Used for check if the directory is exist
                		pwd1 = tokens[1];
                		File file = new File(pwd1);
                		if(/*!file.exists() && */!file.isDirectory())
                			System.out.println("Directory not exists");
                		else
                			pwd = pwd1;
                	}
                	else { //Get into the sub-directory
                		String pwd1; //Used for check if the directory is exist
                		pwd1 = pwd + "/" + tokens[1]; //Get into the sub-directory
                		File file = new File(pwd1);
                		if(/*!file.exists() && */!file.isDirectory())
                			System.out.println("Directory not exists");
                		else
                			pwd = pwd1;
                	}
                	continue;
                }

                ProcessBuilder newProcess = new ProcessBuilder(tokens); //Create an external process
                newProcess.directory(new File(pwd)); //The directory of the process
                Process current = newProcess.start(); //Start the process to execute the command, if the command not exist, throw an io exception
                current.waitFor(); //Wait for the external process to terminate, may throw an interrupt exception
                BufferedReader is = new BufferedReader(new InputStreamReader(current.getInputStream()));
                String line; //Get the output of that demand
                while ((line = is.readLine()) != null) {
                    System.out.println(line); //Print
                }

                is = new BufferedReader(new InputStreamReader(current.getErrorStream())); //If the output is an error
                while ((line = is.readLine()) != null) {
                    System.out.println(line);
                }
                
                is.close();
            } catch (InterruptedException ex) { //exception handling
                System.out.println("InterruptedException");
            } catch (IOException ex) { //exception handling
                System.out.println("No such command");
            }
        }
    }
}
