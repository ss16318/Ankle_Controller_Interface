
from pynput.keyboard import Key, Controller
import serial
import time
import keyboard
import cv2
import numpy as np
import sys
from pygame import mixer 
serialPort = serial.Serial(port = "COM18", baudrate=9600)

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
            firstperipheralAngle = (decodedLine.split("\t")[0])
            baseline_first = firstperipheralAngle
            self.baselineFirstPlayer = float(baseline_first)
            #Play sound and wait 2 seconds
            time.sleep(2)
            line = serialPort.readline()
            decodedLine = line.decode()
            secondperipheralAngle = (decodedLine.split("\t")[1])
            baseline_second = secondperipheralAngle
            self.baselineSecondPlayer = float(baseline_second)
            #Play sound calibration done
        print("calibration done")
    def setLevel(self,level):
        print("Let's set level")

        if self.numberPlayers == 1:
            self.thresholdSinglePlayer = 0.5*level
            print("Threshold ",self.thresholdSinglePlayer)
            return

        if self.numberPlayers == 2:
            if self.thresholdFirstPlayer == 0:
                self.thresholdFirstPlayer = 0.5*level
            elif self.thresholdFirstPlayer!=0 and self.thresholdSecondPlayer==0:
                self.thresholdSecondPlayer = 0.5*level
            else:
                return    

    def playReps(self, number):
         # Load the popular external library
        mixer.init()
        mixer.music.load(self.baseFilename+"//"+number+".mp3")
        mixer.music.play()
    
    def playTrajectory(self):
        flagDown = 0
        flagUp = 0
        maxUpAngle = []
        maxDownAngle = []
        averageAngle = []
        angle_max = 0
        flagAvoidPlay = 0

        
        while True:
            line = serialPort.readline()
            decodedLine = line.decode()
            firstPeripheralAngle = (decodedLine.split("\t")[0])
            first_angle = float(firstPeripheralAngle)
            averageAngle.append(first_angle)

            
            if  first_angle>self.baselineSinglePlayer-self.thresholdSinglePlayer: 
                self.Pykeyboard.release(Key.up)
                flagAvoidPlay = 1

            if  first_angle<self.baselineSinglePlayer-self.thresholdSinglePlayer:
                print("up")
                self.Pykeyboard.press(Key.up)
                if first_angle < angle_max:
                    angle_max = first_angle
                    if flagAvoidPlay == 1:
                        self.playReps("angleHighscore")


            if keyboard.is_pressed('7'):    # Esc key to stop
                return
            if keyboard.is_pressed('8'):
                f = open("SinglePlayerTrajectory.txt", "w")
                f.write(str(angle_max))
                f.write(",")
                f.write(str(np.mean(averageAngle)))
                f.close()
                return

    def holdingHighScore(self):
        allAngles = []
        flagHolding = 0
        thresholdHolding=0.5
        flagStop = 0
        t0=0
        timeElapsed = 0


        while True:
            line = serialPort.readline()
            decodedLine = line.decode()
            peripheralAngle = (decodedLine.split("\t")[0])
            angle = float(peripheralAngle)
            allAngles.append(angle)

           
            if angle>self.baselineSinglePlayer+thresholdHolding and flagHolding ==0:
                print("start")
                self.playReps("holding")
                t0 = time.perf_counter()
                flagHolding = 1
                
            if angle<self.baselineSinglePlayer+thresholdHolding and flagHolding == 1 and flagStop ==0:
                timeElapsed = time.perf_counter()-t0
                if timeElapsed >15:
                    print("stop")
                    self.playReps("woohoo")
                    flagStop = 1

                else:
                    print("stop")
                    self.playReps("error")
                    flagStop = 1

            if time.perf_counter()-t0 > (timeElapsed+0.2) and flagStop==1:
                f = open("holdingHighScore.txt", "w")
                f.write(str(timeElapsed))
                f.write(",")
                f.write(str(allAngles))
                f.close()
                return



        

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
        single_counter_reps = 0
        total_up_reps = 0
        total_down_reps = 0
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
                total_up_reps = total_up_reps + 1
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
                total_down_reps = total_down_reps + 1
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
            if countReps % 5 ==0 and countReps !=0:
                single_counter_reps = single_counter_reps +1
                countReps = single_counter_reps * 5
                if countReps == 20:
                    self.playReps("woohoothats20")
                else:
                    self.playReps(str(countReps))
                countReps = 0
            
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
                f.write(str(total_up_reps))
                f.write(",")
                f.write(str(total_down_reps))
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

        total_down_reps_first_player = 0
        total_up_reps_first_player = 0

        total_down_reps_second_player = 0
        total_up_reps_second_player = 0

        maxUpAngleFirstPlayer = []
        maxDownAngleFirstPlayer = []
        averageAngleFirstPlayer = []

        maxUpAngleSecondPlayer = []
        maxDownAngleSecondPlayer = []
        averageAngleSecondPlayer = []

        countRepsFirstPlayer = 0
        countRepsSecondPlayer = 0
        player1_counter_reps = 0
        player2_counter_reps = 0

        while True:
            line = serialPort.readline()
            decodedLine = line.decode()
            firstPeripheralAngle = (decodedLine.split("\t")[0])
            secondPeripheralAngle = (decodedLine.split("\t")[1])
            first_angle = float(firstPeripheralAngle)
            second_angle = float(secondPeripheralAngle)
            averageAngleFirstPlayer.append(firstPeripheralAngle)
            averageAngleSecondPlayer.append(secondPeripheralAngle)

           

            if first_angle<self.baselineFirstPlayer-self.thresholdFirstPlayer and flagUp==0 and flag_time_up==0:
                flag_time_up = 1
                time.sleep(0.15)
                print("Waiting Up")
                continue
            if first_angle<self.baselineFirstPlayer-self.thresholdFirstPlayer and flagUp==0 and flag_time_up==1:
                print("up")
                flag_time_up = 0
                self.Pykeyboard.press(Key.up)
                self.Pykeyboard.release(Key.up)
                countRepsFirstPlayer = countRepsFirstPlayer + 1
                total_up_reps_first_player = total_up_reps_first_player + 1
                t0 = time.perf_counter()

                flagUp = 1
            

            if first_angle>self.baselineFirstPlayer+self.thresholdFirstPlayer and flagDown ==0 and flag_time_down==0:
                flag_time_down = 1
                time.sleep(0.15)
                print("Waiting Down")

                continue
            if first_angle>self.baselineFirstPlayer+self.thresholdFirstPlayer and flagDown ==0 and flag_time_down==1:
                print("down")
                flag_time_down = 0
                self.Pykeyboard.press(Key.down)
                self.Pykeyboard.release(Key.down)
                countRepsFirstPlayer = countRepsFirstPlayer + 1
                total_down_reps_first_player = total_down_reps_first_player + 1

                t0 = time.perf_counter()


                flagDown = 1
            
            if time.perf_counter()-t0>1.5:
                #print("Reseting Up/Down...")
                flagUp = 0
                flagDown = 0
            ### Player right/left ################

            if second_angle<self.baselineSecondPlayer-self.thresholdSecondPlayer and flagRight==0 and flag_time_right==0:
                flag_time_right = 1
                time.sleep(0.15)
                print("Waiting right")
                continue
            if second_angle<self.baselineSecondPlayer-self.thresholdSecondPlayer and flagRight==0 and flag_time_right==1:
                print("right")
                flag_time_right = 0
                self.Pykeyboard.press(Key.right)
                self.Pykeyboard.release(Key.right)
                countRepsSecondPlayer = countRepsSecondPlayer + 1
                total_up_reps_second_player = total_up_reps_second_player + 1

                t1 = time.perf_counter()

                flagRight = 1
            

            if second_angle>self.baselineSecondPlayer+self.thresholdSecondPlayer and flagLeft ==0 and flag_time_left==0:
                flag_time_left = 1
                time.sleep(0.15)
                print("Waiting left")

                continue
            if second_angle>self.baselineSecondPlayer+self.thresholdSecondPlayer and flagLeft ==0 and flag_time_left==1:
                print("left")
                flag_time_left = 0
                self.Pykeyboard.press(Key.left)
                self.Pykeyboard.release(Key.left)
                countRepsSecondPlayer = countRepsSecondPlayer + 1
                total_down_reps_second_player = total_down_reps_second_player + 1

                t1 = time.perf_counter()
                flagLeft = 1

            if flagUp == 1:
                maxUpAngleFirstPlayer.append(first_angle)
            if flagDown == 1:
                maxDownAngleFirstPlayer.append(first_angle)
            if flagLeft == 1:
                maxUpAngleSecondPlayer.append(second_angle)
            if flagRight == 1:
                maxUpAngleSecondPlayer.append(second_angle)

            

            if time.perf_counter()-t1>1.5:
                print("Reseting Sides...")
                flagLeft = 0
                flagRight = 0

            if countRepsFirstPlayer % 5 ==0 and countRepsFirstPlayer !=0:
                player1_counter_reps = player1_counter_reps +1
                countRepsFirstPlayer = player1_counter_reps * 5
                if countRepsFirstPlayer == 20:
                    self.playReps("woohoothats20")
                else:
                    self.playReps("player1_"+str(countRepsFirstPlayer))
                countRepsFirstPlayer =0
            if countRepsSecondPlayer % 5==0 and countRepsSecondPlayer !=0:
                player2_counter_reps = player2_counter_reps +1
                countRepsSecondPlayer = player2_counter_reps * 5
                if countRepsSecondPlayer == 20:
                    self.playReps("woohoothats20")
                else:
                    self.playReps("player2_"+str(countRepsSecondPlayer))
                countRepsSecondPlayer =0


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
                f.write(str(total_up_reps_first_player))
                f.write(",")
                f.write(str(total_down_reps_first_player))
                f.close()
                f = open("SecondPlayer.txt", "w")
                f.write(str(min(maxUpAngleSecondPlayer)))
                f.write(",")
                f.write(str(min(maxDownAngleSecondPlayer)))
                f.write(",")
                f.write(str(np.mean(averageAngleSecondPlayer)))
                f.write(",")
                f.write(str(total_up_reps_second_player))
                f.write(",")
                f.write(str(total_down_reps_second_player))
                f.close()
                return

flagMenu = 0
while True:
    if flagMenu == 0:
        mixer.init()
        mixer.music.load("Audio\menu.mp3")
        mixer.music.play()
        flagMenu = 1


    key = keyboard.read_key()
    if key == '1': # Set number of players to 1
        footFlex = FootFlex(numberPlayers = 1, baseFilename="Audio")
    if key == '2': # Set number of players to 2
        footFlex = FootFlex(numberPlayers = 2,baseFilename="Audio")
    if key == '3': # Get the neutral position for both 1 and 2 players - call this just once
        footFlex.calibrate()
    if key == '4': # Set the level to easy - call once for 1 player and twice for 2
        footFlex.setLevel(level=1)
    if key == '5': # Set the level to medium - call once for 1 player and twice for 2
        footFlex.setLevel(level=2)
    if key == '6': # Set the level to hard - call once for 1 player and twice for 2
        footFlex.setLevel(level=3)
    if key == '9': # Press this to exit the app. Before press 7 to return to this menu and 8 to save the data in txt file and return to this menu
        sys.exit(0)
    if key == ']': # Play 2048 or simmilar games - should be just 2 players
        if footFlex.numberPlayers == 1:
            footFlex.playSinglePlayerGame()
        if footFlex.numberPlayers == 2:
            footFlex.playMultiplayerGame()
    if key == '[': # Play jetpack joyride or dragon fly - following trajectory based games - should be single player
        footFlex.playTrajectory()

    if key == "+": # Function to test holding time of the foot and angles to plot them on a graph in java. THe time will also be set for a highscore in a leaderboard, mean and max angles can also be taken since all the data is sent to javA.
        footFlex.holdingHighScore()
    
    ### Tutorials are on the java side hence no key above ###

    
    
