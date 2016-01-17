import java.util.Random;

public class Main {
	private static Point genRandomPoint(int dim, Random rnd, int MAXC) {
		int[] tmp = new int[dim];
		for (int j = 0; j < dim; j++) {
			tmp[j] = rnd.nextInt(MAXC) - MAXC / 2;
		}
		return new Point(tmp);
	}

	private static void checkCorrectness() {
		Random rnd = new Random(123);
		for (int test = 0; test < 123123; test++) {
			int dim = 2 + rnd.nextInt(3);
			int n = 1 + rnd.nextInt(20);
			Point[] a = new Point[n];
			final int MAXC = 1 + rnd.nextInt(20);
			for (int i = 0; i < n; i++) {
				a[i] = genRandomPoint(dim, rnd, MAXC);
			}
			NaiveSolver naive = new NaiveSolver(a);
			Tree tree = new Tree(a, dim);
			int qq = 1 + rnd.nextInt(1000);
			for (int q = 0; q < qq; q++) {
				Point from = genRandomPoint(dim, rnd, MAXC);
				Point to = genRandomPoint(dim, rnd, MAXC);
				for (int i = 0; i < dim; i++) {
					if (from.coords[i] > to.coords[i]) {
						int tmp = from.coords[i];
						from.coords[i] = to.coords[i];
						to.coords[i] = tmp;
					}
				}
				int naiveAnswer = naive.getNumberOfPoints(from, to);
				int treeAnswer = tree.getNumberOfPoints(from, to);
				if (naiveAnswer != treeAnswer) {
					throw new AssertionError("failed.");
				}
			}
			System.err.println((test + 1) + " tests passed");
		}
	}

	private static void checkTime() {
		System.err.println("check time:");
		int dim = 2;
		Random rnd = new Random(123);
		int n = 200000;
		int MAXC = (int) 1e9;
		Point[] a = new Point[n];
		for (int i = 0; i < n; i++) {
			a[i] = genRandomPoint(dim, rnd, MAXC);
		}
		long START = System.currentTimeMillis();
		Tree tree = new Tree(a, dim);
		long BUILD = System.currentTimeMillis();
		System.err.println("build: " + (BUILD - START) + "ms");
		int qq = 200000;
		for (int q = 0; q < qq; q++) {
			Point from = genRandomPoint(dim, rnd, MAXC);
			Point to = genRandomPoint(dim, rnd, MAXC);
			for (int i = 0; i < dim; i++) {
				if (from.coords[i] > to.coords[i]) {
					int tmp = from.coords[i];
					from.coords[i] = to.coords[i];
					to.coords[i] = tmp;
				}
			}
			tree.getNumberOfPoints(from, to);
		}
		System.err.println(qq + " queries answered in "
				+ (System.currentTimeMillis() - BUILD) + " ms");
	}

	public static void main(String[] args) {
		checkCorrectness();
		System.err.println();
		checkTime();

	}
}
