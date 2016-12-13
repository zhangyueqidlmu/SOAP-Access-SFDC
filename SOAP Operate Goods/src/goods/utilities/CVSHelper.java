/**
 * 
 */
package goods.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

public class CVSHelper {
	private static final Logger logger = Logger.getLogger(CVSHelper.class);

	public static String generateDateStr() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		return formatter.format(new Date());
	}

	public static String generateYearStr() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		return formatter.format(new Date());
	}

	public static List<CSVRecord> readCSVFile(String filePath, String backupFilePath, String fileName,
			String[] fileHeader) {
		File f = null;
		File newF = null;
		InputStreamReader isr = null;
		CSVParser csvFileParser = null;
		List<CSVRecord> csvRecords = null;
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(fileHeader);
		try {
			f = new File(filePath + fileName);

			if (f.exists()) {
				isr = new InputStreamReader(new FileInputStream(f), "UTF-8");
				csvFileParser = new CSVParser(isr, csvFileFormat);
				csvRecords = csvFileParser.getRecords();
			}
		} catch (Exception e) {
			logger.error("Load CSV failed: " + e.getMessage());
		} finally {
			try {
				if (null != isr) {
					isr.close();
				}

				if (null != csvFileParser) {
					csvFileParser.close();
				}

				if (f.exists() && null != backupFilePath && !"".equals(backupFilePath)) {
					// Back up
					newF = new File(backupFilePath + generateDateStr() + fileName);
					if (!newF.getParentFile().exists()) {
						if (newF.getParentFile().mkdirs()) {
							newF.getParentFile().setWritable(true, false);
						}
					}
					f.renameTo(newF);
				}

			} catch (Exception e) {
				logger.error("Load CSV failed: " + e.getMessage());
			}
		}
		return csvRecords;
	}

	public static void writeCsvFile(String pathName, String fileName, String[] fileHeader, List<List<String>> csvRows) {
		File f = null;
		// FileWriter fileWriter = null;
		OutputStreamWriter write = null;
		CSVPrinter csvFilePrinter = null;
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(ConstantUtility.RECORD_SEPARATOR);
		try {
			f = new File(pathName + fileName);
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
				f.getParentFile().setWritable(true, false);
			}
			
			byte[] uft8bom={(byte)0xef,(byte)0xbb,(byte)0xbf};
			FileOutputStream out = new FileOutputStream(f);
			out.write(uft8bom);
			
			write = new OutputStreamWriter(out, "UTF-8");
			// fileWriter = new FileWriter(write);
			csvFilePrinter = new CSVPrinter(write, csvFileFormat);
			csvFilePrinter.printRecord(fileHeader);

			for (List<String> i : csvRows) {
				csvFilePrinter.printRecord(i);
			}

		} catch (Exception e) {
			logger.error("Write CSV failed: " + e.getMessage());
		} finally {
			try {
				write.flush();
				write.close();
				csvFilePrinter.close();
			} catch (IOException e) {
				logger.error("Write CSV failed: " + e.getMessage());
			}
		}
	}

}
