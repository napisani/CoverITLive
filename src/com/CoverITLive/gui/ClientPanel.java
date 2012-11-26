package com.CoverITLive.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.CoverITLive.client.ChatClientWriter;

public class ClientPanel extends JPanel implements ActionListener, KeyListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6570763185326650432L;
	
	private JButton send;
	private JTextArea textArea;
	private JTextPane msgLog;
	private JScrollPane msgLogPane;
	private ChatClientWriter chatWriter;
	private String strRecipient;
	private JLabel jLabel;
	
	public ClientPanel( String strRecipient, ChatClientWriter chatWriter)
	{
		super(new GridLayout(4, 1));
		
		this.strRecipient = strRecipient;
		jLabel = new JLabel("TO: " + strRecipient);
		
		send = new JButton("SEND");
		textArea = new JTextArea();
		msgLog = new JTextPane();
		msgLogPane = new JScrollPane(msgLog);
		msgLog.setEditable(false);
		msgLogPane.setEnabled(false);

		this.chatWriter = chatWriter;
		
		send.addActionListener(this);
		textArea.addKeyListener(this);
		
		add(jLabel);
		add(msgLogPane);
		add(textArea);
		add(send);
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource().equals(send)) // if the SEND button was pressed
		{
			sendMessage();
		}
		
	}
	
	public void appendMessageLog(String strMessageRecieved)
	{
		StyledDocument styledDoc = msgLog.getStyledDocument();
		SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
		StyleConstants.setForeground(simpleAttributeSet, Color.GREEN);
		StyleConstants.setBackground(simpleAttributeSet, Color.BLACK);
		StyleConstants.setBold(simpleAttributeSet, true);
		msgLog.setCaretPosition(styledDoc.getLength());
		try
		{
			styledDoc.insertString(styledDoc.getLength(), strMessageRecieved, simpleAttributeSet);
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		revalidate();
	}
	
	@Override
	public void keyPressed(KeyEvent keyEvent)
	{
		//Unused
	}
	
	
	@Override
	public void keyReleased(KeyEvent keyEvent)
	{	
		if(keyEvent.getKeyChar() == KeyEvent.VK_ENTER) 
		{ 
			sendMessage();
		} 
	}

	@Override
	public void keyTyped(KeyEvent keyEvent)
	{
		// Unused
	}
	
	private void sendMessage()
	{
		if(textArea.getText() != null && textArea.getText().isEmpty() == false) //only send if the user typed something
		{
			String strMessage = textArea.getText();
			chatWriter.sendMessage(strRecipient, strMessage);
			textArea.setText("");
			if(strRecipient.equals("ALL") == false)
			{
				appendMessageLog(chatWriter.getUsername() + 
								 " ---> " +
								 strRecipient +
								 " : " + 
								 strMessage + "\n");
			}
		}
	}
}
