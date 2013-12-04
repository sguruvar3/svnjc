package com.svnjc;

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.SVNEvent;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNEventAction;
 
/**
 * This class acts as a helper class for "Update" class and
 * this is an implementation of ISVNEventHandler interface
 * @author Siva Guruvareddiar
 *
 */
public class UpdateEventHandler implements ISVNEventHandler {
	private static Logger logger = Logger.getLogger(UpdateEventHandler.class);
	/**
	 * Helper event method for the "Update" class which simulates the "svn up" command
	 * Implementation of ISVNEventHandler interface
	 */
    public void handleEvent(SVNEvent event, double progress) {
        SVNEventAction action = event.getAction();
        String pathChangeType = " ";
        if (action == SVNEventAction.UPDATE_ADD) {
            pathChangeType = "A";
        } else if (action == SVNEventAction.UPDATE_DELETE) {
            pathChangeType = "D";
        } else if (action == SVNEventAction.UPDATE_UPDATE) {
            SVNStatusType contentsStatus = event.getContentsStatus();
            if (contentsStatus == SVNStatusType.CHANGED) {
                pathChangeType = "U";
            }else if (contentsStatus == SVNStatusType.CONFLICTED) {
                pathChangeType = "C";
            } else if (contentsStatus == SVNStatusType.MERGED) {
                pathChangeType = "G";
            }
        } else if (action == SVNEventAction.UPDATE_EXTERNAL) {
        	logger.debug("Fetching external item into '"+ event.getFile().getAbsolutePath() + "'");
            System.out.println("Fetching external item into '"+ event.getFile().getAbsolutePath() + "'");
            logger.debug("External at revision " + event.getRevision());
            System.out.println("External at revision " + event.getRevision());
            return;
        } else if (action == SVNEventAction.UPDATE_COMPLETED) {
        	logger.debug("At revision " + event.getRevision());
            System.out.println("At revision " + event.getRevision());
            return;
        } else if (action == SVNEventAction.ADD){
        	logger.debug("A     " + event.getFile());
            System.out.println("A     " + event.getFile());
            return;
        } else if (action == SVNEventAction.DELETE){
        	logger.debug("D     " + event.getFile());
            System.out.println("D     " + event.getFile());
            return;
        } else if (action == SVNEventAction.LOCKED){
        	logger.debug("L     " + event.getFile());
            System.out.println("L     " + event.getFile());
            return;
        } else if (action == SVNEventAction.LOCK_FAILED){
        	logger.debug("failed to lock    " + event.getFile());
            System.out.println("failed to lock    " + event.getFile());
            return;
        }
 
        SVNStatusType propertiesStatus = event.getPropertiesStatus();
        String propertiesChangeType = " ";
        if (propertiesStatus == SVNStatusType.CHANGED) {
            propertiesChangeType = "U";
        } else if (propertiesStatus == SVNStatusType.CONFLICTED) {
            propertiesChangeType = "C";
        } else if (propertiesStatus == SVNStatusType.MERGED) {
            propertiesChangeType = "G";
        }
 
        String lockLabel = " ";
        SVNStatusType lockType = event.getLockStatus();
        
        if (lockType == SVNStatusType.LOCK_UNLOCKED) {
            lockLabel = "B";
        }
        if (propertiesStatus!=SVNStatusType.INAPPLICABLE && propertiesStatus!=SVNStatusType.UNKNOWN){
        logger.debug(pathChangeType
                + propertiesChangeType
                + lockLabel
                + "       "
                + event.getFile());
        System.out.println(pathChangeType
                + propertiesChangeType
                + lockLabel
                + "       "
                + event.getFile());
    }
    }
 
    /**
     * Checks if the current operation is cancelled.
     * Implementation of ISVNEventHandler interface
     */
    public void checkCancelled() throws SVNCancelException {
    }
 
}