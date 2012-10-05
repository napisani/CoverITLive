package com.CoverITLive.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
		do
		{
			strUsername = JOptionPane.showInputDialog("Enter your username");
		}while(strUsername == null || strUsername.isEmpty());
		ClientPanel cPanel = new ClientPanel(strUsername);
		this.setSize(800, 500);
		getContentPane().add(cPanel,  BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
