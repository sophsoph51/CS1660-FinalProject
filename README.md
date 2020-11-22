## CS1660 PROJECT 2 OPTION INSTRUCTIONS

* <u>Assumptions:</u>

  * You already have a cluster running on their Google Platform Cloud (GCP) Account
  * The JSON credentials file is already downloaded for your specific cluster
  * You already know how to add jar files to Eclipse by right clicking on the project and going to Configure Build Path to add external jars
  * The file structure for AllFiles includes the folder name as AllFiles and inside the folder are all of the text files (not in sub-folders)
  * The file names are uploaded to the storage bucket and seen as follows: "/Hugo", "/Tolstoy" , "/shakespeare", "/AllFiles"
    * if this is not the case, in the code GuiFinalProject.java, this can be changed in the line 113 for Hugo, line 118 for Tolstoy, line 122 for shakespeare, and line 126 for AllFiles
      * you can change the names here accordingly to how they are named in your bucket at the end of the arg1 variable in the last string where these names are located

* <u>Introduction:</u>

  * Previously, my GUI app was running on docker, but due to an operating system issue that could not be resolved in the timeframe for the due date, I was unable to recreate this pattern in time for the project deadline. I understand there is not much leniency, however I ask that you take this into consideration for my grade as I have put an exceptional amount of time into this project.

* <u>Video Link</u>

  * (One Drive shared with your pitt email) : [https://pitt-my.sharepoint.com/:v:/g/personal/sms315_pitt_edu/ETggzEZpQixJnV-ad2OaPrsBPyWa7-z2bJf3lRQK_odEng?e=p1eLXA](https://nam12.safelinks.protection.outlook.com/?url=https%3A%2F%2Fpitt-my.sharepoint.com%2F%3Av%3A%2Fg%2Fpersonal%2Fsms315_pitt_edu%2FETggzEZpQixJnV-ad2OaPrsBPyWa7-z2bJf3lRQK_odEng%3Fe%3Dp1eLXA&data=04|01|sms315%40pitt.edu|f7ad720f0f2a4e4f22de08d88f292173|9ef9f489e0a04eeb87cc3a526112fd0d|1|0|637416754109022878|Unknown|TWFpbGZsb3d8eyJWIjoiMC4wLjAwMDAiLCJQIjoiV2luMzIiLCJBTiI6Ik1haWwiLCJXVCI6Mn0%3D|1000&sdata=DmVcXJgjVUexFLYaCINivUlrVR9sjYEZjzUuD1IuH6E%3D&reserved=0)

* <u>Setup</u>:

  * Since I do not have the docker container running, I have provided my GUI Interface Code file for you to copy in a new Java Project on Eclipse.

    * You can open Eclipse, go to File --> New, and click the Java Project 
    * Make the Project name **FinalProject2** 
    * Make sure the execution environment JRE is set to JavaSE-1.8
    * Project Layout: Create separate folders for sources and class files
    * Click Finish

  * The Project created will appear on the left hand side in the Package Explorer

    * Open up the contents by clicking the arrow on the left hand side of the Project Name

    * Right click on the "**src**" package, hit **New** --> **Class**

    * Make the name of this class **GuiFinalProject.java** --> then click **Finish**

    * Then copy the code from my Github account from the file <u>GuiFinalProject.java</u> into this new Class that was created

    * After copying all of the provided code, install Google Cloud Platform Libraries in Eclipse

      *  Search for Google Cloud Tools for Eclipse (it will look something like this)
      * ![img](https://cloud.google.com/eclipse/docs/images/eclipsemarketplace.png)
      * Click Install

      * You will also need to **add an external jar** to account for the api.services.dataproc imports that are not covered by the Google Cloud Tools in Eclipse
        * I have provided this jar file for you on Github  (google-api-services-dataproc-v1-rev150-1.25.0.jar), however I have also provided the online link below in case it is more convenient
        * Here is the link to download this jar file online:  [Maven Repository: com.google.apis » google-api-services-dataproc » v1-rev150-1.25.0 (mvnrepository.com)](https://mvnrepository.com/artifact/com.google.apis/google-api-services-dataproc/v1-rev150-1.25.0) 
        * After downloading this jar file, go to the project in Eclipse, right click to Build Path --> Configure Build Path --> on the left hand side click **Add External Jars** and select the jar file you downloaded
      * The last thing you will need to add is the Credentials JSON that you must download from when you created a Service Account
        * Here is a link for this if you need a reference on how to create a service account and download the necessary credentials: https://cloud.google.com/docs/authentication/production#create_service_account
      * You will have to download your credentials file on your local computer, and go to the **eclipse-workspace** in your file system(or wherever the Eclipse Project is saved locally on your computer). This is where the projects are stored when you are not using Eclipse directly
        * You will need to copy in your credentials JSON file directly in the main part of your project
        * So, click the **eclipse-workspace** (or wherever your project is located on your computer), then click the folder name of the project ( **FinalProject2** )
        * Then, paste the credentials JSON file on the same level as the following folders/files:
          * ​	.settings, bin, src, .classpath, .project, **JSON file** (this is where you copy it)
      * Once you have done all of these steps and there are no more errors with the importing of libraries in Eclipse, we will need to upload the necessary jar files to the Storage Bucket in your GCP account

  * <u>Uploading Files to Storage Bucket</u>:

    * Since we can assume that your cluster is already created, click on the name of your cluster, and go to the tab that says "Configuration"

    * Under this tab, you will see the **Cloud Storage staging bucket** name that your cluster refers to -- keep this information in mind for the next step

  * Go to the left hand side option menu, and click the **Storage **section
    
    * This is where you will need to upload the provided jar files from my Github to your **Cloud Storage staging bucket** that refers to your specific cluster:
      1. inverted-indices-final.jar
      2. searchterm-final.jar
      3. topn-final.jar
    * This is also where your storage bucket will be holding all of your text files as well
      *  /Hugo, /Tolstoy, /shakespeare, and /AllFiles

* <u>Edit the code to match your Google Account Setup in **GuiFinalProject.java:**</u>

  * You will need to change the credentials in the GuiFinalProject.java code for these items from your account (all of these changes will be made at the beginning of the code under the public class GuiFinalProject line):
    * The **Credentials JSON File**
      * replace the name of your JSON file that you downloaded from your account to the variable *String jsonFile* 
      
    * The **Bucket Name**
      
      * add the name of the bucket name you used when uploading the files to the variable *String gcpBucketName* 
      
    * The **Project Id**

      * you will need to add the project id that you are using for your cluster in the variable name *String projectid* 

        To find the project id, you can go to the Home window of GCP and the first box on the left will say **Project Info** , and underneath you can find the Project ID

    * The **Cluster Name** 
      * you will need to add the name of your cluster that you are using to the variable name *String clusterName* 

    * The **Region**

      * Update the region accordingly to your cluster
      * This information can also be found in the "Configuration" tab of your cluster in the Region section
        *  (this was not mentioned in the video recording as I realized this needed to be changed after the fact, however it is important to update this information as well)

* <u>How to Use GUI Application with GCP</u>:

  * Once you have everything set up (all the jar files and text folders/files are in the bucket storage, and your cluster is up and running):

    * You can go ahead and click the Green Play button at the top of the Eclipse bar to run the GuiFinalProject.java code

  * After you run this, the user-interface GUI should pop up

  * The first component you will see is a drop down selection of all the files a user can choose from:

    * Hugo
    * Tolstoy
    * Shakespeare
    * AllFiles (means you will run Hugo, Tolstoy, and Shakespeare files together)

  * Click the file option you prefer

  * Then click the **Choose Files... ** button to fully select the file

  * Then click the **Construct Inverted Indices** button to construct the inverted indices for the chosen file

  * When it is done with the construction, a new interface will pop up asking the user whether to **Search for a Term**, or find the **Top-N** words in the file(s)

  * <u>If you choose the Search Button</u>:

    * Type in a word you want to search in the input box
    * Click the search button
    * When finished, a ScrollPane Output will return the word you searched for, along with the directory and filename of the word's location as well as the frequency of that word

  * <u>If you choose the Top-N Button</u>:

    * Enter the number of Top-N that you want to search for in the input box
    * Click the search button
    * When finished, a ScrollPane Output will return the top -n words searched for, and will display this with the frequency of each word and the words themselves

  * <u>If you wish to go back to pick a different option than before:</u>

    * Click the "X" at the top right of the GUI to go back to the previous GUI frame

  * <u>If my application was running on docker:</u>

    - You would need to pull my image from my docker repository onto your local machine
    - <u>To Run a GUI on Docker on Windows</u>: 

    * Download Docker Desktop: [Install Docker Desktop on Windows | Docker Documentation](https://docs.docker.com/docker-for-windows/install/)
    * [Windows 10 Docker & GUI | Microsoft Docs](https://docs.microsoft.com/en-us/archive/blogs/jamiedalton/windows-10-docker-gui)
      * set the variable as shown in the link to your internal computer IP address
      * download Xming for Windows: [Download Xming - free - latest version (softonic.com)](https://xming.en.softonic.com/download)

    

    

    

    

    

