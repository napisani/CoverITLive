package com.CoverITLive.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClientResource
{
	


	protected Socket socket = null;
	protected Scanner sysIn = null;
	protected BufferedReader bufferedReader = null;
	protected BufferedWriter bufferedWriter = null;
	
	
	public ChatClientResource(String serverName, int serverPort) throws UnknownHostException, IOException
	{
		System.out.println("Establishing connection. Please wait ...");
		socket = new Socket(serverName, serverPort);
		System.out.println("Connected: " + socket);

		sysIn = new Scanner(System.in);
		bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		System.out.println("Finished opening streams");
	}
	
	public Socket getSocket()
	{
		return socket;
	}


	public void setSocket(Socket socket)
	{
		this.socket = socket;
	}


	public Scanner getSysIn()
	{
		return sysIn;
	}


	public void setSysIn(Scanner sysIn)
	{
		this.sysIn = sysIn;
	}


	public BufferedReader getBufferedReader()
	{
		return bufferedReader;
	}


	public void setBufferedReader(BufferedReader bufferedReader)
	{
		this.bufferedReader = bufferedReader;
	}


	public BufferedWriter getBufferedWriter()
	{
		return bufferedWriter;
	}


	public void setBufferedWriter(BufferedWriter bufferedWriter)
	{
		this.bufferedWriter = bufferedWriter;
	}
	
	
	
}
