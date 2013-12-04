package com.svnjc;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * This class intends to simulate "svn del" command using SVNKit Java Library
 * 
 * @author Siva Guruvareddiar
 * 
 */
public class Delete {
	private static Logger logger = Logger.getLogger(Delete.class);
	private static SVNClientManager svnClientManager;

	/**
	 * simulates the "svn del" or "svn rm" command
	 * 
	 * @param prop
	 */

	public static void executeDeleteProcess(Properties prop, Client client,
			ArrayList<String> filenames) {
		// setup repo factory
		DAVRepositoryFactory.setup();

		String username = "";
		String password = "";

		try {
			username = client.getUserId();
			password = client.getPassword();
		} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
		DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
		svnClientManager = SVNClientManager.newInstance(options, username,
				password);

		for (int i = 0; i < filenames.size(); i++) {
			File wcDir = null;
			if (filenames.get(i).trim().equals(".")) {
				File f = new File(".");
				wcDir = new File(f.getAbsolutePath());
			} else {
				wcDir = new File(filenames.get(i));
			}

			if (!wcDir.exists()) {
				showError("the destination directory '"
						+ wcDir.getAbsolutePath() + "' not exists!", null);
			}

			try {
				SVNWCClient svnWcClient = svnClientManager.getWCClient();
				svnWcClient.setIgnoreExternals(true);
				// signature : void doDelete(File path,boolean force,boolean
				// deleteFiles,boolean dryRun)
				svnWcClient.doDelete(wcDir, true, true, false);
			} catch (SVNException svne) {
				showError("error while deleting directory  " + filenames.get(i)
						+ "'", svne);
			}
		}

	}

	/**
	 * To display Error messages
	 * 
	 */
	private static void showError(String message, Exception e) {
		System.err.println(message + (e != null ? ": " + e.getMessage() : ""));
		logger.debug(message + (e != null ? ": " + e.getMessage() : ""));
		System.exit(1);
	}

	/**
	 * Starting point of the class - the main method
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		if (args.length < 1) {
			logger.debug("Missing CLI arguments ");
			logger.debug("Usage: java Delete <filename(s)>");
			System.exit(256);
		} else {

			try {
				Properties prop = new Properties();
				InputStream fis = Info.class.getResourceAsStream("Config.xml");
				prop.loadFromXML(fis);
				Client client = new Client();
				client.setUserId(prop.getProperty("username"));
				client.setPassword(prop.getProperty("password"));
				ArrayList<String> filenames = new ArrayList<String>(Arrays
						.asList(args));
				executeDeleteProcess(prop, client, filenames);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}