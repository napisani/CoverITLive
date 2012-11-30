package com.CoverITLive.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient
{
	protected Socket socket = null;
	protected DataInputStream streamIn = null;
	protected DataOutputStream streamOut = null;

	public ChatClient(String serverName, int serverPort)
	{
		System.out.println("Establishing connection. Please wait ...");
		try
		{
			socket = new Socket(serverName, serverPort);
			System.out.println("Connected: " + socket);
			start();
		} catch (UnknownHostException uhe)
		{
			System.out.println("Host unknown: " + uhe.getMessage());
		} catch (IOException ioe)
		{
			System.out.println("Unexpected exception: " + ioe.getMessage());
		}
		String line = "";
		while (!line.equals(".bye"))
		{
			try
			{
				line = streamIn.readLine();
				streamOut.writeUTF(line);
				streamOut.flush();
			} catch (IOException ioe)
			{
				System.out.println("Sending error: " + ioe.getMessage());
			}
		}
	}

	public void start() throws IOException
	{
		streamIn = new DataInputStream(System.in);
		streamOut = new DataOutputStream(socket.getOutputStream());
	}

	public void stop()
	{
		try
		{
			if (streamIn != null)
				streamIn.close();
			if (streamOut != null)
				streamOut.close();
			if (socket != null)
				socket.close();
		} catch (IOException ioe)
		{
			System.out.println("Error closing ...");
		}
	}
}
