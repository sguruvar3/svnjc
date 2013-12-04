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
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * This class intends to simulate "svn update" command using SVNKit Java Library
 * @author Siva Guruvareddiar
 *
 */
public class Update {
	private static Logger logger = Logger.getLogger(Update.class);
	private static SVNClientManager svnClientManager;
	private static UpdateEventHandler svnUpdateEventHandler;

	/**
	 * simulates the "svn up" or "svn update" command
	 * @param prop
	 */
	public static void executeUpdateProcess(Properties prop,Client client,File[] wcDir) {
		//setup repo factory
		DAVRepositoryFactory.setup();
		
		String username="";
		String password="";
		
		username= client.getUserId();
		password=client.getPassword();
		
		DefaultSVNOptions options=SVNWCUtil.createDefaultOptions(true);
		svnClientManager = SVNClientManager.newInstance(options, username, password);
		svnUpdateEventHandler = new UpdateEventHandler();
		svnClientManager.getUpdateClient().setEventHandler(svnUpdateEventHandler);
		 //File wcDir = new File(dstPath);
	       for(File file:wcDir){ 
	    
	 
	        try {
	        	SVNUpdateClient updateClient = svnClientManager.getUpdateClient();
	            updateClient.setIgnoreExternals(false);
	            //signature : doUpdate(File path, SVNRevision revision, SVNDepth depth, boolean allowUnversionedObstructions, boolean depthIsSticky)  
	            long l=updateClient.doUpdate(file, SVNRevision.HEAD, SVNDepth.fromRecurse(true), false, false);
	            if (l==-1){
	            	logger.debug("Nothing to update");
	            	logger.debug(l);
	            }
	            else{
	            	logger.debug("Updated to revision "+l);
	            }
	        } catch (SVNException svne) {
	        	showError("error while doing svn up operation on working copy for the location '" + file  + "'", svne);
	        }
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
    	if (args.length<1){
	  		  logger.debug("Missing CLI arguments ");
	 	  	   	  logger.debug("Usage: java Update <filename(s)>");
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
	            			File f= new File(".");
	            			filenames[i]= new File(f.getAbsolutePath());
	            		}
	            		else{
	            			filenames[i]=new File(args[i]);
	            		}
	            	}
	            	executeUpdateProcess(prop,client,filenames);
	            
			}
			catch(Exception e){
				e.printStackTrace();
			}
	  	}
		}
		
	 }
	  	  