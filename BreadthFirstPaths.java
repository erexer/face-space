package facespace;
import java.util.ArrayList;
import java.util.Stack;

public class BreadthFirstPaths {
    
    private boolean[] visited;
    private int[] edgeTo;
    private final int source;
    
    public BreadthFirstPaths(Graph g, int source) {
        visited = new boolean[g.numVertices()];
        edgeTo = new int[g.numVertices()];
        this.source = source;
        bfs(g,source);
    }
    
    private void bfs(Graph g, int source) {
        QueueArray<Integer> queue = new QueueArray<Integer>();
        visited[source] = true;
        queue.enqueue(source);
        while (!queue.isEmpty()) {
            int v = queue.dequeue();
            for (int w : g.adj(v)) {
                if (! visited[w]) {
                    edgeTo[w] = v;
                    visited[w] = true;
                    queue.enqueue(w);
                }
            }
        }
    }
    
    public boolean hasPathTo(int v) {
        return visited[v];
    }
    
    public Stack<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        for (int x = v; x != source; x = edgeTo[x]) {
            path.push(x);
        }
        path.push(source);
        return path;
    }

}
