import redis.clients.jedis.Jedis; 
import redis.clients.jedis.Pipeline; 


public class RedisStringJava { 
   public static void main(String[] args) { 
      //Connecting to Redis server on localhost , para fazer requests em vez de localhost vai ser o IP do host que esta a correr
      Jedis jedis = new Jedis("localhost", 9011); 
      //set the data in redis string 
//      jedis.set("tutorialname", "Redis tutorial"); 
      // Get the stored data and print it 
  //    System.out.println("Stored string in redis:: "+ jedis.get("tutorialname")); */

	/*
		Este i vai depender da memoria pedida, por exemplo 3x este valor da 5gb RAM portanto
	*/
	long fiveMega = 524288000; //500mb
	long correspondentValue = 2097151; //Value to get 500mb being consumed
	long memoryRequirement = 1073741824; 
	
	long value = (memoryRequirement * correspondentValue) / fiveMega;

	Pipeline pipeline = jedis.pipelined();
  	for (long i = 0 ; i < value; i++) {
    		pipeline.sadd("keyyy"+i, "value");
    	// you can call pipeline.sync() and start new pipeline here if you think there're so much operations in one pipeline
  	}
  	pipeline.sync();
   } 
}
