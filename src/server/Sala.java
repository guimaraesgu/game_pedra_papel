package server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * 
 * @author gustavoguimaraes
 *
 */
public class Sala {
	
	public Sala(ServerSocket socktServ, int idSala){
		System.out.println("SERVIDOR JANKENPO LIZARD SPOCK, ESTÁ RODANDO NA SALA " + idSala);
		try {	
			
			Jogo jogo = new Jogo();
			
			/*
			 * CRIAÇÃO DAS CONEXÕES
			 * O CLIENTE SOLICITA CONEXÃO E O SERVER ENVIA O ACCEPT.
			 */
			Jogo.Jogador jogador1 = jogo.new Jogador(socktServ.accept(), "Jogador1", idSala);
			Jogo.Jogador jogador2 = jogo.new Jogador(socktServ.accept(), "Jogador2", idSala);
			
			//SETTA OPONENTE DO JOGADOR. 
			jogador1.setOponente(jogador2);
			jogador2.setOponente(jogador1);
			jogo.jogadorDaVez = jogador1;
			
			/**
			 * CRIAÇÃO DAS THREADS PARA CADA JOGADOR
			 * O COMANDO START INICIALIZA AS THREADS AO CHAMAR 
			 * A FUNÇÃO RUN IMPLEMENTADA NA CLASSE JOGO.JOGADOR
			 */
			jogador1.start();
			System.out.println(jogador1);
			jogador2.start();
			System.out.println(jogador2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
