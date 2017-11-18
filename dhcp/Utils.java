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
			String linea,name,ip,mask,dns,gateway;
			
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
}
