package DijkstarVisualizer;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class Cluster implements Iterable<Point> {
    private LinkedList<Point> pts;
    private double radius;
    private Point center;

    public Point center() {
        return center;
    }

    public int size(){
        return pts.size();
    }

    public double radius(){
        return radius;
    }

    public Point point(int i) {
        return pts.get(i);
    }

    public Point randomPoint() {
        Random r = new Random(System.currentTimeMillis());
        return pts.get(r.nextInt(pts.size()));
    }

    public Cluster(Point center, int radius, boolean[][] pointAt) {
        this.center = center;
        this.radius = radius;
        // pointAt = new boolean[radius * 2][radius * 2];
        pts = new LinkedList<Point>();
        Random r = new Random(System.currentTimeMillis());
        for (int i = 0; i < radius; i++) {
            int xx = (int) (center.getX() + r.nextInt(radius) * Math.cos(r.nextDouble() * Math.PI));
            int yy = (int) (center.getY() + r.nextInt(radius) * Math.cos(r.nextDouble() * Math.PI));
            if (xx <= 0 || yy <= 0 || xx >= 1600 || yy >= 1000)
                continue;
            if (!pointAt[xx][yy]) {
                pts.add(new Point(xx, yy));
                pointAt[xx][yy] = true;
            }
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return pts.iterator();
    }
}
