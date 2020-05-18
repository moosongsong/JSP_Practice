import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MultiChatServer {
	private Map<String, PrintWriter> clients;
	private ServerSocket serverSocket;
	
	public MultiChatServer(int portNumber) throws Exception {
		this.serverSocket = new ServerSocket(portNumber);
		this.clients = new HashMap<>();
	}
	
	public void addClient(Socket clientSocket) throws IOException {		
		String host = clientSocket.getInetAddress().getHostName();
		PrintWriter writer = new PrintWriter(
								new BufferedWriter(
									new OutputStreamWriter(
										clientSocket.getOutputStream()), 1024));
		synchronized (clients) {
			this.clients.put(host, writer);
		}
	}
	
	public void forward(String host, String message) {
		synchronized (clients) {
			Set<Map.Entry<String, PrintWriter>> entrySet = clients.entrySet();
			for (Map.Entry<String, PrintWriter> entry : entrySet) {
				String sendMessage = host + "> " + message;
				entry.getValue().println(sendMessage);
				entry.getValue().flush();
			}
		}
	}
	
	public void remove(String host) {
		synchronized (clients) {
			this.clients.remove(host);
		}
	}
	
	class ClientThread extends Thread {
		private BufferedReader clientReader;
		private String host;
		
		public ClientThread(Socket clientSocket) throws Exception {
			this.host = clientSocket.getInetAddress().getHostName();
			this.clientReader = new BufferedReader(
									new InputStreamReader(
										clientSocket.getInputStream()), 1024);
			MultiChatServer.this.forward("ä�ü���> ", this.host + "���� ä�ù濡 �����Ͽ����ϴ�.");
			MultiChatServer.this.addClient(clientSocket);			
		}
		
		public void run() {
			while (true) {
				try {
					String recvMessage = this.clientReader.readLine();
					if (recvMessage == null) {
						break;
					}
					MultiChatServer.this.forward(this.host, recvMessage);
					if (recvMessage.trim().toLowerCase().equals("bye")) {
						break;
					}
				}
				catch (IOException e) {
					System.err.println("Ŭ���̾�Ʈ���� ������ ������������ ����Ǿ����ϴ�.");
				}
			}
			System.out.println("ä�ü���> " + this.host + "�� ä�ù濡�� �����Ͽ����ϴ�.");
			MultiChatServer.this.remove(this.host);
		}
	}
	
	public void serverStart() {
		try {
			while (true) {
				Socket clientSocket = null;
				try {
					clientSocket = serverSocket.accept();
					Thread ct = new ClientThread(clientSocket);
					ct.start();
				}
				catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("ä�ü����� �����Ͽ����ϴ�.");
	}
	
	public static void main(String[] args) {
		int portNumber = 0;
		
		if (args.length != 1) {
			System.err.println("�߸��� ��� ���Դϴ�.");
			System.err.println("����> java Receiver ��Ʈ��ȣ");
			System.exit(1);
		}
		
		try {
			portNumber = Integer.parseInt(args[0]);
			if ((portNumber < 1) || (portNumber > 65535)) {
				System.err.println("��Ʈ��ȣ�� ������ �߸��Ǿ����ϴ�.");
				System.err.println("���������� ��Ʈ��ȣ ���� [1 ~ 65535]�Դϴ�.");
				System.exit(2);
			}
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(3);
		}
		
		MultiChatServer app = null;
		try {
			app = new MultiChatServer(portNumber);
			System.out.println("ä�ü����� " + portNumber + "��Ʈ�� �����Ǿ����ϴ�.");
			app.serverStart();
		}
		catch (Exception e) {
			System.err.println("���� ������ �����Ͽ����ϴ�.");
		}
	}
	
}
