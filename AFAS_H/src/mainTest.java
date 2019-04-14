import java.io.IOException;

public class mainTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("begin");
		AFAS run = new AFAS(80, 50, 2, 0.18, 0.8, 2.5);
		run.doAFAS(100);// 括号内为迭代次数
	}

}
