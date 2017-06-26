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
		int portNumber = 7000;
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
				String[] requestType = parts[5].split(":");				

                                //make the request to the scheduler
				if (requestType[1] == "service") { //for services
                                	Thread t = new ThreadB(makespan[1], cpu[1], memory[1], requestClass[1], requestRate[1], requestType[1], portNumber);
					portNumber++;
                                	t.start();
	
				} else { //for jobs
                                	Thread t = new ThreadA(makespan[1], cpu[1], memory[1], requestClass[1], requestType[1]);
                                	t.start();
				}
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
        	String makespan, cpu, memory, requestClass, requestType;

                public ThreadA(String makespan, String cpu, String memory, String requestClass, String requestType) {
                        this.makespan = makespan;
                        this.cpu = cpu;
                        this.memory = memory;
                        this.requestClass = requestClass;
			this.requestType = requestType;
                }

                @Override
                public void run() {
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

	//for launching services 
	static class ThreadB extends Thread {
        	String makespan, cpu, memory, requestRate, requestClass, requestType;
		int portNumber;

                public ThreadB(String makespan, String cpu, String memory, String requestClass, String requestRate, String requestType, int portNumber) {
                        this.makespan = makespan;
                        this.cpu = cpu;
                        this.memory = memory;
                        this.requestClass = requestClass;
                        this.requestRate = requestRate;
			this.requestType = requestType;
			this.portNumber = portNumber;
                }

                @Override
                public void run() {
                        String url = "http://146.193.41.142:8000/entrypoint?"+cpu+"&"+memory+"&sergiomendes/java-testing&"+requestClass+"&job";
        //              String url = "http://146.193.41.142:8000/entrypoint?1024&100000000&hello-world&"+requestClass+"&job";
                        try {
				
                		URL obj = new URL(url);
                		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                		int responseCode = con.getResponseCode();

				//must return IP where the host was scheduled
                		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));       

				//TODO HOSTIP from response from scheduler
				String hostIP = "buga";

				Thread t = new ThreadMakeRequests(makespan, requestRate, hostIP, portNumber);
                                t.start();
				

                	}catch(Exception e) {
                        	System.out.println("Exception " + e);
                        }
        	}
	}

	static class ThreadMakeRequests extends Thread {
		int makespan, requestRate, portNumber;
		String hostIP;		

		public ThreadMakeRequests(String makespan, String requestRate, String hostIP, int portNumber) {
			this.makespan = Integer.parseInt(makespan);
			this.requestRate = Integer.parseInt(requestRate);
			this.hostIP = hostIP;
			this.portNumber = portNumber;
		}

		@Override
		public void run () {
			int numWaves = makespan / 10; // number of times we send requests. Each 10 seconds
			for (int i = 0; i < numWaves; i++) {
				for (int j = 0; j < requestRate; j++) {
					try {
						String url = "http://hostIP:" + portNumber + "/timeserver";
						URL obj = new URL(url);
                                		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                                		int responseCode = con.getResponseCode();

                                		//must return IP where the host was scheduled
                                		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));  
					}catch(Exception e) {
						System.out.println("Exception at request rate: " + e);
					}
				}
			}
		}
	}
}

