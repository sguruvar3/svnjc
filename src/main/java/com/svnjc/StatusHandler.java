package com.svnjc;
import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNLock;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.ISVNStatusHandler;
import org.tmatesoft.svn.core.wc.SVNEvent;
import org.tmatesoft.svn.core.wc.SVNEventAction;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusType;
 
/**
 * This class acts as a helper class for Status class and 
 * this is an implementation of ISVNStatusHandler interface
 *  
 * @author Siva Guruvareddiar
 *
 */
public class StatusHandler implements ISVNStatusHandler, ISVNEventHandler {
	private static Logger logger = Logger.getLogger(StatusHandler.class);
    private boolean myIsRemote;
    /**
     * constructor of helper class for the "Status" class which simulates the "svn stat" operation
     * Implementation of ISVNStatusHandler 
     * @param isRemote
     */
    public StatusHandler(boolean isRemote) {
        myIsRemote = isRemote;
    }
    
    /**
     * Helper handle for the "Status" class which simulates the "svn stat" command
     * Implementation of ISVNStatusHandler
     */
    public void handleStatus(SVNStatus status) {
        SVNStatusType contentsStatus = status.getContentsStatus();
 
        String pathChangeType = " ";
        
        boolean isAddedWithHistory = status.isCopied();
        if (contentsStatus == SVNStatusType.STATUS_MODIFIED) {
            pathChangeType = "M";
        } else if (contentsStatus == SVNStatusType.STATUS_CONFLICTED) {
            pathChangeType = "C";
        } else if (contentsStatus == SVNStatusType.STATUS_DELETED) {
            pathChangeType = "D";
        } else if (contentsStatus == SVNStatusType.STATUS_ADDED) {
            pathChangeType = "A";
        } else if (contentsStatus == SVNStatusType.STATUS_UNVERSIONED) {
            pathChangeType = "?";
        } else if (contentsStatus == SVNStatusType.STATUS_EXTERNAL) {
            pathChangeType = "X";
        } else if (contentsStatus == SVNStatusType.STATUS_IGNORED) {
            pathChangeType = "I";
        } else if (contentsStatus == SVNStatusType.STATUS_MISSING
                || contentsStatus == SVNStatusType.STATUS_INCOMPLETE) {
            pathChangeType = "!";
        } else if (contentsStatus == SVNStatusType.STATUS_OBSTRUCTED) {
            pathChangeType = "~";
        } else if (contentsStatus == SVNStatusType.STATUS_REPLACED) {
            pathChangeType = "R";
        } else if (contentsStatus == SVNStatusType.STATUS_NONE
                || contentsStatus == SVNStatusType.STATUS_NORMAL) {
            pathChangeType = " ";
        }
        
        /*String remoteChangeType = " ";
 
        if(status.getRemotePropertiesStatus() != SVNStatusType.STATUS_NONE || 
           status.getRemoteContentsStatus() != SVNStatusType.STATUS_NONE) {
            remoteChangeType = "*";
        }
 */       SVNStatusType propertiesStatus = status.getPropertiesStatus();
        String propertiesChangeType = " ";
        if (propertiesStatus == SVNStatusType.STATUS_MODIFIED) {
            propertiesChangeType = "M";
        } else if (propertiesStatus == SVNStatusType.STATUS_CONFLICTED) {
            propertiesChangeType = "C";
        }
 
        boolean isLocked = status.isLocked();
        boolean isSwitched = status.isSwitched();
        SVNLock localLock = status.getLocalLock();
        SVNLock remoteLock = status.getRemoteLock();
        String lockLabel = " ";
 
        if (localLock != null) {
            lockLabel = "K";
            if (remoteLock != null) {
                if (!remoteLock.getID().equals(localLock.getID())) {
                    lockLabel = "T";
                }
            } else {
                if(myIsRemote){
	                lockLabel = "B";
                }
            }
        } else if (remoteLock != null) {
            lockLabel = "O";
        }
 
        long workingRevision = status.getRevision().getNumber();
        long lastChangedRevision = status.getCommittedRevision().getNumber();
        String offset = "                                ";
        String[] offsets = new String[3];
        offsets[0] = offset.substring(0, 6 - String.valueOf(workingRevision)
                .length());
        offsets[1] = offset.substring(0, 6 - String
                .valueOf(lastChangedRevision).length());
        //status
        offsets[2] = offset.substring(0,
                offset.length()
                        - (status.getAuthor() != null ? status.getAuthor()
                                .length() : 1));
        if (!pathChangeType.equals(" ")){
        System.out.println(pathChangeType
                + propertiesChangeType
                + (isAddedWithHistory ? "+" : " ")
                + (isSwitched ? "S" : " ")
                + lockLabel
                + "  "
                + "  "
                + offsets[2] + status.getFile().getPath());
        logger.debug(pathChangeType
                + propertiesChangeType
                + (isLocked ? "L" : " ")
                + (isAddedWithHistory ? "+" : " ")
                + (isSwitched ? "S" : " ")
                + lockLabel
                + "  "
                + "  "
                + offsets[2] + status.getFile().getPath());
        }
    }
    
    /**
     * Helper event method for the "Status" class which simulates the "svn status" process
     * Implementation of ISVNStatusHandler
     */
    public void handleEvent(SVNEvent event, double progress) {
        SVNEventAction action = event.getAction();
        if(action == SVNEventAction.STATUS_COMPLETED){
            System.out.println("Status against revision:  "+ event.getRevision());
            logger.debug("Status against revision:  "+ event.getRevision());
        }
    
    }
 
    /**
     * Checks if the current operation is cancelled.
     * Implementation of ISVNStatusHandler
     */
    public void checkCancelled() throws SVNCancelException {
    
    }
    
}

