/*
  Battery Monitor

  This example creates a BLE peripheral with the standard battery service and
  level characteristic. The A0 pin is used to calculate the battery level.

  The circuit:
  - Arduino MKR WiFi 1010, Arduino Uno WiFi Rev2 board, Arduino Nano 33 IoT,
    Arduino Nano 33 BLE, or Arduino Nano 33 BLE Sense board.

  You can use a generic BLE central app, like LightBlue (iOS and Android) or
  nRF Connect (Android), to interact with the services and characteristics
  created in this sketch.

  This example code is in the public domain.
*/

#include <ArduinoBLE.h>
#include <Arduino_LSM9DS1.h>

#define BLE_UUID_LED_IMU_SERVICE                  "39B10000-E8F2-537E-4F6C-D104768A1214" //all peripherals for one central need the same UUIDs, modify the UUID's so you don't connect to other students' devices 
#define BLE_UUID_LED_COMMAND                      "39B10000-E8F2-537E-4F6C-D104768A1215" 
#define BLE_UUID_IMU_X                            "39B10000-E8F2-537E-4F6C-D104768A1216"
#define BLE_UUID_IMU_Y                            "39B10000-E8F2-537E-4F6C-D104768A1217"
#define BLE_UUID_IMU_Z                            "39B10000-E8F2-537E-4F6C-D104768A1218"

 // BLE Service
BLEService bleService( BLE_UUID_LED_IMU_SERVICE );

// BLE Characteristics 
BLEByteCharacteristic ledChar( BLE_UUID_LED_COMMAND , BLERead | BLENotify | BLEWrite ); 
BLEFloatCharacteristic imuxChar( BLE_UUID_IMU_X , BLERead | BLENotify | BLEWrite ); 
BLEFloatCharacteristic imuyChar( BLE_UUID_IMU_Y , BLERead | BLENotify | BLEWrite ); 
BLEFloatCharacteristic imuzChar( BLE_UUID_IMU_Z , BLERead | BLENotify | BLEWrite ); 
//You could add another characteristic to differentiate each board (to know which device to command and record from)


// IMU parameters
float AccX, AccY, AccZ;
float GyroX, GyroY, GyroZ;
float accAngleX, accAngleY, accAngleZ, gyroAngleX, gyroAngleY, gyroAngleZ;
float roll, pitch, yaw;
float AccErrorX, AccErrorY, AccErrorZ, GyroErrorX, GyroErrorY, GyroErrorZ;
long elapsedTime, currentTime, previousTime;

int c = 0;
int counter = 0;
void setup() {
  currentTime = millis(); // This should be 0 at the beginning

  // Prepare LED pins.
  
  Serial.begin(9600);    // initialize serial communication
  //while (!Serial); // commented out so peripherals do not need serial monitor

  // begin IMU initialization
  if (!IMU.begin()) {
    Serial.println("Failed to initialize IMU!");
    while (1);
  }
  calculate_IMU_error();

  delay(20);

  pinMode(LED_BUILTIN, OUTPUT); // initialize the built-in LED pin to indicate when a central is connected

  // begin initialization
  if (!BLE.begin()) {
    Serial.println("starting BLE failed!");

    while (1);
  }

  /* Set a local name for the BLE device */
  BLE.setLocalName("BLE_IMU");
  BLE.setAdvertisedService(bleService); // add the service UUID
  bleService.addCharacteristic(ledChar); // add the characteristic
  bleService.addCharacteristic(imuxChar); // add the characteristic
  bleService.addCharacteristic(imuyChar); // add the characteristic
  bleService.addCharacteristic(imuzChar); // add the characteristic
  BLE.addService(bleService); // Add the battery service
  imuxChar.writeValue(0); // set initial value for this characteristic
  imuyChar.writeValue(0); // set initial value for this characteristic
  imuzChar.writeValue(0); // set initial value for this characteristic

  /* Start advertising BLE.  It will start continuously transmitting BLE
     advertising packets and will be visible to remote BLE central devices
     until it receives a new connection */

  // start advertising
  BLE.advertise();
  Serial.println("Bluetooth device active, waiting for connections...");
}

void loop() {
 
  // wait for a BLE central
  BLEDevice central = BLE.central();

  // if a central is connected to the peripheral:
  if (central) {
    Serial.print("Connected to central: ");
    // print the central's BT address:
    Serial.println(central.address());
    // turn on the LED to indicate the connection:
    digitalWrite(LED_BUILTIN, HIGH);

    // while the central is connected:
    while (central.connected()) {
      updateIMU(); //code to read IMU
      updateLED(); //code to command LED
    }
    // when the central disconnects, turn off the LED:
    digitalWrite(LED_BUILTIN, LOW);
    Serial.print("Disconnected from central: ");
    Serial.println(central.address());
  }
}

void updateIMU() {

    float x, y, z;

    // ------------ Read accelerometer ------------
    // Accelerometer range is set at -4 |+4 g with -/+0.122 mg resolution
    if (IMU.accelerationAvailable()) {
      IMU.readAcceleration(AccX, AccY, AccZ);
    }
  
    // ------------ Read gyroscope ------------
    // Gyroscope range is set at -2000 | +2000 dps with +/-70 mdps resolution
    if (IMU.gyroscopeAvailable()) {
      IMU.readGyroscope(GyroX, GyroY, GyroZ);
    }
  
    // ------------ Calculations ------------
    // Calculating Roll and Pitch from the accelerometer data
    accAngleX = (atan(AccY / sqrt(pow(AccX, 2) + pow(AccZ, 2))) * 180 / PI); // AccErrorX ~(0.58) See the calculate_IMU_error()custom function for more details
    accAngleY = (atan(AccX / sqrt(pow(AccY, 2) + pow(AccZ, 2))) * 180 / PI); // AccErrorY ~(-1.58)
    //accAngleY = (atan(-1 * AccX / sqrt(pow(AccY, 2) + pow(AccZ, 2))) * 180 / PI) - AccErrorY; // AccErrorY ~(-1.58)
    accAngleZ = (atan(AccZ / sqrt(pow(AccX, 2) + pow(AccZ, 2))) * 180 / PI);
  
  
    // Time
    previousTime = currentTime;        // Previous time is stored before the actual time read
    currentTime = millis();            // Current time actual time read
    elapsedTime = (currentTime - previousTime) / 1000; // Divide by 1000 to get seconds
  
    // Correct the outputs with the calculated error values
    GyroX = GyroX - GyroErrorX; // GyroErrorX ~(-0.56)
    GyroY = GyroY - GyroErrorY; // GyroErrorY ~(2)
    GyroZ = GyroZ - GyroErrorZ; // GyroErrorZ ~ (-0.8)
  
  
    // Currently the raw values are in degrees per seconds, deg/s, so we need to multiply by seconds (s) to get the angle in degrees
  
    gyroAngleX = gyroAngleX + GyroX * elapsedTime; // deg/s * s = deg
    gyroAngleY = gyroAngleY + GyroY * elapsedTime;
    gyroAngleZ = gyroAngleZ + GyroZ * elapsedTime;
  
  
  
    // ------------ Complementary Filter ------------
    // combine accelerometer and gyro angle values
    float consComplFtr = 0.1;    // Constant for Complimentary Filter
  
    roll = ((1 - consComplFtr) * gyroAngleX) + (consComplFtr * accAngleX);
    pitch = ((1 - consComplFtr) * gyroAngleY) + (consComplFtr * accAngleY);
    yaw = ((1 - consComplFtr) * gyroAngleZ) + (consComplFtr * accAngleZ);
  
    // Add results to an array
    float IMU_rot[3] = {roll, yaw, pitch};

 
    imuxChar.writeValue(roll);
    imuyChar.writeValue(pitch);
    imuzChar.writeValue(yaw);

    Serial.print(roll); 
    Serial.print('\t');
    Serial.print(pitch); 
    Serial.print('\t');         
    Serial.print(yaw); 
    Serial.println('\t'); 
  
}
// ------------ IMU Calibration ------------
void calculate_IMU_error() {
  // We can call this funtion in the setup section to calculate the accelerometer and gyro data error. From here we will get the error values used in the above equations printed on the Serial Monitor.
  // Note that we should place the IMU flat in order to get the proper values, so that we then can the correct values
  // This is due to the lack of magnetometer in the LSM6DS3

  // Read accelerometer values 200 times
  while (c < 200) {
    IMU.readAcceleration(AccX, AccY, AccZ); // Acceleration data is returned in g
    // Sum all readings
    AccErrorX =  (AccErrorX + ((atan((AccY) / sqrt(pow((AccX), 2) + pow((AccZ), 2))) * 180 / PI))) / 2;
    AccErrorY =  (AccErrorY + ((atan((AccX) / sqrt(pow((AccY), 2) + pow((AccZ), 2))) * 180 / PI))) / 2;
    AccErrorZ =  (AccErrorZ + ((atan((AccZ) / sqrt(pow((AccX), 2) + pow((AccZ), 2))) * 180 / PI))) / 2;
    c++;
  }
  //Divide the sum by 200 to get the error value
  AccErrorX = AccErrorX / 200;
  AccErrorY = AccErrorY / 200;
  AccErrorZ = AccErrorZ / 200;

  c = 0;
  // Read gyro values 200 times
  while (c < 200) {
    IMU.readGyroscope(GyroX, GyroY, GyroZ); // Gyroscope data is returned in dps (degrees per second)
    // Sum all readings
    GyroErrorX = GyroErrorX + (GyroX);
    GyroErrorY = GyroErrorY + (GyroY);
    GyroErrorZ = GyroErrorZ + (GyroZ);
    c++;
  }
  //Divide the sum by 200 to get the error value
  GyroErrorX = GyroErrorX / 200;
  GyroErrorY = GyroErrorY / 200;
  GyroErrorZ = GyroErrorZ / 200;

}
void updateLED(){
    if (ledChar.written()) {
    if (ledChar.value()) {   // any value other than 0
      Serial.println("LED on");
      digitalWrite(LED_BUILTIN, HIGH);         // will turn the LED on
    } 
    else {                              // a 0 value
      Serial.println(F("LED off"));
      digitalWrite(LED_BUILTIN, LOW);          // will turn the LED off
    }
  }
}
