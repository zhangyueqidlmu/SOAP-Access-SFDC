/**
 * 
 */
package goods.handler;

import goods.service.SFDCService;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SFDCHandler implements Job {
	private static final Logger logger = Logger.getLogger(SFDCHandler.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		SFDCService sFDCService = null;

		try {
			logger.info("SFDCHandler started!");
			sFDCService = new SFDCService();
			sFDCService.createSFDCConn();
			logger.info("SFDCHandler Completed!");
		} catch (Exception e) {
			logger.error("SFDCHandler failed!" + e.getMessage());
		}

	}

}
