
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

class FastScanner {

    private BufferedReader reader;
    private StringTokenizer tokenizer;

    public FastScanner() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public String next() {
        while (tokenizer == null || !tokenizer.hasMoreTokens()) {
            try {
                String line = reader.readLine();
                if (line == null) {
                    return null;
                }
                tokenizer = new StringTokenizer(line);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return tokenizer.nextToken();
    }

    public int nextInt() {
        return Integer.parseInt(next());
    }
}

class Graph {

    private final int V;            // variavel que guarda a qntd de alunos
    private final List<Integer>[] adj; // vai ser a lista de adjcencia onde vai ter todos os vertices

    @SuppressWarnings("unchecked")
    public Graph(int V) {           //vou receber q qntd de vertices(alunos) que o grafo vai ter
        this.V = V;
        adj = (List<Integer>[]) new List[V];    // instancia a lista com o tamanho de alunos
        for (int v = 0; v < V; v++) {           // percorre cada aluno e cria um vetor em cada aluno
            adj[v] = new ArrayList<>();
        }
    }

    public int V() {            // retorna a quantidade de Vertices do grafo
        return V;
    }

    public void addEdge(int v, int w) {         // eu estabeleço a relacao(amizades) entre os alunos
        adj[v].add(w);                          // atribuindo na lista de V o vertice W
        adj[w].add(v);                          // atribuindo na lista de W o vertice V
    }

    public Iterable<Integer> adj(int v) {   // metodo que vai retornar a lista dos vizinhos(amigos) do parametro
        return adj[v];
    }
}

class NonrecursiveDFS {

    private final int[] equipe;                     // vetor equipe que foi substituido no lugar do marked
    private boolean impossivel = false;             // a varaivel que vai retprmar se for impossivel

    @SuppressWarnings("unchecked")
    public NonrecursiveDFS(Graph G) {               // receber um grafo G
        int V = G.V();                              // atribui a variavel V a quantidade de vertices daquele grafo pelo metodo
        equipe = new int[V];                        // e a definir o vetor equipe de capacidade V(qntd de alunos)

        Iterator<Integer>[] adj = (Iterator<Integer>[]) new Iterator[V]; // cria um array para guardar os marcadores
        for (int v = 0; v < V; v++) {               // faz a logica do marcador ir avançar e aplicando a dfs sem perder a referencia de onde ele veio e ir voltando no backtracking
            adj[v] = G.adj(v).iterator();
        }
        Deque<Integer> stack = new ArrayDeque<>();      // instancia a pilha 
        for (int i = 0; i < V; i++) {                   // for principal que faz com q a gnt explore todos os vertices
            if (equipe[i] == 0) {                       // se a equipe atual nao estiver visitada de primeira instancia
                equipe[i] = 1;                          // marca ela na equipe 1
                stack.push(i);                          // empilha
                while (!stack.isEmpty()) {              // meu laço que vai ate a pilha estiver vazia
                    int v = stack.peek();               // atribuo o v sendo o topo da pilha

                    if (adj[v].hasNext()) {             // se esse vertice tiver um vertice adjacente
                        int w = adj[v].next();          // cria w e joga o prox vertice em w

                        if (equipe[w] == 0) {           // se esse w nao tiver sido visitado
                            equipe[w] = 3 - equipe[v];  // vou atribuir a equipe dele 3 - a equipe do atual
                            stack.push(w);              // e empilha
                        } else if (equipe[w] == equipe[v]) {    // vou fazer outra verificaçao para ver se a equipe do W é igual ao do atual que é o topo
                            impossivel = true;                  // se for a gnt encontrou um ciclo impar e para
                            return;
                        }
                    } else {                                // se nao tiver vizinho desempilha
                        stack.pop();
                    }
                }
            }
        }
    }

    public boolean isImpossivel() {                     // metodo que retorna o outro metodo impossivel
        return impossivel;
    }

    public int[] getEquipes() {                     // retorna o vetor equipe
        return equipe;
    }
}

public class Main {

    public static void main(String[] args) {
        FastScanner scanner = new FastScanner();
        String nStr = scanner.next();
        if (nStr == null) {
            return;
        }

        int n = Integer.parseInt(nStr);
        int m = scanner.nextInt();

        Graph G = new Graph(n);

        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt() - 1;
            int v = scanner.nextInt() - 1;
            G.addEdge(u, v);
        }

        NonrecursiveDFS dfs = new NonrecursiveDFS(G);

        if (dfs.isImpossivel()) {
            System.out.println("IMPOSSIBLE");
        } else {
            StringBuilder sb = new StringBuilder();
            int[] equipes = dfs.getEquipes();
            for (int i = 0; i < n; i++) {
                sb.append(equipes[i]).append(i == n - 1 ? "" : " ");
            }
            System.out.println(sb.toString());
        }
    }
}
