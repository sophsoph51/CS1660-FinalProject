package invertedindices;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
//import org.apache.hadoop.util.GenericOptionsParser;


public class Inverted_Driver {
	
	public static void main(String[] args) throws Exception 
	{
	Configuration conf = new Configuration(); 
	
	Job job = Job.getInstance(conf, "inverted indices"); 
	job.setJarByClass(Inverted_Driver.class); 

	job.setMapperClass(Inverted_Mapper.class); 
	job.setReducerClass(Inverted_Reducer.class); 

	//job.setMapOutputKeyClass(Text.class); 
	//job.setMapOutputValueClass(LongWritable.class); 

	job.setOutputKeyClass(Text.class); 
	job.setOutputValueClass(Text.class); 

	//String input1 = args[0];
	//String input2 = args[1];
	//String input3 = args[2];
	//FileInputFormat.addInputPaths(job, "args[0], args[1], args[2]");
	
	FileInputFormat.addInputPath(job, new Path(args[0])); //add in all 3 files
	FileOutputFormat.setOutputPath(job, new Path(args[1])); 
	job.setNumReduceTasks(1);


	System.exit(job.waitForCompletion(true) ? 0 : 1); 
	}
}
