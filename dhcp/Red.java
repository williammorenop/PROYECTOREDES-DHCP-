/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhcp;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 * @author root
 */



public class Red {



	public class IpAsign
    {

	public byte[] ip;
      public byte[] mac;
      public boolean used;
      public GregorianCalendar timeS;
      public GregorianCalendar timeF;

      IpAsign(byte[] ip)
      {
        this.ip = ip;
        this.used = false;
        this.mac = null;
        this.timeS = this.timeF = null;
      }
      public void toString2() {
    	  System.out.println("|"+Utils.bytesToString(ip)+"\t|\t"+mactostring(mac)+"\t|\t"+used+"\t|\t"+Utils.printTime(timeS)+"\t|\t"+Utils.printTime(timeF)+"\t|\t");
      }
      public String mactostring(byte[] mac)
      {
        String s = "";
        if( mac == null )
        		return s;
        for( int i = 0 ;i  < 6; ++i )
        {
          if( i != 0 )
            s += ":";
          s+=harToString(mac[i]);
        }
        return s;
      }
      private String harToString( byte n )
      {
        return String.valueOf(hexUpperChar(n))+String.valueOf(hexLowerChar(n));
	}
     	private char hexUpperChar(byte b) {
            b = (byte) ((b >> 4) & 0xf);
            if (b == 0) return '0';
            else if (b < 10) return (char) ('0' + b);
            else return (char) ('a' + b - 10);


        }
        private char hexLowerChar(byte b) {
            b = (byte) (b & 0xf);

            if (b == 0) return '0';

            else if (b < 10) return (char) ('0' + b);

            else return (char) ('a' + b - 10);
        }
        public byte[] getIp() {
            return ip;
        }

        public void setIp(byte[] ip) {
            this.ip = ip;
        }

        public byte[] getMac() {
            return mac;
        }

        public void setMac(byte[] mac) {
            this.mac = mac;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public GregorianCalendar getTimeS() {
            return timeS;
        }

        public void setTimeS(GregorianCalendar timeS) {
            this.timeS = timeS;
        }

        public GregorianCalendar getTimeF() {
            return timeF;
        }

        public void setTimeF(GregorianCalendar timeF) {
            this.timeF = timeF;
        }

    }



    public String name;
    public byte[] ipS;
    public byte[] ipF;
    public byte[] gateway;
    public byte[] dns;
    public byte[] mask;
    public byte[] ipU;
    
    public List<IpAsign> assignableIP;

    Red(String name , byte[] ipS,byte[] ipF , byte[] gateway , byte[] dns , byte[] mask )
    {
      this.name = name;
      this.ipS = ipS;
      this.ipU=new byte[4];
      
      for (int i = 0; i < 4; i++) {
		this.ipU [i]= ipS[i];
	}
      this.ipF = ipF;
      this.gateway = gateway;
      this.dns = dns;
      this.mask = mask;
      this.assignableIP = new ArrayList<>();
      //System.out.println("{{{{"+ipS[1]+"{{{"+(int)ipS[1]);
      System.out.println((ipS[0] & 0xFF )+"."+(ipS[1] & 0xFF)+"."+(ipS[2]& 0xFF)+"."+(ipS[3]& 0xFF));
      System.out.println((ipF[0] & 0xFF )+"."+(ipF[1] & 0xFF)+"."+(ipF[2]& 0xFF)+"."+(ipF[3]& 0xFF));
      System.out.println();
    }
    byte[] newIP(int time,byte[] mac)
    {
    	byte[] temp = new byte[4];
        for (int i = 0; i < 4; i++) {
    	temp [i]= ipU[i];
    	}
        
        if(ipU[3]!=255 )
        {
        	ipU[3]++;        	
        }
        else 
        {
        	ipU[3]=0;
        	if(ipU[2]!=255)
        	{
        		ipU[2]++;        		
        	}
        	else
        	{
        		ipU[2]=0;
        		if(ipU[1]!=255)
        		{
        			ipU[1]++; 
        		}
        		else
        		{
        			ipU[1]=0;
            		if(ipU[0]!=255)
            		{
            			ipU[0]++; 
            		}
            		else 
            		{
            			System.err.println("ERROR : FIN IPS");
            			return null;
            		}
        		}
        	}
        }
        this.assignableIP.add(new IpAsign(temp));        
    	assign(assignableIP.get(assignableIP.size()-1), time,mac,false);
		return temp;
 
    }
    
    byte[] nextIp(byte[] chAddr, int time) {
        for( IpAsign ip : assignableIP )
        {
        	//System.out.println("--------------------Entre1");
            if( Utils.isEquals(chAddr,ip.mac) )
            {
                assign(ip,time,chAddr,false);
                return ip.ip;
            }
        }
        for( IpAsign ip : assignableIP )
        {
        	//System.out.println("********************Entre2");
            if( !ip.isUsed() )
            {
                assign(ip,time,chAddr,false);
                return ip.ip;
            }
        }
       // System.out.println("{{{{{{{{{{{{{{{{{{{{Entre3");
        return newIP(time,chAddr);
    }
    public void assign(IpAsign ip, int time,byte[] mac, boolean used) {
        ip.used = used;
        ip.mac = new byte[6];
        for( int i = 0;  i < 6 ; ++i )
        	ip.mac[ i ] = mac[ i ];
        ip.timeF = new GregorianCalendar();
        ip.timeS = new GregorianCalendar();
        if((ip.timeS.equals(ip.timeF)))
        {
        	ip.timeF.add(GregorianCalendar.SECOND, time);        	
        }
      
    }
     void changeState(byte[] ciAddr) {
        for( IpAsign ip : assignableIP )
        {
            if( Utils.compareIp(ip.ip, ciAddr) )
                ip.used = false;
        }
    }
     public IpAsign verificarip (byte[] ciAddr)
     {
	    for (IpAsign ip : assignableIP) {
			if(Utils.compareIp(ip.ip, ciAddr));
			return ip;
		}
	    return null;
     }
     public boolean updateTime (byte[] ciAddr,int time)
     {
	    IpAsign temp= verificarip(ciAddr);
	    if (temp==null) {
			
	    	return false;
		}
	    temp.timeF.add(GregorianCalendar.SECOND, time);
	    return true;
     }
     public void printLine()
     {
    	 System.out.println("------------------------------------------------------------------------------------------------------------------");
     }
     
     public String toString() {
    	//Utils.clearScreen();
    	 System.out.println(" Nombre: "+name+" IP Inicio: "+ Utils.bytesToString(ipS)+ " IP Fin: "+Utils.bytesToString(ipF)+" Mascara: "+Utils.bytesToString(mask)+" DNS: "+Utils.bytesToString(dns)+" GateWay "+Utils.bytesToString(gateway));
    	 printLine();
    	 System.out.println("|\tIP\t|\t\tMAC\t\t|\tUso\t|\tTiempo Inicio\t|\tTiempo Fin\t|\t");    	 
    	 printLine();
    	 
    	 for (IpAsign ipAsign : assignableIP) {
    		if( ipAsign.used )
    			ipAsign.toString2();
    	 }
    	 printLine();
    	 
    	 
		return null;
     }
	public void cambioEstado() {
		for (IpAsign ipAsign : assignableIP) {
			GregorianCalendar horaactual =new GregorianCalendar();
			if(horaactual.after(ipAsign.getTimeF()))
			{
				ipAsign.setUsed(false);
			}
		}		
	}
	public IpAsign agregarIp(byte[] ip) {
		IpAsign temp = verificarip(ip);
		if( temp == null )
		{
			this.assignableIP.add(new IpAsign(ip));
			temp = this.assignableIP.get(this.assignableIP.size()-1);
		}	
		return temp;
	}
}
