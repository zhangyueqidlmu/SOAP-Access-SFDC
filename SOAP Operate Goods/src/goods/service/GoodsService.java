package goods.service;



import goods.schedule.QuartzTask;
import goods.utilities.CVSHelper;
import goods.utilities.ConstantUtility;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.Error;
import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.sobject.Goods__c;


public class GoodsService {
	private static final Logger logger = Logger.getLogger(GoodsService.class);

	private SFDCService sFDCService;
	
	private List<Goods__c> goodsList;
	
	public GoodsService() {
		sFDCService = new SFDCService();
		goodsList = new ArrayList<Goods__c>();
	}
	
	public void execute() throws Exception {
		EnterpriseConnection connection = null;
		try {
			String goodsFilePath = QuartzTask.quartzConfig.getProperties().getProperty("GoodsFilePath");
			String goodsBackFilePath = QuartzTask.quartzConfig.getProperties().getProperty("GoodsFileBackupPath");
			String goodsFileName = QuartzTask.quartzConfig.getProperties().getProperty("GoodsFileName");
			List<CSVRecord> cSVRecordList = CVSHelper.readCSVFile(goodsFilePath, goodsBackFilePath,
					goodsFileName, ConstantUtility.GOODS_FILE_HEAD);
			if(cSVRecordList == null || cSVRecordList.size() == 0 ) {
				logger.warn("Can not find Goods from csv!");
				return;
			} else {
				logger.info("fetch " + cSVRecordList.size() + "records from csv");
			}
			String[] goodsFieldHead = ConstantUtility.GOODS_FILE_HEAD;
			cSVRecordList.remove(0);
			for(CSVRecord record : cSVRecordList) {
				Goods__c goods = new Goods__c();
				goods.setGoods_Code_Unique__c(record.get(goodsFieldHead[0]));
				goods.setGoodsName__c(record.get(goodsFieldHead[1]));
				goods.setGoodsPrice__c(Double.valueOf(record.get(goodsFieldHead[2])));
				goods.setGoodsCostPrice__c(Double.valueOf(record.get(goodsFieldHead[3])));
				goods.setGoodsBrand__c(record.get(goodsFieldHead[4]));
				goods.setGoodsDescribe__c(record.get(goodsFieldHead[5]));
				goodsList.add(goods);
			}
			List<Goods__c> tempGoodsList = new ArrayList<Goods__c>();
			connection = sFDCService.getSFDCConn();
			for(int i=0;i<goodsList.size();i++) {
				tempGoodsList.add(goodsList.get(i));
				if(i %199 == 198 || i == goodsList.size() - 1) {
					SaveResult[] sSRList = connection.create(tempGoodsList.toArray(new Goods__c[tempGoodsList.size()]));
					for (SaveResult sSR : sSRList) {
						if (sSR.isSuccess()) {
							logger.info("ID:" + sSR.getId());
						} else {
							for (Error e : sSR.getErrors()) {
								logger.error("ID:" + sSR.getId());
								logger.error(e.toString());
							}
						}
					}
					tempGoodsList.clear();
				}
			}
		} catch(Exception e) {
			logger.error("Class GoodsService, Method excute! " + e.toString());
			EmailMessageService.sendEmail(connection, e.toString());
			throw new Exception(e.toString());
		} finally {
			sFDCService.closeSFDCConn(connection);
		}
	}
}
