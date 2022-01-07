package server;

import java.net.ServerSocket;

/**
 * 
 * @author gustavoguimaraes
 *
 */
public class Server {

	int PortaServidor = 7000;
	ServerSocket socktServ;
	private static Server server;
	Sala[] sala = new Sala[3];
	public Server(){
		try{
			//INICIALIZA O SERVIDOR.
			socktServ = new ServerSocket(PortaServidor);
			
			while(true){
				for(int i = 0; i < 3; i++){
					int idSala = i+1;
					sala[i] = new Sala(socktServ, idSala);
				}
			}
		}
		catch(Exception e) //SE OCORRER ALGUMA EXCESSÃO, ENTÃO DEVE SER TRATADA (AMIGAVELMENTE)
		{
			System.out.println(" -S- O seguinte problema ocorreu : \n" + e.toString());
		}
		/*finally{
			try {
				socktServ.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
	}
	
	public static void main(String[] args){
		setServer(new Server());
	}

	public static Server getServer() {
		return server;
	}

	public static void setServer(Server server) {
		Server.server = server;
	}
}

