import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static void main(String[] args) {
		ServerSocket sSock = null;
		BufferedReader in = null;
		
		try {
			sSock = new ServerSocket(9000);
			System.out.println("������ 9000�� ��Ʈ�� ������Դϴ�.");

			Socket cSock = sSock.accept();
			InetAddress cInet = cSock.getInetAddress();
			System.out.println(cInet.getHostName() + "�� �����Ͽ����ϴ�.");				
			in = new BufferedReader(new InputStreamReader(cSock.getInputStream()));

			while (true) {
				String str = in.readLine();
				System.out.println(str);
				if (str.toUpperCase().equals("BYE")) {
					break;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try { sSock.close(); } catch (Exception e) {}
		}
	}
}
