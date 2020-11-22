package topN;


	import java.io.IOException;
//import java.util.HashMap;
import java.util.Map; 
	import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable; 
	import org.apache.hadoop.io.Text; 
	import org.apache.hadoop.mapreduce.Reducer; 

	public class TopN_Reducer extends Reducer<Text, 
						LongWritable, LongWritable, Text> { 

		private TreeMap<Long, String> tmap2; 
		private static int topN;

		@Override
		public void setup(Context context) throws IOException, 
										InterruptedException 
		{ 
			tmap2 = new TreeMap<Long, String>(); 
			
			//https://www.geeksforgeeks.org/how-to-find-top-n-records-using-mapreduce/
			Configuration conf = context.getConfiguration();
			
	    	String param = conf.get("myValue"); 
	    	if(param != null && !param.isEmpty()) {
	    		topN = Integer.parseInt(param);
	    		
	    	}else {
		    	
	    		topN=5; //default to top 5 if null value in parameter, otherwise will throw error if do not set a default value
	    	}

	    	
		}

		@Override
		public void reduce(Text key, Iterable<LongWritable> values, 
		Context context) throws IOException, InterruptedException 
		{ 

			String name = key.toString(); 
			long count = 0; 

			for (LongWritable val : values) 
			{ 
				count += val.get(); 
			} 

			
			// so we pass count as key 

			tmap2.put(count, name); 

			// we remove the first key-value 
			// if it's size increases 10 
			if (tmap2.size() > topN) //HAVE TO ADD WHATEVER N INPUT IS
			{ 
				tmap2.remove(tmap2.firstKey()); 
				

				}
				
				
			} 
	

		@Override
		public void cleanup(Context context) throws IOException, 
										InterruptedException 
		{ 

			for (Map.Entry<Long, String> entry : tmap2.entrySet()) 
			{ 
				
				long count = entry.getKey(); 
			String name = entry.getValue(); 

					context.write(new LongWritable(count), new Text(name)); 
				
				
			} 
		}
		}

