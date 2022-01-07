package client;

import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

/**
 * 
 * @author gustavoguimaraes
 *
 */
public class Client implements KeyListener{
	private Socket socket;
	String IPServidor = "127.0.0.1";
	int PortaServidor = 7000;
	BufferedReader entrada;
	PrintWriter saida;
	String jogada;
	
	//GUI
	JFrame frame = new JFrame("JANKENPO LIZARD SPOCK GAME");
	JTextField txtField = new JTextField(50);
	JTextArea  serverMessageArea = new JTextArea(20,50);
	JLabel txtFieldLabel = new JLabel("Command:");
	
	
	//MÉTODO PRINCIPAL DA CLASSE
	public Client() throws Exception{
		socket  = new Socket(IPServidor, PortaServidor);
		entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		saida   = new PrintWriter(socket.getOutputStream(),true); 
		txtField.setEditable(true);
        serverMessageArea.setEditable(false);
        frame.getContentPane().add(txtFieldLabel, "West");
        frame.getContentPane().add(txtField, "South");
        frame.getContentPane().add(new JScrollPane(serverMessageArea), "North");
        frame.pack();
        
        txtField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				saida.println(txtField.getText());
				txtField.setText("");
			}
        });
	}
	public void Play(){
		String respostaServidor;
		try {
			while(true){
				respostaServidor = entrada.readLine();
				if(respostaServidor.startsWith("VALID")){
					serverMessageArea.append("JOGADA VALIDA.\n");
				}else if(respostaServidor.startsWith("NOT_VALID")){
					serverMessageArea.append("JOGADA INVALIDA\n");
				}else if(respostaServidor.startsWith("TIE")){
					serverMessageArea.append("YOU TIED\n");
				}else if(respostaServidor.startsWith("SERVER")){
					serverMessageArea.append(respostaServidor.substring(8) + "\n");
				}else if(respostaServidor.startsWith("RESULT")){
					serverMessageArea.append(respostaServidor.substring(8) + "\n");
				}else if(respostaServidor.startsWith("QUIT")){
					socket.close();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception{
		while(true){
			Client cliente = new Client();
			cliente.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        cliente.frame.setVisible(true);
			cliente.Play();
		}
	}



	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}		