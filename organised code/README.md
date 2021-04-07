The repository contains all the code needed to reproduce FootFlex project from the software point of view.

To facilitate the understanding of the code, the files were re-arranged in the organised code folder. The official and fully functional code is outside it. 
In the following lines the structure of this repository is exaplained (with respect to the strucure of organised code folder).

Inside Arduino folder are two sketches. The first one needs to be uploaded on Arduino IoT which will be connected to the laptop
and run as central device. The second is for the peripheral devices (i.e. two Arduino 33 BLE SENSE) - same code on both microcontrollers.

Audio contains the feedback for the user and the main menu song.

Executables has the application as executable files. One needs to run FootFlexApplication which will call the mainProgram (Python script to handle all the decisions and gather the data).
FootFlexGUI is the Matlab application to visualise the data of the patients.

MatlabScript contains the function for visualising patients data.

PythonScripts has the mainProgram file which is the one used by the Java interface and two more files created during the developpment of
FootFlex. They are not needed for the program to run and are just test files.

src is the main code for the Java interface developped in IntelliJ IDEA. The files that are not inside a folder and Gradle, META-INF folders are part of this as well.

The UserDatabase folder has the text files with the patients/users names and the data which is saved for them.

