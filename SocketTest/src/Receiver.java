import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.NumberFormat;

public class Receiver {
	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		InputStream in = null;
				
		if (args.length != 1) {
			System.err.println("잘못된 명령 행입니다.");
			System.err.println("사용법> java Receiver 포트번호");
			System.exit(1);
		}
		
		int portNumber = 0;		
		try {
			portNumber = Integer.parseInt(args[0]);
			if ((portNumber < 1) || (portNumber > 65535)) {
				System.err.println("포트번호의 범위가 잘못되었습니다.");
				System.err.println("지정가능한 포트번호 범위 [1 ~ 65535]입니다.");
				System.exit(3);
			}
		}
		catch (NumberFormatException e) {
			System.err.println("잘못된 포트번호입니다.");
			System.exit(2);
		}
		
		try {
			serverSocket = new ServerSocket(portNumber);
			System.out.println("서버가 " + portNumber + "포트로 대기중입니다.");
			
			while (true) {
				Socket clientSocket = null;
				
				try {
					clientSocket = serverSocket.accept();
					System.out.println(clientSocket.getInetAddress().getHostName() + " 호스트가 접속하였습니다.");
					in = clientSocket.getInputStream();
					byte[] buffer = new byte[128];
					
					// 만약 STX 또는 ETX가 포함되어 있지 않다면 "수신데이터에 문제가 있습니다." 재수신하도록 수정
					// 만약 Sender로부터 EOT가 수신되면 Sender와의 수신을 종료하고 새로운 접속을 대기한다.
					while (true) {
						in.read(buffer);					
						StringBuffer strBuffer = new StringBuffer(new String(buffer));
	
						if (Common.isEOT(strBuffer)) {
							System.out.println(clientSocket.getInetAddress().getHostName() + " 호스트와의 연결이 종료되었습니다.");
							break;
						}
						
						if (!Common.isValidatePacket(strBuffer)) {
							System.err.println("수신된 전문데이터에 문제가 발생하였습니다.");
							break;
						}
						
						strBuffer.delete(0, 1);
						
						System.out.println("제품코드 : " + strBuffer.substring(0, Common.CODE_LEN).trim());
						strBuffer.delete(0, Common.CODE_LEN);
						
						System.out.println("제품수량 : " + Common.currency(strBuffer.substring(0, Common.COUNT_LEN)));
						strBuffer.delete(0, Common.COUNT_LEN);
						
						System.out.println("제품가격 : " + Common.currency(strBuffer.substring(0, Common.PRICE_LEN)));
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












