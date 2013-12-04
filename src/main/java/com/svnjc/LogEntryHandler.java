package com.svnjc;

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;

/**
 * This class acts as a helper class for Logger class and
 * this is an implementation of ISVNLogEntryHandler interface
 * @author Siva Guruvareddiar
 *
 */
public class LogEntryHandler implements ISVNLogEntryHandler {
	/**
	 * Helper handle method for Logger class which simulates the "svn log" command
	 * Implementation of ISVNLogEntryHandler
	 */
	private static Logger logger = Logger.getLogger(LogEntryHandler.class);
	public void handleLogEntry(SVNLogEntry logEntry)  throws SVNException{
		  long rev=logEntry.getRevision();
		  String date="";
		  String author=logEntry.getAuthor();
		  try{
		    date=logEntry.getDate().toString();
		  }
		  catch(Exception e){
			   date="";
		  }
		  String message=logEntry.getMessage();
		/*  Map map=logEntry.getChangedPaths();
		  Set keys = map.keySet();         // The set of keys in the map.
	      Iterator keyIter = keys.iterator();
	      
	    */
		  if(date!=null && author !=null &&  message!=null){
		  System.out.println("------------------------------------------------------------------------");
	      System.out.println("r"+rev+"\t |"+author+"\t |"+date);
	      System.out.println("\n "+message);
	      logger.debug("------------------------------------------------------------------------");
	      logger.debug("r"+rev+"\t |"+author+"\t |"+date);
	      logger.debug("\n "+message);
		  }
	      /*System.out.println("The map contains the following associations:");
	      while (keyIter.hasNext()) {
	         Object key = keyIter.next();  // Get the next key.
	         Object value = map.get(key);  // Get the value for that key.
	         System.out.println( "   (" + key + "," + value + ")" );
	      }*/
		  
		  
		  
	}

}
