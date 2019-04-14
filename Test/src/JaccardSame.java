import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

/**
 * <b>Description:</b>杰卡德距离语义相似度</br>
 * @author: lcm
 * @Date: 2018-6-2
 */
public class JaccardSame {
	/**
	 * 求交集
	 * 
	 * @author: lcm
	 * @Date: 2018年6月2日
	 * @param T1
	 * @param T2
	 * @return
	 */
	public static Set<String> getIntersection(Vector<String> T1, Vector<String> T2){
		//交集
		Set<String> ia1=new HashSet<String>();
		Set<String> ia2=new HashSet<String>();
		for(int i=0;i<T1.size();i++) {
			ia1.add(T1.get(i));
		}
		for(int i=0;i<T2.size();i++) {
			ia2.add(T2.get(i));
		}
		ia1.retainAll(ia2);
		Set<String> intersection=ia1;
		return intersection;
	}
	/**
	 * 求并集
	 * 
	 * @author: lcm
	 * @Date: 2018年6月2日
	 * @param T1
	 * @param T2
	 * @return
	 */
	public static Set<String> getUnion(Vector<String> T1, Vector<String> T2){
		//并集
		Set<String> ua1=new HashSet<String>();
		Set<String> ua2=new HashSet<String>();
		for(int i=0;i<T1.size();i++) {
			ua1.add(T1.get(i));
		}
		for(int i=0;i<T2.size();i++) {
			ua2.add(T2.get(i));
		}
		ua1.addAll(ua2);
		Set<String> Union=ua1;
		return Union;
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
		Vector<String> strs2 = participle("地震时，会产生很多灰尘和微小颗粒物漂浮在空气中，所形成的冲击波会把这些灰尘“轰”到云层中，于是这些灰尘变成了凝结核，水汽附着在这些凝结核上，再不断碰撞并结合，等增长到空气无法托举的时候，就会变成雨滴落下。");
		
		Set<String> intersection=getIntersection(strs1, strs2);
		Set<String> union=getUnion(strs1, strs2);
		System.out.println("求交集得："+intersection);
		System.out.println("求并集得："+union);
		
		//杰卡德相似度
		double same=(double)intersection.size()/(double)union.size();
		System.out.println("杰卡德相似度为：" + same);
		long et = System.currentTimeMillis();
		System.out.println("程序运行时长为：" + (et - st));
	}
}
