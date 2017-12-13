package facespace;

public class Graph {

	private int numVertices;
	private int numEdges;
	private Bag<Integer>[] adj;
	
	public Graph(int numVertices){
		initializeEmptyGraph(numVertices);
	}
	
	public void initializeEmptyGraph(int numVertices){
		this.numVertices = numVertices;
		this.numEdges = 0;
		adj = (Bag<Integer>[]) new Bag[numVertices];
		for(int v = 0; v < numVertices; v++){
			adj[v] = new BagArray<Integer>();
		}
	}
	
	public void addEdge(int v, int w){
		numEdges++;
		adj[v].add(w);
//		adj[w].add(v);
	}
	
	public Iterable<Integer> adj(int v){
		return(adj[v]);
	}
	
	public int numVertices(){
		return(this.numVertices);
	}
	
	public int numEdges(){
		return(this.numEdges);
	}
	
	public int degree(int v){
		return adj[v].size();
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		String NEWLINE = System.getProperty("line.separator");
		sb.append(numVertices + " vertices, "  +  numEdges + " edges " + NEWLINE);
		for(int v = 0; v < numVertices; v++){
			sb.append(v +": ");
			for(int w : adj[v]){
				sb.append(w + " ");
			}
			sb.append(NEWLINE);
		}
		return(sb.toString());
	}
	

}
