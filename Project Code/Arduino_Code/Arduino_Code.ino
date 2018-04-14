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


// Set these to run example.
#define FIREBASE_HOST "seniorproject-cpit499db.firebaseio.com"
#define FIREBASE_AUTH "Sx2s1pp92HT3TH93f5nt3iX8h8sz6H794K8SaKXO"
#define WIFI_SSID "The Void"
#define WIFI_PASSWORD "S7777777s"
#include <SoftwareSerial.h>
SoftwareSerial RFID(2, 4); // RX and TX

int i;
int x = 0;
String tag = "";
unsigned char stopBuzzer[6] = {0xa0, 0x04, 0xb0, 0x00, 0x01, 0xaB};

void setup()
{
  RFID.begin(9600);    // start serial to RFID reader
  Serial.begin(9600);  // start serial to PC

  // connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  //RFID.write(stopBuzzer,6);
  Firebase.remove("RFID TAG");
}

void loop()
{
  if (RFID.available() > 0)
  {
    i = RFID.read();

    // handle error

    if (Firebase.failed()) {
      Serial.print("setting /number failed:");
      Serial.println(Firebase.error());
      return;
    }

    Serial.print(i);
    tag += String(i);



    x++;
    if (x > 16) {
      Serial.println("");
      Serial.println(tag);
      x = 0;
      Firebase.pushString("RFID TAG", tag);
      tag = "";
      delay(1000);
    }

  }
}




