package dhcp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

public class Utils {

	public static void leerArchivo(String nombre,List<Red> Redes)
	{
		File archivo = new File ("./src/init.txt");
		FileReader fr = null;
		try {
			fr = new FileReader (archivo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		try {
			String linea,name= null,ipS= null,ipF=null,mask= null,dns = null,gateway= null;

			while((linea = br.readLine())!=null)
			{
				System.out.println("---> "+linea);
				if(linea.equals("------------"))
				{
					name=br.readLine();
					ipS=br.readLine();
					ipF=br.readLine();
					mask=br.readLine();
					dns=br.readLine();
					gateway=br.readLine();
				}
				System.out.println("---");
				  Redes.add(new Red (name,intToByte(ipS),intToByte(ipF),intToByte(gateway),intToByte(dns),intToByte(mask)));
				  System.out.println("33");
				//Redes.add(new Red (name,stringToByte(ipS),stringToByte(gateway),stringToByte(dns),stringToByte(mask)));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	///////////////////////////////////
        static public byte[] intToByte( String s )
        {
            // System.out.println(s);
            StringTokenizer st= new StringTokenizer(s);

            byte[] temp = new byte[4];
            String a;
            for( int i =0  ; i < 4 ; ++i )
            {
                a=st.nextToken(".");
                int b=Integer.valueOf(a);
                //System.out.println("{{{{"+b);
                byte c = (byte) b;
                //System.out.println(c); // -22
                //int i2 = c & 0xFF;
                //System.out.println(i2); // 234
 
                temp[ i ] = c;

                
            }
          //  System.out.println((temp[0] & 0xFF )+"."+(temp[1] & 0xFF)+"."+(temp[2]& 0xFF)+"."+(temp[3]& 0xFF));
            return temp;
        }
        //////////////////////////////////
        
        static public byte[] intToByte( int n )
        {
            byte[] temp = new byte[4];
            for( int i=  0; i < 4; ++i )
            {
                  temp[ 3-i ] = 0;
                  temp[ 3-i ] |= n;
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
        boolean res =
        	Utils.unsignedToBytes(gateway[0])==Utils.unsignedToBytes(giAddr[0])   && 
        		Utils.unsignedToBytes(gateway[1])==Utils.unsignedToBytes(giAddr[1]) &&
        				Utils.unsignedToBytes(gateway[2])==Utils.unsignedToBytes(giAddr[2]) 
        				&& 
        				Utils.unsignedToBytes(gateway[3])==Utils.unsignedToBytes(giAddr[3])
        			;
        
        return res;
    }

    static boolean isEquals(byte[] chAddr, byte[] mac) {
        if( chAddr == null || mac == null )
            return false;
       // System.out.println(Utils.macToString(chAddr)+" "+Utils.macToString(mac));
        //return Utils.macToString(chAddr).equals(Utils.macToString(mac));
       
        for( int i = 0 ; i < 6; ++i )
            if( chAddr[ i ] != mac[ i ])
                    return false;
       // System.out.println("jjajaj");
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

	public static String printTime(GregorianCalendar time) {
		return new SimpleDateFormat("hh:mm:ss").format(time.getTime());
	}
	public static String printTime() {
		return printTime(new GregorianCalendar());
	} 
   	private static char hexUpperChar(byte b) {
        b = (byte) ((b >> 4) & 0xf);
        if (b == 0) return '0';
        else if (b < 10) return (char) ('0' + b);
        else return (char) ('a' + b - 10);


    }

    private static char hexLowerChar(byte b) {
        b = (byte) (b & 0xf);

        if (b == 0) return '0';

        else if (b < 10) return (char) ('0' + b);

        else return (char) ('a' + b - 10);
    }
  private static String harToString( byte n )
  {
    return String.valueOf(hexUpperChar(n))+String.valueOf(hexLowerChar(n));
  }
  public static String macToString( byte[] mac) 
  {
	  String s = "";
	  for( int i = 0 ; i < 6 ; ++i )
	  {  if( i != 0 )
			  s+= ":";
		  s += harToString(mac[i]);
	  }
	  return s;
	 }
}
