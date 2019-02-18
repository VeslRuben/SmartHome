#include <Wire.h>
#include <Servo.h>

#define SLAVE_ADDRESS 0x04
byte reg;
byte data;
bool newData = false;

byte temprature;

int servoPin = 11;
Servo servo1;  

void setup() {
//Serial.begin(9600); // start serial for output
// initialize i2c as slave
Wire.begin(SLAVE_ADDRESS);

// define callbacks for i2c communication
Wire.onReceive(receiveData);
Wire.onRequest(sendData);

//Serial.println("Ready!");

servo1.attach(servoPin); 
}

void loop() {
  if(newData){
    if(reg == servoPin){
      servo1.write(data);
      //Serial.println(data);
    }
  }
  //Serial.println(data);
  float voltage = analogRead(A0) * 0.004882814;   //convert the analog reading, which varies from 0 to 1023, back to a voltage value from 0-5 volts
  temprature = (voltage - 0.5) * 100.0;       //convert the voltage to a temperature in degrees Celsius
  delay(10);
}

// callback for received data
void receiveData(int byteCount){

  if(Wire.available()){
    reg = Wire.read();   //Read the reg index
  }
  if(Wire.available()){
    data = Wire.read();   //Read the data
  }
  newData = true;
while(Wire.available()) {
byte flushData = Wire.read(); //Flushes the buffer
  }
}


// callback for sending data
void sendData(){
Wire.write(temprature);
}
