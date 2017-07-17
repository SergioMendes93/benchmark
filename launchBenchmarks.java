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
	static long memoryLimit = new Long("88583700480"); //88gb with 150% overbooking //59055800320; //55gb full capacity of the 5 servers
	static int cpuLimit = 61440; //150% overbooking 40960; //40960 cpu shares total
	static long memorySent = 0; //going to be used to control the workload sent to the server so it does not overload
	static int cpuSent = 0;
	static List<Long> listMakespan = new ArrayList<>(); //this is going to be used to make an average of the makespan of the workloads sent so after that average has passed we can send workload again

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
                                String[] requestType = parts[4].split(":");
				String[] requestImage = parts[5].split(":");
				String[] requestRate = parts[6].split(":");				
				String[] portNumber = parts[7].split(":");				

				memorySent += Long.parseLong(memory[1],10);
				cpuSent += Long.parseLong(cpu[1],10);
				listMakespan.add(Long.parseLong(makespan[1],10));

				if (memorySent > memoryLimit || cpuSent > cpuLimit) {
					memorySent = 0; 
					cpuSent = 0;
					long makespanAux = 0;
					int  length = listMakespan.size();
					for (Long i: listMakespan) { 
						makespanAux += i.longValue();
					}
					listMakespan.clear();
					System.out.print("List makespan size: " + listMakespan.size());
					long waitTime = makespanAux / length;
					System.out.println("Wait time: " + waitTime);
					Thread.sleep(waitTime * 1000); // *1000 because it is in miliseconds
					System.out.println("continuing");
					
				}
				
                                //make the request to the scheduler
				if (requestType[1].equals("service")) { //for services
					if (requestImage[1].equals("sergiomendes/timeserver")) {
                                		Thread t = new ThreadB(makespan[1], cpu[1], memory[1], requestClass[1], requestRate[1], requestType[1], portNumber[1], requestImage[1]);
                                		t.start();
					} else {
                                		Thread t = new ThreadB(makespan[1], cpu[1], memory[1], requestClass[1], requestRate[1], requestType[1], portNumber[1], requestImage[1]);
                                		t.start();
					}
	
				} else { //for jobs
                                	Thread t = new ThreadA(makespan[1], cpu[1], memory[1], requestClass[1], requestType[1], requestImage[1]);
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
//	readTraces();
/*	Thread t = new ThreadB("30000000", "1024", "1000000000", "4", "0", "job", Integer.parseInt(args[0]), "cpu");
        t.start();
	Thread t1 = new ThreadB("30000000", "1024", "1000000000", "4", "0", "job", Integer.parseInt(args[0]), "enhance");
        t1.start();*/
//	Thread t2 = new ThreadB("30000000", "1024", "1000000000", "4", "0", "service", Integer.parseInt(args[0]), "redis");
 //       t2.start();
 
    }
	static class ThreadA extends Thread {
        	String makespan, cpu, memory, requestClass, requestImage, requestType;

                public ThreadA(String makespan, String cpu, String memory, String requestClass, String requestType, String requestImage) {
                        this.makespan = makespan;
                        this.cpu = cpu;
                        this.memory = memory;
			this.requestImage = requestImage;
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
        	String makespan, cpu, memory, requestRate, requestClass, requestType, requestImage, portNumber;
                public ThreadB(String makespan, String cpu, String memory, String requestClass, String requestRate, String requestType, String portNumber, String requestImage) {
                        this.makespan = makespan;
			this.requestImage = requestImage;
                        this.cpu = cpu;
                        this.memory = memory;
                        this.requestClass = requestClass;
                        this.requestRate = requestRate;
			this.requestType = requestType;
			this.portNumber = portNumber;
                }

                @Override
                public void run() {
                        String url = "http://146.193.41.142:8000/entrypoint?"+cpu+"&"+memory+"&"+requestImage+"&"+requestClass+"&"+requestType+"&"+makespan+"&"+portNumber;
        //              String url = "http://146.193.41.142:8000/entrypoint?1024&100000000&hello-world&"+requestClass+"&service";
                        try {
				
                		URL obj = new URL(url);
                		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                		int responseCode = con.getResponseCode();

				//must return IP where the host was scheduled
                		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));       

				/*//TODO HOSTIP from response from scheduler
				String hostIP = "buga";

				Thread t = new ThreadMakeRequests(makespan, requestRate, hostIP, portNumber);
                                t.start();
				*/

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

