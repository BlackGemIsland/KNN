import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
/**
 * <b>Description:</b>欧几里得举例语义相似度</br>
 * 
 * @author: lcm
 * @Date: 2018-6-2
 */
public class EuclideanSame {
	// 阈值
	public static double THRESHOLE = 0.2;

	/**
	 * 返回百分比
	 * 
	 * @author: lcm
	 * @Date: 2018年6月2日
	 * @param T1
	 * @param T2
	 * @return
	 */
	public static double getSimilarity(Vector<String> T1, Vector<String> T2) throws Exception {
		int size = 0, size2 = 0;
		if (T1 != null && (size = T1.size()) > 0 && T2 != null && (size2 = T2.size()) > 0) {

			Map<String, double[]> T = new HashMap<String, double[]>();

			// T1和T2的并集T
			String index = null;
			for (int i = 0; i < size; i++) {
				index = T1.get(i);
				if (index != null) {
					double[] c = T.get(index);
					c = new double[2];
					c[0] = 1; // T1的语义分数Ci
					c[1] = THRESHOLE;// T2的语义分数Ci
					T.put(index, c);
				}
			}

			for (int i = 0; i < size2; i++) {
				index = T2.get(i);
				if (index != null) {
					double[] c = T.get(index);
					if (c != null && c.length == 2) {
						c[1] = 1; // T2中也存在，T2的语义分数=1
					} else {
						c = new double[2];
						c[0] = THRESHOLE; // T1的语义分数Ci
						c[1] = 1; // T2的语义分数Ci
						T.put(index, c);
					}
				}
			}

			// 开始计算，百分比
			Iterator<String> it = T.keySet().iterator();
			double Ssum = 0; // Ssum
			while (it.hasNext()) {
				double[] c = T.get(it.next());
				Ssum += (c[0] - c[1]) * (c[0] - c[1]);

			}
			// 欧几里得距离
			return Math.sqrt(Ssum);
		} else {
			throw new Exception("传参错误！");
		}
	}

	public static Vector<String> participle(String str) {

		Vector<String> str1 = new Vector<String>();// 对输入进行分词

		try {

			StringReader reader = new StringReader(str);
			IKSegmenter ik = new IKSegmenter(reader, true);// 当为true时，分词器进行最大词长切分
			Lexeme lexeme = null;

			while ((lexeme = ik.next()) != null) {
				str1.add(lexeme.getLexemeText());
			}

			if (str1.size() == 0) {
				return null;
			}

			// 分词后
			System.out.println("句子分词后：" + str1);

		} catch (IOException e1) {
			System.out.println();
		}
		return str1;
	}

	public static void main(String[] args) {
		long st = System.currentTimeMillis();
		// 分词
		Vector<String> strs1 = participle("我在看太阳，你在看月亮");
		Vector<String> strs2 = participle("我在看溪水，你在看大海");

		// 欧几里得距离
		double same = 0;
		try {
			same = EuclideanSame.getSimilarity(strs1, strs2);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		System.out.println("语义距离：" + same);
		long et = System.currentTimeMillis();
		System.out.println("程序运行时长为：" + (et - st));
	}

}
