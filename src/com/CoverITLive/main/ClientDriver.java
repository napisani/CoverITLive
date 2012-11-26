package com.CoverITLive.main;

import java.io.IOException;
import java.util.Scanner;

import com.CoverITLive.client.ChatClient;
import com.CoverITLive.client.ChatClientReader;
import com.CoverITLive.client.ChatClientWriter;

public class ClientDriver
{
	public static void main1(String[] args) throws IOException
	{
		System.out.println("CoverITLive Started");
		
		ChatClient oChatClient = new ChatClient("127.0.0.1", 3000);
		oChatClient.start();
	}
	public static void main2(String[] args) throws IOException
	{
		String strUsername;
		System.out.println("CoverITLive Started");
		Scanner in = new Scanner(System.in);
		System.out.println("enter your username");
		
		strUsername = in.nextLine();
		
		//ChatClientWriter oChatClient = new ChatClientWriter("127.0.0.1", 3000, strUsername);
		//oChatClient.start();
		//ChatClientReader oChatClientf = new ChatClientReader("127.0.0.1", 3000);
		//oChatClientf.start();	
	}
}
