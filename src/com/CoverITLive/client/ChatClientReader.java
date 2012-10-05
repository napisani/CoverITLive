package com.CoverITLive.client;

import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import com.CoverITLive.businessobjects.JsonChatObject;
import com.CoverITLive.businessobjects.MessageType;

public class ChatClientReader extends Thread
{



	private ChatClientResource chatClientResource;
	private BufferedReader reader;
	private PropertyChangeSupport messagePropertyChange;
	private PropertyChangeSupport connectedUserPropertyChange;
	private ArrayList<String> alConnectedUsers;
	private String strMessage;
	
	public ChatClientReader(ChatClientResource chatClientResource)
	{
		super();
		this.chatClientResource = chatClientResource;
		reader = this.chatClientResource.getBufferedReader();
		alConnectedUsers = new ArrayList<String>();
		strMessage = "";
		messagePropertyChange = new PropertyChangeSupport(strMessage);
		connectedUserPropertyChange = new PropertyChangeSupport(alConnectedUsers);
	}

	@Override
	public void run()
	{

		try
		{
			String strSocketInput = "";
			while(true)
			{	
				if(strSocketInput != null && strSocketInput.isEmpty() == false)
				{
					System.out.println("Message From Group: " + strSocketInput);
					
					JsonChatObject jChatObjTemp = new JsonChatObject(strSocketInput);
					
					if(jChatObjTemp.getRequestType() == MessageType.STANDARD.ordinal())
					{
						String strMessageTemp =  jChatObjTemp.getUsername() + 
												 " ---> " + 
												 jChatObjTemp.getRecipient() + " : " +
												 jChatObjTemp.getMessage() +
												 "\n";
						messagePropertyChange.firePropertyChange("MESSAGE", strMessage, strMessageTemp);
						strMessage = strMessageTemp;
					}
					else if(jChatObjTemp.getRequestType() == MessageType.GETCONNECTED.ordinal())
					{
						connectedUserPropertyChange.firePropertyChange("CONNECTED_USERS", alConnectedUsers, jChatObjTemp.getConnectedUsers());
						alConnectedUsers = jChatObjTemp.getConnectedUsers();
					}
				}
			   
			    strSocketInput = reader.readLine();
			}

		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public PropertyChangeSupport getMessagePropertyChange()
	{
		return messagePropertyChange;
	}

	public void setMessagePropertyChange(PropertyChangeSupport messagePropertyChange)
	{
		this.messagePropertyChange = messagePropertyChange;
	}

	public PropertyChangeSupport getConnectedUserPropertyChange()
	{
		return connectedUserPropertyChange;
	}

	public void setConnectedUserPropertyChange(PropertyChangeSupport connectedUserPropertyChange)
	{
		this.connectedUserPropertyChange = connectedUserPropertyChange;
	}

}
