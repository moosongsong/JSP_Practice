import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Sender {

	public static void main(String[] args) {
		
		if (!(args.length == 1 || args.length == 2)) {
			System.err.println("잘못된 명령행입니다.");
			System.err.println("사용법> java Sender [서버주소] 포트번호");
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
				System.err.println(args[0] + "호스트를 찾을 수 없습니다.");
				System.exit(2);
			}
			posPort++;
		}
		
		try {
			portNumber = Integer.parseInt(args[posPort]);
			if (portNumber < 1 || portNumber > 65535) {
				System.err.println("잘못된 포트번호입니다.");
				System.err.println("지정가능한 포트번호는 [1 ~ 65535]입니다.");
				System.exit(4);
			}
		}
		catch (NumberFormatException e) {
			System.err.println("잘못된 포트번호입니다.");
			System.exit(3);
		}
		
		Socket socket = null;
		OutputStream out = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			socket = new Socket(ia, portNumber);
			System.out.println(ia.getHostName() + "서버에 접속하였습니다.");
			out = socket.getOutputStream();
			
			while (true) {
				StringBuffer sb = new StringBuffer(128);
				sb.append(Character.toChars(Common.STX));
				
				System.out.print("제품코드 : ");	// 10자리
				String code = in.readLine();
				code = code.substring(0, (code.length() < Common.CODE_LEN) ? code.length() : Common.CODE_LEN).toUpperCase();
				code = String.format("%" + Common.CODE_LEN + "s", code);
				sb.append(code);
				
				while (true) {
					try {
						System.out.print("제품수량 : ");	// 4자리
						String unitCount = in.readLine();
						int count = Integer.parseInt(unitCount);
						if (count < 1 || count > 9999) {
							throw new Exception("수량을 잘못 입력하였습니다. 수량은 1 ~ 9999까지 입력할 수 있습니다.");
						}
						sb.append(String.format("%" + Common.COUNT_LEN + "d", count));
						break;
					}
					catch (NumberFormatException e) {
						System.err.println("잘못 입력하셨습니다. 다시 입력해 주세요.");
					}
					catch (Exception e) {
						System.err.println(e.getMessage());
					}
				}
				
				while (true) {
					try {
						System.out.print("제품가격 : ");	// 10자리
						String unitPrice = in.readLine();
						int price = Integer.parseInt(unitPrice);
						if (price < 0 || price > Integer.MAX_VALUE) {
							throw new Exception("가격을 잘못 입력하였습니다. 가격은 0 ~ " + Integer.MAX_VALUE + "까지 입력이 가능합니다.");
						}
						sb.append(String.format("%" + Common.PRICE_LEN + "d", price));
						break;
					}
					catch (NumberFormatException e) {
						System.err.println("잘못 입력하셨습니다. 다시 입력해 주세요.");
					}
					catch (Exception e) {
						System.err.println(e.getMessage());
					}
				}
				
				sb.append(Character.toChars(Common.ETX));
				
				out.write(sb.toString().getBytes());
				
				System.out.print("계속 전송하시겠습니까 ([y]/n)?");
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
















