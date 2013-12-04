package com.svnjc;

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.SVNEvent;
import org.tmatesoft.svn.core.wc.SVNStatusType;
/**
 * This class acts a helper class for "Merge" class and this is an implementation of 
 * ISVNEventHandler interface
 * 
 * @author Siva Guruvareddiar
 *
 */
public class MergeEventHandler implements ISVNEventHandler {
	private static Logger logger = Logger.getLogger(MergeEventHandler.class);
    public void handleEvent(SVNEvent event, double progress) throws SVNException {
        SVNStatusType contentsStatus = event.getContentsStatus();
        String pathChangeType = " ";
        if (contentsStatus == SVNStatusType.STATUS_MODIFIED) {
            pathChangeType = "M";
        } else if (contentsStatus == SVNStatusType.STATUS_CONFLICTED) {
            pathChangeType = "C";
        } else if (contentsStatus == SVNStatusType.STATUS_DELETED) {
            pathChangeType = "D";
        } else if (contentsStatus == SVNStatusType.STATUS_ADDED) {
            pathChangeType = "A";
        }else if (contentsStatus == SVNStatusType.CHANGED) {
            pathChangeType = "U";
        }  else if (contentsStatus == SVNStatusType.STATUS_UNVERSIONED) {
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
        if(contentsStatus!=SVNStatusType.UNCHANGED && contentsStatus!=SVNStatusType.INAPPLICABLE ){
        	System.out.println(pathChangeType+"   "+event.getFile()); 
        	logger.debug(pathChangeType+"   "+event.getFile());
        }
    }

    public void checkCancelled() throws SVNCancelException {
    }

}