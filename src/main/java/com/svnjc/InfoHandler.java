package com.svnjc;

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.wc.ISVNInfoHandler;
import org.tmatesoft.svn.core.wc.SVNInfo;

/**
 * This class acts as a helper class for the "Info" class and 
 * this is an implementation of ISVNInfoHandler interface
 * @author Siva Guruvareddiar
 *
 */
public class InfoHandler implements ISVNInfoHandler{
	/**
	 * Helper handle method for the Info class which simulates the "svn info" command
	 * Implementation of ISVNInfoHandler 
	 */
	private static Logger logger = Logger.getLogger(InfoHandler.class);
	 public void handleInfo(SVNInfo info){
		 logger.debug("=====================================================");
		 System.out.println("=====================================================");
		 logger.debug("Path: " + info.getFile().getPath());
		 System.out.println("Path: " + info.getFile().getPath());
		 logger.debug("URL: " + info.getURL());
	        System.out.println("URL: " + info.getURL());
	        if (info.isRemote() && info.getRepositoryRootURL() != null) {
	        	logger.debug("Repository Root: "+ info.getRepositoryRootURL());
	            System.out.println("Repository Root: "+ info.getRepositoryRootURL());
	        }
	        if(info.getRepositoryUUID() != null){
	        	logger.debug("Repository UUID: " + info.getRepositoryUUID());
	            System.out.println("Repository UUID: " + info.getRepositoryUUID());
	        }
	        logger.debug("Revision: " + info.getRevision().getNumber());
	        System.out.println("Revision: " + info.getRevision().getNumber());
	        logger.debug("Node Kind: " + info.getKind().toString());
	        System.out.println("Node Kind: " + info.getKind().toString());
	        if(!info.isRemote()){
	        	logger.debug("Schedule: "+ (info.getSchedule() != null ? info.getSchedule() : "normal"));	        
	            System.out.println("Schedule: "+ (info.getSchedule() != null ? info.getSchedule() : "normal"));
	        }
	        logger.debug("Last Changed Author: " + info.getAuthor());
	        System.out.println("Last Changed Author: " + info.getAuthor());
	        logger.debug("Last Changed Revision: "+ info.getCommittedRevision().getNumber());
	        System.out.println("Last Changed Revision: "+ info.getCommittedRevision().getNumber());
	        logger.debug("Last Changed Date: " + info.getCommittedDate());
	        System.out.println("Last Changed Date: " + info.getCommittedDate());
	        if (info.getPropTime() != null) {
	        	logger.debug("Properties Last Updated: " + info.getPropTime());
	            System.out.println("Properties Last Updated: " + info.getPropTime());
	        }
	        if (info.getKind() == SVNNodeKind.FILE && info.getChecksum() != null) {
	            if (info.getTextTime() != null) {
	            	logger.debug("Text Last Updated: " + info.getTextTime());
	                System.out.println("Text Last Updated: " + info.getTextTime());
	            }
	            logger.debug("Checksum: " + info.getChecksum());
	            System.out.println("Checksum: " + info.getChecksum());
	        }
	        if (info.getLock() != null) {
	            if (info.getLock().getID() != null) {
	            	logger.debug("Lock Token: " + info.getLock().getID());
	                System.out.println("Lock Token: " + info.getLock().getID());
	            }
	            logger.debug("Lock Owner: " + info.getLock().getOwner());
	            System.out.println("Lock Owner: " + info.getLock().getOwner());
	            logger.debug("Lock Created: "+ info.getLock().getCreationDate());
	            System.out.println("Lock Created: "+ info.getLock().getCreationDate());
	            if (info.getLock().getExpirationDate() != null) {
	            	logger.debug("Lock Expires: "+ info.getLock().getExpirationDate());
	                System.out.println("Lock Expires: "+ info.getLock().getExpirationDate());
	            }
	            if (info.getLock().getComment() != null) {
	            	logger.debug("Lock Comment: "+ info.getLock().getComment());
	                System.out.println("Lock Comment: "+ info.getLock().getComment());
	            }
	        }
	    }
}

