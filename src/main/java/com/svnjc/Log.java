package com.svnjc;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * This class intends to simulate "svn log" command using SVNKit Java Library
 * @author Siva Guruvareddiar
 *
 */
public class Log {
	private static Logger logger = Logger.getLogger(Log.class);
	private static SVNClientManager svnClientManager;
	private static ISVNLogEntryHandler svnLogEntryHandler;
	
	/**
	 * simulates the "svn log" command
	 * @param prop
	 */
	public static void executeLoggingProcess(Properties prop,Client client,Long limit,File[] wcDir) {
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
        svnLogEntryHandler = new LogEntryHandler();
		
		 //File[] wcDir = {new File(dstPath)};
	        for(File file:wcDir){
	        if (!file.exists()) {
	            showError("the destination directory '"+ file.getAbsolutePath() + "' does not exist!", null);
	        }
	}
	        
	 
	        try {
	        	SVNLogClient svnLogClient = svnClientManager.getLogClient();
	        	svnLogClient.setIgnoreExternals(true);
	            // signature : 	void doLog(File[] paths, SVNRevision startRevision, SVNRevision endRevision, boolean stopOnCopy, boolean discoverChangedPaths,  long limit,  ISVNLogEntryHandler handler) 
	        	svnLogClient.doLog(wcDir,SVNRevision.create(-1), SVNRevision.create(0), true, true,limit,svnLogEntryHandler);//.doLog(wcDir,SVNRevision.create(10), SVNRevision.create(20), SVNRevision.create(10), true, true,false,2,null,svnLogEntryHandler);
	        } catch (SVNException svne) {
	        	showError("error while getting the logs for the  working copy '"   + "'", svne);
	        }
	           
	}
	
	/**
	 * To display Error messages
	 * 
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
    	if (args.length<2){
    		logger.debug("Missing CLI arguments ");
	  	   	logger.debug("Usage: java Log <limit> <filename(s)>");
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
	        	 File[] filenames=new File[args.length-1];
	    		  for(int i=1;i<args.length;i++){
	    			  if (args[i].trim().equals(".")){
	    				  File f= new File(".");
	    				  filenames[i-1]=new File(f.getAbsolutePath());
	    			  }
	    			  else{
	    				  filenames[i-1]=new File(args[i]);
	    			  }
	    		  }
	        	executeLoggingProcess(prop,client,Long.parseLong(args[0]),filenames);
		
    	} catch(Exception e){
			e.printStackTrace();
		}
	}	
	}
    
}