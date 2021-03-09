from pynput.keyboard import Key, Controller
import serial
import time

serialPort = serial.Serial(port = "COM9", baudrate=9600)
keyboard = Controller()

print("Keep still!")
time.sleep(2)
line = serialPort.readline()
baseline = line.decode()
baseline = float(baseline)
#thresholdUp = 20
#threshold = 15
print("Done")
print(baseline)
flagDown = 0
flagUp = 0

def getThreshold( serialPort, deltaTime, effortRatio=0.8):
        starTime = time.time()
        maxUp = -1
        maxDown = 1

        while time.time()-starTime<deltaTime:
            line = serialPort.readline()
            decodedLine = line.decode()
            XAngle = float(decodedLine)
            if XAngle < maxDown:
                maxDown = XAngle
            if XAngle > maxUp:
                maxUp = XAngle
        thresholdUp = maxUp * effortRatio
        thresholdDown = maxDown * effortRatio
        return thresholdUp, thresholdDown

thresholdUp, threshold = getThreshold(serialPort,5)
threshold = abs(threshold)
print(thresholdUp, "  ", threshold)
while True:
    line = serialPort.readline()
    decodedLine = line.decode()
    angle = float(decodedLine)
    if angle<baseline-thresholdUp and flagUp==0:
        print("up")
        keyboard.press(Key.up)
        keyboard.release(Key.up)

        flagUp = 1

    if angle>baseline-threshold*0.1 and angle<baseline+thresholdUp*0.1:
        flagUp = 0
        flagDown = 0

    if angle>baseline+threshold and flagDown ==0:
        print("down")
        keyboard.press(Key.down)
        keyboard.release(Key.down)

        flagDown = 1
