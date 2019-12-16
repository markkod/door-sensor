#include <WiFi.h>
#include <WiFiClient.h>
#include <WebServer.h>
#include "FirebaseESP32.h"

const char* ssid = "IOT_vork";
const char* password = "IOTParool";

const char* firebase_api_key = "AIzaSyBABEOmOf0OEeoz42VtPkIL4QcBIP6WF4I";
const char* firebase_url = "doorsensor-88fe4.firebaseio.com";

const char* doorName = "dummy";

const int led = 13;
bool doorState = false;

FirebaseData firebaseData;
FirebaseJson updateData;


void setup(void) {
  pinMode(led, OUTPUT);
  digitalWrite(led, 0);
  Serial.begin(115200);
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  Serial.println("");

  // Wait for connection
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected to ");
  Serial.println(ssid);
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());

  Firebase.begin("doorsensor-88fe4.firebaseio.com", "2FQSbyevQswCi9oZc2xfl4CEd7BmdSoUmgIyXHKE");

  //5. Optional, set AP reconnection in setup()
  Firebase.reconnectWiFi(true);

  //6. Optional, set number of auto retry when read/store data
  Firebase.setMaxRetry(firebaseData, 3);


  updateData.set("name", doorName);

}

void loop(void) {
  int hallValue = hallRead();
  //Serial.println(hallValue);
  if(hallValue < -30 && doorState) {
    doorState = false;
    updateData.set("isOpenState", false);
      if (Firebase.updateNode(firebaseData, "/doors/update", updateData)) {
        Serial.println("Door closed");
      
        Serial.println(firebaseData.dataPath());
      
        Serial.println(firebaseData.dataType());
      
        Serial.println(firebaseData.jsonString()); 
      
      } else {
        Serial.println(firebaseData.errorReason());
      }
  }
  else if(!doorState) {
    doorState = true;
    updateData.set("isOpenState", true);
      if (Firebase.updateNode(firebaseData, "/doors/update", updateData)) {
        Serial.println("Door opened!");
      
        Serial.println(firebaseData.dataPath());
      
        Serial.println(firebaseData.dataType());
      
        Serial.println(firebaseData.jsonString()); 
      
      } else {
        Serial.println(firebaseData.errorReason());
      }
    
  }
  
}
