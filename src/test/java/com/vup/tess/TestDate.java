package com.vup.tess;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class TestDate {

	public static void main(String[] args) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance( );
		cal.add ( cal.MONTH, - 1 ); //다음달
		cal.set(Calendar.DAY_OF_MONTH,1);
		System.out.println(df.format(cal.getTime()));
		System.out.println(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		String aaa = df.format(cal.getTime());
		System.out.println(aaa.substring(0, 6));
		System.out.println(aaa.substring(6, 8));
		for(int i = 1 ; i < cal.getActualMaximum(Calendar.DAY_OF_MONTH)+1; i++) {
			String sss = null;
			if (i < 10) {
				sss = "0"+i;
			}else {
				sss = ""+i;
			}
			System.out.println("test1111::"+sss);
			System.out.println("test::"+i);
		}

			
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		LocalDate start = LocalDate.now().minusDays(LocalDate.now().getDayOfMonth()-1);
	    LocalDate end = LocalDate.now().minusDays(LocalDate.now().getDayOfMonth()).plusMonths(1);
	    
	    Date asd = sdf.parse(start.toString());
	    String asds = end.toString();
	    System.out.println("Start of month: " + asd);
	    System.out.println("End of month: " + asds);
		
		 try{
	            //Date startDate = sdf.parse(asd);
	            Date endDate = sdf.parse(asds);
	            
	            //System.out.println("Start of month: " + startDate);
	            System.out.println("End of month: " + endDate);
	 
	            //두날짜 사이의 시간 차이(ms)를 하루 동안의 ms(24시*60분*60초*1000밀리초) 로 나눈다.
	            //long diffDay = (startDate.getTime() - endDate.getTime()) / (24*60*60*1000);
	            //System.out.println(diffDay+"일");
	        }catch(ParseException e){
	            e.printStackTrace();
	        }

		
		

	}

}
