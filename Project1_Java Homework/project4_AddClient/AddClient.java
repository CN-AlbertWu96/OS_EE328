/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.net.*;

/**
 *
 * @author AlbertWu
 */
public class AddClient {

    /**
     * @param args the command line arguments
     */
    //Only numbers can be sent, or the sending string is of no use
    public static void main(String[] args)  {//three command-line parameters: server address; server port; a string to send to the server
        try{
            Socket socket = new Socket(InetAddress.getByName(args[0]), Integer.parseInt(args[1])); //Send a request to the server address with a host number
            PrintWriter os=new PrintWriter(socket.getOutputStream()); //Get the output stream from the object of Socket, and create the object of PrintWriter
            
            os.println(args[2]); //Output the String from the command-line parameter args[2] to Server
            os.flush(); //Refresh the output stream in order to make the server get the string immediately
            
            os.close(); //Close the output stream of the socket
            socket.close(); //Close the socket
        }catch(Exception e) {
            System.out.println("Error" + e); //If there is any error, print it
        }
    }
}
