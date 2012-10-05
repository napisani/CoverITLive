package com.CoverITLive.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.CoverITLive.client.ChatClientReader;
import com.CoverITLive.client.ChatClientResource;
import com.CoverITLive.client.ChatClientWriter;

public class ClientPanel extends JPanel implements ActionListener, PropertyChangeListener, KeyListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6570763185326650432L;
	
	private JButton send;
	private JTextArea textArea;
	private JComboBox dropdown; 
	private JTextPane msgLog;
	private JScrollPane msgLogPane;
	private ChatClientReader chatReader;
	private ChatClientWriter chatWriter;
	private ChatClientResource chatResource;
	
	public ClientPanel(String strUsername)
	{
		super(new GridLayout(4, 1));
		
		send = new JButton("SEND");
		textArea = new JTextArea();
		dropdown = new JComboBox();
		dropdown.addItem("ALL"); //explicitly add the ALL option
		msgLog = new JTextPane();
		msgLogPane = new JScrollPane(msgLog);
		msgLog.setEditable(false);
		msgLogPane.setEnabled(false);
	
		//initialize the sockets and streams
		chatResource = new ChatClientResource("127.0.0.1", 3000);
		
		chatReader = new ChatClientReader(chatResource);
		chatReader.start(); // spool reader thread
		
		chatWriter = new ChatClientWriter(chatResource, strUsername);
		
		//establish all listener hooks
		chatReader.getConnectedUserPropertyChange().addPropertyChangeListener(this);
		chatReader.getMessagePropertyChange().addPropertyChangeListener(this);
		send.addActionListener(this);
		textArea.addKeyListener(this);
		
		add(msgLogPane);
		add(dropdown);
		add(textArea);
		add(send);
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource().equals(send)) // if the SEND button was pressed
		{
			boolean bSuccess = sendMessage();
			if(bSuccess)
			{
				textArea.setText("");
			}
		}
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent pcEvent) // a new message was received
	{
		if(pcEvent.getPropertyName() != null && pcEvent.getPropertyName().equals("MESSAGE"))
		{	
			// A new message came in - handle it!
			//System.out.println(pcEvent.getNewValue());
			StyledDocument styledDoc = msgLog.getStyledDocument();
			SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
			StyleConstants.setForeground(simpleAttributeSet, Color.GREEN);
			StyleConstants.setBackground(simpleAttributeSet, Color.BLACK);
			StyleConstants.setBold(simpleAttributeSet, true);
			msgLog.setCaretPosition(styledDoc.getLength());
			try
			{
				styledDoc.insertString(styledDoc.getLength(), (String) pcEvent.getNewValue(), simpleAttributeSet);
			
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		else if(pcEvent.getPropertyName() != null && pcEvent.getPropertyName().equals("CONNECTED_USERS"))
		{
			// A new list of connected users came in - handle it!
			ArrayList<String> alConnectedUsers = (ArrayList<String>) pcEvent.getNewValue();
			alConnectedUsers.add("ALL");
			
		    DefaultComboBoxModel dcbmNewModel = new DefaultComboBoxModel(alConnectedUsers.toArray());  
		    if(dcbmNewModel.getIndexOf(dropdown.getSelectedItem()) >= 0)
		    {
		    	dcbmNewModel.setSelectedItem(dropdown.getSelectedItem());
		    }
		    dropdown.setModel(dcbmNewModel);  
		}
	}
	
	@Override
	public void keyPressed(KeyEvent keyEvent)
	{
		if(keyEvent.getKeyChar() == KeyEvent.VK_ENTER) 
		{ 
			boolean bSuccess = sendMessage();
			if(bSuccess)
			{
				textArea.setText("");
			}
		} 
	}

	@Override
	public void keyReleased(KeyEvent keyEvent)
	{
		// Unused	
	}

	@Override
	public void keyTyped(KeyEvent keyEvent)
	{
		// Unused
	}
	
	public boolean sendMessage()
	{
		if(textArea.getText() != null && textArea.getText().isEmpty() == false) //only send if the user typed something
		{
			if(dropdown.getSelectedItem() != null)
			{
				String strRecipient = (String) dropdown.getSelectedItem();
				String strMessage = textArea.getText();
				chatWriter.sendMessage(strRecipient, strMessage);
			}
			else
			{
				JOptionPane.showInputDialog("No Recipient Selected");
				return false;
			}
		}
		return true;
	}
}
