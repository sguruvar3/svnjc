package com.svnjc;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.ISVNStatusHandler;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatusClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * This class intends to simulate "svn status" command using SVNKit Java Library
 * 
 * @author Siva Guruvareddiar
 * 
 */
public class Status {
	private static Logger logger = Logger.getLogger(Status.class);
	private static SVNClientManager svnClientManager;
	private static ISVNStatusHandler svnStatusHandler;

	/**
	 * simulates the "svn status" or "svn stat" command
	 * 
	 * @param prop
	 */
	public static void executeStatusProcess(Properties prop,Client client,File[] wcDir) {
		// setup repo factory
		DAVRepositoryFactory.setup();

		String username = "";
		String password = "";
		try {
			username = client.getUserId();
			password = client.getPassword();
			} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
		DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
		svnClientManager = SVNClientManager.newInstance(options, username,
				password);
		svnStatusHandler = new StatusHandler(true);

		//File wcDir = new File(dstPath);
		for (File file:wcDir){

		if (!file.exists()) {
			showError("the destination directory '" + file.getAbsolutePath()
					+ "' does not exist!", null);
		}
	

		try {
			SVNStatusClient svnStatusClient = svnClientManager
					.getStatusClient();
			svnStatusClient.setIgnoreExternals(false);
			// signature : long doStatus(File path, SVNRevision revision,SVNDepth
			// depth,boolean remote,boolean reportAll,boolean
			// includeIgnored,boolean collectParentExternals,ISVNStatusHandler
			// handler,Collection changeLists)
			svnStatusClient.doStatus(file, SVNRevision.WORKING,
					SVNDepth.INFINITY, false, false, true, true,
					svnStatusHandler, null);
		} catch (SVNException svne) {
			showError("error while finding status for the location '"
					+ file.getAbsolutePath() + "'", svne);
		}
		}
	}

	/**
	 * To display Error messages
	 */
	private static void showError(String message, Exception e) {
		System.err.println(message + (e != null ? ": " + e.getMessage() : ""));
		logger.debug(message + (e != null ? ": " + e.getMessage() : ""));
		System.exit(1);
	}

	/**
	 * Starting point of the class - the main method
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		if (args.length <1){
	  		  logger.debug("Missing CLI arguments ");
	 	  	   	  logger.debug("Usage: java Status <filename(s)>");
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
	            			File cwd=new File(".");
	            			filenames[i]=new File(cwd.getAbsolutePath());
	            		}
	            		else{
	            			filenames[i]=new File(args[i]);
	            		}
	            	}
	            	executeStatusProcess(prop,client,filenames);
	            
			}
			catch(Exception e){
				e.printStackTrace();
			}
	  	}
		}
		
	 }
	  