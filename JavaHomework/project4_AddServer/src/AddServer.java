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
public class AddServer {
    public static void main(String args[]) {
        try{
            int counter = 0; //Record the input number
            ServerSocket server = null;
            try{
                server = new ServerSocket(Integer.parseInt(args[0])); //Create a serversocket to listen from clients at port args[0]
            }catch(Exception e) {
                System.out.println("can not listen to:" + e); //If there is any error, print it
            }
            
            while(true){
                try{ //If the conversion fails, it just waits for the next connection.
                    Socket socket = null;
                    try{
                        socket=server.accept(); //Use accept() to wait until getting a request from client, if there is one, create an object of Socket and move on
                    }catch(Exception e) {
                        System.out.println("Error1." + e); //If there is any error, print it
                    }

                    String s; //Store the string from the client
                    BufferedReader is=new BufferedReader(new InputStreamReader(socket.getInputStream())); //Get the input stream from the object of Socket, and create the object of BufferedReader

                    s = is.readLine(); //Get the string from the client
                    if(s.equals("exit")) break; //server exits!
                    counter += Integer.parseInt(s); //Increase the counter
                    System.out.println("Roger!");
                    is.close(); //Close the input stream of the socket
                    socket.close(); //Close the Socket
                } catch(Exception e) { //If the conversion fails, it just waits for the next connection
                    System.out.println("Error!");
                }
            }
            
            System.out.println("counter = " + counter); //AddServer prints the value of counter to the screen and exits
            
            server.close(); //Close the ServerSocket
        }catch(Exception e){
            System.out.println("Error2:" + e); //If there is any error, print it
        }
    }
}