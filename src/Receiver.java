import java.io.DataInputStream;
import java.net.Socket;

public class Receiver  implements Runnable{

    Socket socket;
    DataInputStream receive;
    String name;
    User user = new User();

    public Receiver(User user,Socket socket) throws Exception
    {
        this.user = user;
        this.socket = socket;
        receive = new DataInputStream(socket.getInputStream());
       user.AddClient(name);
    }

	public void run()
    {
        try
        {
        	while(true) {}
           
        }catch(Exception e) {
            
            user.RemoveClient(this.name);
        }        
    }
}