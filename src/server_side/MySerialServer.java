package server_side;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MySerialServer implements Server {

	private int port;
	private ClientHandler c;
	private volatile boolean stop;


	public MySerialServer() {
		this.stop = false;
	}

	@Override
	public void open(int port,ClientHandler c) {
		this.port=port;
		this.c=c;
		new Thread(()->{
			try {
				runServer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	@Override
	public void stop() {
		stop=true;
	}



	private void runServer()throws Exception {
		ServerSocket server=new ServerSocket(port);
		if(port==1812){
			System.out.println("Solver server is up on port: "+port+" !");
		    System.out.println("waiting for clients to connect...");
		}
		else
			System.out.println("Client is up !");
		server.setSoTimeout(300000000);
		while(!stop){
			try{
				Socket aClient=server.accept();
				if(port==1812)
					System.out.println("Client is connected to the solver server!");
				else
					System.out.println("Connected to the FlightGear Simulator Server !");
				try {
					c.handleClient(aClient.getInputStream(), aClient.getOutputStream());
					aClient.close();
		} catch (IOException e) {
			System.out.println("invalid input2-output");
			e.printStackTrace();
		}
		}catch(SocketTimeoutException e) {
			System.out.println("Time Out");
			e.printStackTrace();
		}
		}
		server.close();
	}

}
