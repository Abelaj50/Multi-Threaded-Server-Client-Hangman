# Multi-Threaded-Server-Client-Hangman
Classic game of Hangman created with Java and JavaFX using multi-threaded connections between the server and clients. CS 342 Project 3, UIC Spring 2021.

## Table of contents
* [General Info](#general-info)
* [Getting Started](#getting-started)
* [Technologies](#technologies)
* [Credits](#credits)
* [Visual Demonstration](#visual-demonstration)

## General info
This project is the third project for CS 342 (Software Design) at the University of Illinois at Chicago, Spring 2021. Our task was to develop a server that plays a word guessing game where each client connects to that server (similar to Wheel of Fortune or Hangman). Clients that log into the server must guess 3 different words in 3 different categories to win. First, the client picks a category. The server will send the client the number of letters in a word from that category to guess. The client gets to guess a total of six letters, one at a time. Correct guesses do not count towards the six guess total. 
The client will guess a letter and send it to the server. The server will respond with either: the letter is in the word and where that letter is located or the letter is not in the word and how many guesses are left. If the client guesses the word within 6 letter guesses, they can not guess at another word in the same category but must chose from the two remaining. If they do not guess the word correctly, they are free to choose from any of the three categories for another word. 
Clients may guess at a maximum of three words per category. If they do not make a correct guess within three attempts, the game is over. The game is won when the client successfully guesses one word in each category. When the game is over, the client can either play again or quit.

## Getting Started
To begin with, you will want to make sure you have all technologies needed.
* Java 8: https://www.oracle.com/java/technologies/javase/javase8u211-later-archive-downloads.html
    
## Technologies
Project is created with:
* Eclipse version 2022-06 (4.24.0)
* Apache Maven version 3.8.4
* JavaFX version 12.0.1
* Java SE 8

## Credits
All credits for the project idea go to Professor Mark Hallenbeck. All credits to technologies used are given to their owners and all items specified in their respective licenses are adhered to throughout this project.  

## Visual Demonstration
The following link leads to a visual demonstration of the project.
* https://youtu.be/PHkiNQe654g
