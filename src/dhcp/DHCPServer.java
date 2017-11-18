/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhcp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    static int MAX_LENGHT_BUFFER = 1024;
    static int TIME = 90000;

    DHCPServer()
    {
        try {
            DatagramSocket socket = new DatagramSocket(PORT);
            System.out.println("Imprimo socket " + socket);
            System.out.println("Escuchando por el puerto "+PORT+"...");
            boolean listen = true;
            byte[] buf = new byte[ MAX_LENGHT_BUFFER ];
            DatagramPacket packet = new DatagramPacket( buf , buf.length );
            DHCPPackage dhcpPack = new DHCPPackage();
            while(listen) //
            {
              socket.receive(packet);
              dhcpPack.llenada(buf);
                System.out.println(dhcpPack);
            }
        } catch (SocketException ex) {
            Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {
    	List<Red> Redes =new ArrayList<Red>();
    	Utils.leerArchivo("Log.txt", Redes);
       // DHCPServer dhcpServer = new DHCPServer();
    }

}
