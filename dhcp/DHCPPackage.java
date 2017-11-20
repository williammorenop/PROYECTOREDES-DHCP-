
package dhcp;

import java.util.Arrays;

public class DHCPPackage
{
    private byte FIN = (byte) 255;
    public byte op;
    public byte hType;
    public byte hLenght;
    public byte hOps;
    public byte[]   xId;
    public byte[]  secs;
    public byte[]  flags;
    public byte[]  ciAddr;
    public byte[]  yiAddr;
    public byte[]  siAddr;
    public byte[]  giAddr;
    public byte[]  chAddr;
    public byte[]  sname;
    public byte[]  file;
    public byte[]   options;

    DHCPPackage()
    {
      xId = new byte[4];
      secs = new byte[2];
      flags = new byte[2];
      ciAddr = new byte[4];
      yiAddr = new byte[4];
      siAddr = new byte[4];
      giAddr = new byte[4];
      chAddr = new byte[16];
      sname = new byte[64];
      file = new byte[128];
      options = new byte[320];

    }
    public void llenada(byte [] arr)
    {
      this.op=arr[0];
      this.hType=arr[1];
      this.hLenght=arr[2];
      this.hOps=arr[3];
      // 4 - xid
      for(int w=0;w<4;w++)
        xId[w]=arr[4+w];
      // 8 - secs
      for(int h=0;h<2;h++)
        secs[h]=arr[8+h];
      // 10 - flags
      for( int i = 0 ; i < 2 ; ++i )
        flags[ i ] = arr[ 10+i ] ;
      //12 - ciaddr
      for(int w=0;w<4;w++)
        ciAddr[w]=arr[12+w];
      //16 - yiaddr
      for(int h=0;h<4;h++)
        yiAddr[h]=arr[16+h];
      //20 - siaddr
      for( int i = 0 ; i < 4 ; ++i )
        siAddr[ i ] = arr[ 20+i ] ;
      //24 - giaddr
      for(int w=0;w<4;w++)
        giAddr[w]=arr[24+w];
      //28 - chaddr
      for(int h=0;h<16;h++)
        chAddr[h]=arr[28+h];
      //44 - sname
      for(int i=0;i<64;i++)
        sname[i]=arr[44+i];
      //108 - file
      for(int w=0;w<128;w++)
        file[w]=arr[108+w];
      // 236 -options
      for(int h=0;h<320;h++){
        options[h]=arr[236+h];
      }

    }
    public byte[] newOffer(byte[] ipOffer,byte[] ipServer,Red red,int time )
    {
        op = 2; // offer
        yiAddr = ipOffer;
        siAddr = ipServer;
        for( int i = 0 ;i < 316 ; ++i )
               options[ 4+i ] = 0;
        int indx = 4;
        options[ indx++ ] = 53; //tipo
        options[ indx++ ] = 1; //tam
        options[ indx++ ] = 2;// offer
        byte[] tempTime = Utils.intToByte(time);
        options[ indx++ ] = 51; //time
        options[ indx++ ] = 4; //tam
        for( int i = 0 ; i < 4 ; ++i )
            options[ indx++ ] = tempTime[ i ];
        options[ indx++ ] = 1 ; //net
        options[ indx++ ] = 4 ; //tam
        for( int i = 0; i < 4 ; ++i )
              options[ indx++ ] = red.mask[ i ]; //mask
        options[ indx++ ] = 6 ; //dns
        options[ indx++ ] = 4; //tam
        for( int i = 0 ; i < 4 ; ++i )
              options[ indx++ ] = red.dns[ i ]; // dns
        options[ indx ] = FIN;
        return toBytes();
    }
    public byte[] newACK(byte[] ipOffer,byte[] ipServer,Red red,int time)
    {
    	
        op = 2; // offer
        yiAddr = ipOffer;
        siAddr = ipServer;
        for( int i = 0 ;i < 316 ; ++i )
               options[ 4+i ] = 0;
        int indx = 4;
        options[ indx++ ] = 53; //tipo
        options[ indx++ ] = 1; //tam
        options[ indx++ ] = 5;// ACK
        
        byte[] tempTime = Utils.intToByte(time);
        options[ indx++ ] = 51; //time
        options[ indx++ ] = 4; //tam
        for( int i = 0 ; i < 4 ; ++i )
            options[ indx++ ] = tempTime[ i ];
        options[ indx++ ] = 1 ; //net
        options[ indx++ ] = 4 ; //tam
        for( int i = 0; i < 4 ; ++i )
              options[ indx++ ] = red.mask[ i ]; //mask
        options[ indx++ ] = 6 ; //dns
        options[ indx++ ] = 4; //tam
        for( int i = 0 ; i < 4 ; ++i )
              options[ indx++ ] = red.dns[ i ]; // dns
        options[ indx ] = FIN;
        
        return toBytes();
    }
    
    
    public byte[] toBytes()
    {
      byte[] arr = new byte[ 1024 ];
      arr[0]=this.op;
      arr[1]=this.hType;
      arr[2]=this.hLenght;
      arr[3]=this.hOps;
        // 4 - xid
        System.arraycopy(xId, 0, arr, 4, 4);
        // 8 - secs
        System.arraycopy(secs, 0, arr, 8, 2);
      // 10 - flags
      for( int i = 0 ; i < 2 ; ++i )
        arr[ 10+i ] =flags[ i ];
        //12 - ciaddr
            System.arraycopy(ciAddr, 0, arr, 12, 4);
        //16 - yiaddr
        
        for( int i = 0 ; i < 4; ++i )
            arr[ 16+i ] = yiAddr[ i ];
        
      //20 - siaddr
      System.arraycopy(siAddr, 0, arr, 20, 4);

        //24 - giaddr
        System.arraycopy(giAddr, 0, arr, 24, 4);
        //28 - chaddr
        System.arraycopy(chAddr, 0, arr, 28, 16);
        //44 - sname
        System.arraycopy(sname, 0, arr, 44, 64);
        //110 - file
        System.arraycopy(file, 0, arr, 108, 128);
        // 238 -options
        System.arraycopy(options, 0, arr, 236, 320);
      return arr;
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
        private String harToString( byte n )
        {
          return String.valueOf(hexUpperChar(n))+String.valueOf(hexLowerChar(n));
	}

  public long toStringXId()
  {
  	long num = 0;
    	for( int i = 0 ; i < 4 ; ++i )
        {
            num<<=8;
            num|=xId[ 3-i ] ;
        }

    	return num;
  }
  public int toStringSecs()
  {
  	int num = 0;
    	for( int i =0  ;i < 2 ; ++i )
        {
            num <<= 8;
            num |= secs[ 1 ];
        }
    	return num;
  }
  public long toStringFlags(){
  	long num = 0;
    	for( int i = 0 ; i < 2 ; ++i )
        {
            num<<=8;
            num|=flags[ i ] ;
        }
    	return num;
  }
  public String toStringCiAddr()
  {
    return Utils.bytesToString(ciAddr);
  }

  public String toStringYiAddr(){
  	return Utils.bytesToString(this.yiAddr);
  }
   public String toStringSiAddr()
  {
    return Utils.bytesToString(this.siAddr);
  }

  public String toStringGiAddr(){
  	return Utils.bytesToString(this.giAddr);
  }

  public String toStringChAddr()
  {
    String s = "";
    for( int i = 0 ;i  < 6; ++i )
    {
      if( i != 0 )
        s += ":";
      s+=harToString(chAddr[i]);
    }
    return s;
  }

  public String toStringSname(){
  	String s= "NA";
    	return s;
  }

  public String toStringFile(){
  	String s= "NA";
    	return s;
  }

  public String toStringOptions()
  {
    String s= "NA";
    	return s;
  }
  public String toStringOptionsFirst()
  {
      String s = "";
      for( int i = 6; i < 7 ; ++i )
           s += " " + options[i];
      return s;
  }
    @Override
  public String toString()
  {
      String W= "Message Type: "+ op +"\nHardwareType: "+ hType +
              "\nHardware address Length: "+ hLenght +"\nHops: "
              + hOps +"\nTransaction ID: "+  toStringXId()  +
              "\nSeconds elapse: "+  toStringSecs() +
              "\nFlags: "+  toStringFlags()+
              "\nClient IP: "+  toStringCiAddr()+
              "\nYout IP: "+  toStringYiAddr()+
              "\nNext Server IP: "+  toStringSiAddr()+
              "\nRelay agent IP: "+  toStringGiAddr()+
              "\nClient MAC: "+  toStringChAddr()+
              "\nHost name: "+  toStringSname()+
              "\nFile: "+  toStringFile()+
              "\n"+toStringOptionsFirst()+
              "\nOptions "+  toStringOptions()+"\n";
        return W;
    }

    boolean isDiscover() {
        return options[6] == 1;
    }

    boolean isRequest() {
        return options[6]==3;
    }

    boolean isRelease() {
        return options[6]==7;
    }

    byte[] getIpOptions() {
       int indx = 4;
       
        int tam;
        while(true){
                if(options[indx] == (byte)50){
                        return Arrays.copyOfRange(options, indx+2, indx+6);
                }else{
                        tam = (int)options[indx+1];
                        indx+= tam +2;

                }
        }
    }
    public String toStringLog()
    {
    	String ret =  Utils.printTime()+ "\t|\t" +op +
        "\t|\t"+  toStringSecs() +
        "\t\t|\t"+  toStringCiAddr()+
        "\t|\t"+  toStringYiAddr()+
        "\t|\t"+  toStringSiAddr()+
        "\t|\t"+  toStringGiAddr()+
        "\t|\t"+  toStringChAddr()+
        "\t|\t"+ getOptionTipe();
    	
    	return ret;
    }
	private String getOptionTipe() {
		if( this.isDiscover() )
			return "Discover";
		if( this.isRelease() )
			return "Release";
		if( this.isRequest() )
			return "Request";
		return "";
	}
	public byte[] newNACK(byte[] temp, byte[] ipServer, Red red, int time) {
		 op = 2; // offer
	        siAddr = ipServer;
	        for( int i = 0 ;i < 316 ; ++i )
	               options[ 4+i ] = 0;
	        int indx = 4;
	        options[ indx++ ] = 53; //tipo
	        options[ indx++ ] = 1; //tam
	        options[ indx++ ] = 6;// ACK
	        
	        byte[] tempTime = Utils.intToByte(time);
	        options[ indx++ ] = 51; //time
	        options[ indx++ ] = 4; //tam
	        for( int i = 0 ; i < 4 ; ++i )
	            options[ indx++ ] = tempTime[ i ];
	        options[ indx++ ] = 1 ; //net
	        options[ indx++ ] = 4 ; //tam
	        for( int i = 0; i < 4 ; ++i )
	              options[ indx++ ] = red.mask[ i ]; //mask
	        options[ indx++ ] = 6 ; //dns
	        options[ indx++ ] = 4; //tam
	        for( int i = 0 ; i < 4 ; ++i )
	              options[ indx++ ] = red.dns[ i ]; // dns
	        options[ indx ] = FIN;
	        
	        return toBytes();
	}
}
