import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
 
public class RSAServer {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        
        HashMap<String, Store> userdata = new HashMap<String, Store>();
         
        try {
            // ���������� �����ϰ� 5000�� ��Ʈ�� ����(bind) ��Ų��.
            serverSocket = new ServerSocket(5001);
            System.out.println(getTime() + " ������ �غ�Ǿ����ϴ�.");
        } catch (IOException e) {
            e.printStackTrace();
        } // try - catch
         
        while (true) {
            try {
                System.out.println(getTime() + " �����û�� ��ٸ��ϴ�.");
                // ���������� Ŭ���̾�Ʈ�� �����û�� �� ������ ������ ���߰� ��� ��ٸ���.
                // Ŭ���̾�Ʈ�� �����û�� ���� Ŭ���̾�Ʈ ���ϰ� ����� ���ο� ������ �����Ѵ�.
                Socket socket = serverSocket.accept();
                System.out.println(getTime() + socket.getInetAddress() + " �κ��� �����û�� ���Խ��ϴ�.");
                OutputStream out = socket.getOutputStream(); 
                //DataOutputStream dos = new DataOutputStream(out); // �⺻�� ������ ó���ϴ� ������Ʈ��
                ObjectOutputStream oos = new ObjectOutputStream(out);
                InputStream in = socket.getInputStream();
                //DataInputStream dis = new DataInputStream(in);  // �⺻�� ������ ó���ϴ� ������Ʈ��
                ObjectInputStream ois = new ObjectInputStream(in);
                
                Store store = null;
				try {
					store = (Store) ois.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
                if (store.command.equals("O")) {
                	if (userdata.containsKey(store.name)) {
                		oos.writeObject(new Store("E", null, null, 0, null));
                		System.out.println("���� : �̸��ߺ�");
                	} else {
                		store.ip = socket.getInetAddress().toString().split("/")[1];
                		userdata.put(store.name, store);

                		oos.writeObject(new Store("P", null, null, 0, null));
                		System.out.println("���� : "  + store.name);
                	}
                } else if (store.command.equals("S")){
                	if (userdata.containsKey(store.name)) {

                    	System.out.println("�˻� : " + userdata.get(store.name));
                		Store storetemp = userdata.get(store.name);
                		storetemp.command = "P";
                		oos.writeObject(storetemp);
                	} else {
                		System.out.println("���� : ã�� �г��� ����");
                		oos.writeObject(new Store("E", null, null, 0, null));
                	}
                }
                // ��Ʈ���� ������ �޾��ش�.
                ois.close();
                oos.close();
                socket.close();
                
                //System.out.println("�������� : " + userdata.get(s[1]));
            	//userdata.remove(s[1]);
            } catch (IOException e) {
                e.printStackTrace();
            } // try - catch
        } // while
    } // main
     
    static String getTime() {
        SimpleDateFormat f = new SimpleDateFormat("[hh:mm:ss]");
        return f.format(new Date());
    } // getTime    
} // TcpServerTest
class Store implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
	String ip;
	String command;
	int port;
	PublicKey key;
	
	Store(String command, String ip, String name, int port, PublicKey key) {
		this.command = command;
		this.ip = ip;
		this.name = name;
		this.port = port;
		this.key = key;
	}
}