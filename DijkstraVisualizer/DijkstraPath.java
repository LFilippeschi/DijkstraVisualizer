package DijkstarVisualizer;

import java.awt.*;
import java.util.HashMap;

import javax.swing.JFrame;

import Assignment.Assignment3a.SingleLinkedList;
import Lab4.IndexMinPQ;
import Lab4.Lab4Graphs.DirectedEdge;
import Lab4.Lab4Graphs.Queue;
import Lab4.Lab4Graphs.WeightedEdgeDirectedGraph;

public class DijkstraPath {
	private int end;
	private Graphics g;
	private DijkstraVisualize.Window w;
	private HashMap<Integer, Point> map;
	private DirectedEdge[] edgeTo;
	private WeightedEdgeDirectedGraph G;
	private double[] distTo;
	private IndexMinPQ<Double> pq; // min priority queue based on idex (Taken from
									// https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/IndexMinPQ.java.html)
									// and adapted to my needs

	public DijkstraPath(WeightedEdgeDirectedGraph G, int s, int e, DijkstraVisualize.Window w, Graphics g,
			HashMap<Integer, Point> map) {
		this.map = map;
		this.G = G;
		this.w = w;
		this.g = g;
		this.end = e;
		edgeTo = new DirectedEdge[G.V()];
		distTo = new double[G.V()];
		pq = new IndexMinPQ<Double>(G.V());
		for (int v = 0; v < G.V(); v++) // initializes all distances to infinity which means that th evertex is not
										// reachable
			distTo[v] = Double.POSITIVE_INFINITY;
		distTo[s] = 0.0;
		pq.insert(s, 0.0); // insert the first and start relaxing all the edges
		while (!pq.isEmpty()) {
			if (DijkstraVisualize.STEPS)
				paint();
			relax(G, pq.delMin());
		}
	}

	public void paint() {
		w.repaint();
		for (int i = 0; i < G.V(); i++) {
			if (pathTo(i) == null)
				continue;
			for (DirectedEdge e : pathTo(i)) {
				Point p1 = map.get(e.from());
				Point p2 = map.get(e.to());
				w.setBackground(Color.DARK_GRAY);

				g.setColor(new Color(0, 128, 128));
				g.drawLine((int) p1.getX() + 4, (int) p1.getY() + 4, (int) p2.getX() + 4, (int) p2.getY() + 4);
				// try {
				// Thread.sleep(1);
				// } catch (InterruptedException iox) {
				// }
			}
		}

	}

	public DijkstraVisualize.Window w() {
		return w;
	}

	@SuppressWarnings("unchecked")
	private void relax(WeightedEdgeDirectedGraph G, int v) {
		for (DirectedEdge e : (SingleLinkedList<DirectedEdge>) G.adj().arr[v]) { // for each edge e connected to v
			int w = e.to(); // see where you can go v -> w
			if (distTo[w] > distTo[v] + e.weight()) { // if distance to w is greater than distance to v + current
														// edge's weight. we have to update the distance to w
				distTo[w] = distTo[v] + e.weight();
				edgeTo[w] = e; // update how we get to w
				if (pq.contains(w))
					pq.change(w, distTo[w]); // update value in priority queue at w with current distance
				else
					pq.insert(w, distTo[w]); // if the index was not there before insert
			}
		}
	}

	public double distTo(int v) {
		return distTo[v];
	}

	public boolean hasPathTo(int v) {
		return distTo[v] < Double.POSITIVE_INFINITY;
	}

	/**
	 * 
	 * @param v
	 * @return Directed edges passed to get to v
	 */
	public Iterable<DirectedEdge> pathTo(int v) {
		if (!hasPathTo(v))
			return null;
		Queue<DirectedEdge> path = new Queue<DirectedEdge>();
		for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) // start in v and updates e with Directed
																			// edge to get to e until e == null
			path.add(e);
		return path;
	}

}
