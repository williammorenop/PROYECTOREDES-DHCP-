package dhcp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

public class Utils {

	public static void leerArchivo(String nombre,List<Red> Redes)
	{
		File archivo = new File ("./src/Log.txt");
		FileReader fr = null;
		try {
			fr = new FileReader (archivo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		try {
			String linea,name= null,ip= null,mask= null,dns = null,gateway= null;

			while((linea = br.readLine())!=null)
			{
				System.out.println("---> "+linea);
				if(linea.equals("------------"))
				{
					name=br.readLine();
					ip=br.readLine();
					mask=br.readLine();
					dns=br.readLine();
					gateway=br.readLine();
				}
				Redes.add(new Red (name,stringToByte(ip),stringToByte(gateway),stringToByte(dns),stringToByte(mask)));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
        static public byte[] intToByte( int n )
        {
            byte[] temp = new byte[4];
            for( int i=  0; i < 4; ++i )
            {
                  temp[ i ] = 0;
                  temp[ i ] |= n;
                  n <<= 8;
            }
            return temp;
        }
        static public byte[] stringToByte( String s )
        {
            System.out.println(s);
            StringTokenizer st= new StringTokenizer(s);

            byte[] temp = new byte[4];
            String a;
            for( int i =0  ; i < 4 ; ++i )
            {
                a=st.nextToken(".");
                int b=Integer.valueOf(a);
                temp[ i ] |= b;

            }
            return temp;
        }

    static boolean compareIp(byte[] gateway, byte[] giAddr) {
        return gateway[0] == giAddr[0] && gateway[1] == giAddr[1] && gateway[2] == giAddr[2] && gateway[3] == giAddr[3] ;
    }

    static boolean isEquals(byte[] chAddr, byte[] mac) {
        if( chAddr == null || mac == null )
            return false;
        if( chAddr.length != mac.length )
                return false;
        for( int i = 0 ; i < chAddr.length ; ++i )
            if( chAddr[ i ] != mac[ i ])
                    return false;
        return true;
    }

    static String bytesToString(byte[] ip) {
       String s = "";
       for( int i = 0 ; i < 4; ++i )
       {
           if( i != 0 )
               s+=".";
           s+=unsignedToBytes(ip[i]);
       }
       return s;
    }
    public static int unsignedToBytes(byte b)
    {
        return b & 0xFF;
    }

}
