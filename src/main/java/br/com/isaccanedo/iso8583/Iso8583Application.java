package br.com.isaccanedo.iso8583;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.net.*;

@SpringBootApplication
public class Iso8583Application {
	
	public static class Requester{
		Socket requestSocket;
		ObjectOutputStream out;
	 	ObjectInputStream in;
	 	String message;
		Requester(){}
		void run()
		{
			try{
				//1. creating a socket to connect to the server
				requestSocket = new Socket("127.0.0.1", 2004);
				System.out.println("Connected to localhost in port 2004");
				//2. get Input and Output streams
				out = new ObjectOutputStream(requestSocket.getOutputStream());
				out.flush();
				in = new ObjectInputStream(requestSocket.getInputStream());
				//3: Communicating with the server
				do{
					try{
						message = (String)in.readObject();
						System.out.println("server>" + message);
						sendMessage("Hi my server");
						message = "bye";
						sendMessage(message);
					}
					catch(ClassNotFoundException classNot){
						System.err.println("data received in unknown format");
					}
				}while(!message.equals("bye"));
			}
			catch(UnknownHostException unknownHost){
				System.err.println("You are trying to connect to an unknown host!");
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
			finally{
				//4: Closing connection
				try{
					in.close();
					out.close();
					requestSocket.close();
				}
				catch(IOException ioException){
					ioException.printStackTrace();
				}
			}
		}
		void sendMessage(String msg)
		{
			try{
				out.writeObject(msg);
				out.flush();
				System.out.println("client>" + msg);
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}

	public static void main(String[] args) {
		SpringApplication.run(Iso8583Application.class, args);
		Requester client = new Requester();
		client.run();
		
	}

	}
}
