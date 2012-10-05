package com.CoverITLive.businessobjects;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonChatObject
{	
	
	private String strUsername;
	private String strRecipient;
	private String strMessage;
	private int iRequestType;
	private ArrayList<String> alConnectedUsers;
	
	public JsonChatObject()
	{
		strUsername = "";
		strRecipient = "";
		strMessage = "";
		iRequestType = MessageType.STANDARD.ordinal();
		alConnectedUsers = new ArrayList<String>();
	}
	public JsonChatObject(String strUsername, String strRecipient,
			String strMessage, int iRequestType)
	{
		this.strUsername = strUsername;
		this.strRecipient = strRecipient;
		this.strMessage = strMessage;
		this.iRequestType = iRequestType;
		alConnectedUsers = new ArrayList<String>();
	}
	public JsonChatObject(String strJson)
	{
		Gson gson =  new Gson();
	
		JsonElement jElement = new JsonParser().parse(strJson);
		JsonObject jObj = jElement.getAsJsonObject();
		strUsername = gson.fromJson(jObj.get("sender"),String.class);
		strRecipient = gson.fromJson(jObj.get("recipient"),String.class);
		strMessage = gson.fromJson(jObj.get("message"),String.class);
		iRequestType = Integer.parseInt(jObj.get("requestType").toString());
		
		alConnectedUsers = new ArrayList<String>();
		if(iRequestType == MessageType.GETCONNECTED.ordinal())
		{
			JsonArray jArray = jObj.getAsJsonArray("connectedUsers");
		
			for(JsonElement jElem: jArray)
			{
				alConnectedUsers.add(jElem.getAsString());
			}
		}
	}
	
	public String getUsername()
	{
		return strUsername;
	}
	public void setUsername(String strUsername)
	{
		this.strUsername = strUsername;
	}
	public String getRecipient()
	{
		return strRecipient;
	}
	public void setRecipient(String strRecipient)
	{
		this.strRecipient = strRecipient;
	}
	public String getMessage()
	{
		return strMessage;
	}
	public void setMessage(String strMessage)
	{
		this.strMessage = strMessage;
	}
	public int getRequestType()
	{
		return iRequestType;
	}
	public void setRequestType(int iRequestType)
	{
		this.iRequestType = iRequestType;
	}
	
	public ArrayList<String> getConnectedUsers()
	{
		return alConnectedUsers;
	}
	public void setConnectedUsers(ArrayList<String> alConnectedUsers)
	{
		this.alConnectedUsers = alConnectedUsers;
	}

	public JsonObject toJsonObject()
	{
		JsonObject jObj = new JsonObject();
		jObj.addProperty("sender", strUsername);
		jObj.addProperty("recipient", strRecipient);
		jObj.addProperty("message", strMessage);
		jObj.addProperty("requestType", iRequestType);
		return jObj;
	}
	
	public String toString()
	{
		return toJsonObject().toString();
	}
}
