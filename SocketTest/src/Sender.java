import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Sender {

	public static void main(String[] args) {
		
		if (!(args.length == 1 || args.length == 2)) {
			System.err.println("�߸��� �������Դϴ�.");
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
		OutputStream out = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			socket = new Socket(ia, portNumber);
			System.out.println(ia.getHostName() + "������ �����Ͽ����ϴ�.");
			out = socket.getOutputStream();
			
			while (true) {
				StringBuffer sb = new StringBuffer(128);
				sb.append(Character.toChars(Common.STX));
				
				System.out.print("��ǰ�ڵ� : ");	// 10�ڸ�
				String code = in.readLine();
				code = code.substring(0, (code.length() < Common.CODE_LEN) ? code.length() : Common.CODE_LEN).toUpperCase();
				code = String.format("%" + Common.CODE_LEN + "s", code);
				sb.append(code);
				
				while (true) {
					try {
						System.out.print("��ǰ���� : ");	// 4�ڸ�
						String unitCount = in.readLine();
						int count = Integer.parseInt(unitCount);
						if (count < 1 || count > 9999) {
							throw new Exception("������ �߸� �Է��Ͽ����ϴ�. ������ 1 ~ 9999���� �Է��� �� �ֽ��ϴ�.");
						}
						sb.append(String.format("%" + Common.COUNT_LEN + "d", count));
						break;
					}
					catch (NumberFormatException e) {
						System.err.println("�߸� �Է��ϼ̽��ϴ�. �ٽ� �Է��� �ּ���.");
					}
					catch (Exception e) {
						System.err.println(e.getMessage());
					}
				}
				
				while (true) {
					try {
						System.out.print("��ǰ���� : ");	// 10�ڸ�
						String unitPrice = in.readLine();
						int price = Integer.parseInt(unitPrice);
						if (price < 0 || price > Integer.MAX_VALUE) {
							throw new Exception("������ �߸� �Է��Ͽ����ϴ�. ������ 0 ~ " + Integer.MAX_VALUE + "���� �Է��� �����մϴ�.");
						}
						sb.append(String.format("%" + Common.PRICE_LEN + "d", price));
						break;
					}
					catch (NumberFormatException e) {
						System.err.println("�߸� �Է��ϼ̽��ϴ�. �ٽ� �Է��� �ּ���.");
					}
					catch (Exception e) {
						System.err.println(e.getMessage());
					}
				}
				
				sb.append(Character.toChars(Common.ETX));
				
				out.write(sb.toString().getBytes());
				
				System.out.print("��� �����Ͻðڽ��ϱ� ([y]/n)?");
				String str = in.readLine();
				if (str.trim().toLowerCase().equals("n")) {
					String eot = new StringBuffer().append(Common.EOT).toString();
					out.write(eot.getBytes());
					break;					
				}
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try { socket.close(); } catch (Exception e) {}
		}
	}
}















