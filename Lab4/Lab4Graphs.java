/*=============================================================================
 |   Assignment:  LAB 4 - Graph
 |
 |       Author:  Leonardo Filippeschi
 |       Contac:  lfil@kth.se
 |
 |      Created:  5.10.2020
 |  Last edited:  5.10.2020
 |
 |        Class:  ID1020 HT2020 - ALgorithms and Data Structures 
 |
 |   Instructor:  Robert Rönngren
 |
 +-----------------------------------------------------------------------------
 |
 |  Description:  1. Undirected graphs. Write a program based on DFS which can 
 |                answer questions of the type: "Find the a path from X to Y" 
 |                Which should result in a list of vertices traversed from X to 
 |                Y if there is a path.
 |
 |                2. Undirected graphs. Change the program to use BFS.
 |
 |                3. Directed graphs. Write a program that can answer if there 
 |                is a path between any to vertices.
 |
  +-----------------------------------------------------------------------------
 |
 |                              Higher grade assignments
 |
 |                4. Implement a program which allows the user to find the 
 |                shortest path between two nodes in a graph possibly passing 
 |                through a third node. 
 |
 |    Algorithm:  Dijkstra using minimum priority queue based on heap 
 |
 |                                   Complexity
 |
 |         Time: O(ElogV). During initialization we have to create the graph and
 |               symbol table.
 |               It will take E accesses to create the symbol table. after that
 |				 we'll need V accesses to Keys[] array to initialize the keys and
 |               in the end another E accesses to initialize the edges in the graph.
 |               Then the actual Dijkstra algorithm starts and it will run once 
 |               and after that once more for each stop that we want to perform 
 |               along the way. The Algorithm will run once for each Vertex and 
 |               in each run we will either insert or change an element from the
 |               priority queue. The insert operation will result in a swim 
 |               function wich will require log N operations where N is the total
 |               number of Vertices therefore V. The change function will result
 |               in both a sink and a swim as we don't know where the element needs
 |               to be placed in the heap. This operation will also run at most 
 |               log N times. If we sum all the components we get:
 |               2E*log(V) + 2E + V = O(Elog(V))
 |
 |       Memory: O(V+E). The memory complexity of the algorithm itself is V but 
 |               we also have to add the space that we use to create the Graph 
 |               and in that case it will result in O(V+E). The Graph will have 
 |               V SingleLinkedLists and E Directed edges stored in total.
 |               Dijkstra's algorithm will instead use an array of V Directed 
 |               Edges and another array of V double plus the memory for the 
 |               priority queue which will result in two arrays of V int and 
 |               another array of V double. If we sum up all the components we get:
 |               8V + 4E = O(V+E)
 |
 *===========================================================================*/

package Lab4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import Assignment.Assignment3a;
import Assignment.Assignment3a.SingleLinkedList;
import Lab3.Lab3Main;

public class Lab4Graphs {

	// ================================================================================================================================
	// Generic QUEUE Implementation

	/**
	 * Implements a queue based DS using a single linked list (FIFO)
	 */
	public static class Queue<T> implements Iterable<T> {
		private SingleLinkedList<T> list;

		public Queue() {
			list = new SingleLinkedList<T>();
		}

		public void add(T t) {
			list.insert(t);
		}

		public T remove() throws Exception {
			return list.removeLast();
		}

		public boolean isEmpty() {
			return list.isEmpty();
		}

		@Override
		public String toString() {
			return list.toString();
		}

		@Override
		public Iterator<T> iterator() {
			return list.iterator();
		}
	}

	// ================================================================================================================================
	// Generic STACK Implementation

	/**
	 * Implements a Stack DT using an array of generic objects (LIFO)
	 */
	public static class Stack<T> implements Iterable<T> {
		public Object[] arr;
		public int length;
		public int size;
		public int index;

		public Stack() {
			arr = new Object[1];
		}

		public Stack(int size) {
			length = size;
			arr = new Object[size];
		}

		private void resize(int size) {
			Object[] rtn = new Object[size];
			for (int i = 0; i < arr.length; i++) {
				rtn[i] = arr[i];
			}
			arr = rtn;
		}

		private void shrink(int size) {
			Object[] rtn = new Object[size];
			for (int i = 0; i < rtn.length; i++) {
				rtn[i] = arr[i];
			}
			arr = rtn;
		}

		public void push(T t) {
			if (index > arr.length / 2)
				resize(arr.length * 2);
			arr[index++] = t;
			size++;
		}

		@SuppressWarnings("unchecked")
		T pop() {
			if (index > 0) {
				if (index < arr.length / 4)
					shrink(arr.length / 2);
				T e = (T) arr[--index];
				arr[index] = null;
				size--;
				return e;
			} else
				return null;
		}

		@SuppressWarnings("unchecked")
		T get(int i) {
			if (index > 0) {
				T e = (T) arr[i];
				return e;
			} else
				return null;
		}

		public boolean isEmpty() {
			if (index == 0)
				return true;
			else
				return false;
		}

		@Override
		public String toString() {
			return Arrays.toString(arr);
		}

		public Iterator<T> iterator() {
			return new MyIterator();
		}

		private class MyIterator implements Iterator<T> {
			private int i = 0;

			public boolean hasNext() {
				return i < index;
			}

			@SuppressWarnings("unchecked")
			public T next() {
				return (T) arr[i++];
			}
		}
	}

	// ================================================================================================================================
	// Generic Array Implementation accessible by index

	/**
	 * Implements a generic array of objects <T>, accessible by index
	 */
	public static class Array<T> {
		public Object[] arr;
		public int length;
		public int size;

		public Array(int size) {
			length = size;
			arr = new Object[size];
		}

		@SuppressWarnings("unchecked")
		T get(int i) {
			T e = (T) arr[i];
			return e;
		}

		void set(int i, T t) {
			arr[i] = t;
		}

		@Override
		public String toString() {
			return Arrays.toString(arr);
		}
	}

	// ==================================================================================================================================
	// Generic Undirected Graph Implementation, uses Single linked list to keep
	// track of edges

	public static class Graph {
		private int V;
		private int E;
		private Array<SingleLinkedList<Integer>> adj;

		public Graph(int V) {
			this.V = V;
			this.E = 0;
			adj = new Array<SingleLinkedList<Integer>>(V);
			for (int v = 0; v < V; v++) {
				adj.arr[v] = new SingleLinkedList<Integer>();
			}
		}

		public int V() {
			return V;
		}

		public int E() {
			return E;
		}

		public void addEdge(int v, int w) {
			if (!adj.get(v).contains(w))
				adj.get(v).insertLast(w);
			if (!adj.get(w).contains(v))
				adj.get(w).insertLast(v);
			E++;
		}

		public Iterable<Integer> adj(int v) {
			return adj.get(v);
		}

		@SuppressWarnings("unchecked")
		public String toString() {
			String s = "";
			int count = 0;
			for (Object singleLinkedList : adj.arr) {
				s = s.concat(
						"Node(" + count + "): " + ((SingleLinkedList<Integer>) singleLinkedList).toString() + "\n");
				count++;
			}
			return s;
		}
	}

	// ==================================================================================================================================
	// Generic DIrected Graph Implementation, uses Single linked list to keep track
	// of edges

	public static class DirectedGraph {
		private int V;
		private int E;
		private Array<SingleLinkedList<Integer>> adj;

		public DirectedGraph(int V) {
			this.V = V;
			this.E = 0;
			adj = new Array<SingleLinkedList<Integer>>(V);
			for (int v = 0; v < V; v++) {
				adj.arr[v] = new SingleLinkedList<Integer>();
			}
		}

		public int V() {
			return V;
		}

		public int E() {
			return E;
		}

		public void addEdge(int v, int w) {
			if (!adj.get(v).contains(w))
				adj.get(v).insertLast(w);
			E++;
		}

		public Iterable<Integer> adj(int v) {
			return adj.get(v);
		}

		@SuppressWarnings("unchecked")
		public String toString() {
			String s = "";
			int count = 0;
			for (Object singleLinkedList : adj.arr) {
				s = s.concat(
						"Node(" + count + "): " + ((SingleLinkedList<Integer>) singleLinkedList).toString() + "\n");
				count++;
			}
			return s;
		}
	}

	// ==================================================================================================================================
	// Depth First Search Implementation

	public static class DFS {
		private boolean[] marked;
		private int count;
		private Stack<Integer> path;

		public DFS(Graph g, int s, int e) {
			path = new Stack<Integer>(16);
			marked = new boolean[g.V()];
			path = dfs(g, s, "", -1, e);
			if (marked[e] == true)
				;
			// System.out.println(rtn);
			else
				System.out.println("Path not found!");
			;
		}

		/**
		 * DFS search that stops once the vertex has been found
		 * 
		 * @param g
		 * @param v
		 * @param s
		 * @param previous
		 * @param end
		 * @return Stack<Integer>, path to vertex "end" starting from "v"
		 */
		private Stack<Integer> dfs(Graph g, int v, String s, int previous, int end) {
			if (marked[end])
				return path;
			else {
				String rtn = ("[" + v + "]");
				marked[v] = true;
				count++;
				Assignment3a.SingleLinkedList.MyIterator<Integer> myItr = new Assignment3a.SingleLinkedList.MyIterator<Integer>(
						g.adj.get(v)); // don't use foreach method so that we can stop when marked[end]==true
				while (myItr.hasNext() && !marked[end]) {
					int w = myItr.next();
					if (w == end) {
						marked[w] = true;
						path.push(v); // push vertices as we go through the graph, v can be moved outside
						path.push(w);
						return path;
					}
					if (!marked[w]) {
						path.push(v);
						rtn = rtn.concat(dfs(g, w, s, v, end).toString());
					}
				}
				if (!marked[end])
					path.pop(); // pop all the vertices if we have to go back to previous vertices(i.e. when
								// returning without having found end)
				return path;
			}
		}

		public boolean marked(int w) {
			return marked[w];
		}

		public int count() {
			return count;
		}

	}

	// ==================================================================================================================================
	// Directed Graph DFS

	public static class DirectedGDFS {
		private boolean[] marked;
		private boolean hasPathTo;
		private Stack<Integer> path;

		public DirectedGDFS(DirectedGraph G, int s, int e) {
			marked = new boolean[G.V()];
			path = new Stack<Integer>();
			dfs(G, s, e);
		}

		private void dfs(DirectedGraph G, int v, int e) {
			marked[v] = true;
			path.push(v);
			for (int w : G.adj(v)) {
				if (marked[e])
					return;
				if (w == e && !marked[e]) {
					hasPathTo = true;
					marked[w] = true;
					path.push(w);
					return;
				}
				if (!marked[w])
					dfs(G, w, e); // recursive call to lower vertices
			}
			if (!marked[e]) {
				path.pop();
			}
		}

		public boolean marked(int v) {
			return marked[v];
		}

		public Stack<Integer> path() {
			return path;
		}

		public boolean hasPathTo() {
			return hasPathTo;
		}

		public String toString() {
			return path.toString();
		}
	}

	// ==================================================================================================================================
	// Create Undirected Graph from file containing Strings

	public static class DFSSTPath {
		private Lab3Main.arrayST<String, Integer> st; // ST that tracks each vertex to an index
		private String[] keys; // array of Vertices names in position of index
		private Graph G; // Graph generated

		public DFSSTPath(String file, String delimiter) throws FileNotFoundException {
			st = new Lab3Main.arrayST<String, Integer>();
			File f = new File(file);
			Scanner fs = new Scanner(f);
			while (fs.hasNextLine()) {
				String[] a = fs.nextLine().split(delimiter);
				for (int i = 0; i < a.length; i++)
					if (!st.contains(a[i]))
						st.put(a[i], st.getSize());
			}
			keys = new String[st.getSize()];
			for (Comparable name : st.keys) {
				if (name == null)
					break;
				keys[st.get((String) name)] = (String) name;
			}
			fs.close();
			G = new Graph(st.getSize());
			fs = new Scanner(f);
			while (fs.hasNextLine()) {
				String[] a = fs.nextLine().split(delimiter);
				int v = st.get(a[0]);
				for (int i = 1; i < a.length; i++)
					G.addEdge(v, st.get(a[i]));
			}
			fs.close();
		}

		public boolean contains(String s) {
			return st.contains(s);
		}

		public int index(String s) {
			return st.get(s);
		}

		public String name(int v) {
			return keys[v];
		}

		public Graph G() {
			return G;
		}
	}

	// ==================================================================================================================================
	// Create Directed Graph and array of Strings from file

	public static class DGraphInit {
		private Lab3Main.arrayST<String, Integer> st;
		private String[] keys;
		private DirectedGraph G;

		public DGraphInit(String file, String delimiter) throws FileNotFoundException {
			st = new Lab3Main.arrayST<String, Integer>();
			File f = new File(file);
			Scanner fs = new Scanner(f);
			while (fs.hasNextLine()) {
				String[] a = fs.nextLine().split(delimiter);
				for (int i = 0; i < a.length; i++)
					if (!st.contains(a[i]))
						st.put(a[i], st.getSize());
			}
			keys = new String[st.getSize()];
			for (Comparable name : st.keys) {
				if (name == null)
					break;
				keys[st.get((String) name)] = (String) name;
			}
			fs.close();
			G = new DirectedGraph(st.getSize());
			fs = new Scanner(f);
			while (fs.hasNextLine()) {
				String[] a = fs.nextLine().split(delimiter);
				int v = st.get(a[0]);
				for (int i = 1; i < a.length; i++)
					G.addEdge(v, st.get(a[i]));
			}
			fs.close();
		}

		public boolean contains(String s) {
			return st.contains(s);
		}

		public int index(String s) {
			return st.get(s);
		}

		public String name(int v) {
			return keys[v];
		}

		public DirectedGraph G() {
			return G;
		}
	}

	// ==================================================================================================================================
	// Breadth First Search Path implementation

	public static class BFSPath {
		private boolean[] marked;
		private int[] edgeTo;
		private int s;

		public BFSPath(Graph G, int s) throws Exception {
			marked = new boolean[G.V()];
			edgeTo = new int[G.V()];
			this.s = s;
			bfs(G, s);
		}

		private void bfs(Graph G, int s) throws Exception {
			Queue<Integer> queue = new Queue<Integer>();
			marked[s] = true;
			queue.add(s);
			while (!queue.isEmpty()) { // goes until all the vertices have been visited
				int v = queue.remove(); // takes the vertex from the queue respecting the order in which they were put
				for (int w : G.adj(v)) { // iterates over all the vertices adjacent to v
					if (!marked[w]) {
						edgeTo[w] = v; // saves how we got to w (ie from v)
						marked[w] = true; // updates if we reach the vertex
						queue.add(w); // adds new vertex to the queue
					}
				}
			}
		}

		public boolean hasPathTo(int v) {
			return marked[v];
		}

		/**
		 * 
		 * @param v
		 * @return Stack<Integer> with path to v starting from s. null if no path found
		 */
		public Iterable<Integer> pathTo(int v) {
			if (!hasPathTo(v))
				return null;
			Stack<Integer> path = new Stack<Integer>();
			for (int x = v; x != s; x = edgeTo[x]) // stops when we get back to s, edgeTo[x] has where we get ther from
				path.push(x);
			path.push(s);
			return path;
		}
	}

	// ==================================================================================================================================
	// Directed Weighted Edge

	public static class DirectedEdge {
		private int v; // start
		private int w; // end
		private double weight;

		public DirectedEdge(int v, int w, double weight) {
			this.v = v;
			this.w = w;
			this.weight = weight;
		}

		public double weight() {
			return weight;
		}

		public int from() {
			return v;
		}

		public int to() {
			return w;
		}

		public String toString() {
			return String.format("%d->%d %.2f", v, w, weight);
			// return String.format("%d->", v);
		}

	}

	// ==================================================================================================================================
	// Edge Weighted Directed Graph

	public static class WeightedEdgeDirectedGraph {
		private int V;
		private int E;
		private Array<SingleLinkedList<DirectedEdge>> adj; // Generic Array of Single linked list containing Directed
															// edges (eg adj[i] contains all edges from i -> x with
															// respective weight)

		public WeightedEdgeDirectedGraph(int V) {
			this.V = V;
			this.E = 0;
			adj = new Array<SingleLinkedList<DirectedEdge>>(V);
			for (int v = 0; v < V; v++) {
				adj.arr[v] = new SingleLinkedList<DirectedEdge>();
			}
		}

		public int V() {
			return V;
		}

		public Array<SingleLinkedList<DirectedEdge>> adj(){
			return adj;
		}

		public int E() {
			return E;
		}

		@SuppressWarnings("unchecked")
		public void addEdge(DirectedEdge e) {
			((SingleLinkedList<DirectedEdge>) adj.arr[e.from()]).insert(e); // insert edge e in the position adj[vertex
																			// connected to edge e]
			E++;
		}

		/**
		 * 
		 * @param v
		 * @return Iterable<Object> SingleLinkedList<DirectedEdge> at index v ie all
		 *         directed edges connected to v used for foreach in
		 *         Iterable<DirectedEdge>
		 */
		@SuppressWarnings("unchecked")
		public Iterable<Object> adj(int v) {
			return ((Iterable<Object>) adj.arr[v]);
		}

		/**
		 * 
		 * @return SingleLinkedList<DirectedEdge> with all DirectedEdges
		 */
		public Iterable<DirectedEdge> edges() {
			SingleLinkedList<DirectedEdge> list = new SingleLinkedList<DirectedEdge>();
			for (int v = 0; v < V; v++) // all vertices visited
				for (Object e : adj(v)) //
					list.insert((DirectedEdge) e);
			return list;
		}

	}

	// ==================================================================================================================================
	// Shortest Path Weigthed Directed Graph Dijksta

	public static class DijkstraSP {
		private DirectedEdge[] edgeTo;
		private double[] distTo;
		private IndexMinPQ<Double> pq; // min priority queue based on index (Taken from
										// https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/IndexMinPQ.java.html)
										// and adapted to my needs

		public DijkstraSP(WeightedEdgeDirectedGraph G, int s) {
			edgeTo = new DirectedEdge[G.V()];
			distTo = new double[G.V()];
			pq = new IndexMinPQ<Double>(G.V());
			for (int v = 0; v < G.V(); v++) // initializes all distances to infinity which means that th evertex is not
											// reachable
				distTo[v] = Double.POSITIVE_INFINITY;
			distTo[s] = 0.0;
			pq.insert(s, 0.0); // insert the first and start relaxing all the edges
			while (!pq.isEmpty())
				relax(G, pq.delMin());
		}

		@SuppressWarnings("unchecked")
		private void relax(WeightedEdgeDirectedGraph G, int v) {
			for (DirectedEdge e : (SingleLinkedList<DirectedEdge>) G.adj.arr[v]) { // for each edge e connected to v
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

	// ==================================================================================================================================
	// Helper functions to display results from assignments

	public static void run() {

		WeightedEdgeDirectedGraph G = new WeightedEdgeDirectedGraph(11);
		G.addEdge(new DirectedEdge(1, 2, .5));
		G.addEdge(new DirectedEdge(2, 1, .3));
		G.addEdge(new DirectedEdge(2, 5, .6));
		G.addEdge(new DirectedEdge(5, 6, .2));
		G.addEdge(new DirectedEdge(6, 3, .8));
		G.addEdge(new DirectedEdge(5, 3, .3));
		G.addEdge(new DirectedEdge(3, 6, .24));
		G.addEdge(new DirectedEdge(10, 1, .34));
		G.addEdge(new DirectedEdge(8, 6, .11));
		G.addEdge(new DirectedEdge(6, 2, .04));
		G.addEdge(new DirectedEdge(6, 10, .123));
		DijkstraSP sp = new DijkstraSP(G, 10);
		// Graph g = new Graph(10);
		// g.addEdge(1, 2);
		// g.addEdge(2, 1);
		// g.addEdge(1, 9);
		// g.addEdge(2, 3);
		// g.addEdge(2, 5);
		// g.addEdge(3, 1);
		// g.addEdge(4, 6);
		// g.addEdge(7, 8);
		// g.addEdge(9, 7);
		// Scanner sc = new Scanner(System.in);
		// System.out.println("Insert initial and final node: ");
		// int start = sc.nextInt();
		// int end = sc.nextInt();
		// DFS dfs = new DFS(g, start, end);
		System.out.println(G);
	}

	public static void debug() {
		DirectedGraph g = new DirectedGraph(11);
		g.addEdge(1, 2);
		g.addEdge(2, 1);
		g.addEdge(1, 9);
		g.addEdge(2, 3);
		g.addEdge(2, 5);
		g.addEdge(3, 1);
		g.addEdge(4, 6);
		g.addEdge(7, 8);
		g.addEdge(9, 7);
		g.addEdge(7, 10);
		Scanner sc = new Scanner(System.in);
		System.out.println("Insert initial and final node: ");
		int start = sc.nextInt();
		int end = sc.nextInt();
		DirectedGDFS dfs = new DirectedGDFS(g, start, end);
		System.out.println(dfs);
	}

	public static void assignment1() throws FileNotFoundException {
		DFSSTPath dPath = new DFSSTPath(
				"/media/leonardo/SharedVolume/KTH1/Algorithms and data structures/Algorithm and data structure/Lab4/TheDatabase.txt",
				" ");
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("Insert initial and final node 0 to exit: ");
			String start = sc.next();
			if (start.equals("0"))
				break;
			String end = sc.next();
			int s = dPath.st.get(start);
			int e = dPath.st.get(end);
			DFS dfs = new DFS(dPath.G(), s, e);
			String path = "";
			int i = 0;

			while (dfs.path.get(i) != null) {
				int index = dfs.path.get(i++).intValue();
				path = path.concat("[" + dPath.keys[index] + "]");
			}
			System.out.println(path);
		}
	}

	public static void assignment2() throws Exception {
		DFSSTPath dPath = new DFSSTPath(
				"/media/leonardo/SharedVolume/KTH1/Algorithms and data structures/Algorithm and data structure/Lab4/TheDatabase.txt",
				" ");
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("Insert initial and final node or 0 to exit: ");
			String start = sc.next();
			if (start.equals("0"))
				break;
			String end = sc.next();
			int s = dPath.st.get(start);
			int e = dPath.st.get(end);
			BFSPath bfs = new BFSPath(dPath.G(), s);
			String path = "";
			int i = 0;
			if (bfs.hasPathTo(e)) {
				for (Integer it : bfs.pathTo(e)) {
					path = ("[" + dPath.keys[it] + "]") + path;
				}
			}
			System.out.println(path);
		}
	}

	public static void assignment3() throws FileNotFoundException {
		DGraphInit dPath = new DGraphInit(
				"/media/leonardo/SharedVolume/KTH1/Algorithms and data structures/Algorithm and data structure/Lab4/TheDatabase.txt",
				" ");
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("Insert initial and final node or 0 to exit: ");
			String start = sc.next();
			if (start.equals("0"))
				break;
			String end = sc.next();
			int s = dPath.st.get(start);
			int e = dPath.st.get(end);
			DirectedGDFS dfs = new DirectedGDFS(dPath.G(), s, e);
			System.out.println("Path from " + start + " to " + end + ": " + dfs.hasPathTo());
			// System.out.println(dfs);
			String path = "";
			int i = 0;
			for (Integer it : dfs.path) {
				if (it != null)
					path = path.concat("[" + dPath.keys[it] + "]");
			}
			if (path.equals(""))
				System.out.println("No such path found");
			else
				System.out.println(path);
		}

	}

	public static void HAssignment() throws FileNotFoundException {
		Scanner sc = new Scanner(System.in);
		File f = new File(
				"/media/leonardo/SharedVolume/KTH1/Algorithms and data structures/Algorithm and data structure/Lab4/NYC.txt");
		String delimiter = " ";
		Scanner fs = new Scanner(f);
		int V = Integer.parseInt(fs.nextLine());
		int E = Integer.parseInt(fs.nextLine());
		WeightedEdgeDirectedGraph G = new WeightedEdgeDirectedGraph(V);
		while (fs.hasNextLine()) {
			String[] a = fs.nextLine().split(delimiter);
			for (int i = 0; i < a.length; i++)
				G.addEdge(new DirectedEdge(Integer.parseInt(a[0]), Integer.parseInt(a[1]), Double.parseDouble(a[2])));
		}
		// System.out.println("Finished processing file");
		while (true) {
			System.out.println("Insert A, B, C or 0 to exit.");
			int A = sc.nextInt();
			if (A == 0)
				break;
			int B = sc.nextInt();
			int C = sc.nextInt();
			System.out.println("Which is the shortest path from A to B passing through C? ");
			DijkstraSP sp = new DijkstraSP(G, A);
			if (!sp.hasPathTo(B)) {
				System.out.println("No such path found!");
				break;
			}
			System.out.print(sp.pathTo(C));

			DijkstraSP sp1 = new DijkstraSP(G, C);
			if (!sp1.hasPathTo(C)) {
				System.out.println("No such path found!");
				break;
			}
			System.out.println(sp1.pathTo(B));
			System.out.println("Total distance= " + (sp.distTo(C) + sp1.distTo(B)));
		}

	}

	public static void menu() throws Exception {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println(
					"==========================================================================================");
			System.out.println(
					"Select Function:\n 0 - Exit \n 1 - Undirected Graph DFS \n 2 - Undirected Graph BFS \n 3 - Directed Graph DFS \n"
							+ " 4 - Shortest Path from A to B passing Through C \n");
			int choice = sc.nextInt();
			switch (choice) {
				case 0:
					return;
				case 1:
					assignment1();
					break;
				case 2:
					assignment2();
					break;
				case 3:
					assignment3();
					break;
				case 4:
					HAssignment();
					break;
				default:
					break;
			}
		}
	}

	public static void main(String[] args) throws Exception {
		// run();
		// debug();
		menu();
	}

}
