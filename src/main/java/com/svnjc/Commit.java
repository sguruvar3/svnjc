package com.svnjc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
/**
 * This class intends to simulate "svn ci" command using SVNKit Java Library
 * @author Siva Guruvareddiar
 *
 */
public class Commit {
	private static Logger logger = Logger.getLogger(Commit.class);
	private static SVNClientManager svnClientManager;
	private static CommitEventHandler svnCommitEventHandler;
	
	/**
	 * simulates the "svn commit" or "svn ci" command
	 * @param prop
	 * @throws SVNException
	 */
	private static void executeCheckinFilesProcess(Properties prop,Client client, String message, File[] path)
			throws SVNException {
		// setup repo factory
		DAVRepositoryFactory.setup();
		try {
			//	File[] path = readFileNames(prop);
			String[] changelists = {};
			SVNProperties svnProp = new SVNProperties();
			DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
			
			 // For event handlers
			svnClientManager=SVNClientManager.newInstance(options, client.getUserId(),client.getPassword());
			svnCommitEventHandler = new CommitEventHandler();
			svnClientManager.getCommitClient().setEventHandler(svnCommitEventHandler);
			
			SVNCommitClient scc =svnClientManager.getCommitClient();
			//signature : SVNCommitInfo 	doCommit(File[] paths, boolean keepLocks, String commitMessage, SVNProperties revisionProperties, String[] changelists, boolean keepChangelist, boolean force, SVNDepth depth)  
			long newRevision = scc.doCommit(path, true,message, svnProp, changelists, true,	false, SVNDepth.UNKNOWN).getNewRevision();
			if(newRevision ==-1 ){
				logger.debug("Nothing has updated");
			}
			else{
				logger.debug("Committed revision "+newRevision);
			}
			
		} catch (SVNException e) {
			showError("svn: Commit failed : File or directory  is out of date; try updating",e);
		}
	}
	
	/**
	 * Read the file contents and load as a array
	 * @param prop
	 * @return
	 */
	private static File[] readFileNames(Properties prop){
		ArrayList<File> fileList = new ArrayList<File>();
		try{
		BufferedReader bfr = new BufferedReader(new FileReader(prop.getProperty("FileList2Checkin")));
		String str ="";
		while ((str=bfr.readLine())!=null){
		 	   fileList.add(new File(str.trim()));
		}
		}
		catch(IOException e){
				showError("Error occured while trying to checkin files", e);
		}
		File[] fileArr = new File[fileList.size()];
		fileArr = fileList.toArray(fileArr);
		return fileArr;
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
    	if (args.length <2){
			logger.debug("Missing CLI arguments ");
 	  	   	logger.debug("Usage: java Commit <message> <filename(s)>");
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
					String message=args[0];
					File[] filenames=new File[args.length-1];
					for (int i=1; i<args.length;i++){
						if (args[i].trim().equals(".")){
	            			File cwd=new File(".");
	            			filenames[i-1]=new File(cwd.getAbsolutePath());
	            		}
	            		else{
	            			filenames[i-1]=new File(args[i]);
	            		}
						//filenames[i-1]=new File(args[i]);
					}
					executeCheckinFilesProcess(prop,client,message,filenames);
            } catch (Exception e) {
			e.printStackTrace();
		}
		}
	}

}
