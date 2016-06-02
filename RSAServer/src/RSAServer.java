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
            // 서버소켓을 생성하고 5000번 포트와 결합(bind) 시킨다.
            serverSocket = new ServerSocket(5001);
            System.out.println(getTime() + " 서버가 준비되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        } // try - catch
         
        while (true) {
            try {
                System.out.println(getTime() + " 연결요청을 기다립니다.");
                // 서버소켓은 클라이언트의 연결요청이 올 때까지 실행을 멈추고 계속 기다린다.
                // 클라이언트의 연결요청이 오면 클라이언트 소켓과 통신할 새로운 소켓을 생성한다.
                Socket socket = serverSocket.accept();
                System.out.println(getTime() + socket.getInetAddress() + " 로부터 연결요청이 들어왔습니다.");
                OutputStream out = socket.getOutputStream(); 
                //DataOutputStream dos = new DataOutputStream(out); // 기본형 단위로 처리하는 보조스트림
                ObjectOutputStream oos = new ObjectOutputStream(out);
                InputStream in = socket.getInputStream();
                //DataInputStream dis = new DataInputStream(in);  // 기본형 단위로 처리하는 보조스트림
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
                		System.out.println("에러 : 이름중복");
                	} else {
                		store.ip = socket.getInetAddress().toString().split("/")[1];
                		userdata.put(store.name, store);

                		oos.writeObject(new Store("P", null, null, 0, null));
                		System.out.println("접속 : "  + store.name);
                	}
                } else if (store.command.equals("S")){
                	if (userdata.containsKey(store.name)) {

                    	System.out.println("검색 : " + userdata.get(store.name));
                		Store storetemp = userdata.get(store.name);
                		storetemp.command = "P";
                		oos.writeObject(storetemp);
                	} else {
                		System.out.println("에러 : 찾는 닉네임 없음");
                		oos.writeObject(new Store("E", null, null, 0, null));
                	}
                }
                // 스트림과 소켓을 달아준다.
                ois.close();
                oos.close();
                socket.close();
                
                //System.out.println("접속종료 : " + userdata.get(s[1]));
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