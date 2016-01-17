import java.util.Arrays;
import java.util.Comparator;

public class Tree {
	Point[] pts;
	LinkToPoints[] links;
	Tree left, right;
	Tree nextCoordTree;
	int minCoord, maxCoord;
	int d;

	public Tree(Point[] a, final int dimensions) {
		build(a, dimensions, true, null);
	}

	private Tree(Point[] a, final int dimensions, boolean isRoot) {
		build(a, dimensions, isRoot, null);
	}

	private Tree(Point[] a, final int dimensions, LinkToPoints[] links) {
		build(a, dimensions, false, links);
	}

	void build(Point[] a, final int dimensions, boolean isRoot,
			LinkToPoints[] linksFromParent) {
		if (dimensions < 2) {
			throw new AssertionError("Dimension should be >= 2");
		}
		for (Point p : a) {
			if (p.coords.length < dimensions) {
				throw new AssertionError("Point " + p + " doesn't have "
						+ dimensions + " coordinates");
			}
		}
		this.pts = a;
		this.links = linksFromParent;
		if (isRoot) {
			Arrays.sort(pts, new Comparator<Point>() {

				@Override
				public int compare(Point o1, Point o2) {
					return Integer.compare(o1.coords[dimensions - 1],
							o2.coords[dimensions - 1]);
				}
			});
		}
		this.d = dimensions;
		this.maxCoord = pts[pts.length - 1].coords[dimensions - 1];
		this.minCoord = pts[0].coords[dimensions - 1];
		if (dimensions == 2 && links == null) {
			links = new LinkToPoints[pts.length];
			for (int i = 0; i < links.length; i++) {
				links[i] = new LinkToPoints(pts[i], i);
			}
			Arrays.sort(links);
		}
		if (pts.length != 1) {
			int mid = a.length / 2;
			Point[] leftPts = new Point[mid];
			Point[] rightPts = new Point[pts.length - mid];
			for (int i = 0; i < mid; i++) {
				leftPts[i] = pts[i];
			}
			for (int i = 0; i < rightPts.length; i++) {
				rightPts[i] = pts[mid + i];
			}
			if (dimensions > 2) {
				left = new Tree(leftPts, d, false);
				right = new Tree(rightPts, d, false);
			} else {
				LinkToPoints[] leftLinks = new LinkToPoints[mid];
				LinkToPoints[] rightLinks = new LinkToPoints[pts.length - mid];
				int it1 = 0, it2 = 0;
				for (int i = 0; i < links.length; i++) {
					if (links[i].posSortedByY < mid) {
						leftLinks[it1++] = new LinkToPoints(links[i].p,
								links[i].posSortedByY);
					} else {
						rightLinks[it2++] = new LinkToPoints(links[i].p,
								links[i].posSortedByY - mid);
					}
					links[i].posLeft = it1 - 1;
					links[i].posRight = it2 - 1;
				}
				left = new Tree(leftPts, d, leftLinks);
				right = new Tree(rightPts, d, rightLinks);
			}
		}
		if (dimensions > 2) {
			nextCoordTree = new Tree(pts.clone(), d - 1);
		} 
	}

	// point is in range iff c_i >= from_i && c_i <= to_i
	public int getNumberOfPoints(Point from, Point to) {
		int[] coords = to.coords.clone();
		coords[d - 1] = from.coords[d - 1] - 1;
		Point toSub1 = new Point(coords);
		return getNumberOfPointLessThanTo(from, to)
				- getNumberOfPointLessThanTo(from, toSub1);
	}

	// point is in range iff c_i >= from_i && c_i <= to_i for i != d
	// and c_d <= to_d
	private int getNumberOfPointLessThanTo(Point from, Point to) {
		if (d > 2) {
			if (maxCoord <= to.coords[d - 1]) {
				return nextCoordTree.getNumberOfPoints(from, to);
			}
			if (minCoord > to.coords[d - 1]) {
				return 0;
			}
			return left.getNumberOfPointLessThanTo(from, to)
					+ right.getNumberOfPointLessThanTo(from, to);
		} else {
			return getNumberOfPoints2DRoot(to.coords[0], to.coords[1])
					- getNumberOfPoints2DRoot(from.coords[0] - 1, to.coords[1]);
		}
	}

	// x - coords[0], y - coords[1]
	// tree is build by Y
	// number of points <= xTo && <= yTo
	private int getNumberOfPoints2DRoot(int xTo, int yTo) {
		int left = -1, right = links.length;
		while (right - left > 1) {
			int mid = (left + right) >>> 1;
			if (links[mid].p.coords[0] <= xTo) {
				left = mid;
			} else {
				right = mid;
			}
		}
		return getNumberOfPoints2D(left, yTo);
	}

	private int getNumberOfPoints2D(int linkPosX, int yTo) {
		if (linkPosX == -1) {
			return 0;
		}
		if (minCoord > yTo) {
			return 0;
		}
		if (maxCoord <= yTo) {
			return linkPosX + 1;
		}
		return left.getNumberOfPoints2D(links[linkPosX].posLeft, yTo)
				+ right.getNumberOfPoints2D(links[linkPosX].posRight, yTo);
	}

	private class LinkToPoints implements Comparable<LinkToPoints> {
		Point p;
		int posLeft, posRight;
		int posSortedByY;

		public LinkToPoints(Point p, int pos) {
			super();
			this.p = p;
			this.posSortedByY = pos;
		}

		@Override
		public int compareTo(LinkToPoints arg0) {
			return Integer.compare(p.coords[0], arg0.p.coords[0]);
		}

	}
}
