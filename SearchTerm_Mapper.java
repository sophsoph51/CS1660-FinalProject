package search;

import java.io.*;

//import org.apache.hadoop.io.LongWritable;
//import java.util.*; 
import org.apache.hadoop.io.Text;
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.io.LongWritable; 
import org.apache.hadoop.mapreduce.Mapper;


public class SearchTerm_Mapper extends Mapper<Object, 
Text, Text, Text>  {
	
	//private TreeMap<String, String> test;
	  
    @Override
    public void setup(Context context) throws IOException, 
                                     InterruptedException 
    { 
    	//Configuration conf = context.getConfiguration();
        //tmap = new TreeMap<Long, String>(); 
    	//test = new TreeMap<String, String>();
    	//String param = conf.get("myValue"); 
    } 
  
    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException 
    { 
  
        
    	String[] tokens = value.toString().split("\t"); 
    	String word = tokens[0];
    	
    
    			context.write(new Text(word), new Text(tokens[1]));
    
    }
}
        	
        
  


