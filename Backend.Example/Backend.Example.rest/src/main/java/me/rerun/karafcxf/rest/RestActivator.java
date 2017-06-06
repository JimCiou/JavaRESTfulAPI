package me.rerun.karafcxf.rest;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.apache.log4j.Logger;  
import org.apache.log4j.PropertyConfigurator;

public class RestActivator implements BundleActivator {

	Logger logger = Logger.getLogger(RestActivator.class);

	public void start(BundleContext context) {
		logger.warn("Starting the Tracker service");
	}

	public void stop(BundleContext context) {
		logger.warn("Stopping the Tracker service");
	}
}
