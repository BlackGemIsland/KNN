import java.io.IOException;

public class Fish {
	public int dim; // 每条鱼的维度
	public double[] x; // 每条鱼的具体多维坐标
	public double fit; // 鱼的适应值，浓度
	public double visaul; // 每条鱼的视野
	public final double[] H = new double[256];
	public final double[] W = new double[256];

	public Fish(int dim, double visaul) throws IOException {
		super();
		this.dim = dim;
		this.visaul = visaul;
		x = new double[dim];
		for (int i = 0; i < dim; i++)
			x[i] = Math.floor(Math.random());
		fit = 0;
		// init();
	}
	/* getfit = newfunction(this.x[0],this.x[1]); */

	public double distance(Fish f) {
		double a = 0;
		for (int i = 0; i < dim; i++) {
			if (this.x[i] - f.x[i] == 0)
				a = 0.00001;
			else
				a += (this.x[i] - f.x[i]) * (this.x[i] - f.x[i]);
		}
		return Math.sqrt(a);
	}

	public double newfunction(double[] w) throws IOException {

		return -(w[0] * w[0] * w[0] + w[1] * w[1] - 2 * w[0] * w[1]);
	}
}