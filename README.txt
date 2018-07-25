APPLICATION Objective:
	The aim is to create a game similar to Pacman, a game originally manufactured and sold by Namco in 1980.
	It was one of the most popular games everthat ushered ina new genre of video game when most arcade games were space shooters.
	The aim of the game is for the protagonist (i.e. Pacman, the yellow circle) to eat as many pellets (orange dotsand special cherries at times)
	as possible to get a high score, while avoiding the ghost enemies (red, blue, pink and orange blobs) and losing lives.

HOW TO COMPILE AND INSTALL THE GAME:
On Eclipse:
	To Load the projects:
		Start Eclipse.
		Select any working folder
		Go to File -> Open projects from File System...
		Enter the Directory of Java_Gaming
		Click Finish

	To Run:
		In Package Explorer, Select Java_Gaming
		Run to Play!

On Linux Terminal:
	Open Terminal on the folder Java_Gaming or Set directory on folder Java_Gaming
	Enter:
		to Compile:	javac src/main/model/*.java src/main/view/*.java src/main/*.java
		to Run:		java -cp src/ main.MainApp

This game is built upon JavaFX, JRE System Library jdk1.8.0_131
Therefore the game needs it to compile.
JavaFX is bundled with JRE8

	To Download JRE System Library jdk1.8.0_131:
		http://www.oracle.com/technetwork/java/javase/downloads/java-archive-javase8-2177648.html
	To Set Up Eclipse To Use Specific JDK:
		http://techiedan.com/2009/10/19/set-up-jdk-in-eclipse/
	Instruction how to install java:
		https://askubuntu.com/questions/48468/how-do-i-install-java/88058#88058