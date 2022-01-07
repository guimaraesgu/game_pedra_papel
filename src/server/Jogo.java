package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Rock, Paper, Scissor, Lizard, Spock Game
 * @author gustavoguimaraes
 *
 */
public class Jogo {
	public static final int ROCK = 1;
	public static final int SCISSOR = 2;
	public static final int PAPER  = 3;
	public static final int LIZARD = 4;
	public static final int SPOCK  = 5;
	
	public Jogador jogadorDaVez;
	public static int[] jogadas;
	private static String result;
	
	public Jogo(){
		jogadas = new int[3];
		jogadas[0] = 0;
		setResult("");
	}
	
	public void broadcastResult(){
			jogadorDaVez.mensagemSaida.println(getResult());
			jogadorDaVez.oponente.mensagemSaida.println(getResult());
	}
	
	public synchronized boolean jogada(Jogador jogador, int jogada){
		if(jogador.idJogador == jogadorDaVez.idJogador && jogadaValida(jogada)){
			jogadas[0]++;
			if(jogador.idJogador == "Jogador1"){
				jogadas[1] = jogada;
			}else{
				jogadas[2] = jogada;
			}
			jogadorDaVez = jogador.oponente;
			if(jogadas[0] == 2) {
				gameEngine(jogadas[1], jogadas[2]);
				broadcastResult();
				jogadas[0] = 0;
			}
			return true;
		}
		return false;
	}
	
	private static boolean jogadaValida(int jogada){
		if(jogada == 1 || jogada == 2 || jogada == 3 || jogada == 4 || jogada == 5) return true;
		return false;
	}
	
	public static void gameEngine(int p1, int p2){
		if( p1 == 1 && p2 == 1 || p1 == 2 && p2 == 2 || 
				p1 == 3 && p2 == 3 || p1 == 4 && p2 == 4 || p1 == 5 && p2 == 5 ){
			result = "TIE";
		}
		else if ( p1 == 1 && p2 == 2 || p1 == 2 && p2 == 1 ){
			result = "RESULT: Rock Crushes Scissor";
		}
		else if ( p1 == 1 && p2 == 3 || p1 == 3 && p2 == 1){
			result = "RESULT: Paper Covers Rock";
		}
		else if ( p1 == 1 && p2 == 4 || p1 == 4 && p2 == 1){
			result = "RESULT: Rock Crushes Lizard";
		}
		else if ( p1 == 1 && p2 == 5 || p1 == 5 && p2 == 1){
			result = "RESULT: Spock Vaporizes Rock";
		}
		else if ( p1 == 2 && p2 == 3 || p1 == 3 && p2 == 2){
			result = "RESULT: Scissor Cuts Paper";
		}
		else if ( p1 == 2 && p2 == 4 || p1 == 4 && p2 == 2){
			result = "RESULT: Scissor Decapitates Lizard";
		}
		else if ( p1 == 2 && p2 == 5 || p1 == 5 && p2 == 2){
			result = "RESULT: Spock Smashes Scissor";
		}
		else if ( p1 == 3 && p2 == 4 || p1 == 4 && p2 == 3){
			result = "RESULT: Lizard Eats Paper";
		}
		else if ( p1 == 3 && p2 == 5 || p1 == 5 && p2 == 3){
			result = "RESULT: Paper Disproves Spock";
		}
		else if ( p1 == 4 && p2 == 5 || p1 == 5 && p2 == 4){
			result = "RESULT: Lizard Poisons Spock";
		}
	}

	public String getResult() {
		return result;
	}

	public static void setResult(String result) {
		Jogo.result = result;
	}

	/**
	 * CLASSE JOGADOR
	 * @author gustavoguimaraes
	 *
	 */
	public class Jogador extends Thread {
		public Jogador oponente;
		public Socket socket;
		public BufferedReader leitorJogada;
		public PrintWriter mensagemSaida;
		public String idJogador;
		public boolean jogou;
		public int jogada;
		public int idSala;
		
		public Jogador(Socket socket, String idJogador, int idSala){
			this.socket = socket;
			this.idJogador = idJogador;
			this.idSala = idSala;
			
			try {
				mensagemSaida = new PrintWriter(socket.getOutputStream(), true);
				leitorJogada  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				mensagemSaida.println("SERVER: WELCOME " + idJogador.toUpperCase() + 
										". YOU'RE CONNECTED IN ROOM "+ idSala + ".");
				mensagemSaida.println("SERVER: WAITING FOR OPPONENT");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		public void setOponente(Jogador oponente){
			this.oponente = oponente;
		}
		
		public void run(){
			
			mensagemSaida.printf("SERVER: ALL PLAYERS CONNECTED.\nLETS PLAY!\n");
			mensagemSaida.println("SERVER: OPTIONS (ENTER THE DESIRED NUMBER)");
			mensagemSaida.println("SERVER: 1. ROCK");
			mensagemSaida.println("SERVER: 2. PAPER");
			mensagemSaida.println("SERVER: 3. SCISSOR");
			mensagemSaida.println("SERVER: 4. LIZARD");
			mensagemSaida.println("SERVER: 5. SPOCK");
			mensagemSaida.println("SERVER: 0. EXIT");
			
			while(true){
				try {
					jogadorDaVez.mensagemSaida.println("SERVER: " + jogadorDaVez.idJogador + " MAKE YOUR MOVE.");
					
					jogada = Integer.parseInt(leitorJogada.readLine());
					if(jogada(this, jogada)){
						;
					}else if(jogada == 0){
						mensagemSaida.println("QUIT");
						jogadorDaVez.mensagemSaida.println(this.idJogador + " saiu do jogo.");
						jogadorDaVez.oponente.mensagemSaida.println(this.idJogador + " saiu do jogo.");
						return;
					}else{
						mensagemSaida.println("NOT_VALID");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}