import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;



public class Client extends Thread{

	
	Socket socketClient;
	
	ObjectOutputStream out;
	ObjectInputStream in;
		
	public HashMap<String, Integer> WordGuessesLeft = new HashMap<String, Integer>();
	public String currCategory = new String();
	public String currWord = new String();
	
	ArrayList<String> failedAList = new ArrayList<String>();
	ArrayList<String> failedBList = new ArrayList<String>();
	ArrayList<String> failedCList = new ArrayList<String>();
	
	private Consumer<Serializable> callback;
	String guessedL = new String();
	
	Client(Consumer<Serializable> call){
	
		callback = call;
		
		WordGuessesLeft.put("shirt", 6);
		WordGuessesLeft.put("jacket", 6);
		WordGuessesLeft.put("pants", 6);
		WordGuessesLeft.put("toucan", 6);
		WordGuessesLeft.put("owl", 6);
		WordGuessesLeft.put("flamingo", 6);
		WordGuessesLeft.put("black", 6);
		WordGuessesLeft.put("violet", 6);
		WordGuessesLeft.put("cyan", 6);
	}
	
	public void run() {
		
		try {
		socketClient= new Socket("127.0.0.1",5555);
	    out = new ObjectOutputStream(socketClient.getOutputStream());
	    in = new ObjectInputStream(socketClient.getInputStream());
	    socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {}
		
		while(true) {
			 
			try {
				String message = in.readObject().toString();
			
				callback.accept(message);
			}
			catch(Exception e) {}
		}
	
    }
	
	public void send(String data) {
		
		try {
			out.writeObject(data);
			guessedL = data;


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
