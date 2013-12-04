package com.svnjc;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
/**
 * This class intends to simulate "svn co" command using SVNKit Java Library
 * @author Siva Guruvareddiar
 *
 */
public class Checkout {
	private static Logger logger = Logger.getLogger(Checkout.class);

	private static SVNClientManager svnClientManager;
	private static ISVNEventHandler svnUpdateEventHandler;
	/**
	 * simulates the "svn co" command
	 * @param prop
	 */
	public static void executeCheckoutProcess(Properties prop,Client client,SVNURL svnURL,String dstPath) {
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
        svnUpdateEventHandler = new UpdateEventHandler();
		svnClientManager.getUpdateClient().setEventHandler(svnUpdateEventHandler);
		File wcDir = null;
		if (dstPath.trim().equals(".")){
			File cwd=new File(".");
			wcDir=new File(cwd.getAbsolutePath());	
		}
		else{
			wcDir = new File(dstPath);
		}
		    
	     if (wcDir.exists()) {
	            showError("the destination directory '"+ wcDir.getAbsolutePath() + "' already exists!", null);
	     }
	    
	        try {
	        	SVNUpdateClient updateClient = svnClientManager.getUpdateClient();
	            updateClient.setIgnoreExternals(false);
	            //signature : long doCheckout(SVNURL url, File dstPath, SVNRevision pegRevision, SVNRevision revision, SVNDepth depth, boolean allowUnversionedObstructions) 
	            long l=updateClient.doCheckout(svnURL, wcDir, SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.fromRecurse(true),false);
	            if (l==-1){
	            	logger.debug("Revision is -1");
	            }
	            logger.debug(l);
	        } catch (SVNException svne) {
	        	logger.debug("error while checking out a working copy for the location '" + svne.getErrorMessage().getMessage()  );
	            showError("error while checking out a working copy for the location '" + svne.getErrorMessage().getMessage(),svne  );
	        }
	        
	}
	
	/**
	 * To display Error messages
	 * 
	 */
    private static void showError(String message, Exception e){
    	logger.debug(message+(e!=null ? ": "+e.getMessage() : ""));
        System.err.println(message+(e!=null ? ": "+e.getMessage() : ""));
        System.exit(1);
    }
    
    
    /**
	 * Starting point of the class - the main method
	 * 
	 * @param args
	 */
	public static void main(String args[]){
		if (args.length !=2){
			logger.debug("Missing CLI arguments ");
 	  	   	logger.debug("Usage: java Checkout <svn_url> <workspace>");
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
		     	SVNURL svnURL=SVNURL.parseURIEncoded(args[0]);
				String dstPath=args[1];
				executeCheckoutProcess(prop,client,svnURL,dstPath);   
				}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
    
}
}