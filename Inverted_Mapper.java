package invertedindices;
//import java.awt.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

//import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.io.IntWritable;
//import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
//import org.apache.hadoop.io.LongWritable; 
import org.apache.hadoop.mapreduce.Mapper;
//import org.apache.hadoop.mapreduce.Mapper.Context; 
public class Inverted_Mapper extends Mapper<Object, 
Text, Text, Text>{

	HashMap<Integer, String> map1 = new HashMap<Integer, String>();

	    public void map(Object key, Text value, Context context
	                    ) throws IOException, InterruptedException {
	    //https://acadgild.com/blog/building-inverted-index-mapreduce
	   
	    String folder = ((FileSplit)context.getInputSplit()).getPath().toString();
	    List<String> path = new ArrayList<>(Arrays.asList(folder.split("/")));
	
	      String terms = value.toString().toLowerCase().trim();
	      String delim = "\"[]{}(),.:;*_-+' '?!'|#";
	    
	      //https://stackoverflow.com/questions/27685839/removing-stopwords-from-a-string-in-java
	      ArrayList<String> stopWordsList = new ArrayList<String>();
	      stopWordsList.add("the");
	      stopWordsList.add("can");
	      stopWordsList.add("that");
	      stopWordsList.add("as");
	      stopWordsList.add("would");
	      stopWordsList.add("should");
	      stopWordsList.add("could");
	      stopWordsList.add("and");
	      stopWordsList.add("was");
	      stopWordsList.add("from");
	      stopWordsList.add("have");
	      stopWordsList.add("you");
	      stopWordsList.add("i");
	      stopWordsList.add("this");
	      stopWordsList.add("not");
	      stopWordsList.add("on");
	      stopWordsList.add("with");
	      stopWordsList.add("is");
	      stopWordsList.add("it");
	      stopWordsList.add("to");
	      stopWordsList.add("in");
	      stopWordsList.add("of");

	      // Remove special characters
	      //https://mkyong.com/java/java-stringtokenizer-example/ --and added any necessary characters for given files
	    
	      StringTokenizer itr = new StringTokenizer(terms,delim);
	     
	    
	      String [] doc = {};
	   
	       
	    	while(itr.hasMoreElements()) {
	    	doc = itr.nextElement().toString().split(" ");
	      
	      }
	      
	    for(String words: doc) {
	    	if(!stopWordsList.contains(words)) {
	    	
	    	context.write(new Text(words), new Text(path.get(path.size()-2) + "|" + path.get(path.size()-1)));
	    	}
	    }
	      
	     
	  
	    }
	  }
