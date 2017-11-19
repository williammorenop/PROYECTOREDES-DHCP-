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
    
    public List<IpAsign> assignableIP;

    Red(String name , byte[] ipS , byte[] gateway , byte[] dns , byte[] mask )
    {
      this.name = name;
      this.ipS = ipS;
      this.gateway = gateway;
      this.dns = dns;
      this.mask = mask;
      this.assignableIP = new ArrayList<>();
      byte[] temp = new byte[4];
      int ini0 = Utils.unsignedToBytes(ipS[0]);
      int ini1 = Utils.unsignedToBytes(ipS[1]);
      int ini2 = Utils.unsignedToBytes(ipS[2]);
      int ini3 = Utils.unsignedToBytes(ipS[3]);
      for( int i0 = ini0 ; i0 <= 255 ; ++i0 )
      {
        for( int i1 = ini1 ; i1 <= 255 ; ++i1 )
        {

          for( int i2 = ini2 ; i2 <= 255; ++i2 )
          {

            for( int i3 = ini3 ; i3 <= 255;++i3 )
            {
                temp[0] = (byte)i0;
                temp[1] = (byte)i1;
                temp[2] = (byte)i2;
                temp[3] = (byte)i3;
                System.out.println("Voy a ingresar :"+Utils.bytesToString(temp));
                this.assignableIP.add( new IpAsign( temp ) );
            }
            ini3 = 0;
          }
          ini2 = 0;
        }
        ini1 = 0;
      }
    }
    
    Red(String name , byte[] ipS,byte[] ipF , byte[] gateway , byte[] dns , byte[] mask )
    {
      this.name = name;
      this.ipS = ipS;
      this.ipF = ipF;
      this.gateway = gateway;
      this.dns = dns;
      this.mask = mask;
      this.assignableIP = new ArrayList<>();
      //System.out.println("{{{{"+ipS[1]+"{{{"+(int)ipS[1]);
      // System.out.println((  System.out.println((temp[0] & 0xFF )+"."+(temp[1] & 0xFF)+"."+(temp[2]& 0xFF)+"."+(temp[3]& 0xFF)); )+"."+(temp[1] & 0xFF)+"."+(temp[2]& 0xFF)+"."+(temp[3]& 0xFF));
      byte[] temp = new byte[4];
      for (int i =(ipS[0] & 0xFF ); i < (ipF[0] & 0xFF ); i++) {
		for (int j = (ipS[1] & 0xFF ); j <(ipF[1] & 0xFF ); j++) {
			for (int j2 = (ipS[2] & 0xFF ); j2 < (ipF[2] & 0xFF ); j2++) {
				for (int k = (ipS[3] & 0xFF ); k < (ipF[3] & 0xFF ); k++) {
					temp[0] = (byte)i;
	                temp[1] = (byte)j;
	                temp[2] = (byte)j2;
	                temp[3] = (byte)k;
	                
	                System.out.println("METO: "+(temp[0] & 0xFF )+"."+(temp[1] & 0xFF)+"."+(temp[2]& 0xFF)+"."+(temp[3]& 0xFF)+"."+(temp[1] & 0xFF)+"."+(temp[2]& 0xFF)+"."+(temp[3]& 0xFF));
	                
	                this.assignableIP.add(new IpAsign(temp));
				}
			}
		}
	}
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
        return null;
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
