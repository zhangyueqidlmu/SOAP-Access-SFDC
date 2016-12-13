package goods.schedule;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import goods.handler.GoodsHandler;
import goods.handler.SFDCHandler;
import goods.utilities.QuartzConfig;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzTask {
	private static final Logger logger = Logger.getLogger(QuartzTask.class);

	public static final QuartzConfig quartzConfig = new QuartzConfig();

	public static void main(String[] args) {
		Scheduler scheduler = null;
		SchedulerFactory schedulerfactory = null;
		JobDetail job = null;
		String expression = "";
		Trigger trigger = null;

		try {
			logger.info("Service started!");

			// Get factory
			schedulerfactory = new StdSchedulerFactory();
			scheduler = schedulerfactory.getScheduler();

			
			// read goods list
			job = newJob(GoodsHandler.class).withIdentity("Goods Job", "group1").build();
			expression = quartzConfig.getProperties().getProperty("GoodsCronExpression");
			trigger = newTrigger().withIdentity("Goods Trigger", "group1").startNow()
					.withSchedule(CronScheduleBuilder.cronSchedule(expression)).build();
			scheduler.scheduleJob(job, trigger);
			logger.info("Goods started!");
			
			// Verify with SAP
			
			// Session
			job = newJob(SFDCHandler.class).withIdentity("SFDCSession Job", "group2").build();
			expression = quartzConfig.getProperties().getProperty("SFDCCronExpression");
			trigger = newTrigger().withIdentity("SFDCSession Trigger", "group4").startNow()
					.withSchedule(CronScheduleBuilder.cronSchedule(expression)).build();
			scheduler.scheduleJob(job, trigger);
			logger.info("SFDCSession Job started!");

			// Run job
			scheduler.start();
			logger.info("Scheduler start");
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			logger.info("Service completed!");
		}
	}
}
