import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.api.services.dataproc.Dataproc;
import com.google.api.services.dataproc.model.HadoopJob;
import com.google.api.services.dataproc.model.Job;
import com.google.api.services.dataproc.model.JobPlacement;
import com.google.api.services.dataproc.model.SubmitJobRequest;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.gax.paging.Page;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JComboBox;

public class GuiFinalProject {

	private JFrame frame;
	private JTextField textField;
	private String fileSelected;
	private String outputFolder = "";
	private String searchOutputFolder = "";
	private String topNOutputFolder = "";
	private JTextArea textResults;
	private String jsonFile = "final-project-cs1660-d489989b38f3.json";
	private String gcpBucketName = "dataproc-staging-us-central1-13377474563-kcarq305";
	private String projectid = "white-ground-293400";
	private String clusterName = "inverted-indices";
	private String region = "us-central1";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)  {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiFinalProject window = new GuiFinalProject();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GuiFinalProject() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("unchecked")
	private void initialize() {
		
		frame = new JFrame("Sophie Search Engine");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton chooseFiles = new JButton("Choose Files...");
		@SuppressWarnings("rawtypes")
		final JComboBox comboBox  = new JComboBox();
		comboBox.addItem("Hugo");
		comboBox.addItem("Tolstoy");
		comboBox.addItem("Shakespeare");
		comboBox.addItem("All Files");
		
		chooseFiles.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
		
				fileSelected = (String) comboBox.getSelectedItem();
			
				
			}
		});
		
		JButton invertedIndices = new JButton("Construct Inverted Indices");
		
		invertedIndices.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String arg1 = "";
				String arg2= "";
				if(fileSelected == "Hugo") {
					outputFolder = "hugoOutput" + System.currentTimeMillis();
					 arg1 = "gs://" + gcpBucketName +"/"+ "Hugo";
					 arg2 = "gs://" + gcpBucketName + "/"+ outputFolder;
				}else if(fileSelected == "Tolstoy") {
					outputFolder = "tolstoyOutput" + System.currentTimeMillis();

					 arg1 = "gs://" + gcpBucketName + "/" +"Tolstoy";
					 arg2 = "gs://" + gcpBucketName + "/" +outputFolder;
				}else if(fileSelected == "Shakespeare") {
					outputFolder = "shakespeareOutput" +  System.currentTimeMillis();
					 arg1 = "gs://" + gcpBucketName +"/" +"shakespeare";
					 arg2 = "gs://" + gcpBucketName + "/"+ outputFolder;
				}else { //all files
					outputFolder = "allfilesOutput" +  System.currentTimeMillis();
					 arg1 = "gs://" + gcpBucketName + "/"+"AllFiles/";
					 
					 arg2 = "gs://" + gcpBucketName + "/"+ outputFolder;
				}
				 Job job = null;
				try {
					//https://github.com/googleapis/java-dataproc/blob/master/samples/snippets/src/main/java/SubmitJob.java
					//https://stackoverflow.com/questions/35611770/how-do-you-use-the-google-dataproc-java-client-to-submit-spark-jobs-using-jar-files
					//https://github.com/GoogleCloudPlatform/java-docs-samples/blob/master/auth/src/main/java/com/google/cloud/auth/samples/AuthExample.java
					//https://cloud.google.com/dataproc/docs/guides/submit-job#dataproc-submit-job-java
					GoogleCredentials creds = GoogleCredentials.fromStream(new FileInputStream(jsonFile)).createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
					HttpRequestInitializer  request= new HttpCredentialsAdapter(creds);
					Dataproc dproc = new Dataproc.Builder(new NetHttpTransport(), new JacksonFactory(),  request).setApplicationName("invert").build();
					Job newjob = dproc.projects().regions().jobs().submit(projectid, region , new SubmitJobRequest().setJob(new Job().setPlacement(new JobPlacement(). setClusterName(clusterName))
							.setHadoopJob(new HadoopJob().setMainClass("invertedindices.Inverted_Driver").setJarFileUris(ImmutableList.of("gs://" + gcpBucketName+ "/inverted-indices-final.jar")).setArgs(ImmutableList.of(arg1,arg2))))).execute();
					
				     String id = newjob.getJobUuid();
	                 //https://stackoverflow.com/questions/35704048/what-is-the-best-way-to-wait-for-a-google-dataproc-sparkjob-in-java
				     //https://stackoverflow.com/questions/38303453/dataproc-client-googleapiclient-method-to-get-list-of-all-jobsrunnng-stopp
	                   job= dproc.projects().regions().jobs().get(projectid, region, id).execute();
	                    String status = job.getStatus().getState();
	                    while (!status.equalsIgnoreCase("DONE") && !status.equalsIgnoreCase("CANCELLED") && !status.equalsIgnoreCase("ERROR")) {
	                        System.out.println("Job not done yet; current state: " + job.getStatus().getState());
	                        Thread.sleep(5000);
	                        job = dproc.projects().regions().jobs().get(projectid, region, id).execute();
	                        status = job.getStatus().getState();
	                    }

	                    System.out.println("Job terminated in state: " + job.getStatus().getState());
	                    //System.out.println(arg2);
	                    
	                    
	                } catch(Exception err) {
	                	
	                    err.printStackTrace();
					
				}
				
				 try {
					 //https://cloud.google.com/storage/docs/listing-objects#code-samples
					 //https://cloud.google.com/docs/authentication/production#auth-cloud-explicit-java	                    
	                  GoogleCredentials creds = GoogleCredentials.fromStream(new FileInputStream(jsonFile))
	                 		.createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
	                   
	                      Storage storage = StorageOptions.newBuilder().setCredentials(creds).setProjectId(projectid).build().getService();

	                      // https://cloud.google.com/storage/docs/listing-objects#storage-list-objects-java
	                      //https://www.baeldung.com/java-google-cloud-storage
	                      //https://stackoverflow.com/questions/44121510/how-to-read-a-file-from-google-cloud-storage-in-java
	                      Bucket bucket =
	                          storage.get(gcpBucketName);
	                      Page<Blob> blobs = bucket.list(Storage.BlobListOption.prefix(outputFolder));
	                      for(Blob blob :blobs.iterateAll()) {
	                      //	System.out.println(blob.getContent());
	                      	String blobcontent = new String(blob.getContent());
	                    	textResults = new JTextArea(blobcontent);
	                
	                      }
 
	                 
	                } catch(Exception err) {
	                    err.printStackTrace();
	                }
				
				//COMPUTE ALGORITHM HERE-- ok so this one has to be deployed on the gcp cluster
				JFrame frame2 = new JFrame("Sophie Search Engine");
				frame2.setBounds(100, 100, 450, 300);
				JButton searchTerm = new JButton("Search Term");
				
				searchTerm.addActionListener(new ActionListener() {
			
					public void actionPerformed(ActionEvent e) {
						JButton searchFunction = new JButton("Search");
						
						searchFunction.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent event) {
							String searchWord = textField.getText();
						searchOutputFolder = "searchTermOutput"+  System.currentTimeMillis();
						String arg1 = "gs://" + gcpBucketName+"/"+outputFolder;
						String arg2 = "gs://"+ gcpBucketName +"/" + searchOutputFolder;
						try {
							GoogleCredentials creds = GoogleCredentials.fromStream(new FileInputStream(jsonFile)).createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
							HttpRequestInitializer request = new HttpCredentialsAdapter(creds);
							Dataproc dproc = new Dataproc.Builder(new NetHttpTransport(), new JacksonFactory(), request).setApplicationName("search").build();
							Job newjob = dproc.projects().regions().jobs().submit(projectid, region , new SubmitJobRequest().setJob(new Job().setPlacement(new JobPlacement(). setClusterName(clusterName))
									.setHadoopJob(new HadoopJob().setMainClass("search.SearchTerm_Driver").setJarFileUris(ImmutableList.of("gs://"+ gcpBucketName +"/searchterm-final.jar" )).setArgs(ImmutableList.of("-D myValue="+searchWord,arg1,arg2))))).execute();
						     String id = newjob.getJobUuid();
			                    Job job = dproc.projects().regions().jobs().get(projectid,region, id).execute();

			                    String status = job.getStatus().getState();
			                    while (!status.equalsIgnoreCase("DONE") && !status.equalsIgnoreCase("CANCELLED") && !status.equalsIgnoreCase("ERROR")) {
			                        System.out.println("Job not done yet; current state: " + job.getStatus().getState());
			                        Thread.sleep(5000);
			                        job = dproc.projects().regions().jobs().get(projectid, region, id).execute();
			                        status = job.getStatus().getState();
			                    }

			                    System.out.println("Job terminated in state: " + job.getStatus().getState());
			                    System.out.println(arg2);
			                    
			                    
			                } catch(Exception err) {
			                	
			                    err.printStackTrace();
							
						}
						JPanel jpanel = new JPanel(new BorderLayout());
						JLabel label = new JLabel("Output items:");
						label.setAlignmentX(1);
						label.setAlignmentY(1);
						jpanel.add(label, BorderLayout.NORTH);
						 try {
							 //https://cloud.google.com/storage/docs/listing-objects#code-samples		                    
			                  GoogleCredentials creds = GoogleCredentials.fromStream(new FileInputStream(jsonFile))
			                 		.createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
			                  
			                      Storage storage = StorageOptions.newBuilder().setCredentials(creds).setProjectId(projectid).build().getService();

			                      Bucket bucket =
			                          storage.get(gcpBucketName);
			                      Page<Blob> blobs = bucket.list(Storage.BlobListOption.prefix(searchOutputFolder));
			                      for(Blob blob :blobs.iterateAll()) {
			                      	System.out.println(blob.getContent());
			                      	String blobcontent = new String(blob.getContent());
			                      	textResults = new JTextArea(blobcontent);
			                
			                      }

			                      
			                 
			                } catch(Exception err) {
			                    err.printStackTrace();
			                }
						 JScrollPane scrPane = new JScrollPane(textResults);
						 jpanel.add(scrPane);
			             scrPane.setPreferredSize(new Dimension(800,750));
			             frame.add(jpanel, BorderLayout.NORTH);
			             scrPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			             scrPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			             JOptionPane.showMessageDialog(null, scrPane);
			             
			
			         
						}
						
					});
						JFrame searchFrame = new JFrame("Sophie's Search Engine");
						searchFrame.setBounds(100, 100, 450, 300);
						searchFrame.setVisible(true);
						
						textField = new JTextField();
						textField.setColumns(5);
						
						JLabel searchLabel = new JLabel("Enter your search term");
						
						GroupLayout searchLayout = new GroupLayout(searchFrame.getContentPane());
						searchLayout.setHorizontalGroup(
						searchLayout.createParallelGroup(Alignment.LEADING)
							.addGroup(searchLayout.createSequentialGroup()
								.addGap(137)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(148, Short.MAX_VALUE))
							.addGroup(searchLayout.createSequentialGroup()
								.addGap(158)
								.addComponent(searchLabel, GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
								.addGap(185))
							.addGroup(searchLayout.createSequentialGroup()
								.addGap(112)
								.addComponent(searchFunction, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(118, Short.MAX_VALUE))
					);
						searchLayout.setVerticalGroup(
							searchLayout.createParallelGroup(Alignment.TRAILING)
							.addGroup(searchLayout.createSequentialGroup()
								.addGap(26)
								.addComponent(searchLabel)
								.addPreferredGap(ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
								.addComponent(textField)
								.addGap(59)
								.addComponent(searchFunction, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
								.addGap(25))
					);
					searchFrame.getContentPane().setLayout(searchLayout);
						System.out.println("search");
						
						
					}
				});
				
				
				JButton topN = new JButton("Top-N");
				
				topN.addActionListener(new ActionListener() {
			
					public void actionPerformed(ActionEvent e) {
						
						JButton topNFunction = new JButton("Search");
						topNFunction.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent event) {
							int topnNum = Integer.parseInt(textField.getText());
						
						topNOutputFolder = "topNOutput"+  System.currentTimeMillis();
						String arg1 = "gs://" + gcpBucketName +"/"+outputFolder;
						String arg2 = "gs://" + gcpBucketName+ "/" + topNOutputFolder;
						try {
							GoogleCredentials creds = GoogleCredentials.fromStream(new FileInputStream(jsonFile)).createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
							HttpRequestInitializer request = new HttpCredentialsAdapter(creds);
							Dataproc dproc = new Dataproc.Builder(new NetHttpTransport(), new JacksonFactory(), request).setApplicationName("topn").build();
							Job newjob = dproc.projects().regions().jobs().submit(projectid, region , new SubmitJobRequest().setJob(new Job().setPlacement(new JobPlacement(). setClusterName(clusterName))
									.setHadoopJob(new HadoopJob().setMainClass("topN.TopN_Driver").setJarFileUris(ImmutableList.of("gs://"+ gcpBucketName + "/topn-final.jar" )).setArgs(ImmutableList.of("-D myValue="+topnNum,arg1,arg2))))).execute();
						     String id = newjob.getJobUuid();

			                    Job job = dproc.projects().regions().jobs().get(projectid, region, id).execute();

			                    String status = job.getStatus().getState();
			                    while (!status.equalsIgnoreCase("DONE") && !status.equalsIgnoreCase("CANCELLED") && !status.equalsIgnoreCase("ERROR")) {
			                        System.out.println("Job not done yet; current state: " + job.getStatus().getState());
			                        Thread.sleep(5000);
			                        job = dproc.projects().regions().jobs().get(projectid, region, id).execute();
			                        status = job.getStatus().getState();
			                    }

			                    System.out.println("Job terminated in state: " + job.getStatus().getState());
			                   // System.out.println(arg2);
			                    
			                    
			                } catch(Exception err) {
			                	
			                    err.printStackTrace();
							
						}
						JPanel jpanel = new JPanel(new BorderLayout());
						JLabel label = new JLabel("Output items:");
						label.setAlignmentX(1);
						label.setAlignmentY(1);
						jpanel.add(label, BorderLayout.NORTH);
						
						 try {
							 //https://cloud.google.com/storage/docs/listing-objects#code-samples
			                    
			                  GoogleCredentials creds = GoogleCredentials.fromStream(new FileInputStream(jsonFile))
			                 		.createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
			                   Storage storage = StorageOptions.newBuilder().setCredentials(creds).setProjectId(projectid).build().getService();

			                      Bucket bucket =
			                          storage.get(gcpBucketName);
			                      Page<Blob> blobs = bucket.list(Storage.BlobListOption.prefix(topNOutputFolder));
			                      for(Blob blob :blobs.iterateAll()) {
			                      //	System.out.println(blob.getContent());
			                      	String blobcontent = new String(blob.getContent());
			                      	textResults = new JTextArea(blobcontent);

			                      
			                      }

			                      
			                 
			                } catch(Exception err) {
			                    err.printStackTrace();
			                }
						 JScrollPane scrPane = new JScrollPane(textResults);
						 jpanel.add(scrPane);
			             scrPane.setPreferredSize(new Dimension(800,750));
			             frame.add(jpanel, BorderLayout.NORTH);
			             scrPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			             scrPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			             JOptionPane.showMessageDialog(null, scrPane);
           
			            
						 
						}
						
						
					});
						JFrame topNFrame = new JFrame("Sophie's Search Engine");
						topNFrame.setBounds(100, 100, 450, 300);
						topNFrame.setVisible(true);
						
						textField = new JTextField();
						textField.setColumns(10);
						
						JLabel topNLabel = new JLabel("Enter your Top-N term");
						
						GroupLayout topNLayout = new GroupLayout(topNFrame.getContentPane());
						topNLayout.setHorizontalGroup(
						topNLayout.createParallelGroup(Alignment.LEADING)
							.addGroup(topNLayout.createSequentialGroup()
								.addGap(137)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(148, Short.MAX_VALUE))
							.addGroup(topNLayout.createSequentialGroup()
								.addGap(158)
								.addComponent(topNLabel, GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
								.addGap(185))
							.addGroup(topNLayout.createSequentialGroup()
								.addGap(112)
								.addComponent(topNFunction, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(118, Short.MAX_VALUE))
					);
						topNLayout.setVerticalGroup(
							topNLayout.createParallelGroup(Alignment.TRAILING)
							.addGroup(topNLayout.createSequentialGroup()
								.addGap(26)
								.addComponent(topNLabel)
								.addPreferredGap(ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
								.addComponent(textField)
								.addGap(59)
								.addComponent(topNFunction, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
								.addGap(25))
					);
					topNFrame.getContentPane().setLayout(topNLayout);
						
						
						System.out.println("topn");
					}
				});
				frame2.setVisible(true);
				
				JLabel lblNewLabel2 = new JLabel("Successfully created inverted indices!");
				GroupLayout groupLayout2 = new GroupLayout(frame2.getContentPane());
				groupLayout2.setHorizontalGroup(
				groupLayout2.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout2.createSequentialGroup()
						.addGap(137)
						.addComponent(searchTerm, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(148, Short.MAX_VALUE))
					.addGroup(groupLayout2.createSequentialGroup()
						.addGap(158)
						.addComponent(lblNewLabel2, GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
						.addGap(185))
					.addGroup(groupLayout2.createSequentialGroup()
						.addGap(112)
						.addComponent(topN, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(118, Short.MAX_VALUE))
			);
				groupLayout2.setVerticalGroup(
					groupLayout2.createParallelGroup(Alignment.TRAILING)
					.addGroup(groupLayout2.createSequentialGroup()
						.addGap(26)
						.addComponent(lblNewLabel2)
						.addPreferredGap(ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
						.addComponent(searchTerm)
						.addGap(59)
						.addComponent(topN, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
						.addGap(25))
			);
			frame2.getContentPane().setLayout(groupLayout2);
		
			}
		});
		
		JLabel lblNewLabel = new JLabel("Load My Engine");
		
	
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(152)
							.addComponent(chooseFiles, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE)
							.addGap(46))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(158)
							.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
							.addGap(79))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(112)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(invertedIndices, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)))
					.addGap(106))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(26)
					.addComponent(lblNewLabel)
					.addGap(18)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(chooseFiles)
					.addGap(25)
					.addComponent(invertedIndices, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}