
from pynput.keyboard import Key, Controller
import serial
import time
import keyboard
import cv2
import numpy as np
import sys
from pygame import mixer 
#serialPort = serial.Serial(port = "COM18", baudrate=9600)

class FootFlex():
    def __init__(self, numberPlayers, baseFilename):
        self.numberPlayers = numberPlayers
        self.thresholdFirstPlayer = 0 #Up/Down
        self.thresholdSecondPlayer = 0
        self.baseFilename = baseFilename
        self.Pykeyboard = Controller()
        print("Number players set")

    def calibrate(self):
        if self.numberPlayers == 1:
            line = serialPort.readline()
            decodedLine = line.decode()
            singleperipheralAngle = (decodedLine.split("\t")[0])
            baselineSinglePlayer = singleperipheralAngle
            self.baselineSinglePlayer = float(baselineSinglePlayer)
        if self.numberPlayers == 2:
            line = serialPort.readline()
            decodedLine = line.decode()
            firstperipheralAngle = (decodedLine.split("\t")[1])
            baseline_first = firstperipheralAngle
            self.baseline_first = float(baseline_first)
            #Play sound and wait 2 seconds
            time.sleep(2)
            line = serialPort.readline()
            decodedLine = line.decode()
            secondperipheralAngle = (decodedLine.split("\t")[0])
            baseline_second = secondperipheralAngle
            self.baseline_second = float(baseline_second)
            #Play sound calibration done
        print("calibration done")
    def setLevel(self,level):
        print("Let's set level")

        if self.numberPlayers == 1:
            self.thresholdSinglePlayer = 0.5*level
            print("Threshold ",self.thresholdSinglePlayer)

            self.playSinglePlayerGame()
        if self.numberPlayers == 2:
            if self.thresholdFirstPlayer == 0:
                self.thresholdFirstPlayer = 0.5*level
            else:
                self.thresholdSecondPlayer = 0.5*level
            self.playMultiplayerGame()

    def playReps(self, number):
         # Load the popular external library
        mixer.init()
        mixer.music.load(self.baseFilename+"//"+number+".mp3")
        mixer.music.play()

    def playSinglePlayerGame(self):
        print("Single Player")
        flagDown = 0
        flagUp = 0
        flag_time_up = 0
        flag_time_down = 0
        t0 = 0
        t1 = 0
        maxUpAngle = []
        maxDownAngle = []
        averageAngle = []
        countReps = 0
        while True:
            line = serialPort.readline()
            decodedLine = line.decode()
            peripheralAngle = (decodedLine.split("\t")[0])
            angle = float(peripheralAngle)
            averageAngle.append(angle)

            if angle<self.baselineSinglePlayer-self.thresholdSinglePlayer and flagUp==0 and flag_time_up==0:
                flag_time_up = 1
                time.sleep(0.15)
                print("Waiting Up")
                continue
            if angle<self.baselineSinglePlayer-self.thresholdSinglePlayer and flagUp==0 and flag_time_up==1:
                print("up")
                flag_time_up = 0
                self.Pykeyboard.press(Key.up)
                self.Pykeyboard.release(Key.up)
                countReps = countReps + 1
                t0 = time.perf_counter()
                flagUp = 1
            

            if angle>self.baselineSinglePlayer+self.thresholdSinglePlayer and flagDown ==0 and flag_time_down==0:
                flag_time_down = 1
                time.sleep(0.15)
                print("Waiting Down")

                continue
            if angle>self.baselineSinglePlayer+self.thresholdSinglePlayer and flagDown ==0 and flag_time_down==1:
                print("down")
                flag_time_down = 0
                self.Pykeyboard.press(Key.down)
                self.Pykeyboard.release(Key.down)
                countReps = countReps + 1
                t0 = time.perf_counter()
                flagDown = 1
            
            if time.perf_counter()-t0>1.5:
                print("Reseting Up/Down...")
                flagUp = 0
                flagDown = 0
            ### Player Up/Down ################

            if flagUp == 1:
                maxUpAngle.append(angle)
            if flagDown == 1:
                maxDownAngle.append(angle)
            if countReps % 5 and countReps !=0:
                self.playReps(str(countReps))
            
            if keyboard.is_pressed('7'):    # Esc key to stop
                return
            if keyboard.is_pressed('8'):
                f = open("SinglePlayer.txt", "w")
                f.write(str(min(maxUpAngle)))
                f.write(",")
                f.write(str(max(maxDownAngle)))
                f.write(",")
                f.write(str(np.mean(averageAngle)))
                f.write(",")
                f.write(str(len(maxUpAngle)))
                f.write(",")
                f.write(str(len(maxDownAngle)))
                f.close()
                return

    
    def playMultiplayerGame(self):
        flagDown = 0
        flagUp = 0
        flagLeft = 0
        flagRight = 0
        flag_time_up = 0
        flag_time_down = 0
        flag_time_left = 0
        flag_time_right = 0
        t0 = 0
        t1 = 0
        maxUpAngleFirstPlayer = np.empty()
        maxDownAngleFirstPlayer = np.empty()
        averageAngleFirstPlayer = np.empty()

        maxUpAngleSecondPlayer = np.empty()
        maxDownAngleSecondPlayer = np.empty()
        averageAngleSecondPlayer = np.empty()

        countRepsFirstPlayer = 0
        countRepsSecondPlayer = 0
        while True:
            line = serialPort.readline()
            decodedLine = line.decode()
            firstPeripheralAngle = (decodedLine.split("\t")[1])
            secondperipheralAngle = (decodedLine.split("\t")[0])
            first_angle = float(firstPeripheralAngle)
            second_angle = float(secondperipheralAngle)
            averageAngleFirstPlayer.append(secondperipheralAngle)
            averageAngleSecondPlayer.append(firstPeripheralAngle)

            

            if second_angle<baseline_second-self.thresholdFirstPlayer and flagUp==0 and flag_time_up==0:
                flag_time_up = 1
                time.sleep(0.15)
                print("Waiting Up")
                continue
            if second_angle<baseline_second-self.thresholdFirstPlayer and flagUp==0 and flag_time_up==1:
                print("up")
                flag_time_up = 0
                self.Pykeyboard.press(Key.up)
                self.Pykeyboard.release(Key.up)
                countRepsFirstPlayer = countRepsFirstPlayer + 1
                t0 = time.perf_counter()

                flagUp = 1
            

            if second_angle>baseline_second+self.thresholdFirstPlayer and flagDown ==0 and flag_time_down==0:
                flag_time_down = 1
                time.sleep(0.15)
                print("Waiting Down")

                continue
            if second_angle>baseline_second+self.thresholdFirstPlayer and flagDown ==0 and flag_time_down==1:
                print("down")
                flag_time_down = 0
                self.Pykeyboard.press(Key.down)
                self.Pykeyboard.release(Key.down)
                countRepsFirstPlayer = countRepsFirstPlayer + 1

                t0 = time.perf_counter()


                flagDown = 1
            
            if time.perf_counter()-t0>1.5:
                print("Reseting Up/Down...")
                flagUp = 0
                flagDown = 0
            ### Player Up/Down ################

            if first_angle<baseline_first-self.thresholdSecondPlayer and flagRight==0 and flag_time_right==0:
                flag_time_right = 1
                time.sleep(0.15)
                print("Waiting right")
                continue
            if first_angle<baseline_first-self.thresholdSecondPlayer and flagRight==0 and flag_time_right==1:
                print("right")
                flag_time_right = 0
                self.Pykeyboard.press(Key.right)
                self.Pykeyboard.release(Key.right)
                countRepsSecondPlayer = countRepsSecondPlayer + 1

                t1 = time.perf_counter()

                flagRight = 1
            

            if first_angle>baseline_first+self.thresholdSecondPlayer and flagLeft ==0 and flag_time_left==0:
                flag_time_left = 1
                time.sleep(0.15)
                print("Waiting left")

                continue
            if first_angle>baseline_first+self.thresholdSecondPlayer and flagLeft ==0 and flag_time_left==1:
                print("left")
                flag_time_left = 0
                self.Pykeyboard.press(Key.left)
                self.Pykeyboard.release(Key.left)
                countRepsSecondPlayer = countRepsSecondPlayer + 1
                t1 = time.perf_counter()
                flagLeft = 1

            if flagUp == 1:
                maxUpAngleFirstPlayer.append(angle)
            if flagDown == 1:
                maxDownAngleFirstPlayer.append(angle)
            if flagLeft == 1:
                maxUpAngleSecondPlayer.append(angle)
            if flagRight == 1:
                maxUpAngleSecondPlayer.append(angle)

            if time.perf_counter()-t1>1.5:
                print("Reseting Sides...")
                flagLeft = 0
                flagRight = 0

            if countRepsFirstPlayer % 5 and countRepsFirstPlayer !=0:
                self.playReps("player1_"+str(countRepsFirstPlayer))
            if countRepsSecondPlayer % 5 and countRepsSecondPlayer !=0:
                self.playReps("player2_"+str(countRepsFirstPlayer))

            if keyboard.is_pressed('7') =='7':
                return
            if keyboard.is_pressed('7') == '8':
                f = open("FirstPlayer.txt", "w")
                f.write(str(min(maxUpAngleFirstPlayer)))
                f.write(",")
                f.write(str(min(maxDownAngleFirstPlayer)))
                f.write(",")
                f.write(str(np.mean(averageAngleFirstPlayer)))
                f.write(",")
                f.write(str(len(maxUpAngleFirstPlayer)))
                f.write(",")
                f.write(str(len(maxDownAngleFirstPlayer)))
                f.close()
                f = open("SecondPlayer.txt", "w")
                f.write(str(min(maxUpAngleSecondPlayer)))
                f.write(",")
                f.write(str(min(maxDownAngleSecondPlayer)))
                f.write(",")
                f.write(str(np.mean(averageAngleSecondPlayer)))
                f.write(",")
                f.write(str(len(maxUpAngleSecondPlayer)))
                f.write(",")
                f.write(str(len(maxDownAngleSecondPlayer)))
                f.close()
                return


while True:

    key = keyboard.read_key()
    if key == '1':
        footFlex = FootFlex(numberPlayers = 1, baseFilename="Audio")
    if key == '2':
        footFlex = FootFlex(numberPlayers = 2,baseFilename="Audio")
    if key == '3':
        footFlex.calibrate()
    if key == '4':
        footFlex.setLevel(level=1)
    if key == '5':
        footFlex.setLevel(level=2)
    if key == '6':
        footFlex.setLevel(level=3)
    if key == '9':
        sys.exit(0)
    

    
    
