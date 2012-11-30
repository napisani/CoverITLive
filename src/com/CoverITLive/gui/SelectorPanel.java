package com.CoverITLive.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.CoverITLive.client.ChatClientReader;
import com.CoverITLive.client.ChatClientWriter;

public class SelectorPanel extends JPanel implements ListSelectionListener
{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 4698585594919292136L;
	private JList jlRecipients;
	private DefaultListModel recipListModel;
	private HashMap<String, ClientPanel> hmRecipientToPanel;
	private ChatClientWriter chatWriter;
	private Box boxRightCurrent;
	private Box boxLeftCurrent;
	private String strCurrentName;
	
	public SelectorPanel(String strUsername, ChatClientWriter chatWriter) 
	{
		setLayout(new BorderLayout());
		
		this.chatWriter = chatWriter;
		
		//initialize recipient list and the panel map
		hmRecipientToPanel = new HashMap<String, ClientPanel>();
		recipListModel = new DefaultListModel();
		recipListModel.addElement("ALL");
		jlRecipients = new JList(recipListModel);
		
		hmRecipientToPanel.put("ALL", new ClientPanel("ALL", chatWriter));
		
		//add the list to a scrolling pane
	    JScrollPane pane = new JScrollPane(jlRecipients);

	    //  Format the list and the buttons in a vertical box
	    Box leftBox = new Box(BoxLayout.Y_AXIS);
	    Box rightBox = new Box(BoxLayout.Y_AXIS);
	    
	    // set the current box so it can be changed later
	    boxRightCurrent = rightBox;
	    boxLeftCurrent = leftBox;
	    
	    //add hooks for selection listener
	    jlRecipients.addListSelectionListener(this);

	    //rightBox.add(clientPanel);
	    
	    leftBox.add(pane);
	    
	    strCurrentName = "ALL";
	    
	    //initialize the right box to the conversation with the group - ALL
	    rightBox.add(hmRecipientToPanel.get(strCurrentName)); 
	    
	    add(leftBox, BorderLayout.WEST);
	    add(rightBox, BorderLayout.CENTER);
	}

	@Override
	public void valueChanged(ListSelectionEvent selectionEvent)
	{
		//as long as the user is not currently in the middle of selecting a recipient
		if(selectionEvent.getValueIsAdjusting() == false)
		{
			//get the recipient name of the selected index
			JList list = (JList) selectionEvent.getSource();
	        Object selectionValues[] = list.getSelectedValues();
	        
	        if(selectionValues.length > 0) // if one or more item is selected
	        {
	        	//take only the first selected item and use it - ignore extras
	        	String strRecipient = (String) selectionValues[0];
	        	ClientPanel cPanelCurrent = hmRecipientToPanel.get(strRecipient);
	        	
	        	if(cPanelCurrent != null)
	        	{
	        		//swap out the ClientPanel for the new recipient
	        		swapInNewPanel(cPanelCurrent, strRecipient);
	        	}
	        }
		}
	}
	
	private void swapInNewPanel(ClientPanel cPanel, String strNewRecipient)
	{
		//box it up
    	Box rightBox = new Box(BoxLayout.Y_AXIS);
    	rightBox.add(cPanel);
    	remove(boxRightCurrent);
    	add(rightBox); //add to panel
    	boxRightCurrent = rightBox;
    	strCurrentName = strNewRecipient; // the current recipient name
    	revalidate(); //refresh the panel
	}
	
	private void cleanUpRecipientMap(ArrayList<String> alNewNames)
	{
		HashMap<String, ClientPanel> hmNewRecipients = new HashMap<String, ClientPanel>();
		for(String strName: alNewNames)
		{
			ClientPanel cPanel = hmRecipientToPanel.get(strName);
			if(cPanel == null)
			{
				cPanel = new ClientPanel(strName, chatWriter);
			}
			hmNewRecipients.put(strName, cPanel);
		}
		hmRecipientToPanel = hmNewRecipients;
		
		if(alNewNames.contains(strCurrentName) == false) // if current recipient logs off
		{
			ClientPanel cPanel = hmRecipientToPanel.get("ALL");
			if(cPanel != null) //switch to ALL
			{
				swapInNewPanel(cPanel, "ALL");
			}
			else // if ALL is not there (this should never happen)
			{
				//cycle through all of the available recipients
				for(Map.Entry<String, ClientPanel> entry: hmRecipientToPanel.entrySet())
				{
					if(entry.getValue() != null) 
					{
						//swap in the first panel that isn't null
						swapInNewPanel(entry.getValue(), entry.getKey());
						break;
					}
				}
			}
			
		}
	}
	
	public void setRecipientList(ArrayList<String> alNewRecipients)
	{
		recipListModel = new DefaultListModel();

		for(String strRecipient: alNewRecipients)
		{
			recipListModel.addElement(strRecipient);
		}
		
		jlRecipients = new JList(recipListModel);
		
		//add the list to a scrolling pane
	    JScrollPane pane = new JScrollPane(jlRecipients);
	    
	    //add hooks for selection listener
	    jlRecipients.addListSelectionListener(this);

	    //  Format the list and the buttons in a vertical box
	    Box leftBox = new Box(BoxLayout.Y_AXIS);
	    leftBox.add(pane);
	    remove(boxLeftCurrent);
	    add(leftBox,  BorderLayout.WEST);
	    boxLeftCurrent = leftBox;
	    
	    cleanUpRecipientMap(alNewRecipients);
		
		revalidate();
		repaint();
	}
	
	
	//flukey 
	/*
	public void setRecipientList2(ArrayList<String> alNewRecipients)
	{
		recipListModel.removeAllElements();
		for(String strRecipient: alNewRecipients)
		{
			recipListModel.addElement(strRecipient);
		}
		cleanUpRecipientMap(alNewRecipients);
		
		revalidate();
		repaint();
	}
	*/
	
	public JList getJlRecipients()
	{
		return jlRecipients;
	}

	public void setJlRecipients(JList jlRecipients)
	{
		this.jlRecipients = jlRecipients;
	}

	public HashMap<String, ClientPanel> getRecipientToPanel()
	{
		return hmRecipientToPanel;
	}

	public void setRecipientToPanel(HashMap<String, ClientPanel> hmRecipientToPanel)
	{
		this.hmRecipientToPanel = hmRecipientToPanel;
	}
	
	
}
