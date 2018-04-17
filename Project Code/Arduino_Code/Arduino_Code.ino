#include <ESP8266HTTPClient.h>
#include <ArduinoJson.h>
#include <Firebase.h>
#include <FirebaseArduino.h>
#include <FirebaseCloudMessaging.h>
#include <FirebaseError.h>
#include <FirebaseHttpClient.h>
#include <FirebaseObject.h>
#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <SoftwareSerial.h>
SoftwareSerial RFID(2, 4); // to define software serial manually pin 2 for RX and pin 4 for TX
//----------------------------------------------------------------------------------------------------------------------------
// Set these to run example.
#define FIREBASE_HOST "seniorproject-cpit499db.firebaseio.com"  // DATABASE NAME
#define FIREBASE_AUTH "Sx2s1pp92HT3TH93f5nt3iX8h8sz6H794K8SaKXO"  // FIREBASE KEY (Web API Key)
#define WIFI_SSID "STC_HAMDAN"   // NETWORK NAME
#define WIFI_PASSWORD "s500600ss7"    //NETWORK PASSWORD


//----------------------------------------------------------------------------------------------------------------------------

// VARIABLES
boolean water = true, coakcola = true, milk = true, juice = true, yogurt = true, pepsi = true;
int inputRead;
int count = 0;
String tag = "";
String path = "/FRIDGE/sLjJxotWl6YNvcSSTdWAkM0MuZS2"; //Tha path where to put our fridge data
//----------------------------------------------------------------------------------------------------------------------------

// this is were code start
void setup()
{
  RFID.begin(9600);    // start serial to RFID reader to read and write command;
  Serial.begin(9600);  // start serial to PC (just for checking - not usufel for)

  // beggin connecting to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");

  // check if wifi is connect
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP()); // print the current ip address

  // connect to the firebase (fridge database)
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.remove(path); // empty the database in case of some products is out of the fridge
}

//----------------------------------------------------------------------------------------------------------------------------
void loop()
{

  // start scanning the rfid tags

  if (RFID.available() > 0)
  {
    inputRead = RFID.read();

    // handle error

    if (Firebase.failed()) {
      Serial.print("setting /number failed:");
      Serial.println(Firebase.error());
      return;
    }

    Serial.print(inputRead);
    tag += String(inputRead);



    count++;

    // rfid reader is sending data byte by byte
    // use the next condition to split the data (each RFID tag data is a 16 byte in total)
    if (count > 16) {
      Serial.println("");


      // these conditions prevent the data dublication
      if (tag == "002260021191911053316591660103255" && water == true) {
        Firebase.pushString(path , "water");    // add the product to the fridge database
        water = false;  // prevent the data to be dublicated
      }
      else if (tag == "002260021191911053514433620103255" && coakcola == true) {
        Firebase.pushString(path , "coak-cola");
        coakcola = false;
      }
      else if (tag == "0022600211919110524112841900223255" && milk == true) {
        Firebase.pushString(path , "milk");
        milk = false;
      }
      else if (tag == "000000003222162369990251255" && juice == true) {
        Firebase.pushString(path , "juice");
        juice = false;
      }
      else if (tag == "0000000032221623691010249255" && yogurt == true) {
        Firebase.pushString(path , "yogurt");
        yogurt = false;
      }
      else if (tag == "0000000032221623691000250255" && pepsi == true) {
        Firebase.pushString(path , "pepsi");
        pepsi = false;
      }

      count = 0; // reset counter to read next RFID tag data
      tag = ""; // reset tag to
    }

  }
}




