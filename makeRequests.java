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

public class makeRequests {
	public static void main(String[] args) throws Exception {
		boolean keep = true;
		while (keep) {
			for (int j = 0; j < Integer.parseInt(args[2]); j++) {
                 	       try {
       		                        String url = "http://146.193.41."+args[0] + ":" + args[1] + "/" + args[3];
                                        URL obj = new URL(url);
                                        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                                        int responseCode = con.getResponseCode();
                                                //must return IP where the host was scheduled
                                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())); 
					String inputLine;
					StringBuffer response = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					} 
					System.out.println("Time " + response);
                                }catch(Exception e) {
					keep = false;
					break;					
                                }
                        }
			if (keep)
				Thread.sleep(100000);
		}
	}
}
