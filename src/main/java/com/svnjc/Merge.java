package com.svnjc;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.dav.http.DefaultHTTPConnectionFactory;
import org.tmatesoft.svn.core.internal.io.dav.http.IHTTPConnectionFactory;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * This class intends to simulate "svn Merge" command using SVNKit Java Library
 * @author Siva Guruvareddiar
 *
 */
public class Merge {
	private static Logger logger = Logger.getLogger(Merge.class);
	private static SVNClientManager svnClientManager;
	
	/**
	 * simulates the "svn Merge" command 
	 * @param prop
	 */
	public static void executeMergeProcess(Properties prop,Client client,SVNURL url1,SVNURL url2,String dstPath) {
		//setup repo factory
		IHTTPConnectionFactory factory =     new DefaultHTTPConnectionFactory(null, true, null); 
		DAVRepositoryFactory.setup(factory);
		
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
		MergeEventHandler eventHandler = new MergeEventHandler();
		svnClientManager = SVNClientManager.newInstance(options, username, password);
        svnClientManager.setEventHandler(eventHandler);
		 File wcDir = new File(dstPath);
	        
	        if (!wcDir.exists()) {
	            showError("the destination directory '"+ wcDir.getAbsolutePath() + "' does not exist!", null);
	        }
	        wcDir.mkdirs();
	 
	        try {
	        	SVNDiffClient svnDiffClient = svnClientManager.getDiffClient();
	            svnDiffClient.setIgnoreExternals(false);
	            
	            //signature: void doMerge(SVNURL url1, SVNRevision revision1, SVNURL url2, SVNRevision revision2, File dstPath, SVNDepth depth, boolean useAncestry, boolean force, boolean dryRun, boolean recordOnly)   
	            svnDiffClient.doMerge(url1,SVNRevision.COMMITTED, url2,SVNRevision.COMMITTED, new File(dstPath), SVNDepth.UNKNOWN,true,true, false, true);
	            
	            
	        } catch (Exception svne) {
	        	logger.debug("Error during the merge operation "+svne.getMessage());
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
    	if (args.length !=4){
    		  logger.debug("Missing CLI arguments ");
   	  	   	  logger.debug("Usage: java Merge <URL1> <URL2> <DST_PATH>");
   	  	      System.exit(256);
    	}
    	else{
    		try{
    		SVNURL url1 = SVNURL.parseURIEncoded(args[0]);
    		SVNURL url2 = SVNURL.parseURIEncoded(args[1]);
    		if (args[2].trim().equals(".")){
    			File cwd=new File(".");
    			args[2]=cwd.getAbsolutePath();
    		}
    		Properties prop = new Properties();
			InputStream fis =  Info.class.getResourceAsStream("Config.xml");
			prop.loadFromXML(fis);
			Client client=new Client();
			client.setUserId(prop.getProperty("username"));
			client.setPassword(prop.getProperty("password"));
    	    		executeMergeProcess(prop,client,url1,url2,args[2]);
            }catch(Exception e){
			e.printStackTrace();
		}
    	}	
	}
}