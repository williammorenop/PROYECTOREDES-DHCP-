package dhcp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Utils {
	
	public static void leerArchivo(String nombre,List<Red> Redes)
	{
		File archivo = new File (".\\Log.txt");
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
            byte[] temp = new byte[4];
            String[] ts=s.split(".");
            for( int i =0  ; i < ts.length ; ++i )
                temp[ i ] = Byte.parseByte(ts[i]);
            return temp;
        }
}
