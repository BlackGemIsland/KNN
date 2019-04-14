import java.io.IOException;
import java.util.Date;

public class AFAS {
	// ��Ⱥ��Ŀ
	private int fishNum;
	// ���Դ���
	private int tryTime;
	// ά��
	private int dim;
	// �˹����ƶ�����
	private double step;
	// ӵ��������
	private double delta;
	// ��Ұ��Χ
	private double visual;
	// �˹���Ⱥ����Χ������㣬����ʱ����һ����
	Fish[] fish;
	Fish bestfish;
	Fish[] nextfish;
	// ��������
	int index;
	double[][] vector;
	private int[] choosed;
	// ��Χ����Ⱥ��Ŀ fishCount
	public int scopelength;

	public AFAS() {

	}

	public AFAS(int fishNum, int tryTime, int dim, double step, double delta, double visual) throws IOException {
		super();
		this.fishNum = fishNum;
		this.tryTime = tryTime;
		this.dim = dim;
		this.step = step;
		this.delta = delta;
		this.visual = visual;
		fish = new Fish[fishNum];
		nextfish = new Fish[3];
		vector = new double[fishNum][dim];
		choosed = new int[fishNum];
		index = 0;
		init();
	}

	public void doAFAS(int num) throws IOException {
		long startTime = new Date().getTime();
		double a = 0.0;
		int count = 1; // ������Ҵ���
		int len = 0;
		while (count <= num) {

			for (int i = 0; i < fishNum; i++) {
				prey(i);
				swarm(i);
				follow(i);
				bulletin(i);
				System.out.println("��" + count + "���" + i + "�������");
			}
			System.out.println(count + "��ǰ����ֵ��" + bestfish.fit);
			for (int i = 0; i < dim; i++) {
				System.out.print("λ��" + (i + 1) + ":  " + bestfish.x[i]);
			}
			System.out.println();
			count++;
			System.out.println("step:" + step + "    visaul:" + visual);

		}
		System.out.println("����ֵ��" + bestfish.fit);
		for (int i = 0; i < dim; i++) {
			System.out.print("λ��" + (i + 1) + ":  " + bestfish.x[i]);
		}
		long endTime = new Date().getTime();
		System.out.println("���������м�ʱ�� " + (endTime - startTime) + " ���롣");
	}

	// ������
	private void bulletin(int i) throws IOException {
		Fish maxfish = new Fish(dim, visual);
		maxfish = nextfish[0];
		for (int j = 0; j < 3; j++) {
			if (nextfish[j].fit > maxfish.fit && nextfish[j].x[0] != 0 && nextfish[j].x[1] != 0) {
				maxfish = nextfish[j];
			}
		}
		if (maxfish.fit < fish[i].fit) {
			return;
		}
		fish[i] = maxfish;
		if (maxfish.fit > bestfish.fit)
			bestfish = maxfish;
	}

	// β����Ϊ
	private void follow(int i) throws IOException {
		nextfish[2] = new Fish(dim, visual);
		Fish minfish = new Fish(dim, visual); // ����λ��
		minfish = fish[i];
		Fish[] scope = getScopefish(i);
		int key = i;
		if (scope != null) {
			for (int j = 0; j < scope.length; j++) {
				if (scope[j].fit < minfish.fit) {
					minfish = scope[j];
					key = j;
				}
			}
			if (minfish.fit >= fish[i].fit)
				prey(i);
			else {
				Fish[] newScope = getScopefish(key);
				if (newScope != null) {
					if (newScope.length * minfish.fit < delta * fish[i].fit) {
						double dis = fish[i].distance(minfish);
						for (int k = 0; k < dim; k++) {
							nextfish[2].x[k] = (fish[i].x[k]
									+ (minfish.x[k] - fish[i].x[k]) * step * Math.random() / dis);
						}
						nextfish[2].fit = nextfish[2].newfunction(nextfish[2].x);
					} else
						prey(i);
				} else
					prey(i);
			}
		} else
			prey(i);

	}

	// Ⱥ����Ϊ
	private void swarm(int i) throws IOException { // swam start
		nextfish[1] = new Fish(dim, visual);
		double[] center = new double[dim]; // ����λ��
		for (int j = 0; j < dim; j++)
			center[j] = 0;
		Fish[] scope = getScopefish(i);
		if (scope != null) {
			for (int j = 0; j < scope.length; j++) // �����˹��������λ��
			{
				for (i = 0; i < dim; ++i)
					center[i] += scope[j].x[i];
			}
			for (i = 0; i < dim; i++)
				center[i] /= scope.length; // �˹��������λ��
			// ��������
			double dis = 0.0;
			Fish centerfish = new Fish(dim, visual);
			centerfish.x = center;
			centerfish.fit = centerfish.newfunction(centerfish.x);
			dis = fish[i].distance(centerfish);
			if (centerfish.fit > fish[i].fit && scope.length * centerfish.fit < delta * fish[i].fit) {
				for (int j = 0; j < dim; j++) {
					nextfish[1].x[j] = (int) (fish[i].x[j]
							+ (centerfish.x[j] - fish[i].x[j]) * step * Math.random() / dis);

				}
				nextfish[1].fit = nextfish[1].newfunction(nextfish[1].x);
			} else
				prey(i);
		} else
			prey(i);
	} // swam end

	// ��ʳ��Ϊ
	private void prey(int i) throws IOException { // prey start

		Fish newfish = new Fish(dim, visual);
		newfish.fit = 0;
		nextfish[0] = new Fish(dim, visual);
		for (int k = 0; k < tryTime; k++) // ����try_number�γ���
		{
			for (int j = 0; j < dim; j++) {
				newfish.x[j] = ((Math.random() / 2) * visual);

			}
			newfish.fit = newfish.newfunction(newfish.x);

			if (newfish.fit > fish[i].fit) {
				double dis = fish[i].distance(newfish);
				for (int j = 0; j < dim; j++) {

					nextfish[0].x[j] = (fish[i].x[j] + (newfish.x[j] - fish[i].x[j]) * step * Math.random() / dis);

				}
				nextfish[0].fit = nextfish[0].newfunction(nextfish[0].x);
			} else {

				for (int j = 0; j < dim; j++) {
					nextfish[0].x[j] = (int) (fish[i].x[j] + visual * (Math.random() / 2));

					nextfish[0].fit = nextfish[0].newfunction(nextfish[0].x);

				}

			}
		}
	}// prey end

	// ��÷�Χ����
	private Fish[] getScopefish(int i) {
		int num = 0;
		for (int j = 0; j < fishNum; j++) {
			choosed[j] = -1;
			if (fish[i].distance(fish[j]) < visual) {
				choosed[j] = i;
				num++;
			}
		}
		if (num != 0) {
			Fish[] scope = new Fish[num];
			int k = 0;
			for (int j = 0; j < fishNum; j++) {
				if (choosed[j] != -1)
					scope[k++] = fish[choosed[j]];
			}
			return scope;
		}
		return null;
	}

	private void init() throws IOException {
		for (int i = 0; i < fishNum; i++) {
			fish[i] = new Fish(dim, visual);
			fish[i].fit = fish[i].newfunction(fish[i].x);
		}
		bestfish = new Fish(dim, visual);
		bestfish.fit = -999999;
	}
}