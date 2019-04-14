import java.io.IOException;

public class mainTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("begin");
		AFAS run = new AFAS(10, 5, 2, 5, 0.2, 10);
		run.doAFAS(40);// 括号内为迭代次数
	}

}
