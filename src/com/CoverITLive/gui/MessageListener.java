package com.CoverITLive.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.CoverITLive.businessobjects.JsonChatObject;

public class MessageListener implements PropertyChangeListener
{
	private SelectorPanel selectorPanel;
	private JFrame jFrame;
	
	public MessageListener(JFrame jFrame,SelectorPanel selectorPanel)
	{
		this.selectorPanel = selectorPanel;
		this.jFrame = jFrame;
	}	
	
	@Override
	public void propertyChange(PropertyChangeEvent pcEvent) // a new message was received
	{
		if(pcEvent.getPropertyName() != null && pcEvent.getPropertyName().equals("MESSAGE"))
		{	
			// A new message came in - handle it!
			JsonChatObject jChatObj = (JsonChatObject) pcEvent.getNewValue();
			
			String strMessage =  jChatObj.getUsername() + 
								 " ---> " + 
								 jChatObj.getRecipient() + " : " +
								 jChatObj.getMessage() + "\n";
			
			ClientPanel cPanel;
			
			//if the message was sent to everyone
			if("ALL".equals(jChatObj.getRecipient()) == true)
			{
				cPanel = selectorPanel.getRecipientToPanel().get("ALL"); //use the ALL panel
			}
			else
			{
				cPanel = selectorPanel.getRecipientToPanel().get(jChatObj.getUsername()); //get panel by user's name
			}
			
			if(cPanel != null) // as long a panel exists to add this message to
			{
				cPanel.appendMessageLog(strMessage);
			}
			jFrame.repaint();
			
			
		}
		else if(pcEvent.getPropertyName() != null && pcEvent.getPropertyName().equals("CONNECTED_USERS"))
		{
			ArrayList<String> alConnectedUsers = ((JsonChatObject) pcEvent.getNewValue()).getConnectedUsers();
			
			// explicitly add the ALL recipient to all 
			alConnectedUsers.add("ALL");
			//System.out.println(alConnectedUsers.toString());
			selectorPanel.setRecipientList(alConnectedUsers);
			jFrame.repaint();
		}
	}
}
