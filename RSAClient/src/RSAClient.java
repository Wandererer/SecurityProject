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
			String serverIP = "127.0.0.1"; // 127.0.0.1 & localhost ����
			System.out.println("������ �������Դϴ�. ���� IP : " + serverIP);

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

				// ������ �����Ͽ� ������ ��û�Ѵ�.
				socket = new Socket(serverIP, 5001);

				OutputStream out = socket.getOutputStream();
				//dos = new DataOutputStream(out); // �⺻�� ������ ó���ϴ� ������Ʈ��
				oos = new ObjectOutputStream(out);
				// ������ �Է½�Ʈ���� ��´�.
				InputStream in = socket.getInputStream();
				//dis = new DataInputStream(in); // �⺻�� ������ ó���ϴ� ������Ʈ��
				ois = new ObjectInputStream(in);

				System.out.println("��ȣ");

				System.out.println("ä�� ���? Y/N");

				String command = scan.next();

				if (command.equals("Y")) {

					int port = 10000;

					do {
						cs = new Chat(++port);
					} while (cs.portcheck());

					System.out.println("�̸��� �Է����ּ���.");
					oos.writeObject(new Store("O", null, scan.next(), port, mYpublicKey));
				} else if (command.equals("N")) {
					System.out.println("������ �̸��� �Է����ּ���.");
					oos.writeObject(new Store("S", null, scan.next(), 0, null));
					if (ois == null) {
						ois = new ObjectInputStream(in);
						System.out.println("�׽�Ʈ");
					}
				}
				store = ((Store)ois.readObject());
			} while (store.command.equals("E"));

			if (store.ip != null) {
				cs = new Chat(store.ip, store.port, mYpublicKey);
			} else {
				System.out.println("check");
			}
			// �޼��� ���� �κ�
			cs.seticnj(icnj);
			cs.startchat(store.key, scan);

			// ��Ʈ���� ������ �ݴ´�.
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
			System.out.println("����");
		} catch (IOException e) {
			check = true;
			System.out.println("��õ�");
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
				System.out.println("�����");
				socket = serverSocket.accept();
				System.out.println("����");
			}
			InputStream in = socket.getInputStream();
			dis = new DataInputStream(in); // �⺻�� ������ ó���ϴ� ������Ʈ��
			OutputStream out = socket.getOutputStream();
			dos = new DataOutputStream(out); // �⺻�� ������ ó���ϴ� ������Ʈ��
			if (key == null) {
				ObjectInputStream ois = new ObjectInputStream(in);
				try {
					key = (PublicKey) ois.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println("����Ű ����");
				ObjectOutputStream oos = new ObjectOutputStream(out);
				
				oos.writeObject(myKey);
			}

			WriteThread wt = new WriteThread(dos, key, scan, icnj);
			wt.start();
			ReadThread rt = new ReadThread(dis, icnj);
			rt.start();

			System.out.println("ä�ý���");

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