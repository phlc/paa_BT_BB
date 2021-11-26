/*
Pucminas - Ciência da Computação - Coração Eucarístico
PAA - Manhã

Larissa Gomes Domingues 
Pedro Henrique Lima Carvalho 

Trabalho Branch and Bound e Backtracking

Codigo de Backtracking - N-Queens
Encontrar em um tabuleiro NxN de quantas maneiras diferentes N rainhas podem ser posicionadas
de forma quem uma não ataque a outra

*/

//Dependencias 
import java.util.Scanner;


/*
Classe Principal - BacktrackingNQueens
*/
public class BacktrackingNQueens{
    /*
    nQueens - Calcula de quantas maneiras diferentes N rainhas podem ser posicionada no tabuleiro NxN
    sem quem uma ataque a outra
    @param int n
    @return int quantidade
    */
    public static int nQueens(int n){
        //Declaracoes
        int quantidade = 0;
        int queens = n;

        //Criar tabuleiro
        int[][] tabuleiro = new int[n][n];

        //Iniciar colocacao das rainhas
        for(int i=0; i<n; i++){
                //Colocar 1 rainha 1 linha
                tabuleiro[0][i]=1;
                queens--;

                //proximo nivel
                quantidade += nQueens(tabuleiro, queens);

                //desfazer
                queens++;
                tabuleiro[0][i]=0;
        }

        return quantidade;
    }

    /*
    nQueens - Oveload
    @param int[][] t, int q
    @return int q
    */
    public static int nQueens(int[][] t, int q){
        //Declaracoes
        int quantidade = 0;
        int n = t.length;
        
        //caso base - todas rainhas colocadas
        if(q==0){

            //Mostrar tabuleiros
            for(int i=0; i<t.length; i++){
                for(int j=0; j<t.length; j++){
                    System.out.print(t[i][j]);
                }
                System.out.println();
            }
            System.out.println();

            quantidade = 1;
        }

        //recorrencia
        else{
            //colocar outra rainhas
            for(int i=0; i<n; i++){
                //verificar se posicao nova posicao para a rainha é valida
                if(check(t, n-q, i)){
                    //chamar funcao recursiva
                    quantidade += nQueens(t, --q);

                    //desfazer
                    q++;
                    t[n-q][i] = 0;
                    }
            }
        }

        return quantidade;
    }

    /*
    check - verifica se a posicao é valida para nova rainha
    @param int[][] t, int i, int j
    @return boolean
    */
    public static boolean check(int[][] t, int i, int j){
        //Declaracoes
        boolean r = false;
        int n = t.length;

        //testar se posicao vazia
        if(t[i][j]==0){
            r = true;
            t[i][j] = 1;
        }

        //testar linha
        for(int k=j+1; r && k%n!=j; k++){
            if(t[i][k%n]!=0){
                r=false;
                t[i][j] = 0;
            }
        }

        //testar coluna
        for(int k=i+1; r && k%n!=i; k++){
            if(t[k%n][j]!=0){
                r=false;
                t[i][j] = 0;
            }
        }

        //testar diagonais
        for(int k=i+1, l=j+1; k<n && l<n; k++, l++){
            if(t[k][l]!=0){
                r=false;
                t[i][j] = 0;
            }
        }
        for(int k=i-1, l=j-1; k>=0 && l>=0; k--, l--){
            if(t[k][l]!=0){
                r=false;
                t[i][j] = 0;
            }
        }
        for(int k=i+1, l=j-1; k<n && l>=0; k++, l--){
            if(t[k][l]!=0){
                r=false;
                t[i][j] = 0;
            }
        }
        for(int k=i-1, l=j+1; k>=0 && l<n; k--, l++){
            if(t[k][l]!=0){
                r=false;
                t[i][j] = 0;
            }
        }

        return r;
    }

    /*
    main
    */
    public static void main(String[] args){
        //Declaracoes
        Scanner reader = new Scanner(System.in);
        int n = 0;

        //Ler n
        System.out.print("N (0 para sair): ");
        n = reader.nextInt();

        //Repetir até entrada == 0
        while(n!=0){
            System.out.println();
            System.out.println("Número de arranjos possíveis: "+nQueens(n));

            System.out.print("N (0 para sair): ");
            n = reader.nextInt();
        }

        //Encerramento
        reader.close();

    }  
}