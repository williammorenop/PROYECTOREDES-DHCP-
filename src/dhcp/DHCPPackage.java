
package dhcp;

public class DHCPPackage
{
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
      options = new byte[792];

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
      for(int h=0;h<2;h++)
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
      //110 - file
      for(int w=0;w<128;w++)
        file[w]=arr[110+w];
      // 238 -options
      for(int h=0;h<750;h++){
        options[h]=arr[238+h];
      }
        
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
            num|=xId[ i ] ;
        }
        
    	return num;
  }
  public int toStringSecs()
  {
  	int num = 0;
    	for( int i =0  ;i < 2 ; ++i )
        {
            num <<= 8;
            num |= secs[ i ];
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
    String s = "";
    for( int i = 0 ;i  < 4; ++i )
    {
      if( i != 0 )
        s += ".";
      s+=Byte.toString(ciAddr[i]);
    }
    return s;
  }
  
  public String toStringYiAddr(){
  	String s= "";
    	for( int i = 0 ; i < 4 ; ++i )
        {
          if(i!=0)
            s += ".";
          s+=Byte.toString(yiAddr[ i ]);
        }
    	return s;
  }
   public String toStringSiAddr()
  {
    String s = "";
    for( int i = 0 ;i  < 4; ++i )
    {
      if( i != 0 )
        s += ".";
      s+=Byte.toString(siAddr[i]);
    }
    return s;
  }
       
  public String toStringGiAddr(){
  	String s= "";
    	for( int i = 0 ; i < 4 ; ++i )
        {
          if(i!=0)
            s += ".";
          s+=Byte.toString(giAddr[ i ]);
        }
    	return s;
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
              "\nOptions "+  toStringOptions()+"\n";
        return W;
    }	       

}

             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             
             