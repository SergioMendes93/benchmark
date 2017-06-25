import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.net.URL;
import java.net.URLConnection;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.io.*;
import java.net.*;
import java.util.*;

public class launchBenchmarks {
	private static void readTraces() {
                try {
	                BufferedReader br = new BufferedReader(new FileReader("traces.txt"));
                        String line = br.readLine();

                        while (line != null) {
                                String[] parts = line.split(",");
                                String[] makespan = parts[0].split(":");
                                String[] cpu = parts[1].split(":");
                                String[] memory = parts[2].split(":");
                                String[] requestClass = parts[3].split(":");
                                String[] requestRate = parts[4].split(":");

                                //make the request to the scheduler
                                Thread t = new ThreadA(makespan[1], cpu[1], memory[1], requestClass[1], requestRate[1]);
                                t.start();

                                line = br.readLine();
                        }
                        br.close();
		} catch (Exception e) {
			System.out.println("Exception reading trace file " + e);
                } 
        }


    public static void main(String[] args) throws Exception {
	readTraces();
    }

	static class ThreadA extends Thread {
        	String makespan, cpu, memory, requestRate, requestClass;

                public ThreadA(String makespan, String cpu, String memory, String requestClass, String requestRate) {
                        this.makespan = makespan;
                        this.cpu = cpu;
                        this.memory = memory;
                        this.requestClass = requestClass;
                        this.requestRate = requestRate;
                }

                @Override
                public void run() {

                        System.out.println("Sending request class : " + requestClass + " cpu: " + cpu + " memory " + memory + " with makespan: " + makespan + " and request rate: " + requestRate);


/*


                        String url = "http://146.193.41.142:8000/entrypoint?"+cpu+"&"+memory+"&sergiomendes/java-testing&"+requestClass+"&job";
        //              String url = "http://146.193.41.142:8000/entrypoint?1024&100000000&hello-world&"+requestClass+"&job";
                        try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                int responseCode = con.getResponseCode();

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));       

                }catch(Exception e) {
                        System.out.println("Exception " + e);
                        }

                }*/
        	}
	}
}

