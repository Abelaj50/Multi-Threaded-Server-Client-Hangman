

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class GuiServer extends Application{

	
	TextField s1,s2,s3,s4, c1, guessField;
	Button serverChoice,clientChoice,b1, category1, category2, category3;
	HashMap<String, Scene> sceneMap;
	GridPane grid;
	HBox buttonBox;
	VBox clientBox;
	Scene startScene;
	BorderPane startPane;
	Server serverConnection;
	Client clientConnection;
	ListView<String> listItems, listItems2;
	ArrayList<String> apparelList = new ArrayList<String>();
	ArrayList<String> birdsList = new ArrayList<String>();
	ArrayList<String> colorsList = new ArrayList<String>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("The Networked Client/Server GUI Example");
		
		this.serverChoice = new Button("Server");
		this.serverChoice.setStyle("-fx-pref-width: 300px");
		this.serverChoice.setStyle("-fx-pref-height: 300px");
		
		this.serverChoice.setOnAction(e->{ primaryStage.setScene(sceneMap.get("server"));
											primaryStage.setTitle("This is the Server");
				serverConnection = new Server(data -> {
					Platform.runLater(()->{
						listItems.getItems().add(data.toString());
					});

				});
											
		});
		
		
		this.clientChoice = new Button("Client");
		this.clientChoice.setStyle("-fx-pref-width: 300px");
		this.clientChoice.setStyle("-fx-pref-height: 300px");
		
		this.clientChoice.setOnAction(e-> {primaryStage.setScene(sceneMap.get("client"));
											primaryStage.setTitle("This is a client");
											clientConnection = new Client(data->{
							Platform.runLater(()->{listItems2.getItems().add(data.toString());
											});
							});
							
											clientConnection.start();
		});
		
		this.buttonBox = new HBox(400, serverChoice, clientChoice);
		startPane = new BorderPane();
		startPane.setPadding(new Insets(70));
		startPane.setCenter(buttonBox);
		
		startScene = new Scene(startPane, 800,800);
		
		listItems = new ListView<String>();
		listItems2 = new ListView<String>();
		
		c1 = new TextField();
		b1 = new Button("Send");
		

		b1.setOnAction(e->{
			
			/* If we have a valid character to send, and a category chosen, we will enter this body.. */
			if((clientConnection.currCategory.length() > 0) && (c1.getText().length() > 0)) {
				
				/* We will send the user input char and execute the following lines of code as long as we still have guesses for that word remaining. */
				if(clientConnection.WordGuessesLeft.get(clientConnection.currWord) != 0) {
						
					clientConnection.send(c1.getText()); 
					c1.clear();
					listItems2.getItems().add("Your guess: " + clientConnection.guessedL);
					
					
					/* If the current category is 'apparel', this is how we will handle the character checking. */
					if(clientConnection.currCategory == "apparel") {
						guessLetterFunction("Apparel", primaryStage);
					}
					
					/* If the current category is 'birds', this is how we will handle the character checking. */
					else if (clientConnection.currCategory == "birds") {
						guessLetterFunction("Birds", primaryStage);
					}
					
					/* If the current category is 'colors', this is how we will handle the character checking. */
					else if (clientConnection.currCategory == "colors") {
						guessLetterFunction("Colors", primaryStage);
					}
					
					/* If all three of our category buttons are disabled, that means the player has won the game! */
					if(((category1.isDisabled() == true) && (category2.isDisabled() == true) && (category3.isDisabled() == true)) && !(clientConnection.failedAList.size() == 3 || clientConnection.failedBList.size() == 3 || clientConnection.failedCList.size() == 3)) {
						listItems2.getItems().add("CONGRATULATIONS! YOU HAVE WON THE GAME!!!");
					
						primaryStage.setScene(sceneMap.get("failedScreen"));
					}
				}
				
				/* If we do not have any guesses remaining for the current word, then the user has pressed send after the fact.
				 * The user must pick a category again, either the same category or a new one. */
				
				else if(clientConnection.WordGuessesLeft.get(clientConnection.currWord) == 0) {			
					listItems2.getItems().add("You have used up all your guesses for this word.\nPlease try again with either the same or a different category.");
				}
			}
			
			/* If the user has pressed send but not picked a category. */
			else if (clientConnection.currCategory.length() <= 0) {
				listItems2.getItems().add("Pick a category first before guessing a character.");
			}
			
			/* If the user has pressed send but not input a character. */
			else if (c1.getText().length() <= 0) {
				listItems2.getItems().add("You must enter in a character to guess!");
			}
			
			else {
				listItems2.getItems().add("Unknown error occured: Please quit game and report error to aabrah44@uic.edu immediately.");
			}
		});
		
		
		/* This allows the client's text input to only allow one character. */
		c1.setTextFormatter(new TextFormatter<String>((Change e)->{
		    String deleteText = e.getControlNewText();
		    if (deleteText.length() > 1) {return null;} 
		    else {return e;}
		}));
		
		
		/* Adding the words to the category lists. */
		apparelList.addAll(Arrays.asList("shirt", "jacket", "pants"));
		birdsList.addAll(Arrays.asList("toucan", "owl", "flamingo"));
		colorsList.addAll(Arrays.asList("black", "violet", "cyan"));

		
		/*****************************************
		 * Category buttons are described below. *
		 *****************************************/
		
		category1 = new Button("Apparel");
		category1.setOnAction(e->{
			
		/* As long as we haven't failed 3 words in the apparel category, we will enter this body. */
		if(clientConnection.failedAList.size() != 3) {
			
			clientConnection.currCategory = "apparel";
			categoryBuilderFunction(clientConnection.failedAList, apparelList);
		}
		
		else {
			loseGameMethod();
		}

		});
		
		
		category2 = new Button("Birds");
		category2.setOnAction(e->{
			
		/* As long as we haven't failed 3 words in the apparel category, we will enter this body. */
		if(clientConnection.failedBList.size() != 3) {
			
			clientConnection.currCategory = "birds";
			categoryBuilderFunction(clientConnection.failedBList, birdsList);
		}
		
		else {
			loseGameMethod();
		}

		});
		
		category3 = new Button("Colors");
		category3.setOnAction(e->{
			
		/* As long as we haven't failed 3 words in the apparel category, we will enter this body. */
		if(clientConnection.failedCList.size() != 3) {
			
			clientConnection.currCategory = "colors";
			categoryBuilderFunction(clientConnection.failedCList, colorsList);
		}
		
		else {
			loseGameMethod();
		}

		});
		
		sceneMap = new HashMap<String, Scene>();
		
		sceneMap.put("server",  createServerGui());
		sceneMap.put("client",  createClientGui());
		sceneMap.put("failedScreen", failedScreen(primaryStage));
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
		
		primaryStage.setScene(startScene);
		primaryStage.show();
		
	}
	
	/* This is the helper method that will handle building the category buttons. */
	public void categoryBuilderFunction(ArrayList<String> failedList, ArrayList<String> categoryList) {
		
		boolean noPriorFailedWord = true;
		int randomNum = -1;
		
		/* ********************************************************* * 
		 * This while loop provides a check to make sure 			 *
		 * a random chosen word hasn't already had a failed attempt. *
		 * If a word has been found to have had a failed attempt, 	 *
		 * the while loop will iterate until a random word is chosen *
		 * that doesn't have a failed attempt.						 *
		 * ********************************************************* */
		
		while(noPriorFailedWord == true) {
			
			noPriorFailedWord = false;
			Random rn = new Random();
			randomNum = rn.nextInt(2 - 0 + 1) + 0;
			
			/* This for loop ensures that we randomize a word in the chosen category that has not already had a failed attempt. */
			for(int i = 0; i < failedList.size(); i++)
				if(categoryList.get(randomNum) == failedList.get(i)) {noPriorFailedWord = true;}
		}

		/* Here we will set the client's current word to the random word chosen in the prior statement. */
		clientConnection.currWord = categoryList.get(randomNum);

		/* This statement print's the chosen category and length in the random word chosen. */
		listItems2.getItems().add("Category chosen: " + clientConnection.currCategory + ". Letters in word: " + clientConnection.currWord.length());
		guessField.clear();
		
		for (int i = 0; i < clientConnection.currWord.length(); i++) 
			guessField.appendText("_ ");
	}
	
	/* This is the helper method that will handle checking guessed characters. */
	public void guessLetterFunction(String category, Stage primaryStage) {
		
		/* This is a flag to check if we need to remove a life, based on an incorrectly guessed character. */
		boolean removeLife = true;
		
		/* This loop checks if the user sent character is in the current word. */
		for (int i = 0; i < clientConnection.currWord.length(); i++) {
		
			/* If the character was found in the current word (at index i) we will enter this statement's body. */
			if (clientConnection.guessedL.toString().charAt(0) == clientConnection.currWord.charAt(i) ) {
				
				String oldGuess = new String();
				
				/* Here, we copy in our textfield's string into oldGuess to retain a copy. */
				for(int guessIndex = 0; guessIndex < guessField.getLength(); guessIndex++) {
					
					if(guessField.getText().charAt(guessIndex) != ' ' ) 
						oldGuess = oldGuess.concat(Character.toString(guessField.getText().charAt(guessIndex)));
				}
				
				guessField.clear(); 
				
				/* This loop will update our textfield based on the previous string (oldGuess) and the new user sent character. */
				for (int j = 0; j < clientConnection.currWord.length(); j++) {
					
					/* If we are not at the index where the character was found (at index i), we will replace the text with what was there previously. */
					if (j != i) 
						guessField.appendText(oldGuess.charAt(j) + " ");
					
					/* If we are at the index where the character was found (at index i), we will replace the text with the user sent character. */
					if (j == i) 
						guessField.appendText(clientConnection.guessedL.toString() + " ");
				}
											
				removeLife = false;
			}
			
			/* This body will check if we have guessed the entire word correctly and will handle that case if so. */
			if((i == clientConnection.currWord.length() - 1) && removeLife == false) {
				
				String possibleFinal = new String();
				
				for(int guessIndex = 0; guessIndex < guessField.getLength(); guessIndex++) {
					
					if(guessField.getText().charAt(guessIndex) != ' ' ) 
						possibleFinal = possibleFinal.concat(Character.toString(guessField.getText().charAt(guessIndex)));
				}
				
				if(possibleFinal.equals(clientConnection.currWord)) {
					
					if(!((category1.isDisabled() == true && category2.isDisabled() == true) || (category1.isDisabled() == true && category3.isDisabled() == true) || (category2.isDisabled() == true && category3.isDisabled() == true))) 
						listItems2.getItems().add("CONGRATULATIONS! You have guessed the word correctly.\nTry your luck in the other categories!");
					
					clientConnection.currWord = "";
					clientConnection.currCategory = "";
					
					if(category.equals("Apparel")) {
						category1.setDisable(true);
					}
					else if(category.equals("Birds")) {
						category2.setDisable(true);
					}
					else if(category.equals("Colors")) {
						category3.setDisable(true);
					}
				}
			}
		}
		
		/* If we did not find the user sent character within the current word, we will remove a life. */
		if(removeLife == true) {
			
			/* Here, we will reduce the life by 1, and print a message for the user to see. */
			Integer remainingLives = clientConnection.WordGuessesLeft.get(clientConnection.currWord) - 1;
			clientConnection.WordGuessesLeft.put(clientConnection.currWord, remainingLives);
			
			if(remainingLives != 1)
				listItems2.getItems().add("The guessed character '" + clientConnection.guessedL + "' isn't in the word! You have: " + remainingLives + " tries left!");
			else
				listItems2.getItems().add("The guessed character '" + clientConnection.guessedL + "' isn't in the word! You have: " + remainingLives + " try left!");
			
			
			/* This case details what will be done when a player uses up all 6 guesses. */
			if(remainingLives < 1) {
				
				if(category.equals("Apparel")) {
					clientConnection.failedAList.add(clientConnection.currWord);
				}
				else if(category.equals("Birds")) {
					clientConnection.failedBList.add(clientConnection.currWord);
				}
				else if(category.equals("Colors")) {
					clientConnection.failedCList.add(clientConnection.currWord);
				}
				
				listItems2.getItems().add("You have used up all your guesses for this word.\nPlease try again with either the same or a different category.");
				
				int wordsRemainingA = 3 - clientConnection.failedAList.size();
				int wordsRemainingB = 3 - clientConnection.failedBList.size();
				int wordsRemainingC = 3 - clientConnection.failedCList.size();
				listItems2.getItems().add("WORDS REMAINING:\n\tApparel:\t" + wordsRemainingA + "\n\tBirds:\t\t" + wordsRemainingB + "\n\tColors:\t\t" + wordsRemainingC);
				
				if(wordsRemainingA == 0 || wordsRemainingB == 0 || wordsRemainingC == 0) {
					loseGameMethod();

					
					clientConnection.currCategory = "";
					clientConnection.currWord = "";
					
					clientConnection.WordGuessesLeft.put("shirt", 6);
					clientConnection.WordGuessesLeft.put("jacket", 6);
					clientConnection.WordGuessesLeft.put("pants", 6);
					clientConnection.WordGuessesLeft.put("toucan", 6);
					clientConnection.WordGuessesLeft.put("owl", 6);
					clientConnection.WordGuessesLeft.put("flamingo", 6);
					clientConnection.WordGuessesLeft.put("black", 6);
					clientConnection.WordGuessesLeft.put("violet", 6);
					clientConnection.WordGuessesLeft.put("cyan", 6);
					
					clientConnection.failedAList.clear();		
					clientConnection.failedBList.clear();
					clientConnection.failedCList.clear();
					clientConnection.guessedL = "";
					guessField.clear();
					
					PauseTransition pause = new PauseTransition(Duration.seconds(10));
					pause.play();
					primaryStage.setScene(sceneMap.get("failedScreen"));
				}
			}
		}

	}
	
	/* Method called whenever the user loses the game. */
	public void loseGameMethod() {
		
		listItems2.getItems().add("You have failed to guess 3 words correctly within one category.\nGAME OVER!");
		clientConnection.currWord = "";
		clientConnection.currCategory = "";
		category1.setDisable(true);
		category2.setDisable(true);
		category3.setDisable(true);
	}
	
	public Scene createServerGui() {
		
		BorderPane pane = new BorderPane();
		
		TextField portNumField = new TextField();
		portNumField.setPromptText("Enter port number here...");
		Button setPortNum = new Button("Set");
		setPortNum.setMaxSize(80, .5);
		setPortNum.setPrefSize(80, .5);
		
		setPortNum.setOnAction(e->{
			serverConnection.socketNum = Integer.parseInt(portNumField.getText());
			portNumField.clear();
		});

		HBox serverPane = new HBox(20, portNumField, setPortNum);
		
		pane.setPadding(new Insets(70));
		pane.setStyle("-fx-background-color: coral");
		
		pane.setCenter(listItems);
		pane.setTop(serverPane);
		
		pane.setAlignment(serverPane, Pos.TOP_CENTER);
		pane.setMargin(serverPane, new Insets(10));
	
		return new Scene(pane, 500, 400);
		
	}
	
	public Scene createClientGui() {
		
		BorderPane clientPane = new BorderPane();
		
		guessField = new TextField();
		guessField.setEditable(false);

		HBox clientHBox = new HBox(10, category1, category2, category3, guessField);
		
		clientBox = new VBox(10, c1,b1,listItems2);
		clientBox.setStyle("-fx-background-color: blue");
		
		clientPane.setCenter(clientBox);
		clientPane.setTop(clientHBox);
		
		clientPane.setMargin(clientBox, new Insets(10));
		
		
		return new Scene(clientPane, 400, 300);
		
	}
	
	public Scene failedScreen(Stage primaryStage) {
		
		BorderPane bp = new BorderPane();
		TextField loseMsg = new TextField();
		Button retryButton, quitButton;
		HBox buttonBox;
		
		loseMsg.setText("GaMe OvEr!");
		loseMsg.setAlignment(Pos.CENTER);
		loseMsg.setFont(Font.font("Arial", FontWeight.BOLD, 36));
		loseMsg.setEditable(false);

		retryButton = new Button("Retry");
		retryButton.setMaxSize(200, 75);
		retryButton.setPrefSize(200, 75);
		retryButton.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		
		quitButton = new Button("Quit");
		quitButton.setMaxSize(200, 75);
		quitButton.setPrefSize(200, 75);
		quitButton.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
				
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
		
		retryButton.setOnAction(e-> {primaryStage.setScene(sceneMap.get("client"));
		primaryStage.setTitle("This is a client");
		Client clientConnection = new Client(data->{
				listItems2.getItems().clear();
				category1.setDisable(false);
				category2.setDisable(false);
				category3.setDisable(false);
				Platform.runLater(()->{listItems2.getItems().add(data.toString());
			});
		});
		clientConnection.start();
		});

		buttonBox = new HBox(50, quitButton, retryButton);
		bp.setMargin(buttonBox, new Insets(30));
		
		bp.setPadding(new Insets(30));
		bp.setTop(loseMsg);
		bp.setBottom(buttonBox);
		
		bp.setAlignment(buttonBox, Pos.BOTTOM_CENTER);
		

		return new Scene(bp, 400, 300);
		
	}

}
