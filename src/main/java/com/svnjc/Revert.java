package com.svnjc;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * This class intends to simulate "svn revert" command using SVNKit Java Library
 * @author Siva Guruvareddiar
 *
 */
public class Revert {
	private static Logger logger = Logger.getLogger(Revert.class);
	private static SVNClientManager svnClientManager;
	
	/**
	 * simulates the "svn revert" command
	 * @param prop
	 */
	public static void executeRevertProcess(Properties prop,Client client,File[] wcDir) {
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
		
        for (File file:wcDir){
            if (!file.exists()) {
	            showError("the destination directory '"+ file.getAbsolutePath() + "' not exists!", null);
	        }
        }
	 
	        try {
	        	SVNWCClient svnWcClient = svnClientManager.getWCClient();
	            svnWcClient.setIgnoreExternals(true);
	            //signature :  void 	doRevert(File path,SVNDepth depth, COlletion arg2) 
	            svnWcClient.doRevert(wcDir, SVNDepth.UNKNOWN, null);
	            } catch (SVNException svne) {
	                showError("error while reverting local changes inside working copy " ,  svne);
	        }
	        }
	
	/**
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
	 	  	   	  logger.debug("Usage: java Revert <filename(s)>");
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
	            	for (int i=0;i<args.length;i++){
	            		if (args[i].trim().equals(".")){
	            			File f=new File(".");
	            			filenames[i]=new File(f.getAbsolutePath());
	            		}
	            		else{
	            			filenames[i]=new File(args[i]);
	            		}
	            	}
	               	executeRevertProcess(prop,client,filenames);
	            
			}
			catch(Exception e){
				e.printStackTrace();
			}
	  	}
		}
		
	 }