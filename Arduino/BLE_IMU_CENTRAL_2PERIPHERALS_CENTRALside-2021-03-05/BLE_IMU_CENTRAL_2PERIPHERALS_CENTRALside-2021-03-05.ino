/*
  This example creates a BLE central that scans for peripherals with an LED and IMU service.
  The sketch will subscribe to the LED and IMU characteristic and send the values to the Serial Plotter.
  Change from Serial.print to Serial.write to send data through the serial port to another application
  There is no error checking. The goal is only to show how multiple BLE peripherals can be connected to one BLE central.

  The circuit:
  - BLE Central MUST be an Arduino Nano IoT
  - BLE Peripherals can be Arduino BLE Sense and possibly also Arduino Nano IoT

  This example code is in the public domain.
*/

#include <ArduinoBLE.h>

//----------------------------------------------------------------------------------------------------------------------
// BLE UUIDs
//----------------------------------------------------------------------------------------------------------------------

// https://www.bluetooth.com/specifications/gatt/services//
// https://www.bluetooth.com/specifications/gatt/characteristics/

#define BLE_UUID_LED_IMU_SERVICE                  "39B10000-E8F2-537E-4F6C-D104768A1214" //all peripherals for one central need the same UUIDs, modify the UUID's so you don't connect to other students' devices 
#define BLE_UUID_LED_COMMAND                      "39B10000-E8F2-537E-4F6C-D104768A1215" 
#define BLE_UUID_IMU_X                            "39B10000-E8F2-537E-4F6C-D104768A1216"
#define BLE_UUID_IMU_Y                            "39B10000-E8F2-537E-4F6C-D104768A1217"
#define BLE_UUID_IMU_Z                            "39B10000-E8F2-537E-4F6C-D104768A1218"

#define BLE_MAX_PERIPHERALS 2 // you may be able to connect up to 8 peripherals
#define BLE_SCAN_INTERVALL 5000 //how long you will wait in ms to connect to all peripherals

BLEDevice peripherals[BLE_MAX_PERIPHERALS];
BLECharacteristic ledCharacteristics[BLE_MAX_PERIPHERALS]; //initialize each characteristic
BLECharacteristic imuxCharacteristics[BLE_MAX_PERIPHERALS]; //initialize each characteristic
BLECharacteristic imuyCharacteristics[BLE_MAX_PERIPHERALS]; //initialize each characteristic
BLECharacteristic imuzCharacteristics[BLE_MAX_PERIPHERALS]; //initialize each characteristic
int peripheralsConnected = 0;

const int BLE_LED_PIN = LED_BUILTIN;
const int BLE_SCAN_LED_PIN = LED_BUILTIN;


void setup()
{
  Serial.begin( 9600 );
  while ( !Serial ); //comment this out if you are not using Arduino for plotting/monitoring

  pinMode( BLE_SCAN_LED_PIN, OUTPUT ); 

  Serial.print( "starting BLE \n" );
  BLE.begin(); // start BLE

  digitalWrite( BLE_SCAN_LED_PIN, HIGH ); // blink while scanning for peripherals
  BLE.scanForUuid( BLE_UUID_LED_IMU_SERVICE );

  int peripheralCounter = 0;
  unsigned long startMillis = millis();
  while ( millis() - startMillis < BLE_SCAN_INTERVALL && peripheralCounter < BLE_MAX_PERIPHERALS )
  {
    BLEDevice peripheral = BLE.available();

    if ( peripheral )
    {
      Serial.print( "peripheral found \n" );
      if ( peripheral.localName() == "BLE_IMU" )
      {
        Serial.print( "peripheral name is BLE_IMU" );
        boolean peripheralAlreadyFound = false;
        for ( int i = 0; i < peripheralCounter; i++ )
        {
          if ( peripheral.address() == peripherals[i].address() )
          {
            peripheralAlreadyFound = true;
          }
        }
        if ( !peripheralAlreadyFound )
        {
          peripherals[peripheralCounter] = peripheral;
          peripheralCounter++;
        }
      }
    }
  }

  BLE.stopScan();
  digitalWrite( BLE_SCAN_LED_PIN, LOW ); // done scanning for peripherals

  for ( int i = 0; i < peripheralCounter; i++ )
  {
    peripherals[i].connect(); // connect to each peripheral
    peripherals[i].discoverAttributes();
    Serial.print( "peripheral told to connect \n" );
    //initialize each characteristic
    BLECharacteristic ledCharacteristic = peripherals[i].characteristic( BLE_UUID_LED_COMMAND );
    if ( ledCharacteristic )
    {
      ledCharacteristics[i] = ledCharacteristic;
      ledCharacteristics[i].subscribe();
    }

    BLECharacteristic imuxCharacteristic = peripherals[i].characteristic( BLE_UUID_IMU_X );
    if ( imuxCharacteristic )
    {
      imuxCharacteristics[i] = imuxCharacteristic;
      imuxCharacteristics[i].subscribe();
    }

    BLECharacteristic imuyCharacteristic = peripherals[i].characteristic( BLE_UUID_IMU_Y );
    if ( imuyCharacteristic )
    {
      imuyCharacteristics[i] = imuyCharacteristic;
      imuyCharacteristics[i].subscribe();
    }

    BLECharacteristic imuzCharacteristic = peripherals[i].characteristic( BLE_UUID_IMU_Z );
    if ( imuzCharacteristic )
    {
      imuzCharacteristics[i] = imuzCharacteristic;
      imuzCharacteristics[i].subscribe();
    }
  }
  peripheralsConnected = peripheralCounter;
}

float imux[BLE_MAX_PERIPHERALS];
float imuy[BLE_MAX_PERIPHERALS];
float imuz[BLE_MAX_PERIPHERALS];
bool newDataPrint = false;
int buttonState = 0;
float imux1, imuy1, imuz1;
void loop()
{
  newDataPrint = false;
  for ( int i = 0; i < peripheralsConnected; i++ )
  {
    if ( imuxCharacteristics[i].valueUpdated() )
    {
      newDataPrint = true;
      imuxCharacteristics[i].readValue( &imux1, 4 );
      imux[i] = imux1;
      //Serial.print( imux[i] );
    }
    if ( imuyCharacteristics[i].valueUpdated() )
    {
      newDataPrint = true;
      float imuy1;
      imuyCharacteristics[i].readValue( &imuy1, 4 );
      imuy[i] = imuy1;
      //Serial.print( imuy[i] );
    }
    if ( imuzCharacteristics[i].valueUpdated() )
    {
      newDataPrint = true;
      float imuz1;
      imuzCharacteristics[i].readValue( &imuz1, 4 );
      imuz[i] = imuz1;
      //Serial.print( imuz[i] );
    }
  }
  
  if ( newDataPrint )
  {
    if (buttonState == 0) {
      // write 0x01 to turn the LED on
      buttonState = 1;
      ledCharacteristics[0].writeValue((byte)0x01);
      ledCharacteristics[1].writeValue((byte)0x01);
    } else {
      // write 0x00 to turn the LED off
      buttonState = 0;
      ledCharacteristics[0].writeValue((byte)0x00); //to peripheral 0
      ledCharacteristics[1].writeValue((byte)0x00); //to peripheral 1
    }
    
    for ( int i = 0; i < peripheralsConnected; i++ )
    {
      //Serial.print("Peripheral ");
      //Serial.print(i);
      //Serial.print(" : ");
      //Serial.print('\t');
      //Serial.print(imux[i]);
      //Serial.print('\t');
      Serial.print(imuy[i]);
      Serial.print('\t');       
      //Serial.print(imuz[i]);
      //Serial.println('\t');
    }
    Serial.print( "\n" );
  }
}
