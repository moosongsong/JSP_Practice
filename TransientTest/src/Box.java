import java.io.Serializable;

public class Box implements Serializable {
	private static final long serialVersionUID = 3094061860273535868L;
	private int width;
	private int height;
	
	public Box(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void setWidth(int width) { this.width = width; }
	public void setHeight(int height) { this.height = height; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public String toString() {
		return ("���̰� " + width + "�̸� ���̰� " + height + " ������ " + (width * height) + "�� �簢��");
	}
}
