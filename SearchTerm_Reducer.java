package search;


	import java.io.IOException;
import java.util.HashMap;
//import java.util.Map; 
	//import java.util.TreeMap;

	import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.io.LongWritable;
//import org.apache.hadoop.io.LongWritable; 
	import org.apache.hadoop.io.Text; 
	import org.apache.hadoop.mapreduce.Reducer; 

	public class SearchTerm_Reducer  extends Reducer<Text, 
	Text, Text, Text> { 

		public static String searchterm;
		public static String param;

		@Override
		public void setup(Context context) throws IOException, 
										InterruptedException 
		{ 
			Configuration conf = context.getConfiguration();
	    	 param = conf.get("myValue"); 
	    	
	    	if(param != null && !param.isEmpty()) {
	    		searchterm = param.toString();
	    		
	    	}else {
		    	
	    		searchterm="a"; //default to top 5 if null value in parameter, otherwise will throw error if do not set a default value
	    	}
		}

		@Override
		public void reduce(Text key, Iterable<Text> values, 
		Context context) throws IOException, InterruptedException 
		{ 
	    	

		    HashMap<String,String> map = new HashMap<String,String>();

			
			String name = key.toString(); 
			
			for (Text val : values) 
			{ 
				if(name.equals(searchterm)) {
					map.put( name, val.toString());

				}
			}
			
			StringBuilder docValueList = new StringBuilder();
		    for(String docID : map.keySet()){
		      docValueList.append(map.get(docID) + "");
		      context.write(new Text(name), new Text(docValueList.toString())); 
		    }
					

			
				}
		
	
		} 


