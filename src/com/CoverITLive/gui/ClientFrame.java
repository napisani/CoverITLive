package com.CoverITLive.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.CoverITLive.client.ChatClientReader;
import com.CoverITLive.client.ChatClientResource;
import com.CoverITLive.client.ChatClientWriter;

public class ClientFrame extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4296556127181469716L;
	
	public ClientFrame()
	{
		super("CoverITLive - Client");
		String strUsername;
		ChatClientResource chatResource;
		ChatClientWriter chatWriter;
		ChatClientReader chatReader;
		
		do
		{
			strUsername = JOptionPane.showInputDialog("Enter your username");
		}while(strUsername == null || strUsername.isEmpty());
		
		chatResource = getClientResource();
		
		chatWriter = new ChatClientWriter(chatResource, strUsername);
		chatReader = new ChatClientReader(chatResource);
		
		//ClientPanel cPanel = new ClientPanel(strUsername, chatWriter, chatReader, "ALL");
		SelectorPanel selectorPanel = new SelectorPanel(strUsername, chatWriter);
		
		MessageListener msgListener = new MessageListener(this, selectorPanel);
		//establish all listener hooks
		chatReader.getConnectedUserPropertyChange().addPropertyChangeListener(msgListener);
		chatReader.getMessagePropertyChange().addPropertyChangeListener(msgListener);
		
		chatReader.start(); // spool up reader thread
		
		this.setSize(800, 500);
		getContentPane().add(selectorPanel,  BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	private ChatClientResource getClientResource()
	{
		ChatClientResource chatResource = null;
		String strHostAddress;
		do
		{
			strHostAddress = "127.0.0.1";
			//strHostAddress = JOptionPane.showInputDialog("Enter your host address");
		}while(strHostAddress == null || strHostAddress.isEmpty());
		try
		{
			chatResource = new ChatClientResource(strHostAddress, 3000);
		}
		catch(Exception e)
		{
			//JOptionPane.showInternalConfirmDialog(this, "A connection error occured");
			JOptionPane.showMessageDialog(new JFrame(), "A connection error occured connecting to " + strHostAddress, 
					"Connection Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return chatResource;
	}
}
