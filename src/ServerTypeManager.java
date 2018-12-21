import java.util.*;
import java.util.concurrent.locks.*;

public class ServerTypeManager{

	private Condition livre;
	private Lock l;
	private String type;
	private int n_servidores_livres;
	private int n_servidores_leilões;
	private Map<String, Servidor> servers;
	private Queue<Licitacao> queue;

	public ServerTypeManager(Map<String, Servidor> servers, String type, int size){
		this.l = new ReentrantLock();
		this.livre = this.l.newCondition();
		this.n_servidores_livres = size;
		this.n_servidores_leilões = 0;
		this.servers = servers;
		this.type = type;
		this.queue = new PriorityQueue<>();
	}

	public double libertar(String n_server){
		Servidor server_to_free;
		double price = 0;
		try{
			l.lock();
			server_to_free = this.servers.get(n_server);
			price = server_to_free.freeServer();
			this.n_servidores_livres++;
			livre.signalAll();
		}
		finally{l.unlock();}
		System.out.println(price);
		return price;
	}

	public String adquirir(double price, String owner){
		String server_id = "";
		Servidor server = null;
		try{
			l.lock();
			while(n_servidores_livres == 0 && n_servidores_leilões == 0)
				livre.await();

			//implementar tirar dos leiloes
			n_servidores_livres--;

			for(Servidor s : servers.values()){
				if (s.getStatus() == 0){
					server = s;
					break;
				}
			}

			server.buyServer(price,owner);
			server_id = server.getID();
		}
		catch(InterruptedException e){}
		finally{
			l.unlock();
		}

		return server_id;
	}


    /**
     * Método que retorna o tipo do
     * @return
     */
	public String getType(){
		return this.type;
	}


    /**
     * Método que retorna todos os servidores ligados ao SMT
     * @return lista dos servidores
     */
	public Collection<Servidor> getServers(){
		return this.servers.values();
	}
}
