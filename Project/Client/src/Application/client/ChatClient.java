// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package Application.client;

import Application.Common.ChatIF;
import Application.Common.Message;
import Application.Common.MessageFromClient;
import Application.Common.Subscriber;
import ocsf.AbstractClient;


import java.io.*;
import java.util.ArrayList;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  public static Subscriber s1 = new Subscriber(null,null,null,null,null,null,null);
  public static ArrayList<Subscriber> subs = new ArrayList<>();
  public static boolean awaitResponse = false;
  public static String servermsg = new String();
  /**
   * The interface type variable.  It allows the implementation of
   * the display method in the client.
   */
  ChatIF clientUI;

  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
	 
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    //openConnection();
  }

  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
	  subs = new ArrayList<>();
	  System.out.println("--> handleMessageFromServer");
	  System.out.println(msg);
     
	  awaitResponse = false;
	  String st;
	  st=msg.toString();
	  BufferedReader bufReader = new BufferedReader(new StringReader(st));
	  //String[] lines = st.split(System.getProperty("line.separator"));
	  //System.out.println("bbbbbbbb " + lines);
	  String line=null;
	  try {
		while( (line=bufReader.readLine()) != null )
		  {
			s1 = new Subscriber(null,null,null,null,null,null,null);
			  String[] result = line.split("\\s");
			  s1.setFirstname(result[0]);
			  s1.setLastname(result[1]);
			  s1.setId(result[2]);
			  s1.setPhonenumber(result[3]);
			  s1.setEmailaddress(result[4]);
			  s1.setCreditcardnumber(result[5]);
			  s1.setSubscribernumber(result[6]);
			  subs.add(s1);
		  }
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 // for (String line : lines) {

	 // }
  }
  
  public void handleMessageFromServer(String msg) 
  {
	  System.out.println("--> handleMessageFromServer");
	  System.out.println(msg);
	  servermsg = msg.toString();
	  awaitResponse = false;
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  
  public void handleMessageFromClientUI(String message)  
  {
    try
    {
    	openConnection();//in order to send more than one message
       	awaitResponse = false;
        Message msg = new Message(message, MessageFromClient.LOGIN_REQUEST); // TODO: change the login request to generic requests.

        try{
            sendToServer(msg);
        }catch (Exception ex){
            ex.printStackTrace();
        }

		// wait for response
		while (awaitResponse) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }
    catch(IOException e)
    {
    	e.printStackTrace();
      quit();
    }
  }
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class


