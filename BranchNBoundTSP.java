/*
Pucminas - Ciência da Computação - Coração Eucarístico
PAA - Manhã

Larissa Gomes Domingues 
Pedro Henrique Lima Carvalho 

Trabalho Branch and Bound e Backtracking

Código de Branch and Bound - Traveling Salesman Problem

*/

//Dependencias 
import java.util.Scanner;
import java.util.ArrayList;


class Grafo{
    public static final int INFINITO = 99999;
    public int[][] matrizAdj;
    public int numVertices;

    
    // Construtor
    // @param int p_numVertices
    public Grafo(int p_numVertices){
        this.numVertices = p_numVertices;
        this.initMatriz();
    }

    /* Método para adicionar aresta entre vértices 1 e 2
     * @param int vert1, int vert2, int val
     */
    public void adicionarAresta(int vert1, int vert2, int val){
        this.matrizAdj[vert1][vert2] = val;
    }

    public void printGrafo(){
        System.out.print("   ");
        for(int i = 0; i < this.numVertices; i++)
            System.out.print(i + "  ");

        System.out.println("");
        for(int i = 0; i < this.numVertices; i++){
            System.out.print(i + " ");

            for(int j = 0; j < this.numVertices; j++){
                System.out.print(this.matrizAdj[i][j] + " ");
            }
            System.out.println("");
        }
    }

    /* Método para inicializar matriz de adjacência
     * que irá representar o grafo
     */
    private void initMatriz(){
        this.matrizAdj = new int[this.numVertices][this.numVertices];
        
        //Inicializando grafo sem arestas
        for(int i = 0; i < this.numVertices; i++){
            for(int j = 0; j < this.numVertices; j++)
                this.matrizAdj[i][j] = INFINITO;
        }
    }
};

/* Classe para representar nós da árvore 
 * para encontrar o resultado utilizando a abordagem 
 * Branch and Bound
 */
class No{
    public int[][] matrizReduzida;
    public int[] caminho;
    public int numVertices, reducao, atual, anterior;
    
    // Construtor vazio
    public No(){
        this.reducao = Grafo.INFINITO;
    }

    /* Construtor Nos intermediários e folhas
     * @param int p_numVertices, int[][] p_matrizAdj, int[] p_caminho, int p_atual, int p_anterior
     */
    public No(int p_numVertices, int[][] p_matrizAdj, int[] p_caminho, int p_atual, int p_anterior){
        this.numVertices = p_numVertices;
        this.reducao = 0;
        this.caminho = p_caminho;
        this.atual = p_atual;
        this.anterior = p_anterior;
        // Inicializar matriz de redução e realizar calculo de redução
        this.initMatriz(p_matrizAdj, p_atual, p_anterior);
    }

    /* Construtor No raiz
     * @param int p_numVertices, int[][] p_matrizAdj, int[] p_caminho, int p_atual
     */
    public No(int p_numVertices, int[][] p_matrizAdj, int[] p_caminho, int p_atual){
        this.numVertices = p_numVertices;
        this.reducao = 0;
        this.caminho = p_caminho.clone();        
        this.atual = p_atual;
        // Inicializar matriz de redução e realizar calculo de redução
        this.initMatriz(p_matrizAdj);
    }

    public void printMatriz(){
        System.out.print("   ");
        for(int i = 0; i < this.numVertices; i++)
            System.out.print(i + "  ");

        System.out.println("");
        for(int i = 0; i < this.numVertices; i++){
            System.out.print(i + " ");

            for(int j = 0; j < this.numVertices; j++){
                if(this.matrizReduzida[i][j] != Grafo.INFINITO)
                    System.out.print(this.matrizReduzida[i][j] + " ");
                else
                    System.out.print("I ");
            }
            System.out.println("");
        }
    }

    public void printNo(){
        System.out.print("Menor caminho: \n");
        System.out.print("   0");

        for(int i = 1; i < this.numVertices; i++)
            System.out.print(" - " + this.caminho[i]);

        System.out.println("\nSoma dos pesos = " + this.reducao);

    }

    /* Método para inicializar matriz de redução e calcular redução -> No Raiz
     * @param int[][] matrizAdj
     */
    private void initMatriz(int[][] matrizAdj){
        this.matrizReduzida = new int[this.numVertices][this.numVertices];
        
        // Copiando matriz
        for(int i = 0; i < this.numVertices; i++){
            for(int j = 0; j < this.numVertices; j++)
                this.matrizReduzida[i][j] = matrizAdj[i][j];
        }
        // Redução por linha
        reduzirLinha();
        // Redução por coluna
        reduzirColuna();
    }

    /* Método para inicializar matriz de redução e calcular redução -> Nos
     * folha e intermediários
     * @param int[][] matrizAdj, int p_atual, int p_anterior
     */
    private void initMatriz(int[][] matrizAdj, int p_atual, int p_anterior){
        this.matrizReduzida = new int[this.numVertices][this.numVertices];
        
        // Copiando matriz
        for(int i = 0; i < this.numVertices; i++){
            for(int j = 0; j < this.numVertices; j++)
                if(j != p_atual && i != p_anterior)
                    this.matrizReduzida[i][j] = matrizAdj[i][j];
                else // linha p_atual e coluna p_anterior = inifinito -> excluir possíveis caminhos
                   this.matrizReduzida[i][j] = Grafo.INFINITO; 
        }

        this.matrizReduzida[p_atual][p_anterior] = Grafo.INFINITO; 
        
        // Redução por linha
        reduzirLinha();
        // Redução por coluna
        reduzirColuna();
    }

    /* Método para reduzir linhas de matrizReduzida
     */
    private void reduzirLinha(){
        // Variável para armazenar menor valor de cada linha
        int menorLinha = Grafo.INFINITO;

        for(int i = 0; i < this.numVertices; i++){
            // Encontrar menor valor de linha i
            for(int j = 0; j < this.numVertices; j++)
                if(menorLinha > this.matrizReduzida[i][j])
                    menorLinha = this.matrizReduzida[i][j];
            // Reduzir menor valor de todos elementos da linha i
            for(int j = 0; j < this.numVertices; j++){
                if(this.matrizReduzida[i][j] != Grafo.INFINITO) // Não reduzir elementos infinitos
                    this.matrizReduzida[i][j] -= menorLinha;
            }
            // Redução =  Redução + menor elemento da linha i
            if(menorLinha != Grafo.INFINITO) // Não considerar redução de elementos infinitos
                this.reducao += menorLinha;
            // Limpar menor elemento para próxima iteração
            menorLinha = Grafo.INFINITO;
        }
    }

    /* Método para reduzir colunas de matrizReduzida
     */
    private void reduzirColuna(){
        // Variável para armazenar menor valor de cada coluna
        int menorColuna = Grafo.INFINITO;

        for(int i = 0; i < this.numVertices; i++){
            // Encontrar menor valor de coluna i
            for(int j = 0; j < this.numVertices; j++)
                if(menorColuna > this.matrizReduzida[j][i])
                    menorColuna = this.matrizReduzida[j][i];

            // Reduzir menor valor de todos elementos da coluna i
            for(int j = 0; j < this.numVertices; j++){
                if(this.matrizReduzida[j][i] != Grafo.INFINITO)
                    this.matrizReduzida[j][i] -= menorColuna; // Não reduzir elementos infinitos
            }
            // Redução =  Redução + menor elemento da coluna i
            if(menorColuna != Grafo.INFINITO) // Não considerar redução de elementos infinitos
                this.reducao += menorColuna;
            // Limpar menor elemento para próxima iteração
            menorColuna = Grafo.INFINITO;
        }
    }
}

/*
 * Classe Principal - BranchNBoundTSP
 */
public class BranchNBoundTSP{
    /* Método recursivo para fazer busca em profundidade na árvore de possíveis
     * soluções para o problema de caixeiro viajante de um grafo.
     * @param No pai, ArrayList<Integer> naoVisitados, int atual, int anterior, 
     *        int[] caminho, int nivel, No menorCaminhoAtual
     * @return No -> menor caminho para candidato a solução recebido como parâmento
     */
    public static No TSPRec(No pai, ArrayList<Integer> naoVisitados, int atual, int anterior, 
                             int[] caminho, int nivel, No menorCaminhoAtual){
        No aux;
        // Inciando No de menor caminho como vazio
        No menorCaminho = new No();
        int caminhoAtual[] = caminho.clone();
        // Adicionando vertice atual ao vetor de caminho
        caminhoAtual[nivel] = atual;

        // Iniciando No atual da árvore de possíveis soluções
        No n = new No(pai.numVertices, pai.matrizReduzida, caminhoAtual, atual, anterior);
        // Calculo da redução -> A redução irá sinalizar o menor caminho possível
        // para esse estado, o caminho final poderá ser maior que a redução
        n.reducao = n.reducao + pai.reducao + pai.matrizReduzida[anterior][atual];

        // Se array de não visitados estiver vazio -> No atual = folha
        // Resultado para o caminho atual foi encontrado
        if(naoVisitados.isEmpty()){
            // Se menorCaminho calculado pelo No atual < menor caminho atual
            // atualizar menorCaminho atual
            return menorCaminho = menorCaminhoAtual.reducao < n.reducao ? menorCaminhoAtual : n;
        } else{
            // Se menor caminho possível calculado pela redução for menor que 
            // algum menor caminho já encontrado -> quebrar recursão
            if(n.reducao > menorCaminhoAtual.reducao)
                return n;

            // Número de vértices ainda não visitados
            int numVerticesRestantes = n.numVertices - nivel - 1;
            // Visitar Nos não visitados na árvore de possíveis soluções 
            for(int i = 0; i < numVerticesRestantes; i++){
                // Escolher um vértice não visitado
                int prox = naoVisitados.remove(0);

                // Chamando método recursivo para solucionar Caixeiro viajante
                aux = TSPRec(n, naoVisitados, prox, atual, caminhoAtual, nivel + 1, menorCaminhoAtual);
                // Se menorCaminho calculado pelo No atual < menor caminho atual
                // atualizar menorCaminho atual
                menorCaminho = menorCaminho.reducao < aux.reducao ? menorCaminho : aux;
                // Adicionar vértice escolhido em novamente em naoVistados, para
                // que eles seja visitado em próximos nós 
                naoVisitados.add(prox);
            }
        }

        return menorCaminho;
    }

    /* Método para resolver o problema do caixeiro viajante em
     * um grafo a partir de uma abordagem que utiliza um algoritmo
     * Branch and Bound
     * @param Grafo g
     * @return No contendo menor caminho
     */
    public static No TSP(Grafo g){
        // Inciando No de menor caminho como vazio
        No menorCaminho = new No();
        No aux;

        // Array para armazenar caminhos
        int[] caminhoAtual = new int[g.numVertices + 1];

        // Vertice inicial -> 0
        caminhoAtual[0] = 0;

        // Controle de vertices não visitados
        ArrayList<Integer> naoVisitados = new ArrayList();
        
        // Inicando vetor de vertices de vértices não visitados
        for(int i = 1; i < g.numVertices; i++)
            naoVisitados.add(i);
        // Iniciando No raiz
        No n = new No(g.numVertices, g.matrizAdj, caminhoAtual, 0);

        // Variável para armzenar próximo vértice a ser visitado na estrutura de 
        // árvore que enumera possíveis soluções
        int prox;
        
        for(int i = 0; i < g.numVertices; i++) {
            // Escolher um vértice não visitado
            prox = naoVisitados.remove(0);
            // Chamando método recursivo para solucionar Caixeiro viajante
            aux = TSPRec(n, new ArrayList(naoVisitados), prox, 0, caminhoAtual, 1, menorCaminho);
            // Se menorCaminho calculado pelo método recursivo < menor caminho atual
            // atualizar menorCaminho atual
            menorCaminho = menorCaminho.reducao < aux.reducao ? menorCaminho : aux;
            
            // Adicionar vértice escolhido em novamente em naoVistados, para
            // que eles seja visitado em próximos nós 
            naoVisitados.add(prox);
        }

        return menorCaminho;
    }

    /*
    main
    */
    public static void main(String[] args){
        Scanner reader = new Scanner(System.in);
        int numVertices, numAretas, v1, v2, valor;

        System.out.print("Número de vértices: ");
        numVertices = reader.nextInt();

        System.out.print("Número de arestas: ");
        numAretas = reader.nextInt();

        // Criando grafo
        Grafo g = new Grafo(numVertices);

        // Repetição para adicionar arestas no grafo g 
        for(int i = 0; i < numAretas; i++){
            System.out.println("Aresta [" + (i+1) + "]");
            
            // Leitura vértice 1 da aresta i
            System.out.print("Vértice 1: ");
            v1 = reader.nextInt();

            // Leitura vértice 2 da aresta i
            System.out.print("Vértice 2: ");
            v2 = reader.nextInt();

            // Leitura peso da aresta v1 - v2
            System.out.print("Peso: ");
            valor = reader.nextInt();

            // Adicionando aresta entre v1 e v2
            g.adicionarAresta(v1, v2, valor);
        }

       TSP(g).printNo();
    }  
}