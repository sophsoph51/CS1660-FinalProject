package topN;


	import org.apache.hadoop.conf.Configuration; 
	import org.apache.hadoop.fs.Path; 
	import org.apache.hadoop.io.LongWritable; 
	import org.apache.hadoop.io.Text;
	import org.apache.hadoop.mapreduce.Job; 
	import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
	import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat; 
	import org.apache.hadoop.util.GenericOptionsParser; 

	public class TopN_Driver {

		public static void main(String[] args) throws Exception 
		{ 
			Configuration conf = new Configuration(); 
			String[] otherArgs = new GenericOptionsParser(conf, 
									args).getRemainingArgs(); 
			
			conf.set("topn", ""); //default for command line input
			if (otherArgs.length < 2) 
			{ 
				System.err.println("Error: please provide two paths"); 
				System.exit(2); 
			} 

			
			//File fileInput = new File(otherArgs[0]);
			//String[] fileInput = otherArgs[0];
					
			
			Job job = Job.getInstance(conf, "least 5"); 
			job.setJarByClass(TopN_Driver.class); 

			job.setMapperClass(TopN_Mapper.class); 
			job.setReducerClass(TopN_Reducer.class); 

			job.setMapOutputKeyClass(Text.class); 
			job.setMapOutputValueClass(LongWritable.class); 

			job.setOutputKeyClass(LongWritable.class); 
			job.setOutputValueClass(Text.class); 

			job.setNumReduceTasks(1);
			//have to get N input
			//also have to read output file from inverted indices
			//String input1 = args[0];
			
			
			FileInputFormat.addInputPath(job, new Path(otherArgs[0])); //add in all 3 files
			//FileInputFormat.addInputPath(job, new Path(otherArgs[1])); 
			//FileInputFormat.addInputPath(job, new Path(otherArgs[2])); 
			FileOutputFormat.setOutputPath(job, new Path(otherArgs[1])); 

			System.exit(job.waitForCompletion(true) ? 0 : 1); 
		} 
	} 

