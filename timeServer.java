import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.net.URL;
import java.net.URLConnection;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class timeServer {
	public static void main(String[] args) throws Exception{
	 	HttpServer server = HttpServer.create(new InetSocketAddress(Integer.parseInt(args[0])), 0);
	 //	HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        	server.createContext("/timeserver", new MyHandler());
        	server.setExecutor(null); // creates a default executor
		server.start(); 
    	}

	static class MyHandler implements HttpHandler {
    	//este metodo e quem trata dos requests 
 	       @Override
        	public void handle(HttpExchange t) throws IOException {     
           	 	//cada request recebido sera tratado por uma thread diferente, passamos como parametro, o request recebido
           		Thread thread = new MyThread(t);
           		thread.start();         
        	}
	}

	static class MyThread extends Thread {
    		HttpExchange t;

    		public MyThread(HttpExchange t)
    		{
    			this.t = t;
    		}
    		//codigo corrido quando fazemos start na thread
    		@Override
    		public void run() 
    		{			
			Calendar cal = Calendar.getInstance();
        		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

			String response = "Current time: " + sdf.format(cal.getTime());

			try {
            			t.sendResponseHeaders(200, response.length());
            			OutputStream os = t.getResponseBody();
            			os.write(response.getBytes());
            			os.close();
			}catch (Exception e) {
				System.out.println("Time server response exception: " + e);
			}
    		}
	}
}
