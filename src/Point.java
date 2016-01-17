import java.util.Arrays;

public class Point {
	int[] coords;

	Point(int[] coords) {
		this.coords = coords;
	}

	@Override
	public String toString() {
		return "Point [" + Arrays.toString(coords) + "]";
	}

}
