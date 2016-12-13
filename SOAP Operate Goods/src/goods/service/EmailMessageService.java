package goods.service;

import goods.schedule.QuartzTask;

import org.apache.log4j.Logger;

import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.SendEmailResult;
import com.sforce.soap.enterprise.SingleEmailMessage;

public class EmailMessageService {
	private static final Logger logger = Logger.getLogger(EmailMessageService.class);

	public static void sendEmail(EnterpriseConnection connection, String Message) throws Exception {
		SingleEmailMessage em = null;
		try {

			em = new SingleEmailMessage();
			em.setToAddresses(QuartzTask.quartzConfig.getProperties().getProperty("ToAddresses").split(";"));
			em.setSubject("SOAP Operate Goods Exception");
			em.setSenderDisplayName("SOAP Operate Goods Program");
			em.setPlainTextBody("Exception: " + Message);
			SendEmailResult[] results = connection.sendEmail(new SingleEmailMessage[] { em });
			if (results[0].isSuccess()) {
				logger.info("The email was sent successfully.");
			} else {
				logger.info("The email failed to send: " + results[0].getErrors()[0].getMessage());
			}
		} catch (Exception e) {
			logger.error("Class EmailMessageService, Method sendEmail! " + e.getMessage());
		}

	}
}
