package invertedindices;

import java.io.IOException;
import java.util.HashMap;
//import java.util.Map; 
//import java.util.TreeMap; 

//import org.apache.hadoop.io.LongWritable; 
import org.apache.hadoop.io.Text; 
import org.apache.hadoop.mapreduce.Reducer; 
public class Inverted_Reducer 
	 
     extends Reducer<Text,Text,Text,Text> {
  /*
  Reduce method collects the output of the Mapper calculate and aggregate the word's count.
  */
  public void reduce(Text key, Iterable<Text> values,
                     Context context
                     ) throws IOException, InterruptedException {

    HashMap<String,Integer> map = new HashMap<String,Integer>();
   
     //information used from https://acadgild.com/blog/building-inverted-index-mapreduce
   
    String docId = key.toString();
    for (Text val : values) {
      if (map.containsKey(val.toString())) {
        map.put(val.toString(), map.get(val.toString()) + 1);
      } else {
        map.put(val.toString(), 1);
      }
    }
    StringBuilder docValueList = new StringBuilder();
    for(String docID : map.keySet()){
      docValueList.append(docID + ":" + map.get(docID) + " ");
    }
    context.write(new Text(docId), new Text(docValueList.toString()));
  }
}

