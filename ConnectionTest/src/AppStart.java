import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

import dao.CategoryDAO;
import dao.ComicDAO;
import vo.Category;
import vo.Comic;

public class AppStart {
	public static final String URL = "jdbc:mariadb://localhost:3306/example?user=example&password=1234";
	
	public static void main(String[] args) {
		Connection con = null;
		ArrayList<Category> list = null;
		
		try {
			con = DriverManager.getConnection(URL);
			con.setAutoCommit(false);
			
			Category ca = new Category("COMB", "����");
			Comic co = new Comic(28, "�׽�Ʈ�Է�", 20000, "LECT", "HUNI", ca);
			if (CategoryDAO.insert(con, ca)) {
				if (ComicDAO.insert(con, co)) {
					con.commit();
					System.out.println("���������� ��ϵǾ����ϴ�.");
				}
				else {
					con.rollback();
					System.out.println("�ڹ� ��Ͽ� �����Ͽ����ϴ�.");					
				}
			}
			else {
				con.rollback();
				System.out.println("ī�װ� ��Ͽ� �����Ͽ����ϴ�.");
			}
			con.setAutoCommit(true);
			
			list = CategoryDAO.getCategory(con);
			for (int i=0 ; i<list.size(); i++) {
				Category category = list.get(i);
				category.setComics(ComicDAO.getComics(con, category));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try { con.close(); } catch (Exception e) {}
		}
	}
}
