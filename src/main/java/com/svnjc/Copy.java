package com.svnjc;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCopyClient;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
/**
 * This class intends to simulate "svn copy" command using SVNKit Java Library
 * @author Siva Guruvareddiar
 *
 */
public class Copy {
	private static Logger logger = Logger.getLogger(Copy.class);
	private static SVNClientManager svnClientManager;
	private static SVNCopyClient copyClient;
	
	/**
	 * simulates the "svn copy" command
	 * @param prop
	 * @throws SVNException
	 */
	private static void executeCopyProcess(Properties prop,Client client,SVNURL srcURL,SVNURL dstURL,String commitMessage)
			throws SVNException {
		// setup repo factory
		DAVRepositoryFactory.setup();
		try {
			
			DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
			
			 // For event handlers
			svnClientManager=SVNClientManager.newInstance(options, client.getUserId(),client.getPassword());
			
			
			copyClient = svnClientManager.getCopyClient();
			//signature :  SVNCommitInfo 	doCopy(SVNCopySource[] sources, SVNURL dst, boolean isMove, boolean makeParents, boolean failWhenDstExists, String commitMessage, SVNProperties revisionProperties)   
			long newRevision =copyClient.doCopy(new SVNCopySource[] {new SVNCopySource(SVNRevision.HEAD, SVNRevision.HEAD, srcURL)},
	                dstURL, false, true, false, commitMessage, null).getNewRevision();
			if(newRevision ==-1 ){
				logger.debug("Nothing has been copied");
				}
			else{
				logger.debug("Committed revision "+newRevision+".");
			}
			
		} catch (SVNException e) {
			showError("svn copy failed :"+e.getErrorMessage().getMessage(),e);
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
    public static void main(String[] args) {
    	if(args.length !=3){
    	  	   logger.debug("Missing CLI arguments ");
    	  	   logger.debug("Usage: java Copy <SRC_URL> <DST_URL> <MESSAGE>");
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
    			        SVNURL srcUrl=SVNURL.parseURIEncoded(args[0]);
    			        SVNURL dstUrl = SVNURL.parseURIEncoded(args[1]);
    			        String message = args[2];
    			        executeCopyProcess(prop,client,srcUrl,dstUrl,message);
    			        } 
    				catch (Exception e) {
    					e.printStackTrace();
    				}
    				}
	}

}

