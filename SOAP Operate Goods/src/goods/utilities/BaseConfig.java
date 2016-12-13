package goods.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import org.apache.log4j.Logger;

public class BaseConfig {
	private static final Logger logger = Logger.getLogger(BaseConfig.class);

	Properties properties;

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public BaseConfig(String fileName) {
		this.properties = new Properties();
		Reader reader = null;
		try {
			logger.info("Load " + fileName);
			reader = new InputStreamReader(new FileInputStream(new File(fileName)), "utf-8");
			this.properties.load(reader);
		} catch (IOException e1) {
			logger.error("Load " + fileName + " failed!" + e1);
		} catch (Exception e2) {
			logger.error("Load " + fileName + " failed!:" + e2);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e3) {
				logger.error("Close stream error:" + e3);
			}
		}
	}
}
