import java.io.*;
import java.net.*;
import java.util.*;

public class server{
	
	static String  line;
	static String temp_line;
	static int port_num;//the global variable for the read function
	
	public static void read() {
		File file = new File("C:/Users/skyey/Desktop/4학년 2학기/컴퓨터네트워크 및 실습/text.txt");
		try (BufferedReader buffer = new BufferedReader(new FileReader(file))) {
			String temp_line;
			
			line = buffer.readLine();
			temp_line = buffer.readLine();
			port_num = Integer.parseInt(temp_line);
			//line = "localhost";
			//port_num = 9999;
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}//if the file doesn't exist, then do this default..
	}//read information from the text file
	
	public static void main(String args[] ){
		read();//get the port number 
		Socket socket = null;
		System.out.println("연결을 기다리고 있습니다...");
		
		User user = new User();
		ServerSocket server_socket = null;
		int count = 0;
		Thread thread[] = new Thread[10];
		
		try {
			server_socket = new ServerSocket(port_num);
			
			while(true) {
				socket = server_socket.accept();
				
				thread[count] = new Thread(new Receiver(user,socket));
				thread[count].start();
				count++;
				System.out.println(count); //접속한 클라이언트 수
				System.out.println("연결되었습니다.");
			}
		}catch(Exception e) {
			
		};
	}
}