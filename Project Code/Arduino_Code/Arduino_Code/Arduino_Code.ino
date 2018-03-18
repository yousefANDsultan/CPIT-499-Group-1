#include <SoftwareSerial.h>
SoftwareSerial RFID(2, 4); // RX and TX

int i;
int x = 0;

unsigned char stopBuzzer[6]={0xa0, 0x04, 0xb0, 0x00, 0x01, 0xaB};

void setup()
{
  RFID.begin(9600);    // start serial to RFID reader
  Serial.begin(9600);  // start serial to PC 
  //RFID.write(stopBuzzer,6);
}

void loop()
{ 
  if (RFID.available() > 0) 
  {
     i = RFID.read();
    
     Serial.print(i);
    x++;
    if (x>16){
      Serial.println("");
      x=0;
      
      }
  }

   
}

