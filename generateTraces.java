import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.io.*;


class generateTraces {
	private static double calculateExponentialRandomNumber() {
		Random rand = new Random();
		double u = rand.nextDouble();

		int lambda = 5; 

		double result = -Math.log(1-(1-	Math.exp(-lambda)) * u) / lambda;
		return result;
	}

	//maps the value return by the exponential distribution to workload makespan (min(0) 30s max(1) 30 minutes)
	private static int mapMakespan() {
		double randomNumber = calculateExponentialRandomNumber();
		Double ms = new Double(randomNumber * 1800); //1800 because it is 30 minutes in seconds)
		
		int makespan = ms.intValue();
		if (makespan < 30)  //the minimum is 30 seconds
			makespan = 30;		
			
		return makespan;
	}
	
	//maps the value returned by the expoential distribution to workload CPU requirements (min 2 shares, max 1024 shares (100% cpu usage)	
	private static int mapCPU() {
		double randomNumber = calculateExponentialRandomNumber();
		Double ms = new Double(randomNumber * 1024);
		
		int cpu = ms.intValue();
		
		if (cpu < 2) 
			cpu = 2; //2 is minimum shares
		
		return cpu;
		
	}

	//maps the value returned by the expoential distribution to workload memory requirements (min 4mb shares, max 2gb shares 	
	private static long mapMemory() {
		double randomNumber = calculateExponentialRandomNumber();
		long maxMemory =  2147483648L; //2gb in bytes
		Double ms = new Double(randomNumber * maxMemory);
		
		long memory = ms.intValue();
		
		if (memory < 4194304) 
			memory = 4194304; //4194304 (4mb) is minimum memory
		
		return memory;
		
	}

	//maps the value returned by the expoential distribution to request rate to services (min 1 max 50 requests per second) 	
	private static long mapRequestRate() {
		double randomNumber = calculateExponentialRandomNumber();
		Double ms = new Double(randomNumber * 50);
		
		int requestRate = ms.intValue();
		return requestRate;
		
	}

	/*
	Class request distribution:
	class 1: 10% chance
	class 2: 30% chance
	class 3: 45% chance
	class 4: 15% chance	
	*/
	private static int generateRequestClass() {
		Random rand = new Random();
                double probability = rand.nextDouble();
		int requestClass;		

		if (probability < 0.1) 
			requestClass = 1;
		else if (probability < 0.4)
			requestClass = 2;
		else if (probability < 0.85)
			requestClass = 3;
		else
			requestClass = 4;
		return requestClass;
	}

	//method used to save the traces to a file so all scheduling algorithms use the same traces to benchmark
	private static void saveToFile(int makespan, int cpu, long memory, int requestClass, int requestRate) {
		String dataToSave = "makespan:" + makespan + ",cpu:" + cpu + ",memory:" + memory + ",requestClass:" + requestClass + ",requestRate:" + requestRate;

		try(FileWriter fw = new FileWriter("traces.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
    			PrintWriter out = new PrintWriter(bw))
		{
    			out.println(dataToSave);
		} catch (IOException e) {
			System.out.println("Exception writing to file: " + e);
}		}

	private static void generateTrace() {
		int makespan = mapMakespan();
		int cpu	= mapCPU();
		long memory = mapMemory();
		int requestClass = generateRequestClass();
		
		saveToFile(makespan, cpu, memory, requestClass, 0);
		
		System.out.println("First trace makespan: " + makespan + " seconds, cpu: " + cpu + " shares, memory: " + memory + " bytes, requestclass: " + requestClass);
	}
	
	public static void main(String args[]) {
//      	  while(true) {}
		generateTrace();
	}

	private static void testing() {
	HashMap<Double,Integer> hmap = new HashMap<Double,Integer>();

	for (int i = 0; i < 1000; i++) {
		double result = calculateExponentialRandomNumber();
		
		if (result < 0.1)
		{
			int count = hmap.containsKey(0.1) ? hmap.get(0.1) : 0;			
			hmap.put(0.1,++count);
		}	
		else if (result < 0.2)
		{
			
			int count = hmap.containsKey(0.2) ? hmap.get(0.2) : 0;			
			hmap.put(0.2,++count);
		}	
		else if (result < 0.3)
		{
			int count = hmap.containsKey(0.3) ? hmap.get(0.3) : 0;			
			hmap.put(0.3,++count);
		}	
		else if (result < 0.4)
		{
			int count = hmap.containsKey(0.4) ? hmap.get(0.4) : 0;			
			hmap.put(0.4,++count);
		}	
		else if (result < 0.5)
		{
			int count = hmap.containsKey(0.5) ? hmap.get(0.5) : 0;			
			hmap.put(0.5,++count);
		}	
		else if (result < 0.6)
		{
			int count = hmap.containsKey(0.6) ? hmap.get(0.6) : 0;			
			hmap.put(0.6,++count);
		}	
		else if (result < 0.7)
		{
			int count = hmap.containsKey(0.7) ? hmap.get(0.7) : 0;			
			hmap.put(0.7,++count);
		}	
		else if (result < 0.8)
		{
			int count = hmap.containsKey(0.8) ? hmap.get(0.8) : 0;			
			hmap.put(0.8,++count);
		}	
		else if (result < 0.9)
		{
			int count = hmap.containsKey(0.9) ? hmap.get(0.9) : 0;			
			hmap.put(0.9,++count);
		}	
		else if (result < 1.0)
		{
			int count = hmap.containsKey(1.0) ? hmap.get(1.0) : 0;			
			hmap.put(1.0,++count);
		}	
	}

		System.out.println("Key: 0.1 " + " Value " + hmap.get(0.1));
		System.out.println("Key: 0.2 " + " Value " + hmap.get(0.2));
		System.out.println("Key: 0.3 " + " Value " + hmap.get(0.3));
		System.out.println("Key: 0.4 " + " Value " + hmap.get(0.4));
		System.out.println("Key: 0.5 " + " Value " + hmap.get(0.5));
		System.out.println("Key: 0.6 " + " Value " + hmap.get(0.6));
		System.out.println("Key: 0.7 " + " Value " + hmap.get(0.7));
		System.out.println("Key: 0.8 " + " Value " + hmap.get(0.8));
		System.out.println("Key: 0.9 " + " Value " + hmap.get(0.9));
		System.out.println("Key: 1.0 " + " Value " + hmap.get(1.0));
   }   

	}

