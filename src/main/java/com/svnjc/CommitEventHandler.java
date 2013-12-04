package com.svnjc;

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNProperty;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.SVNEvent;
import org.tmatesoft.svn.core.wc.SVNEventAction;
 /**
  * This class acts a helper class for "Commit" class and this is an implementation of 
  * ISVNEventHandler interface
  * @author Siva Guruvareddiar
  *
  */
public class CommitEventHandler implements ISVNEventHandler {
	/**
	 * Handler event for the commit handler that does the "svn ci" or "svn commit"
	 * Implementation of ISVNEventHandler class
	 */
	private static Logger logger = Logger.getLogger(CommitEventHandler.class);
	public void handleEvent(SVNEvent event, double progress) {
		SVNEventAction action = event.getAction();
        if (action == SVNEventAction.COMMIT_MODIFIED) {
        	logger.debug("Sending   " + event.getFile());
            System.out.println("Sending   " + event.getFile());
        } else if (action == SVNEventAction.COMMIT_DELETED) {
        	logger.debug("Deleting   " + event.getFile());
            System.out.println("Deleting   " + event.getFile());
        } else if (action == SVNEventAction.COMMIT_REPLACED) {
        	logger.debug("Replacing   " + event.getFile());
            System.out.println("Replacing   " + event.getFile());
        } else if (action == SVNEventAction.COMMIT_DELTA_SENT) {
        	logger.debug("Transmitting file data....");
            System.out.println("Transmitting file data....");
        } else if (action == SVNEventAction.COMMIT_ADDED) {
        	String mimeType = event.getMimeType();
            if (SVNProperty.isBinaryMimeType(mimeType)) {
            	logger.debug("Adding  (bin)  "+ event.getFile());
                System.out.println("Adding  (bin)  "+ event.getFile());
            } else {
            	logger.debug("Adding         "+ event.getFile());
                System.out.println("Adding         "+ event.getFile());
            }
        }
 
    }
    /**
     * Checks if the current operation is cancelled.
     * Implementation of ISVNEventHandler class
     */
    public void checkCancelled() throws SVNCancelException {
    }
}