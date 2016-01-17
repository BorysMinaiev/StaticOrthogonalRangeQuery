public class NaiveSolver {
	Point[] pts;

	public NaiveSolver(Point[] a) {
		this.pts = a;
	}

	int getNumberOfPoints(Point from, Point to) {
		int result = 0;
		for (Point p : pts) {
			boolean ok = true;
			for (int i = 0; i < p.coords.length; i++) {
				if (p.coords[i] < from.coords[i] || p.coords[i] > to.coords[i]) {
					ok = false;
					break;
				}
			}
			if (ok) {
				result++;
			}
		}
		return result;
	}
}
