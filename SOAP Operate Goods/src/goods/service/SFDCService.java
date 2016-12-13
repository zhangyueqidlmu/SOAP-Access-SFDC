/**
 * 
 */
package goods.service;

import goods.utilities.ConstantUtility;
import goods.utilities.SFDCConfig;

import org.apache.log4j.Logger;

import com.sforce.soap.enterprise.Connector;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class SFDCService {
	private static final Logger logger = Logger.getLogger(SFDCService.class);

	public EnterpriseConnection connection;

	private SFDCConfig sFDCConfig;

	public SFDCService() {
		sFDCConfig = new SFDCConfig();
	}

	public EnterpriseConnection getSFDCConn() {
		if (null != this.connection) {
			return this.connection;
		} else {
			createSFDCConn();
			return this.connection;
		}
	}

	public void createSFDCConn() {
		ConnectorConfig SFDCConfig = null;
		try {
			SFDCConfig = new ConnectorConfig();
			SFDCConfig.setUsername(sFDCConfig.getProperties().getProperty("SFDCUserName"));
			SFDCConfig.setPassword(sFDCConfig.getProperties().getProperty("SFDCUserPWD"));
			if (ConstantUtility.NEED_PROXY.equals(sFDCConfig.getProperties().getProperty("NeedProxy"))) {
				SFDCConfig.setProxy(sFDCConfig.getProperties().getProperty("ProxyUrl"),Integer.valueOf(sFDCConfig.getProperties().getProperty("ProxyPort")));
				SFDCConfig.setProxyUsername(sFDCConfig.getProperties().getProperty("ProxyUserName"));
				SFDCConfig.setProxyPassword(sFDCConfig.getProperties().getProperty("ProxyPwd"));
			}
			logger.info("Login SFDC start!");
			this.connection = Connector.newConnection(SFDCConfig);
			logger.info("Login SFDC end!");
		} catch (ConnectionException e) {
			logger.error("Login failed!" + e.getMessage());
			logger.error("Login failed!" + e.toString());
		} finally {

		}
	}

	public void closeSFDCConn(EnterpriseConnection connection) {
		try {

			if (null != connection) {
				logger.info("Logout SFDC start!");
				connection.logout();
			} else {
				return;
			}
			logger.info("Logout SFDC end!");
		} catch (ConnectionException e) {
			logger.error("Logout failed!" + e.getMessage());
		}
	}
}
