package com.svnjc;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * This class intends to simulate "svn diff" command using SVNKit Java Library
 * @author Siva Guruvareddiar
 *
 */
public class Diff {
	private static Logger logger = Logger.getLogger(Diff.class);
	private static SVNClientManager svnClientManager;
	
	/**
	 * simulates the "svn diff" command 
	 * @param prop
	 */
	public static void executeDiffProcess(Properties prop,Client client,File[] wcDir) {
		//setup repo factory
		DAVRepositoryFactory.setup();
		
		String username="";
		String password="";
		
		try{
			username= client.getUserId();
			password=client.getPassword();
		}
		catch(Exception e){
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
		DefaultSVNOptions options=SVNWCUtil.createDefaultOptions(true);
		svnClientManager = SVNClientManager.newInstance(options, username, password);
        
		// File[] wcDir ={ new File(dstPath)};
	        
	     if (!wcDir[0].exists()) {
	            showError("the destination directory '"+ wcDir[0].getAbsolutePath() + "' does not exist!", null);
	        }
	        
	 
	     try {
	        	SVNDiffClient svnDiffClient = svnClientManager.getDiffClient();
	            svnDiffClient.setIgnoreExternals(false);
	            OutputStream os = System.out;
                //  signature :  void doDiff(File[] paths,SVNRevision rN,SVNRevision rM,SVNRevision pegRevision,SVNDepth depth,boolean useAncestry,OutputStream result,Collection changeLists)
	            svnDiffClient.doDiff(wcDir,SVNRevision.WORKING,SVNRevision.HEAD,SVNRevision.HEAD,SVNDepth.INFINITY,true,os,null);
	            os.flush();
	            } catch (Exception svne) {
	        	showError("error while doing a diff for the location '" + prop.getProperty("svnUrl")  + "'", svne);
	        }
	     }
	
	/**
	 * 
	 * To display Error messages
	 */
    private static void showError(String message, Exception e){
        System.err.println(message+(e!=null ? ": "+e.getMessage() : ""));
        logger.debug(message+(e!=null ? ": "+e.getMessage() : ""));
        System.exit(1);
    }
    
   /**
    * Starting point of the class - the main method
    * 
    * @param args
    */
    public static void main(String args[]){
    	 if (args.length <1){
    			logger.debug("Missing CLI arguments ");
    		  	   	logger.debug("Usage: java Diff <filename(s)>");
    		  	    System.exit(256);
    		}
    		else{
    	      try{
    	    	  Properties prop = new Properties();
	    			InputStream fis =  Info.class.getResourceAsStream("Config.xml");
	    			prop.loadFromXML(fis);
	    			Client client=new Client();
	    			client.setUserId(prop.getProperty("username"));
	    			client.setPassword(prop.getProperty("password")); 
    	    		  File[] filenames=new File[args.length];
    	    		  for(int i=0;i<args.length;i++){
    	    			  if (args[i].trim().equals(".")){
    	    				  File f=new File(".");
    	    				  filenames[i]=new File(f.getAbsolutePath());
    	    			  }
    	    			  else{
    	    				  filenames[i]=new File(args[i]);
    	    			  }
    	    		  }
    	    		  executeDiffProcess(prop,client,filenames);
    	    	  
    	}catch(Exception e){
			e.printStackTrace();
		}
    		}
		
	}
}
