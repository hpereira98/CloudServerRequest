import java.util.*;
import java.io.*;
import java.util.concurrent.locks.*;
import java.net.Socket;

public class Bidcheck implements Runnable{
	private Servidor s;
	private PrintWriter out;

	public Bidcheck(Servidor s,PrintWriter out){
		this.s = s;
		this.out = out;
	}

	public void run(){

		try{
			boolean valid = s.warningAfterBid();

			if (valid){
				this.out.println("WARNING: " + s.getID() + " WAS SOLD TO OTHER USER");
			}

		}
		catch(InterruptedException a){a.getMessage();}
	}

}