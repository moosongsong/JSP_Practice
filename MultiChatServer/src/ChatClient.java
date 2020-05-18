import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

class Receiver extends Thread {
	private BufferedReader in;
	
	public Receiver(BufferedReader in) {
		this.in = in;
	}
	
	public void run() {
		while (true) {
			try {
				String recvMessage = in.readLine();
				System.out.println(recvMessage);
				try {
					Thread.sleep(1);
				}
				catch (InterruptedException e) {
					break;
				}
			}
			catch (IOException e) {
				System.err.println("�������� ������ ����Ǿ����ϴ�.");
				break;
			}
		}
		System.out.println("�޽��� ���� �����带 �����Ͽ����ϴ�.");
	}
}

public class ChatClient {
	public static void main(String[] args) {
		if (!(args.length == 1 || args.length == 2)) {
			System.err.println("�߸��� ������Դϴ�.");
			System.err.println("����> java Sender [�����ּ�] ��Ʈ��ȣ");
			System.exit(1);
		}
		
		int portNumber = 0;
		int posPort = 0;
		InetAddress ia = InetAddress.getLoopbackAddress();
		
		if (args.length == 2) {
			try {
				ia = InetAddress.getByName(args[0]);
			}
			catch (UnknownHostException e) {
				System.err.println(args[0] + "ȣ��Ʈ�� ã�� �� �����ϴ�.");
				System.exit(2);
			}
			posPort++;
		}
		
		try {
			portNumber = Integer.parseInt(args[posPort]);
			if (portNumber < 1 || portNumber > 65535) {
				System.err.println("�߸��� ��Ʈ��ȣ�Դϴ�.");
				System.err.println("���������� ��Ʈ��ȣ�� [1 ~ 65535]�Դϴ�.");
				System.exit(4);
			}
		}
		catch (NumberFormatException e) {
			System.err.println("�߸��� ��Ʈ��ȣ�Դϴ�.");
			System.exit(3);
		}
		
		Socket socket = null;
		PrintWriter socketWriter = null;
		BufferedReader consoleReader = null;
		Thread receiveThread = null;
		
		try {
			socket = new Socket(ia, portNumber);
			System.out.println(ia.getHostName() + " ä�ü����� �����Ͽ����ϴ�.");
			consoleReader = new BufferedReader(new InputStreamReader(System.in), 1024);
			socketWriter = new PrintWriter(
							new BufferedWriter(
								new OutputStreamWriter(
									socket.getOutputStream()), 1024));
			receiveThread = new Receiver(
								new BufferedReader(
									new InputStreamReader(
										socket.getInputStream()), 1024));
			receiveThread.start();
			
			while (true) {
				String sendMessage = consoleReader.readLine();
				socketWriter.println(sendMessage);
				socketWriter.flush();
				if (sendMessage.trim().toLowerCase().equals("bye")) {
					break;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			receiveThread.interrupt();
			try { socket.close(); } catch (Exception e) {}
		}
		System.out.println("���α׷��� �����մϴ�.");
	}
}














