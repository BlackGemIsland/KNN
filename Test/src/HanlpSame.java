
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

/**
 * <b>Description:</b>汉明距离语义相似度</br>
 * @author: lcm
 * @Date: 2018-6-2
 */
public class HanlpSame {
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
	public static Map<String, Double> getSimilarity(Vector<String> T1, Vector<String> T2) throws Exception {
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
			Map<Integer, Double> v1 = new HashMap<Integer, Double>();// 句子1的语义向量v1
			Map<Integer, Double> v2 = new HashMap<Integer, Double>();// 句子2的语义向量v2
			int mkey = 1;//
			while (it.hasNext()) {
				double[] c = T.get(it.next());
				v1.put(mkey, c[0]);
				v2.put(mkey, c[1]);
				mkey = mkey + 1;
			}
			double dis = 0;
			for (int i = 1; i < mkey; i++) {
				if (!v1.get(i).equals(v2.get(i))) {
					dis = dis + 1;
				}
			}
			Map<String, Double> resule = new HashMap<String, Double>();
			resule.put("all", (double) v1.size());
			resule.put("dis", dis);
			// 百分比
			return resule;
		} else {
			throw new Exception("传参错误！");
		}
	}

	/**
	 * 分词
	 * 
	 * @author: lcm
	 * @Date: 2018年6月2日
	 * @param str
	 * @return
	 */
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
		Vector<String> strs1 = participle("地震时，地下储藏的巨大能量瞬间释放出来，地面温度会升高，这有助于地表的水分向空气中蒸发，空气中水汽含量上升，更容易形成降雨。");
		Vector<String> strs2 = participle(
				"地震时，会产生很多灰尘和微小颗粒物漂浮在空气中，所形成的冲击波会把这些灰尘“轰”到云层中，于是这些灰尘变成了凝结核，水汽附着在这些凝结核上，再不断碰撞并结合，等增长到空气无法托举的时候，就会变成雨滴落下。");

		// 汉明距离
		double all = 0;
		double dis = 0;
		double same = 0;
		try {
			all = HanlpSame.getSimilarity(strs1, strs2).get("all");
			dis = HanlpSame.getSimilarity(strs1, strs2).get("dis");
			same = 1 - dis / all;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("语义向量元素个数="+all);
		System.out.println("汉明距离="+dis);
		System.out.println("语义相似度为：" + same);
		long et = System.currentTimeMillis();
		System.out.println("程序运行时长为：" + (et - st));
	}

}
