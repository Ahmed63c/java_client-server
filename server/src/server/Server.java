/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 *
 * @author mido
 */
public  class Server {
    
    //global variable for all algorithms 
    //static because main method is static
           static byte [] process=new byte[10];
           static  byte [] arrival=new byte[10];
           static byte [] burst=new byte[10];
            static byte [] type=new byte[10];
           static String rcvd="";
            static int data=0;
          static  byte[] turnaround=new byte[10];
           static byte[] waiting=new byte[10];

        
 
    public static  void main(String[] args) {
        
             DatagramSocket aSocket=null;

try{
            
          aSocket =new DatagramSocket(6789);
          
          
        
        while(true){    
                //recieve data
                DatagramPacket request1 =new DatagramPacket(process, process.length);
                aSocket.receive(request1);
                DatagramPacket request2 =new DatagramPacket(arrival, arrival.length);
                aSocket.receive(request2);
                DatagramPacket request3 =new DatagramPacket(burst, burst.length);
                aSocket.receive(request3);
                DatagramPacket request4 =new DatagramPacket(type, type.length);
                aSocket.receive(request4);
                //read integer from byte array using stream
                ByteArrayInputStream in = new ByteArrayInputStream(request4.getData());
                for (int i=0; i< request4.getLength(); i++)
                        {
                                  data = in.read();
                                    }
    //the integer data retrived from byte array start from 48 in a sequence of 1>49,2>50,3>51,4>52
     //this viewed in debugging mode 
     // idont know why            
                if(data==49){
                    fcfs();
                    
                }
                      else if(data==50){
                    sjfnonpreemptive();
                }
                       else if((int)data==51){
                    sjfpreemptive();
                }
                       else if(data==52){
                           RR();
                           
                       }
              
                
   
                //send processing data
                // all algoritms working with the same arrays turnaround and waiting
                DatagramPacket reply1= new DatagramPacket(turnaround,turnaround.length,request1.getAddress(),request1.getPort());
                aSocket.send(reply1);
                
                DatagramPacket reply2= new DatagramPacket(waiting,waiting.length,request1.getAddress(),request1.getPort());
                aSocket.send(reply2);
            } 
                
   
            
        }catch(SocketException e){
            System.out.println("Socket:"+e.getMessage());
        }catch(IOException e){
            System.out.println("IO:"+e.getMessage());
        }
    }
    public static void fcfs(){
                  

            for(int i=1;i<5;i++)

                {
                    waiting[i]=(byte) (waiting[i-1]+burst[i-1]);
                    waiting[i]=(byte) (waiting[i]-arrival[i]);
                }
            for(int i=0;i<5;i++)
                {
                    turnaround[i]=(byte) (waiting[i]+burst[i]);
                }
                
    }
    public static void sjfnonpreemptive (){
        
            
           // sjf non premptive   
                byte WT[], TAT[];
                WT = new byte[10];
                TAT = new byte[10];

                byte temp;
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (burst[j] > burst[j + 1]) {
                            temp = burst[j];
                            burst[j] = burst[j + 1];
                            burst[j + 1] = (byte) (temp);

                            temp = WT[j];
                            WT[j] = WT[j + 1];
                            WT[j + 1] = (byte) (temp);
                        }
                    }
                }

                for (int i = 0; i < 5; i++) {
                    TAT[i] = (byte) (burst[i] + WT[i]);
                    WT[i + 1] = TAT[i];
                }

                  TAT[5] = (byte) (WT[5] + burst[5]);

                for(int k=0;k<5;k++){
                   waiting[k]=WT[k];
                   turnaround[k]=TAT[k];
                }
           
            
    }      
    public static void sjfpreemptive(){
        
                   byte []tatp =new byte[10];
                   byte []wtp =new byte[10];
                   byte tbt = 0;
        for (int i = 0; i < 5; i++) {
            tbt = (byte) (tbt + burst[i]);
        }

       
        int q2 = 0;
         for (int a = 0; a < tbt; a++) {
            int q = Min(burst, arrival, tbt, a, 5); //get shortest job index
            if (q != q2) {
            wtp[q] = (byte) a;                //first one wt is zero default
            tatp[q] = (byte) (a + burst[q]); //first turnaroun equal to burst time
            }
             
            burst[q] = (byte) (burst[q] - 1);// minus one then do ti agin over whole chart
            q2 = q;                       
        }
          
         for(int d=0;d<5;d++){
             waiting[d]=wtp[d];
             turnaround[d]=tatp[d];   
         }       
                
                             
                             
                 
    }
    public static void RR(){
                       
                   byte wtr[] = new byte[10];
                   byte tatr[] = new byte[10];    
                   int i, j, k, sum = 0;
                   
                   
                for (i = 0; i < 5; i++) {
                    arrival[i] = burst[i];
                }
                for (i = 0; i < 5; i++) {
                    wtr[i] = 0;
                }
                do {
                    for (i = 0; i < 5; i++) {
                        //quantum is 4
                        if (burst[i] > 4) {
                            burst[i] -= 4;
                            for (j = 0; j < 5; j++) {
                                if ((j != i) && (burst[j] != 0)) {
                                    wtr[j] += 4;
                                }
                            }
                        } else {
                            for (j = 0; j < 5; j++) {
                                if ((j != i) && (burst[j] != 0)) {
                                    wtr[j] += burst[i];
                                }
                            }
                            burst[i] = 0;
                        }
                    }
                    sum = 0;
                    for (k = 0; k < 5; k++) {
                        sum = sum + burst[k];
                    }
                } 
                while (sum != 0);
                for (i = 0; i < 5; i++) {
                    tatr[i] = (byte) (wtr[i] + arrival[i]);
                }
                for(k=0;k<5;k++){
                    waiting[k]=wtr[k];
                    turnaround[k]=tatr[k];
                }

        
    }
     private static int Min(byte b[], byte a[], byte tbt, int r, int n) {
        int j = 0;

        int min = tbt;

 

        for (int i = n; i > 0; i--) {

            if (b[i] < min && b[i] > 0 && r >= a[i]) {

                min = b[i];

                j = i;

            }

        }

        return j;
}
      
}
    
    


