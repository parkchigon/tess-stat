package com.vup.tess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.vup.tess.proc.worker.StatDailyProc;
import com.vup.tess.proc.worker.StatHourProc;
import com.vup.tess.proc.worker.StatMonthlyProc;
import com.vup.tess.proc.worker.StatYearlyProc;



@SpringBootApplication
@EnableScheduling
public class TessStatApplication {
  
	@Autowired
	private StatHourProc statHourProc;
	
	@Autowired
	private StatDailyProc statDailyProc;
	
	@Autowired
	private StatMonthlyProc statMonthlyProc;
	
	@Autowired
	private StatYearlyProc statYearlyProc;
	
	public static void main(String[] args) {
		SpringApplication.run(TessStatApplication.class, args);
	}
	
	@Scheduled(cron = "${schedule-job.stat-hour.delay}")
	public void startPointHourStat() throws InterruptedException{
		statHourProc.doService();
	}
	
	@Scheduled(cron = "${schedule-job.stat-day.delay}")
	public void startPointDailyStat() throws InterruptedException{
		statDailyProc.doService(); 
	}

	@Scheduled(cron = "${schedule-job.stat-month.delay}")
	public void startPointMonthlyStat() throws InterruptedException{
		statMonthlyProc.doService(); 
	}
	
	@Scheduled(cron = "${schedule-job.stat-year.delay}")
	public void startPointYearlyStat() throws InterruptedException{
		statYearlyProc.doService();
	}
	
}

