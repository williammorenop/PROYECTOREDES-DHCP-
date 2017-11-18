/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhcp;

import java.io.IOException;
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

    private static Red getRed(byte[] giAddr) {
        for (Red r : redes) {
            if( Utils.compareIp(r.gateway,giAddr) )
                return r;
        }
        return null;
    }

    DHCPServer()
    {
        try {
            DatagramSocket socket = new DatagramSocket(PORT);
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
                System.out.println(dhcpPack);
                queue.add(dhcpPack);
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
           Scanner s=new Scanner (System.in);
            System.out.println("INGRESE EL TIEMPO DE ALQUILER EN SEGUNDOS");
           TIME = s.nextInt();
            
            
            myIp = Inet4Address.getLocalHost().getAddress();
            redes =new ArrayList<>();
            queue = new LinkedList<>();
            //Utils.leerArchivo("Log.txt", redes);
            new Thread(
                new Runnable() {
                    public void run() {
                        update(); //el hilo que va a imprimir
                    }
                }
            ).start();
            DHCPServer dhcpServer = new DHCPServer();
        } catch (UnknownHostException ex) {
            Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static private void update()
    {
        try {
            DHCPPackage pack;
            byte[] buffer;
            DatagramSocket socketSend = new DatagramSocket(SENDPORT);
            DatagramPacket send = null;
            while( true )
            {
                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                while( !queue.isEmpty() )
                {
                    pack = queue.peek();
                    Red rPack = getRed(pack.giAddr);
                    
                    if( pack.isDiscover() )
                    {
                        byte[] ip = rPack.nextIp(pack.chAddr,TIME); // nextIp tacha la ip
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
                        buffer = pack.newACK( pack.getIpOptions() , myIp , rPack, TIME);
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
                    else if( pack.isRelease() )
                    {
                        rPack.changeState( pack.ciAddr );
                    }
                    System.out.println(pack);
                    queue.remove();
                }
            }
        } catch (SocketException ex) {
            Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
