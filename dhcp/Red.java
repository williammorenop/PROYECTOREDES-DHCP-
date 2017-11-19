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
    byte[] newIP(int time)
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
    	assign(assignableIP.get(assignableIP.size()-1), time);
		return temp;
 
    }
    
    byte[] nextIp(byte[] chAddr, int time) {
        for( IpAsign ip : assignableIP )
        {
            if( Utils.isEquals(chAddr,ip.mac) )
            {
                assign(ip,time);
                return ip.ip;
            }
        }
        for( IpAsign ip : assignableIP )
        {
            if( !ip.isUsed() )
            {
                assign(ip,time);
                return ip.ip;
            }
        }
        return newIP(time);
    }
    private void assign(IpAsign ip, int time) {
        ip.used = true;
        ip.timeS = new GregorianCalendar();
        ip.timeF = new GregorianCalendar();
        ip.timeF.add(GregorianCalendar.SECOND, time);
      
    }
     void changeState(byte[] ciAddr) {
        for( IpAsign ip : assignableIP )
        {
            if( Utils.compareIp(ip.ip, ciAddr) )
                ip.used = false;
        }
    }
}
