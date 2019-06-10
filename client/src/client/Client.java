package client;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author mido
 */
public class Client {

    /**
     * @param args the command line arguments
     */
     public static void main(String[] args) {
        
       { 
        DatagramSocket aSocket=null;
       try{
              aSocket=new DatagramSocket();
            
            InetAddress aHost= InetAddress.getByName("localhost");
            int serverPort=6789;
            
            
            //input to program
           byte[] process={1,2,3,4,5};
           byte[] arrival={0,0,0,0,0};
           byte[] burst={15,7,4,8,12};
           
           
          
           
         
           DatagramPacket request1= new DatagramPacket(process, process.length ,aHost , serverPort);
           DatagramPacket request2= new DatagramPacket(arrival , arrival.length ,aHost , serverPort);
           DatagramPacket request3= new DatagramPacket(burst , burst.length ,aHost , serverPort);
            
            //input to determine type of algotithm
           //1->fcfs ,2->sjfnonpre ,3->sjfpre ,4->RR
            int num=4; 
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream s = new PrintStream( out );
            s.print(num);
            byte[] array = out.toByteArray();
            //using stream so it easy to convert it to byte array
           DatagramPacket request4= new DatagramPacket(array , array.length ,aHost , serverPort);

           
           
            aSocket.send(request1); 
            aSocket.send(request2);
            aSocket.send(request3);
            aSocket.send(request4);
             
            
            
           //output arrays
           byte[] turnaround=new byte[10];
           byte[] waiting=new byte[10];
           
      
            DatagramPacket reply1= new DatagramPacket(turnaround, turnaround.length);
            aSocket.receive(reply1);
             DatagramPacket reply2= new DatagramPacket(waiting, waiting.length);
            aSocket.receive(reply2);
            
            int averageturnaround = 0,averagewaiting = 0;
                System.out.println("Turnaround Time Array:");
                
	        for(int i=0;i<5;i++)
                {
                    System.out.println(turnaround[i]);
                }
                System.out.println("average turnaround Time=");
                for(int i=0;i<5;i++)
                {
                    averageturnaround+=turnaround[i];
                }
                        
                System.out.println(averageturnaround/5);
                System.out.println("Waiting Time Array:");
                    for(int i=0;i<5;i++)
                {
                    System.out.println(waiting[i]);
                }
                System.out.println("average waiting Time=");
                for(int i=0;i<5;i++)
                {
                    averagewaiting+=waiting[i];
                }
               
                System.out.println(averagewaiting/5);

            
        }catch(SocketException e)
        {
            System.out.println("Socket:"+e.getMessage());
        }catch(IOException e)
        {
            System.out.println("IO:"+e.getMessage());
        }
        }
    } 
     
    }
    

