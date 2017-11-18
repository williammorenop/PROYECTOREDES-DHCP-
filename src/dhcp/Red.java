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
    public byte[] ip;
    public byte[] gateway;
    public byte[] dns;
    public byte[] mask;
    public List<IpAsign> assignableIP;

    Red(String name , byte[] ip , byte[] gateway , byte[] dns , byte[] mask )
    {
      this.name = name;
      this.ip = ip;
      this.gateway = gateway;
      this.dns = dns;
      this.mask = mask;
      this.assignableIP = new ArrayList<>();
      byte[] temp = new byte[4];
      byte ini0 = ip[0];
      byte ini1 = ip[1];
      byte ini2 = ip[2];
      byte ini3 = ip[3];
      for( byte i0 = ini0 ; i0 <= mask[0] ; ++i0 )
      {
        for( byte i1 = ini1 ; i1 <= mask[1] ; ++i1 )
        {

          for( byte i2 = ini2 ; i2 <= mask[2] ; ++i2 )
          {

            for( byte i3 = ini3 ; i3 <= mask[3] ; ++i3 )
            {
                temp[0] = i0;
                temp[1] = i1;
                temp[2] = i2;
                temp[3] = i3;
                this.assignableIP.add( new IpAsign( temp ) );
            }
            ini3 = 0;
          }
          ini2 = 0;
        }
        ini1 = 0;
      }
    }
}
