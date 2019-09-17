#include <ESP8266WiFi.h>
#include <PubSubClient.h>
//#include <OneWire.h>
#include <DallasTemperature.h>



#define GPIO3 3
#define ONE_WIRE_BUS 0


const char* inTopic = "esp8266_in";
const char* outTopic = "esp8266_out";
float temp = 0.0;

WiFiClient espClient;
PubSubClient client(espClient);
OneWire oneWire(ONE_WIRE_BUS);
DallasTemperature sensors(&oneWire);

void callback(char* topic, byte* payload, unsigned int length) {
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
  }
  if (((char)payload[0] == 'O') && ((char)payload[1] == 'N'))
  {
     digitalWrite(GPIO3,HIGH); 
  }
  if (((char)payload[0] == 'O') && ((char)payload[1] == 'F') && ((char)payload[2] == 'F')) 
  {
      digitalWrite(GPIO3,LOW);
  }
  if (((char)payload[0] == 'T') && ((char)payload[1] == 'E') && ((char)payload[2] == 'M'))
  {
    sensors.requestTemperatures();
    temp = sensors.getTempCByIndex(0);
    client.publish(outTopic,String(temp).c_str(), true);
  }
  if (((char)payload[0] == 'S') && ((char)payload[1] == 'T') && ((char)payload[2] == 'A') && ((char)payload[3] == 'T'))
  {
     if(digitalRead(GPIO3))
     {
        client.publish(outTopic,"ON", true);
     }
    else
    {
        client.publish(outTopic,"OFF", true);
     } 
  }

}

void setup() {

  Serial.begin(115200,SERIAL_8N1,SERIAL_TX_ONLY);
  pinMode(GPIO3, OUTPUT);
  digitalWrite(GPIO3,LOW);
  sensors.begin();
  
  WiFi.begin("Redmi", "1234567890");
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(100);
    yield();
  }
  client.setServer("farmer.cloudmqtt.com", 18511);
  client.setCallback(callback);

  while (!client.connected()) {
    client.connect("ESP32Client", "hfkdazjv", "O7S893boW_-o" );
    delay(500);
  }
  client.subscribe(inTopic);//topic name="abc"

}

void loop() {
  client.loop();
}
