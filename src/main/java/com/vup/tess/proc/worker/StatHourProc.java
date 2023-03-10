package com.vup.tess.proc.worker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vup.tess.model.PointMaster;
import com.vup.tess.model.RawData;
import com.vup.tess.model.StatHourly;
import com.vup.tess.proc.service.PointMasterService;
import com.vup.tess.proc.service.StatHourService;

@Service
public class StatHourProc {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PointMasterService pointMasterService;
	
	@Autowired 
	private StatHourService statHourService;
	
	public void doService() {
		try {
			logger.info("########### Start hourly statistics ########### ");
			List<PointMaster> pointMaster = null;
			//pointList get
			pointMaster = pointMasterService.findAll();
			
			for(int i = 0 ; i < pointMaster.size() ; i ++) {
				System.out.println("pointMaster.size()::"+pointMaster.size());
				//현재 시간 가져오기
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH00");
				Calendar time = Calendar.getInstance();
				String now = format.format(time.getTime());
				//한시간전 날짜 가져오기
				time.add(Calendar.HOUR, -1);
				String beforeHour = format.format(time.getTime());
				
				//쿼리concat을 하기 위해 날짜 조합
				String nowHour = now.substring(9, 13);
				String pointStatusDate = now.substring(0, 8)+nowHour;
				String beforeOneHour = beforeHour.substring(9, 13);
				String beforePointStatusDate = beforeHour.substring(0, 8)+beforeOneHour;
				
				String factoryId =  pointMaster.get(i).getSeq().getFactoryId().getFactoryId();
				String pointId = pointMaster.get(i).getSeq().getPointId();
				String energyGroupId = pointMaster.get(i).getEnergyGroupId().getEnergyGroupId();
				Integer pointPeriod = pointMaster.get(i).getPointPeriod();

				//적산일 경우 데이터 구하기 
				if("3101".equals(pointMaster.get(i).getPointValueTypeCd().getCodeId())) {
					List<RawData> oneHourList = statHourService.findByPointMasterAndFullPointStatusBetween(factoryId,energyGroupId,pointId,beforePointStatusDate,pointStatusDate);
					StatHourly statHour = null;
					String statDateH = beforePointStatusDate.substring(0, 10);
					if(oneHourList.size() == 0) {
						logger.info("no data!!!!");
						statHour = StatHourly.builder()
								.statDateH(statDateH)
								.pointMaster(pointMaster.get(i))
								.build();
						statHourService.statHourlySave(statHour);
					}else {	
						//리스트에서 처음과 마지막 값을 가져와 마지막 값 - 처음 값
						BigDecimal firstValue = oneHourList.get(0).getPointValue();;
						BigDecimal lastvalue = oneHourList.get(oneHourList.size() -1).getPointValue();
						
						BigDecimal diffValue = lastvalue.subtract(firstValue);
						
						String firstPointStatusTime = oneHourList.get(0).getPointStatusTime();
						String lastPointStatusTime = oneHourList.get(oneHourList.size() -1).getPointStatusTime();
						
						//마지막 시간이 24시일 경우 DB에는 0000 으로 들어가서 2400으로 변경
						if(lastPointStatusTime.equals("0000")) {
							lastPointStatusTime = "2400";
						}
						
						SimpleDateFormat sdf = new SimpleDateFormat("HHmm", Locale.KOREA);
						
						Date toFirstPointStatusTime = sdf.parse(firstPointStatusTime);
						Date toLastPointStatusTime = sdf.parse(lastPointStatusTime);
						long diff = toLastPointStatusTime.getTime() - toFirstPointStatusTime.getTime();
						long minute = diff / 1000/60;
						int period = (int) (minute/pointPeriod);
						
						if(period == 0) {
							period = 1;
						}
						logger.info("period::"+period);
						//평균값을 구하기 위해 period를 BigDecimal로 변경
						BigDecimal decimalPeriod = new BigDecimal(period);
						logger.debug("decimalPeriod({}),period({}),diffValue({})", decimalPeriod, period, diffValue);
						BigDecimal avrPoint = diffValue.divide(decimalPeriod, 3, BigDecimal.ROUND_CEILING);
														
						statHour = StatHourly.builder()
								.statDateH(statDateH)
								.pointMaster(pointMaster.get(i))
								.pointValue(diffValue.abs())
								.pointAverage(avrPoint.abs())
								.pointCount(period)
								.build();
						statHourService.statHourlySave(statHour);
						//System.out.println("111111111111111");
					}
				//순시값일 경우 데이터 구하기
				}else {
					List<RawData> oneHourList = statHourService.findByPointMasterAndFullPointStatusBetween(factoryId,energyGroupId,pointId,beforePointStatusDate,pointStatusDate);
					StatHourly statHour = null;
					String statDateH = beforePointStatusDate.substring(0, 10);
					if(oneHourList.size() == 0) {
						logger.info("no data!!!!");
						statHour = StatHourly.builder()
								.statDateH(statDateH)
								.pointMaster(pointMaster.get(i))
								.build();
						statHourService.statHourlySave(statHour);
					}else {
						
						List<BigDecimal> invoices = new LinkedList<>();
						for(int j = 0 ; j < oneHourList.size() ; j++) {
							invoices.add(oneHourList.get(j).getPointValue());
						}
						BigDecimal sum = BigDecimal.ZERO;
						for(BigDecimal value : invoices) {
					        sum = sum.add(value);
					    }
						
					    BigDecimal decimalPeriod = new BigDecimal(oneHourList.size());
					    BigDecimal avrPoint = sum.divide(decimalPeriod).setScale(3, RoundingMode.HALF_EVEN);
	
					    statHour = StatHourly.builder()
								.statDateH(statDateH)
								.pointMaster(pointMaster.get(i))
								.pointValue(sum)
								.pointAverage(avrPoint)
								.pointCount(oneHourList.size())
								.build();
					    statHourService.statHourlySave(statHour);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
