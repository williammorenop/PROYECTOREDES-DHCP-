/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhcp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Thread.sleep;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import dhcp.Red.IpAsign;

/**
 *
 * @author johan
 */
public class DHCPServer {

    /**
     * @param args the command line arguments
     */

    static int PORT = 67;
    static int SENDPORT = 68;
    static String BROADCAST = "255.255.255.255";
    static int MAX_LENGHT_BUFFER = 1024;
    static int TIME = 90000;
    static List<Red> redes;
    static Queue<DHCPPackage> queue;
    static byte[] myIp;
    static PrintWriter fileLog;
	private static Scanner s;


    private static Red getRed(byte[] giAddr) {
        for (Red r : redes) {
            if( Utils.compareIp(r.gateway,giAddr) )
                return r;
        }
        return null;
    }

    DHCPServer()
    {
    	DatagramSocket socket;
        try {
            socket = new DatagramSocket(PORT);
            System.out.println("Imprimo socket " + socket);
            System.out.println("Escuchando por el puerto "+PORT+"...");
            boolean listen = true;
            byte[] buf = new byte[ MAX_LENGHT_BUFFER ];
            DatagramPacket packet = new DatagramPacket( buf , buf.length );
            while(listen) //
            {
              socket.receive(packet);
              DHCPPackage dhcpPack = new DHCPPackage();
              dhcpPack.llenada(buf);
              if( dhcpPack.op == 1 )
              {
                //System.out.println(dhcpPack);
            	
                queue.add(dhcpPack);
                //System.out.println(dhcpPack.toStringLog());
                
              }
            }
        } catch (SocketException ex) {
            Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {
        try {
        	fileLog = new PrintWriter("log.txt");
        	fileLog.println("\tTime\t\t|\tMT|\t"
        			+"sec\t|\t\tClientIp\t\t|\t\tYourIp\t\t|\t\t"
        			+"NServerIp\t\t|\t\tRelaIp\t\t|\t\tClientMAC\t\t|\tOptions");
        	fileLog.close();
           s = new Scanner (System.in);
            System.out.println("INGRESE EL TIEMPO DE ALQUILER EN SEGUNDOS");
            TIME = s.nextInt();
            
            
            myIp = Inet4Address.getLocalHost().getAddress();
            redes =new ArrayList<>();
            queue = new LinkedList<>();
            Utils.leerArchivo("src/Log.txt", redes);
            //System.out.println("3444");
            new Thread(
                new Runnable() {
                    public void run() {
                        update(); //el hilo que va a imprimir
                    }
                }
            ).start();
            //System.out.println("55555");
            DHCPServer dhcpServer = new DHCPServer();
           // System.out.println("6");
        } catch (UnknownHostException | FileNotFoundException ex) {
            Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static private void update()
    {
    	boolean nAck;
    	DatagramSocket socketSend;
        try {
            DHCPPackage pack;
            byte[] buffer;
             socketSend = new DatagramSocket(SENDPORT);
            DatagramPacket send = null;
            while( true )
            {
            	nAck = false;
                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                fileLog = new PrintWriter(new FileOutputStream(new File("log.txt"),true));
                while( !queue.isEmpty() )
                {
                	pack = queue.peek();
                	fileLog.println(pack.toStringLog());
                    Red rPack = getRed(pack.giAddr);
                    if( rPack == null )
                    {
                        System.out.println("No encontre la red");
                        queue.remove();
                        continue;
                    }
                        if( pack.isDiscover() )
                    {
                        //System.out.println("Discover");
                        byte[] ip = rPack.nextIp(pack.chAddr,TIME); // nextIp tacha la ip
                        if( ip == null )
                        {
                            System.out.println("No encontre una ip");
                            queue.remove();
                            continue;
                        }
                        //System.out.println(Utils.bytesToString(ip));
                        buffer = pack.newOffer(ip, myIp , rPack, TIME);
                        
                        try {
                            send = new DatagramPacket(
                                    buffer , buffer.length ,
                                    InetAddress.getByName(BROADCAST),
                                    SENDPORT );
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            socketSend.send(send);
                        } catch (IOException ex) {
                            Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }
                    else if( pack.isRequest() )
                    {
                    	byte[] temp=null;
                    	
                    	InetAddress teme;
                    	if(!Utils.bytesToString(pack.ciAddr).equals("0.0.0.0"))
                    	{ 
                    		temp = pack.ciAddr;
                    		teme=Inet4Address.getByAddress(temp);
                    		rPack.updateTime(pack.ciAddr, TIME);
                    	}
                    	else
                    	{
                    		teme=InetAddress.getByName(BROADCAST);
                    		temp=pack.getIpOptions();
                    		IpAsign tt = rPack.verificarip(temp);
                    		
                    		if( tt != null && tt.used == true && !Utils.isEquals(tt.mac, pack.chAddr) )
                    		{
                    			
                    			nAck = true;
                    		
                    		}
                    		else
                    		{
                    			rPack.assign(rPack.agregarIp(temp), TIME, pack.chAddr,true);
                    		}
                    	}
                    	if( !nAck )
                    		buffer = pack.newACK(temp , myIp , rPack, TIME);
                    	else
                    		buffer = pack.newNACK( temp , myIp , rPack , TIME );
                    	//System.out.println("request");
                       // System.out.println(buffer.length);
						send = new DatagramPacket(
						        buffer , buffer.length ,
						        teme,
						        SENDPORT );
                        try {
                            socketSend.send(send);
                        } catch (IOException ex) {
                            Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    else if( pack.isRelease() )
                    {
                        //System.out.println("Release");
                        rPack.changeState( pack.ciAddr );
                    }
                    //System.out.println(pack);
                    queue.remove();
                }
                fileLog.close();
                ///////////////////////////
                for (Red red : redes) {
					red.cambioEstado();
				}
           	 System.out.println("****************************************"+Utils.printTime()+"********************************************************************************************************************************");

                for (Red red : redes) {
                	red.toString();
                }
                System.out.println("************************************************************************************************************************************************************************");
                /////////////////////////
            }
        } catch (SocketException | UnknownHostException | FileNotFoundException ex) {
            Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
