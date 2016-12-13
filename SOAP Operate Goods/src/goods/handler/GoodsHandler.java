package goods.handler;

import goods.service.GoodsService;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class GoodsHandler implements Job {
	private static final Logger logger = Logger.getLogger(GoodsHandler.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		GoodsService goodsService = null;

		try {
			logger.info("GoodsHandler started!");
			goodsService = new GoodsService();
			goodsService.execute();
			logger.info("GoodsHandler Completed!");
		} catch (Exception e) {
			logger.error("GoodsHandler failed!" + e.getMessage());
		}

	}
}
