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

    /* Metodo para adicionar aresta entre vertice 1 e 2
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

    /* Método para inicializar matriz de adjacencia
     * que ira representar o grafo
     * O(n^2) sendo n = this.numVertice 
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

    public No(int p_numVertices, int[][] p_matrizAdj, int[] p_caminho, int p_atual, int p_anterior){
        this.numVertices = p_numVertices;
        this.reducao = 0;
        this.caminho = p_caminho;
        this.atual = p_atual;
        this.anterior = p_anterior;

        this.initMatriz(p_matrizAdj, p_atual, p_anterior);
    }

    public No(int p_numVertices, int[][] p_matrizAdj, int[] p_caminho, int p_atual){
        this.numVertices = p_numVertices;
        this.reducao = 0;
        this.caminho = p_caminho;
        this.atual = p_atual;

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

    private void initMatriz(int[][] matrizAdj){
        this.matrizReduzida = new int[this.numVertices][this.numVertices];
        
        // Copiando matriz
        for(int i = 0; i < this.numVertices; i++){
            for(int j = 0; j < this.numVertices; j++)
                this.matrizReduzida[i][j] = matrizAdj[i][j];
        }

        reduzirLinha();
        reduzirColuna();
    }

    private void initMatriz(int[][] matrizAdj, int p_atual, int p_anterior){
        this.matrizReduzida = new int[this.numVertices][this.numVertices];
        
        // Copiando matriz
        for(int i = 0; i < this.numVertices; i++){
            for(int j = 0; j < this.numVertices; j++)
                if(j != p_atual && i != p_anterior)
                    this.matrizReduzida[i][j] = matrizAdj[i][j];
                else
                   this.matrizReduzida[i][j] = Grafo.INFINITO; 
        }

        this.matrizReduzida[p_atual][p_anterior] = Grafo.INFINITO; 

        reduzirLinha();
        reduzirColuna();
    }

    private void reduzirLinha(){
        int menorLinha = Grafo.INFINITO;
        for(int i = 0; i < this.numVertices; i++){
            for(int j = 0; j < this.numVertices; j++)
                if(menorLinha > this.matrizReduzida[i][j])
                    menorLinha = this.matrizReduzida[i][j];

            for(int j = 0; j < this.numVertices; j++){
                if(this.matrizReduzida[i][j] != Grafo.INFINITO)
                    this.matrizReduzida[i][j] -= menorLinha;
            }

            if(menorLinha != Grafo.INFINITO)
                this.reducao += menorLinha;

            menorLinha = Grafo.INFINITO;
        }
    }

    private void reduzirColuna(){
        int menorColuna = Grafo.INFINITO;
        for(int i = 0; i < this.numVertices; i++){
            for(int j = 0; j < this.numVertices; j++)
                if(menorColuna > this.matrizReduzida[j][i])
                    menorColuna = this.matrizReduzida[j][i];

            for(int j = 0; j < this.numVertices; j++){
                if(this.matrizReduzida[j][i] != Grafo.INFINITO)
                    this.matrizReduzida[j][i] -= menorColuna;
            }

            if(menorColuna != Grafo.INFINITO)
                this.reducao += menorColuna;
           
            menorColuna = Grafo.INFINITO;
        }
    }
}

/*
Classe Principal - BranchNBoundTSP
*/
public class BranchNBoundTSP{

    public static int TSPRec(No pai, ArrayList<Integer> naoVisitados, int atual, int anterior, 
                             int[] caminhoAtual, int nivel, int menorCaminhoAtual){
        int aux;
        int menorCaminho = Grafo.INFINITO;

        caminhoAtual[nivel] = atual;
        No n = new No(pai.numVertices, pai.matrizReduzida, caminhoAtual, atual, anterior);
        n.reducao = n.reducao + pai.reducao + pai.matrizReduzida[anterior][atual];

        //System.out.println(n.reducao );
        
        if(naoVisitados.isEmpty()){
            return menorCaminho = menorCaminho < n.reducao ? menorCaminho : n.reducao;
        } else{
            for(int i = 0; i < n.numVertices - nivel - 1; i++){
                int prox = naoVisitados.remove(0);
                aux = TSPRec(n, naoVisitados, prox, atual, caminhoAtual, nivel + 1, menorCaminhoAtual);

                menorCaminho = menorCaminho < aux ? menorCaminho : aux;
                naoVisitados.add(prox);
            }
        }

        return menorCaminho;
    }

    public static int TSP(Grafo g){
        int menorCaminho = Grafo.INFINITO;
        int aux = 0;
        int[] caminhoAtual = new int[g.numVertices + 1];
        caminhoAtual[0] = 0;
        ArrayList<Integer> naoVisitados = new ArrayList();
        

        for(int i = 1; i < g.numVertices; i++)
            naoVisitados.add(i);

        No n = new No(g.numVertices, g.matrizAdj, caminhoAtual, 0);
       // n.printMatriz();
       
        int prox;
        for(int i = 0; i < g.numVertices; i++) {
            prox = naoVisitados.remove(0);
            aux = TSPRec(n, new ArrayList(naoVisitados), prox, 0, caminhoAtual, 1, menorCaminho);
            menorCaminho = menorCaminho < aux ? menorCaminho : aux;
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

        // System.out.print("Número de vértices: ");
        numVertices = reader.nextInt();

        //System.out.print("Número de arestas: ");
        numAretas = reader.nextInt();

        // Criando grafo
        Grafo g = new Grafo(numVertices);

        // Repetição para adicionar arestas no grafo g 
        for(int i = 0; i < numAretas; i++){
          //  System.out.println("Aresta [" + (i+1) + "]");
            
            // Leitura vértice 1 da aresta i
          //  System.out.print("Vértice 1: ");
            v1 = reader.nextInt();

            // Leitura vértice 2 da aresta i
            //System.out.print("Vértice 2: ");
            v2 = reader.nextInt();

            // Leitura peso da aresta v1 - v2
            //System.out.print("Peso: ");
            valor = reader.nextInt();

            // Adicionando aresta entre v1 e v2
            g.adicionarAresta(v1, v2, valor);
        }

       // g.printGrafo();
       //TSP(g);
       System.out.println(TSP(g));
    }  
}