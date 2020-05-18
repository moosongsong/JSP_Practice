import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.NumberFormat;

public class Receiver {
	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		InputStream in = null;
				
		if (args.length != 1) {
			System.err.println("�߸��� ��� ���Դϴ�.");
			System.err.println("����> java Receiver ��Ʈ��ȣ");
			System.exit(1);
		}
		
		int portNumber = 0;		
		try {
			portNumber = Integer.parseInt(args[0]);
			if ((portNumber < 1) || (portNumber > 65535)) {
				System.err.println("��Ʈ��ȣ�� ������ �߸��Ǿ����ϴ�.");
				System.err.println("���������� ��Ʈ��ȣ ���� [1 ~ 65535]�Դϴ�.");
				System.exit(3);
			}
		}
		catch (NumberFormatException e) {
			System.err.println("�߸��� ��Ʈ��ȣ�Դϴ�.");
			System.exit(2);
		}
		
		try {
			serverSocket = new ServerSocket(portNumber);
			System.out.println("������ " + portNumber + "��Ʈ�� ������Դϴ�.");
			
			while (true) {
				Socket clientSocket = null;
				
				try {
					clientSocket = serverSocket.accept();
					System.out.println(clientSocket.getInetAddress().getHostName() + " ȣ��Ʈ�� �����Ͽ����ϴ�.");
					in = clientSocket.getInputStream();
					byte[] buffer = new byte[128];
					
					// ���� STX �Ǵ� ETX�� ���ԵǾ� ���� �ʴٸ� "���ŵ����Ϳ� ������ �ֽ��ϴ�." ������ϵ��� ����
					// ���� Sender�κ��� EOT�� ���ŵǸ� Sender���� ������ �����ϰ� ���ο� ������ ����Ѵ�.
					while (true) {
						in.read(buffer);					
						StringBuffer strBuffer = new StringBuffer(new String(buffer));
	
						if (Common.isEOT(strBuffer)) {
							System.out.println(clientSocket.getInetAddress().getHostName() + " ȣ��Ʈ���� ������ ����Ǿ����ϴ�.");
							break;
						}
						
						if (!Common.isValidatePacket(strBuffer)) {
							System.err.println("���ŵ� ���������Ϳ� ������ �߻��Ͽ����ϴ�.");
							break;
						}
						
						strBuffer.delete(0, 1);
						
						System.out.println("��ǰ�ڵ� : " + strBuffer.substring(0, Common.CODE_LEN).trim());
						strBuffer.delete(0, Common.CODE_LEN);
						
						System.out.println("��ǰ���� : " + Common.currency(strBuffer.substring(0, Common.COUNT_LEN)));
						strBuffer.delete(0, Common.COUNT_LEN);
						
						System.out.println("��ǰ���� : " + Common.currency(strBuffer.substring(0, Common.PRICE_LEN)));
						strBuffer.delete(0, Common.PRICE_LEN);						
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try { serverSocket.close(); } catch (Exception e) {}
		}
	}
}












