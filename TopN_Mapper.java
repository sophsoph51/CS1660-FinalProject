package topN;


	import java.io.*; 
	import java.util.*; 
	import org.apache.hadoop.io.Text;
//import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable; 
	import org.apache.hadoop.mapreduce.Mapper; 

	  
	public class TopN_Mapper extends Mapper<Object, 
	                            Text, Text, LongWritable> { 
	  
	    private TreeMap<String, Long> test;
	  
	    @Override
	    public void setup(Context context) throws IOException, 
	                                     InterruptedException 
	    { 
	   
	    	test = new TreeMap<String, Long>();
	    	//String param = conf.get("myValue"); 
	    } 
	  
	    @Override
	    public void map(Object key, Text value, Context context) throws IOException, InterruptedException 
	    { 
	    	//https://www.geeksforgeeks.org/how-to-find-top-n-records-using-mapreduce/
	    
	    	String[] tokens = value.toString().split("\t"); 
	    	String word = tokens[0];
	    	List<String> path = new ArrayList<>(Arrays.asList(tokens[1].split(" ")));
	    	
	    	long frequencies = 0;
	    	
	    	for(String s: path) {
	    		
	    			String [] num = s.split(":");
	    			frequencies += Long.parseLong(num[1]);

	    		test.put(word, frequencies);
	    	}
	   
	    		
	    	}
	    	
	    	
	  
	    @Override
	    public void cleanup(Context context) throws IOException, 
	                                       InterruptedException 
	    { 
	  
	        for (Map.Entry<String, Long> entry : test.entrySet())  
	        { 
        	
	            
	        	long count = entry.getValue();
	        	String name = entry.getKey();
    
					context.write(new Text(name), new LongWritable(count)); 
				
	        	
	        }

	   
	         
	    } 
	} 

