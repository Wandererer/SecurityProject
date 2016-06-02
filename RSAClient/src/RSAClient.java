import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Scanner;

import ICNJLibrary.ICNJLibrary;

public class RSAClient {
	public static void main(String[] args) {
		try {
			String serverIP = "127.0.0.1"; // 127.0.0.1 & localhost 본인
			System.out.println("서버에 연결중입니다. 서버 IP : " + serverIP);

			Scanner scan = new Scanner(System.in);

			Socket socket;
			DataInputStream dis;
			ObjectInputStream ois = null;
			DataOutputStream dos;
			ObjectOutputStream oos = null;
			String[] s = null;

			Chat cs = null;
			
			ICNJLibrary icnj = new ICNJLibrary();

			PublicKey mYpublicKey = icnj.Init();
			System.out.println(mYpublicKey);
			Store store = null;
			do {

				// 소켓을 생성하여 연결을 요청한다.
				socket = new Socket(serverIP, 5001);

				OutputStream out = socket.getOutputStream();
				//dos = new DataOutputStream(out); // 기본형 단위로 처리하는 보조스트림
				oos = new ObjectOutputStream(out);
				// 소켓의 입력스트림을 얻는다.
				InputStream in = socket.getInputStream();
				//dis = new DataInputStream(in); // 기본형 단위로 처리하는 보조스트림
				ois = new ObjectInputStream(in);

				System.out.println("암호");

				System.out.println("채팅 대기? Y/N");

				String command = scan.next();

				if (command.equals("Y")) {

					int port = 10000;

					do {
						cs = new Chat(++port);
					} while (cs.portcheck());

					System.out.println("이름을 입력해주세요.");
					oos.writeObject(new Store("O", null, scan.next(), port, mYpublicKey));
				} else if (command.equals("N")) {
					System.out.println("상대방의 이름을 입력해주세요.");
					oos.writeObject(new Store("S", null, scan.next(), 0, null));
					if (ois == null) {
						ois = new ObjectInputStream(in);
						System.out.println("테스트");
					}
				}
				store = ((Store)ois.readObject());
			} while (store.command.equals("E"));

			if (store.ip != null) {
				cs = new Chat(store.ip, store.port, mYpublicKey);
			} else {
				System.out.println("check");
			}
			// 메세지 전송 부분
			cs.seticnj(icnj);
			cs.startchat(store.key, scan);

			// 스트림과 소켓을 닫는다.
			ois.close();
			//dis.close();
			oos.close();
			//dos.close();
			socket.close();
		} catch (ConnectException ce) {
			ce.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} // try - catch
	} // main
} // TcpClientTest

class Chat {
	ServerSocket serverSocket;
	Socket socket;
	InputStream in;
	DataInputStream dis;
	Boolean check = true;
	DataOutputStream dos;
	PublicKey myKey;
	ICNJLibrary icnj;

	Chat(String ip, int port, PublicKey myKey) {
		try {
			socket = new Socket(ip, port);
			this.myKey = myKey;
			check = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	Chat(int port) {
		try {
			serverSocket = new ServerSocket(port);

			System.out.println(port);

			check = false;
			System.out.println("성공");
		} catch (IOException e) {
			check = true;
			System.out.println("재시도");
			// e.printStackTrace();
		}
	}
	
	void seticnj (ICNJLibrary icnj) {
		this.icnj = icnj;
	}

	Boolean portcheck() {
		return check;
	}

	void startchat(PublicKey key, Scanner scan) {
		try {
			if (check == false) {
				System.out.println("대기중");
				socket = serverSocket.accept();
				System.out.println("접속");
			}
			InputStream in = socket.getInputStream();
			dis = new DataInputStream(in); // 기본형 단위로 처리하는 보조스트림
			OutputStream out = socket.getOutputStream();
			dos = new DataOutputStream(out); // 기본형 단위로 처리하는 보조스트림
			if (key == null) {
				ObjectInputStream ois = new ObjectInputStream(in);
				try {
					key = (PublicKey) ois.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println("공개키 전송");
				ObjectOutputStream oos = new ObjectOutputStream(out);
				
				oos.writeObject(myKey);
			}

			WriteThread wt = new WriteThread(dos, key, scan, icnj);
			wt.start();
			ReadThread rt = new ReadThread(dis, icnj);
			rt.start();

			System.out.println("채팅시작");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	class WriteThread extends Thread {
		DataOutputStream dos;
		Scanner scan;
		PublicKey key;
		ICNJLibrary icnj;

		WriteThread(DataOutputStream dos, PublicKey key, Scanner scan, ICNJLibrary icnj) {
			this.dos = dos;
			this.scan = scan;
			this.key = key;
			this.icnj = icnj;
		}

		public void run() {
			while (true) {
				try {
					byte[] encryptMsg = icnj.Encrypt(scan.nextLine(), key);

					//System.out.println(new String(encryptMsg));

					dos.write(encryptMsg);
					//dos.writeUTF(scan.nextLine());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	class ReadThread extends Thread {
		DataInputStream dis;
		ICNJLibrary icnj;

		ReadThread(DataInputStream dis, ICNJLibrary icnj) {
			this.dis = dis;
			this.icnj = icnj;
		}

		public void run() {
			while (true) {
				try {
					byte[] encryptMsg = new byte[128];
					dis.read(encryptMsg);

					System.out.println(icnj.Decrypt(encryptMsg));

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
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