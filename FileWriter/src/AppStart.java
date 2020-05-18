import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class AppStart {
	public static void main(String[] args) {
		List<Person> students = new ArrayList<>();
		students.add(new Person("ȫ�浿", 20));
		students.add(new Person("��û��", 16));
		students.add(new Person("�����", 30));
		
		FileWriter fw = null;
		PrintWriter out = null;
		BufferedWriter bw = null;
		
		try {
			fw = new FileWriter("C:/temp/data.txt");
			bw = new BufferedWriter(fw);
			out = new PrintWriter(bw);
			for (Person p : students) {
				out.printf("%s:%d\n", p.getName(), p.getAge());
			}
			out.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			out.close();
		}
	}
}
