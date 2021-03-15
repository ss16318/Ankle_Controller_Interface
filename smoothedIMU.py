from pynput.keyboard import Key, Controller
import serial
import time

serialPort = serial.Serial(port = "COM18", baudrate=9600)
keyboard = Controller()

print("Keep still - Player up/down!")
time.sleep(2)
line = serialPort.readline()
decodedLine = line.decode()
secondperipheralAngle = (decodedLine.split("\t")[0])
baseline_second = secondperipheralAngle
baseline_second = float(baseline_second)

print("Done")
print(baseline_second)

print("Keep still - Player left/right!")
time.sleep(2)
line = serialPort.readline()
decodedLine = line.decode()
firstperipheralAngle = (decodedLine.split("\t")[1])
baseline_first = firstperipheralAngle
baseline_first = float(baseline_first)

print("Done")
print(baseline_first)

flagDown = 0
flagUp = 0
flagLeft = 0
flagRight = 0
threshold = 1.5
flag_time_up = 0
flag_time_down = 0
flag_time_left = 0
flag_time_right = 0
t0 = 0
t1 = 0

while True:
    line = serialPort.readline()
    decodedLine = line.decode()
    firstPeripheralAngle = (decodedLine.split("\t")[1])
    secondperipheralAngle = (decodedLine.split("\t")[0])
    first_angle = float(firstPeripheralAngle)
    second_angle = float(secondperipheralAngle)
    

    if second_angle<baseline_second-threshold and flagUp==0 and flag_time_up==0:
        flag_time_up = 1
        time.sleep(0.15)
        print("Waiting Up")
        continue
    if second_angle<baseline_second-threshold and flagUp==0 and flag_time_up==1:
        print("up")
        flag_time_up = 0
        keyboard.press(Key.up)
        keyboard.release(Key.up)
        t0 = time.perf_counter()

        flagUp = 1
    

    if second_angle>baseline_second+threshold and flagDown ==0 and flag_time_down==0:
        flag_time_down = 1
        time.sleep(0.15)
        print("Waiting Down")

        continue
    if second_angle>baseline_second+threshold and flagDown ==0 and flag_time_down==1:
        print("down")
        flag_time_down = 0
        keyboard.press(Key.down)
        keyboard.release(Key.down)
        t0 = time.perf_counter()


        flagDown = 1
    
    if time.perf_counter()-t0>1.5:
        print("Reseting Up/Down...")
        flagUp = 0
        flagDown = 0
    ### Player Up/Down ################

    if first_angle<baseline_first-threshold and flagRight==0 and flag_time_right==0:
        flag_time_right = 1
        time.sleep(0.15)
        print("Waiting right")
        continue
    if first_angle<baseline_first-threshold and flagRight==0 and flag_time_right==1:
        print("right")
        flag_time_right = 0
        keyboard.press(Key.right)
        keyboard.release(Key.right)
        t1 = time.perf_counter()

        flagRight = 1
    

    if first_angle>baseline_first+threshold and flagLeft ==0 and flag_time_left==0:
        flag_time_left = 1
        time.sleep(0.15)
        print("Waiting left")

        continue
    if first_angle>baseline_first+threshold and flagLeft ==0 and flag_time_left==1:
        print("left")
        flag_time_left = 0
        keyboard.press(Key.left)
        keyboard.release(Key.left)
        t1 = time.perf_counter()


        flagLeft = 1

    if time.perf_counter()-t1>1.5:
        print("Reseting Sides...")
        flagLeft = 0
        flagRight = 0



    
    """ 
    if first_angle<baseline_first-threshold and flagRight==0:
        print("right")
        keyboard.press(Key.right)
        keyboard.release(Key.right)

        flagRight = 1

    if first_angle>baseline_first-threshold*0.1 and first_angle<baseline_first+threshold*0.1:
        flagLeft = 0
        flagRight = 0

    if first_angle>baseline_first+threshold and flagLeft ==0:
        print("left")
        keyboard.press(Key.left)
        keyboard.release(Key.left)

        flagLeft = 1
"""