
from pynput.keyboard import Key, Controller
import serial
import time
import keyboard
import numpy as np
import sys
import matplotlib.pyplot as plt
import matplotlib.animation as animation
from matplotlib import style
import random
from pygame import mixer 
from scipy.signal import savgol_filter
serialPort = serial.Serial(port = "COM10", baudrate=9600)

class FootFlex():
    def __init__(self, numberPlayers, baseFilename):
        self.numberPlayers = numberPlayers
        self.thresholdFirstPlayer = 0 #Up/Down
        self.thresholdSecondPlayer = 0
        self.flag=0
        self.baseFilename = baseFilename
        self.Pykeyboard = Controller()
        print("Number players set")

## Calibrate the foot, this is both single and multiplayer
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
## Set the level for single and multiplayer
    def setLevel(self,level):
        self.flag = self.flag + 1
        print("Let's set level")
        print(self.flag)

        if self.numberPlayers == 1:
            self.thresholdSinglePlayer = 0.75*level
            print("Threshold ",self.thresholdSinglePlayer)
            # self.playTrajectory()

        elif self.numberPlayers == 2:
            if self.thresholdFirstPlayer == 0 and self.flag == 1:
                self.thresholdFirstPlayer = 0.75*level
                print("First " ,self.thresholdFirstPlayer)
                self.flag = self.flag + 1
                
            elif self.thresholdFirstPlayer!=0 and self.thresholdSecondPlayer==0 and self.flag == 3:
                self.thresholdSecondPlayer = 0.75*level
                print("Second ",self.thresholdSecondPlayer)  
                # self.playMultiplayerGame()
        

    
## Play sounds!!
    def playReps(self, number):
         # Load the popular external library
        mixer.init()
        mixer.music.load(self.baseFilename+"//"+number+".mp3")
        mixer.music.play()
        
## Play a trajectory based game i.e. jetpack joyride  
    def playTrajectory(self):
        startTimer = time.perf_counter()
        flagDown = 0
        flagUp = 0
        maxUpAngle = []
        maxDownAngle = []
        allAngles = []
        angle_max = 0
        flagAvoidPlay = 0

        flagWaitSound = 0
        t0=0
        while True:
            line = serialPort.readline()
            decodedLine = line.decode()
            firstPeripheralAngle = (decodedLine.split("\t")[0])
            first_angle = float(firstPeripheralAngle)
            allAngles.append(first_angle)

            
            if  first_angle<self.baselineSinglePlayer+self.thresholdSinglePlayer: 
                self.Pykeyboard.release(Key.enter)
                flagAvoidPlay = 1

            if  first_angle>self.baselineSinglePlayer+self.thresholdSinglePlayer:
                print("up")
                self.Pykeyboard.press(Key.enter)
                if first_angle > angle_max:
                    angle_max = first_angle
                    if flagAvoidPlay == 1 and flagWaitSound == 0:
                        self.playReps("angleHighscore")
                        flagWaitSound = 1
                        t0 = time.perf_counter()
            if time.perf_counter() - t0 > 3:
                flagWaitSound = 0

            if keyboard.is_pressed('8'):
                allAngles = savgol_filter(allAngles, 35, 3)
                minA = (min(allAngles)-self.baselineSinglePlayer)
                minSinglePlayer = np.round((minA*10),2)
                maxA = (max(allAngles)-self.baselineSinglePlayer)
                maxSinglePlayer = np.round((maxA*10),2)
                timerSingle = (time.perf_counter()-startTimer)
                timerSingle = str(np.round(timerSingle,2))
            
                f = open("User1.txt", "w")
                f.write(str(minSinglePlayer))
                f.write(",")
                f.write(str(maxSinglePlayer))
                f.write(",")
                f.write(str(0))
                f.write(",")
                f.write(str(0))
                f.write(",")
                f.write(str(0))
                f.write(",")
                f.write(timerSingle)
                f.close()
                return


## Play multiplayer games, this is 2048 - this is the main function    
    def playMultiplayerGame(self):
        startTimer = time.perf_counter()
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

        all_first_angles = []

        all_second_angles = []

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
            all_first_angles.append(first_angle)
            second_angle = float(secondPeripheralAngle)
            all_second_angles.append(second_angle)

            if first_angle<self.baselineFirstPlayer+self.thresholdFirstPlayer - 0.15 and flagUp==0 and flag_time_up==0:
                flag_time_up = 1
                time.sleep(0.15)
                # print("Waiting up")
                continue
            if first_angle>self.baselineFirstPlayer+self.thresholdFirstPlayer - 0.15 and flagUp==0 and flag_time_up==1:
                print("Up")
                flag_time_up = 0
                self.Pykeyboard.press(Key.up)
                self.Pykeyboard.release(Key.up)
                countRepsFirstPlayer = countRepsFirstPlayer + 1
                total_up_reps_first_player = total_up_reps_first_player + 1
                t0 = time.perf_counter()
                flagUp = 1
            

            if first_angle>self.baselineFirstPlayer-self.thresholdFirstPlayer - 0.15 and flagDown ==0 and flag_time_down ==0:
                flag_time_down = 1
                time.sleep(0.15)
                # print("Waiting Down")
                continue

            if first_angle<self.baselineFirstPlayer-self.thresholdFirstPlayer - 0.15 and flagDown ==0 and flag_time_down ==1:
                print("Down")
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

            if second_angle>self.baselineSecondPlayer-self.thresholdSecondPlayer and flagLeft==0 and flag_time_left ==0:
                flag_time_left = 1
                time.sleep(0.15)
                # print("Waiting right")
                continue
            if second_angle<self.baselineSecondPlayer-self.thresholdSecondPlayer and flagLeft==0 and flag_time_left==1:
                print("Left")
                flag_time_left = 0
                self.Pykeyboard.press(Key.left)
                self.Pykeyboard.release(Key.left)
                countRepsSecondPlayer = countRepsSecondPlayer + 1
                total_down_reps_second_player = total_down_reps_second_player + 1
                t1 = time.perf_counter()
                flagLeft = 1
            
            if second_angle < self.baselineSecondPlayer+self.thresholdSecondPlayer and flagRight ==0 and flag_time_right ==0:
                flag_time_right = 1
                time.sleep(0.15)
                continue

            if second_angle>self.baselineSecondPlayer+self.thresholdSecondPlayer and flagRight ==0 and flag_time_right==1:
                print("right")
                flag_time_right = 0
                self.Pykeyboard.press(Key.right)
                self.Pykeyboard.release(Key.right)
                countRepsSecondPlayer = countRepsSecondPlayer + 1
                total_up_reps_second_player = total_up_reps_second_player + 1
                t1 = time.perf_counter()
                flagRight = 1

            
            if time.perf_counter()-t1>1.5:
                # print("Reseting Sides...")
                flagLeft = 0
                flagRight = 0

            if countRepsFirstPlayer % 5 ==0 and countRepsFirstPlayer !=0:
                player1_counter_reps = player1_counter_reps +1
                countRepsFirstPlayer = player1_counter_reps * 5
                if countRepsFirstPlayer == 20:
                    self.playReps("woohoothats20")
                elif countRepsFirstPlayer < 20:
                    self.playReps("player1_"+str(countRepsFirstPlayer))
                else:
                    pass
                countRepsFirstPlayer =0
            if countRepsSecondPlayer % 5==0 and countRepsSecondPlayer !=0:
                player2_counter_reps = player2_counter_reps +1
                countRepsSecondPlayer = player2_counter_reps * 5
                if countRepsSecondPlayer == 20:
                    self.playReps("woohoothats20")
                elif countRepsSecondPlayer < 20:
                    self.playReps("player2_"+str(countRepsSecondPlayer))
                else:
                    pass
                countRepsSecondPlayer = 0

            if keyboard.is_pressed('8'):

                all_first_angles_filtered = savgol_filter(all_first_angles, 35, 3)
                all_second_angles_filtered = savgol_filter(all_second_angles, 35, 3)

                minA = (min(all_first_angles_filtered)-self.baselineFirstPlayer)
                minFirstPlayer = np.round((minA*10),2)
                maxA = (max(all_first_angles_filtered)-self.baselineFirstPlayer)
                maxFirstPlayer = np.round((maxA*10),2)

                firstAngle_filtered = savgol_filter(all_first_angles,31,3)
                firstAngle_SE = np.sqrt(10*np.mean((all_first_angles-firstAngle_filtered)**2))
                
                mark_first, score_first = self.markSmoothness(firstAngle_SE)

                minA = min(all_second_angles_filtered)-self.baselineSecondPlayer
                minSecondPlayer = np.round((minA*10),2)
                maxA = max(all_second_angles_filtered)-self.baselineSecondPlayer
                maxSecondPlayer = np.round((maxA*10),2)

                secondAngle_filtered = savgol_filter(all_second_angles,31,3)
                secondAngle_SE = np.sqrt(10*np.mean((all_second_angles-secondAngle_filtered)**2))
                mark_second, score_second = self.markSmoothness(secondAngle_SE)

                timerBoth = (time.perf_counter()-startTimer)
                timerBoth = str(np.round(timerBoth,2))
            
                f = open("User1.txt", "w")
                f.write(str(minFirstPlayer))
                f.write(",")
                f.write(str(maxFirstPlayer))
                f.write(",")
                f.write(str(total_up_reps_first_player))
                f.write(",")
                f.write(str(total_down_reps_first_player))
                f.write(",")
                # f.write(mark_first)
                # f.write(",")
                f.write(str(score_first))
                f.write(",")
                f.write(timerBoth)
                f.close()
                f = open("User2.txt", "w")
                f.write(str(minSecondPlayer))
                f.write(",")
                f.write(str(maxSecondPlayer))
                f.write(",")
                f.write(str(total_up_reps_second_player))
                f.write(",")
                f.write(str(total_down_reps_second_player))
                f.write(",")
                # f.write(mark_second)
                # f.write(",")
                f.write(str(score_second))
                f.write(",")
                f.write(timerBoth)
                f.close()
                return

## Follow the wave for multiplayer, this assesses smoothness
    def followTheWave(self):
                # Create figure for plotting
        fig = plt.figure()
        ax = fig.add_subplot(1, 1, 1)
        xs = [] #store trials here (n)
        ys = [] #store relative frequency here
        rs = [] #for theoretical probability

        # This function is called periodically from FuncAnimation
        
        def animate(i, xs, ys, baselineFirstPlayer, baselineSecondPlayer):
                
            #Aquire and parse data from serial port
            
            line=serialPort.readline()      #ascii
            line_as_list = line.split(b'\t')
            player1_follow = float(line_as_list[0])
            relProb = line_as_list[1]
            relProb_as_list = relProb.split(b'\n')
            player2_follow = float(relProb_as_list[0])
            
            # Add x and y to lists
            xs.append((player1_follow - baselineFirstPlayer)*10)
            
            ys.append((player2_follow - baselineSecondPlayer)*10)

            # Draw x and y lists
            ax.clear()
           
            ax.plot(ys, label="Player 1")
            ax.plot(xs, label="Player 2")

            # Format plot
            plt.ylim([-50,50])
            plt.xticks(rotation=45, ha='right')
            plt.subplots_adjust(bottom=0.30)
            plt.title('Follow the wave!')
            plt.ylabel('Foot angle')
            plt.xlabel('Time')
            plt.legend()
    
            
        # Set up plot to call animate() function periodically
        serialPort.flushInput()
        time.sleep(0.3)
        ani = animation.FuncAnimation(fig, animate, fargs=(xs, ys,self.baselineFirstPlayer, self.baselineSecondPlayer), interval=10)
        plt.show()
        return xs, ys
        
## This is the escape plot, the function opens on exiting the assessment of smoothness
    def plotAndEscape (self, xs, ys):
        xs_filtered = savgol_filter(xs,31,3)
        xs_SE = np.sqrt(np.mean((xs-xs_filtered)**2))
        
        mark_x, xs_score = self.markSmoothness(xs_SE)

        ys_filtered = savgol_filter(ys,31,3)
        ys_SE = np.sqrt(np.mean((ys-ys_filtered)**2))
        
        mark_y, ys_score = self.markSmoothness(ys_SE)


        fig, ax = plt.subplots(2)
        ax[0].plot(xs)
        ax[0].plot(xs_filtered)
        ax[0].set_title('The results are in... how smooth was your movement?')
        # these are matplotlib.patch.Patch properties
        props = dict(boxstyle='round', facecolor='wheat', alpha=0.5)
        textstr= "Player 1:" + "\n" + "Score: "+str(np.round(xs_score))+"%"+"\n"+"Grade: "+mark_x
        # place a text box in upper left in axes coords
        ax[0].text(0.05, 0.95, textstr, transform=ax[0].transAxes,fontsize=10,
                verticalalignment='top', bbox=props)

        ax[1].plot(ys)
        ax[1].plot(ys_filtered)
        # these are matplotlib.patch.Patch properties
        props = dict(boxstyle='round', facecolor='wheat', alpha=0.5)
        textstr_y="Player 2:" + "\n" + "Score: "+str(np.round(ys_score))+"%"+"\n"+"Grade: "+mark_y
        # place a text box in upper left in axes coords
        ax[1].text(0.05, 0.95, textstr_y, transform=ax[1].transAxes,fontsize=10,
                verticalalignment='top', bbox=props)
        plt.show()
        xs_score = np.round(xs_score,2)
        xs_filtered = np.round(xs_filtered,2)

        ys_score = np.round(ys_score,2)
        ys_filtered = np.round(ys_filtered,2)
        f = open("User1.txt", "w")
        # f.write(str(mark_x))
        # f.write(",")
        f.write(str(min(xs_filtered)))
        f.write(",")
        f.write(str(max(xs_filtered)))
        f.write(",")
        f.write(str(0))
        f.write(",")
        f.write(str(0))
        f.write(",")
        f.write(str(xs_score))
        f.write(",")
        f.write(str(0))
        f.write(",")
        f.close()

        f = open("User2.txt", "w")
        # f.write(str(mark_y))
        # f.write(",")
        f.write(str(min(ys_filtered)))
        f.write(",")
        f.write(str(max(ys_filtered)))
        f.write(",")
        f.write(str(0))
        f.write(",")
        f.write(str(0))
        f.write(",")
        f.write(str(ys_score))
        f.write(",")
        f.write(str(0))
        f.write(",")
        f.close()

## Single player equivalent
    def followTheWaveSinglePlayer(self):
        fig = plt.figure()
        ax = fig.add_subplot(1, 1, 1)
        xs = [] #store trials here (n)

        # This function is called periodically from FuncAnimation
        
        def animateSinglePlayer(i, xs, baselineFirstPlayer):
                
            #Aquire and parse data from serial port
            
            line=serialPort.readline()      #ascii
            line_as_list = line.split(b'\t')
            player1_follow = float(line_as_list[0])
            
            # Add x and y to lists
            xs.append((player1_follow - baselineFirstPlayer)*10)
            

            # Draw x and y lists
            ax.clear()
           
            ax.plot(xs)

            # Format plot
            plt.ylim([-50,50])
            plt.xticks(rotation=45, ha='right')
            plt.subplots_adjust(bottom=0.30)
            plt.title('Try and move your foot smoothly!')
            plt.ylabel('Foot angle')
            plt.xlabel('Time')
            plt.legend()
    
            
        # Set up plot to call animate() function periodically
        serialPort.flushInput()
        time.sleep(0.3)
        ani = animation.FuncAnimation(fig, animateSinglePlayer, fargs=(xs, self.baselineSinglePlayer), interval=10)
        plt.show()
        return xs
        
## Same as above but for single player
    def plotAndEscapeSinglePlayer (self, xs):
        xs_filtered = savgol_filter(xs,31,3)
        xs_SE = np.sqrt(np.mean((xs-xs_filtered)**2))
        print(xs_SE)
        mark_x, xs_score = self.markSmoothness(xs_SE)


        fig, ax = plt.subplots()
        ax.plot(xs)
        ax.plot(xs_filtered)
        ax.set_title('The results are in... how smooth was your movement?')
        # these are matplotlib.patch.Patch properties
        props = dict(boxstyle='round', facecolor='wheat', alpha=0.5)
        textstr= "Score: "+str(np.round(xs_score))+"%"+"\n"+"Grade: "+mark_x
        # place a text box in upper left in axes coords
        ax.text(0.05, 0.95, textstr, transform=ax.transAxes,fontsize=10,
                verticalalignment='top', bbox=props)

        xs_score = np.round(xs_score,2)
        xs_filtered = np.round(xs_filtered,2)

        plt.show()
        f = open("User1.txt", "w")
        # f.write(str(mark_x))
        # f.write(",")
        f.write(str(min(xs_filtered)))
        f.write(",")
        f.write(str(max(xs_filtered)))
        f.write(",")
        f.write(str(0))
        f.write(",")
        f.write(str(0))
        f.write(",")
        f.write(str(xs_score))
        f.write(",")
        f.write(str(0))
        f.write(",")
        f.close()


        
## This function marks the players smoothness giving them a grade and score as %
    def markSmoothness (self, input):
        print(input)
        # Find the score from the RMSE
        if input < 10:
            input_score = (1 - (input)/(10)) *100
        else:
            input_score = 0

        if input_score < 50 and input_score >= 40:
            mark = "D"
        elif input_score < 60 and input_score >= 50:
            mark = "C"
        elif input_score < 70 and input_score >= 60:
            mark = "B"
        elif input_score < 80 and input_score >= 70:
            mark = "A"
        elif input_score > 80:
            mark = "A*"
        else:
            mark = "E - Try again! :)"

        return mark, np.round(input_score,2)

       
## Begin with the menu
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
        time.sleep(0.15)
    if key == '5': # Set the level to medium - call once for 1 player and twice for 2
        footFlex.setLevel(level=2)
        time.sleep(0.15)
    if key == '6': # Set the level to hard - call once for 1 player and twice for 2
        footFlex.setLevel(level=3)
        time.sleep(0.15)
    if key == '9': # Press this to exit the app. Before press 7 to return to this menu and 8 to save the data in txt file and return to this menu
        sys.exit(0)
    if key == '0': # Play 2048 or simmilar games - should be just 2 players - THINK THIS MIGHT BE IRRELEVANT NOW
        if footFlex.numberPlayers == 1:
            footFlex.playTrajectory()
        if footFlex.numberPlayers == 2:
            footFlex.playMultiplayerGame()
    # if key == '[': # Play jetpack joyride or dragon fly - following trajectory based games - should be single player
    #     footFlex.playTrajectory()

    # if key == "+": # - THINK THIS MIGHT BE IRRELEVANT NOW
    #     footFlex.holdingHighScore()
    if key == "7": ## This plays the wave assessment both single and multiplayer versions, needs integration with Java
        if footFlex.numberPlayers == 1:
            xs = footFlex.followTheWaveSinglePlayer()
            footFlex.plotAndEscapeSinglePlayer(xs)
        if footFlex.numberPlayers == 2:
            xs, ys = footFlex.followTheWave()
            footFlex.plotAndEscape(xs, ys)
        
    ### Tutorials are on the java side hence no key above ###

    
    
