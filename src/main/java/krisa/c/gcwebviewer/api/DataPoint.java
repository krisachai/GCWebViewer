package krisa.c.gcwebviewer.api;


import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

public class DataPoint {
	List<Point2D> points;

	public DataPoint() {

		points = new LinkedList<>();

	}

	public void addPoint(double x, double y) {
		Point2D pt = new Point2D.Double(x, y);
		points.add(pt);
	}

	public List<Point2D> getPoints() {
		return points;

	}
}
