import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AppStart {
	public static void main(String[] args) {
		Connection con = null;
		String url = "jdbc:mariadb://127.0.0.1:3306/kihee";
		String user = "kihee";
		String pass = "iloveyou7";
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			System.out.println("JDBC ����̹��� DriverManager�� ����Ͽ����ϴ�.");
			con = DriverManager.getConnection(url, user, pass);
			System.out.println("�����ͺ��̽� ������ ���������� �����Ͽ����ϴ�.");
		}
		catch (ClassNotFoundException e) {
			System.err.println("JDBC ����̹� �˻��� �����Ͽ����ϴ�.");
		}		
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try { con.close(); } catch (Exception e) {}
		}
	}
}
