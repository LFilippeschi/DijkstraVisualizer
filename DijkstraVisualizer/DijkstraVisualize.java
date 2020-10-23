package DijkstarVisualizer;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Assignment.Assignment3a.SingleLinkedList;
import Lab4.Lab4Graphs;
import Lab4.Lab4Graphs.DirectedEdge;
import Lab4.Lab4Graphs.WeightedEdgeDirectedGraph;
import Lab4.IndexMinPQ;

public class DijkstraVisualize { // window 800,800

    public static final int myHEIGHT = 1000;
    public static final int myWIDTH = 1600;
    public static boolean STEPS = true;
    // public static final double ratio = .5;

    @SuppressWarnings("serial")
    public static class Window extends JPanel {
        private LinkedList<Integer> x;
        private LinkedList<Integer> y;
        private boolean[][] pointAt = new boolean[myWIDTH][myHEIGHT]; // tells me if the spot is used
        private HashMap<Integer, Point> map; // connects index to a point so that i can use it with a shortest path algo
        private WeightedEdgeDirectedGraph graph;
        private DijkstraPath path;
        private int start = -1;
        private int end = -1;
        private int N;
        private int V;

        private LinkedList<Cluster> clusters;
        private int currentPoint = 0;

        public DijkstraPath path() {
            return path;
        }

        public Window() {
            N = 0;
            clusters = new LinkedList<Cluster>();
            map = new HashMap<Integer, Point>();
            x = new LinkedList<Integer>();
            y = new LinkedList<Integer>();
            for (int i = 0; i < myWIDTH; i++)
                x.add(i);
            for (int i = 0; i < myHEIGHT; i++)
                y.add(i);
        }

        public void createCluster() {
            Random r = new Random(System.currentTimeMillis());
            for (int i = 0; i < 10; i++) {
                Point p = new Point(r.nextInt(1600), r.nextInt(1000));
                clusters.add(new Cluster(p, r.nextInt(150) + 30, pointAt));
            }
            for (Cluster cluster : clusters) {
                for (Point point : cluster) {
                    // System.out.println("N: " + N + " ,Point: " + point); // debug
                    map.put(N++, point);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException iox) {
                    }
                    repaint();
                }
            }
            this.V = map.size();

        }

        public void connectCluster() {
            Scanner sc = new Scanner(System.in);
            Random r = new Random(System.currentTimeMillis());
            int run = 0;
            graph = new WeightedEdgeDirectedGraph(map.size());
            System.out.println("Insert sparcity of cluster: ");
            double ratio = sc.nextDouble();
            for (Cluster cluster : clusters) { // select each cluster
                // System.out.println(cluster + " size: " + cluster.size()); // debug
                // System.out.println("run: " + run); // debug

                for (int i = 0; i < cluster.size() * ratio; i++) { // run through each point * 2
                    int index = r.nextInt(cluster.size()) + currentPoint;
                    int index2 = r.nextInt(cluster.size()) + currentPoint;
                    Point pt = map.get(index);
                    while (index2 == index) {
                        index2 = r.nextInt(cluster.size()) + currentPoint;
                    }
                    Point pt2 = map.get(index2);
                    // System.out.println("p1: " + pt + " p2: " + pt2);
                    // System.out.println("index: " + (index + currentPoint) + " index2: " + (index2
                    // + currentPoint));
                    // System.out.println("currentPoint offset " + currentPoint);
                    graph.addEdge(new DirectedEdge(index, index2, pt.distance(pt2))); // i
                                                                                      // need
                                                                                      // two
                                                                                      // points
                                                                                      // randomly
                                                                                      // selected
                    graph.addEdge(new DirectedEdge(index2, index, pt2.distance(pt)));
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException iox) {
                    }
                    repaint();
                }

                if (run++ < clusters.size() - 1) {
                    for (int j = 0; j < 2; j++) {
                        int index = r.nextInt(cluster.size()) + currentPoint; // connect each cluster
                        int index2 = cluster.size() + currentPoint + r.nextInt(9) + 1;
                        // System.out.println("index: " + (index) + " index2: " + (index2) + " map size:
                        // " + map.size());
                        Point pt = map.get(index);
                        Point pt2 = map.get(index2);

                        graph.addEdge(new DirectedEdge(index, index2, pt.distance(pt2))); // i
                                                                                          // need
                                                                                          // two
                                                                                          // points
                                                                                          // randomly
                                                                                          // selected
                        graph.addEdge(new DirectedEdge(index2, index, pt2.distance(pt)));
                    }
                }
                currentPoint += cluster.size();
                repaint();
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException iox) {
            }
            repaint();
        }

        public void populate() { // insert points
            Random r = new Random(System.currentTimeMillis());
            Scanner sc = new Scanner(System.in);
            System.out.println("Insert number of vertices");
            int NumVertices = sc.nextInt();
            V = NumVertices;
            for (int i = 0; i < NumVertices; i++) {
                int xx = r.nextInt(x.size()); // select a random index from the x list where i keep the indeces of the
                                              // screen
                int yy = r.nextInt(y.size());
                pointAt[xx][yy] = true;
                N++;
                map.put(i, new Point(x.remove(xx), y.remove(yy))); // point i will be unique
                if (STEPS) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException iox) {
                    }
                    repaint();
                }
            }
        }

        public void paint(Graphics g) {
            super.paint(g);
            setBackground(Color.DARK_GRAY);
            for (int i = 0; i < map.size(); i++) { // draws vertices
                if (map.get(i) == null)
                    return;
                Point tmp = map.get(i);
                if (i == start) {
                    g.setColor(Color.RED);
                    g.fillOval((int) tmp.getX() - 4, (int) tmp.getY() - 4, 15, 15);
                } else {
                    if (i == end) {
                        g.setColor(Color.BLUE);
                        g.fillOval((int) tmp.getX() - 4, (int) tmp.getY() - 4, 15, 15);
                    } else {
                        g.setColor(Color.GREEN);
                        g.fillOval((int) tmp.getX(), (int) tmp.getY(), 8, 8);
                    }
                }
            }
            int currentIndex = 0;
            for (Cluster cluster : clusters) {
                String msg = currentIndex + "-" + (currentIndex + cluster.size());
                int x = (int) cluster.center().getX();
                int y = (int) (cluster.center().getY() - cluster.radius());
                g.setColor(Color.WHITE);
                g.drawString(msg, x, y);
                g.setColor(Color.GRAY);
                g.drawOval((int) (cluster.center().getX() - cluster.radius() * 1.1),
                        (int) (cluster.center().getY() - cluster.radius() * 1.1), (int) (cluster.radius() * 2.3),
                        (int) (cluster.radius() * 2.3));
                currentIndex += cluster.size();
            }

            for (int i = 0; i < map.size(); i++) { // draws edges
                Point tmp = map.get(i);
                if (graph == null)
                    return;
                if (graph.adj(i) == null)
                    return;
                for (Object e : graph.adj(i)) { // singlelinked list of edges from i

                    g.setColor(Color.gray);
                    Point tmp2 = map.get(((DirectedEdge) e).to());
                    g.drawLine((int) tmp.getX() + 4, (int) tmp.getY() + 4, (int) tmp2.getX() + 4,
                            (int) tmp2.getY() + 4);
                }
            }
            if (path == null)
                return;
            for (int i = 0; i < V(); i++) {
                if (path.pathTo(i) == null)
                    continue;
                for (DirectedEdge e : path.pathTo(i)) {
                    Point p1 = map.get(e.from());
                    Point p2 = map.get(e.to());

                    g.setColor(new Color(0, 128, 128));
                    g.drawLine((int) p1.getX() + 4, (int) p1.getY() + 4, (int) p2.getX() + 4, (int) p2.getY() + 4);
                }
            }
            if (path.hasPathTo(end)) {
                for (DirectedEdge e : path.pathTo(end)) { // draw shortest path from a to b
                    Point p1 = map.get(e.from());
                    Point p2 = map.get(e.to());

                    g.setColor(Color.red);
                    g.drawLine((int) p1.getX() + 4, (int) p1.getY() + 4, (int) p2.getX() + 4, (int) p2.getY() + 4);
                }
                String dist = "Shortest path distance: " + Double.toString(path.distTo(end));
                g.setColor(Color.white);
                g.drawString(dist, 800 - (dist.length() / 2)*7, 10);
            }
        }

        public int N() {
            return N;
        }

        public int V() {
            return V;
        }

        public WeightedEdgeDirectedGraph Graph() {
            return graph;
        }

        public void createGraph() { // create edges
            Scanner sc = new Scanner(System.in);
            System.out.println("Insert Ratio Vertices to Edges");
            double ratio = sc.nextDouble();
            Random r = new Random(System.currentTimeMillis());
            graph = new WeightedEdgeDirectedGraph(N());
            for (int i = 0; i < V() * ratio; i++) {
                int index = r.nextInt(map.size());
                int index2 = r.nextInt(map.size());
                Point pt = map.get(index);
                while (index2 == index) {
                    index2 = r.nextInt(map.size());
                }
                Point pt2 = map.get(index2);
                graph.addEdge(new DirectedEdge(index, index2, pt.distance(pt2))); // i need two points randomly selected
                graph.addEdge(new DirectedEdge(index2, index, pt2.distance(pt)));
                if (STEPS) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException iox) {
                    }
                    repaint();
                }
            }
        }

        public void findShortestPath() {
            Scanner sc = new Scanner(System.in);
            System.out.println("Insert A and B vertices 0 to exit.");
            start = sc.nextInt();
            if (start == 0)
                return;
            while (start >= V()) {
                System.out.format("Invalid element! A must be between 0 and %d\n", V());
                start = sc.nextInt();
            }
            end = sc.nextInt();
            while (end >= V()) {
                System.out.format("Invalid element! B must be between 0 and %d\n", V());
                end = sc.nextInt();
            }
            path = new DijkstraPath(Graph(), start, end, this, this.getGraphics(), map);
        }

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        JFrame frame = new JFrame("Dijkstra visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.DARK_GRAY);
        frame.setSize(myWIDTH + 10, myHEIGHT + 80);
        frame.setResizable(false);

        Window w = new Window();
        frame.add(w);
        frame.setVisible(true);
        System.out.println("Insert 1 for random points, 2 for clusters: ");
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                w.populate();
                w.createGraph();
                while (true) {
                    System.out.println("Select flag for visualization, 1 = true, 0 = false, -1 to exit");
                    int val = sc.nextInt();
                    if (val == -1)
                        break;
                    else if (val == 1)
                        STEPS = true;
                    else
                        STEPS = false;
                    w.findShortestPath();
                    w.repaint();
                    if (w.path().hasPathTo(w.end))
                        System.out.println("Shortest path found!");
                    else
                        System.out.println("SHortest path not found!");

                }
                break;
            case 2:
                w.createCluster();
                w.connectCluster();
                while (true) {
                    System.out.println("Select flag for visualization, 1 = true, 0 = false, -1 to exit");
                    int val = sc.nextInt();
                    if (val == -1)
                        break;
                    else if (val == 1)
                        STEPS = true;
                    else
                        STEPS = false;
                    w.findShortestPath();
                    w.repaint();
                    if (w.path().hasPathTo(w.end))
                        System.out.println("Shortest path found!");
                    else
                        System.out.println("SHortest path not found!");

                }
                break;

            default:
                break;
        }

    }
}
