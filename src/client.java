import java.io.*;
import java.net.*;
import java.util.*;

public class client{
	
	public static String  line;
	static String temp_line;
	static int port_num;//global variable for the read function

	public static void read() {
		File file = new File("C:/Users/skyey/Desktop/4학년 2학기/컴퓨터네트워크 및 실습/text.txt");
		try (BufferedReader buffer = new BufferedReader(new FileReader(file))) {
			
			String temp_line;
			line = buffer.readLine();
			temp_line = buffer.readLine();
			port_num = Integer.parseInt(temp_line);
			
		} catch (IOException e) {
			 line = "localhost";
			 port_num = 9999; 
		}//if the file doesn't exist,then do this default part
	}//read the information from the text file.

	public static void main(String[] args)throws Exception {
		
		Socket socket = null;
		
		try {
			read();
			socket = new Socket(line,port_num);       
            Thread th = new Thread(new Login());
            th.start();
		}catch(IOException e) {
			
		}
	}
}