import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class User{
    public String name;
    public Socket socket;
    public static HashMap<String,DataOutputStream> client 
                                           = new HashMap<String,DataOutputStream>(); 

  
    public synchronized void AddClient(String name) 
    {                     
    	this.name = name;
        try {
            System.out.println(name + " (이)가 입장했습니다.");
            client.put(name, new DataOutputStream(socket.getOutputStream()));
            System.out.println("참여한 인원 : "+client.size());
        }catch(Exception e){}
    }
    public synchronized void RemoveClient(String name)  
    {
    	this.name = name;
        try {
            client.remove(name);
            System.out.println(name + " (이)가 퇴장했습니다.");
            System.out.println("참여한 인원 : "+client.size());
        }catch(Exception e) {}
    }
	
    
	public synchronized void send(String msg, String name) throws Exception {
		Iterator iterator = client.keySet().iterator();
		while (iterator.hasNext()) {
			String clientname = (String) iterator.next();
			client.get(clientname).writeUTF(name + ":" + msg);
		}
	}
	 
}