import java.util.ArrayList;

public class Dutie {
	private String code;
	private String name;
	ArrayList<Employee> employees;
	
	public Dutie() {}
	
	public Dutie(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<Employee> getEmployees() {
		return employees;
	}
	
	public void setEmployee(ArrayList<Employee> employees) {
		this.employees = employees;
	}
}
