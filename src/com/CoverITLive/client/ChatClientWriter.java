package com.CoverITLive.client;

import java.io.BufferedWriter;
import java.io.IOException;

import com.CoverITLive.businessobjects.JsonChatObject;
import com.CoverITLive.businessobjects.MessageType;

/**
 * 
 * @author napisani
 * @category 
 */
public class ChatClientWriter
{
	private String strUsername;
	private ChatClientResource chatClientResource;
	private BufferedWriter writer;
	public ChatClientWriter(ChatClientResource chatClientResource, String strUsername)
	{
		super();
		this.chatClientResource = chatClientResource;
		this.setUsername(strUsername);
		writer = chatClientResource.getBufferedWriter();
		sendInitialConnectionData();
	}

	private void sendInitialConnectionData()
	{
		try
		{
			JsonChatObject oInitialData = new JsonChatObject();
			oInitialData.setRequestType(MessageType.INITIAL.ordinal());
			oInitialData.setUsername(strUsername);
			writer.write(oInitialData.toString() + "\n");
			writer.flush();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void sendMessage(String strRecipient, String strMessage)
	{

		try
		{
			JsonChatObject oChatObj = new JsonChatObject();
			oChatObj.setUsername(strUsername);
			oChatObj.setRecipient(strRecipient);
			oChatObj.setMessage(strMessage);
			System.out.println("sent:\n" + oChatObj.toString());
			writer.write(oChatObj.toString() + "\n");
			writer.flush();
		}
		catch (IOException ioe)
		{
			System.out.println("Sending error: " + ioe.getMessage());
		}
		
		
	}

	/**
	 * @param strUsername the strUsername to set
	 */
	public void setUsername(String strUsername)
	{
		this.strUsername = strUsername;
	}

	/**
	 * @return the strUsername
	 */
	public String getUsername()
	{
		return strUsername;
	}


	
}
