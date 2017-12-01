package com.baeldung.ib.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.TimeZone;

import com.ib.client.Contract;

public class Util {

	private Properties prop;
	
	public Util(){
//		prop= getProp();
	}
	
	
	public static void writeToFile(String data, String fileName,boolean overWrite){

			BufferedWriter bw = null;
			FileWriter fw = null;

			try {

				fw = new FileWriter("/Users/mgebreki/Documents/fireball_doc/" + fileName,!overWrite);
				bw = new BufferedWriter(fw);
				bw.write(data + "\n");

			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {

					if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}
			}		
	}
	
	public static List<String> readFile(String fileName,boolean includesPath){
			List<String> readList = new ArrayList<String>();
			BufferedReader br = null;
			FileReader fr = null;
			String filePath = "/home/msghe/Documents/" + fileName;
			if(includesPath)filePath = fileName;
			
			try {
				File f = new File(filePath);
				if(!f.exists()) return readList;
				
				fr = new FileReader(filePath);
				br = new BufferedReader(fr);

				String sCurrentLine;

				br = new BufferedReader(new FileReader(filePath));

				while ((sCurrentLine = br.readLine()) != null) {
					readList.add(sCurrentLine);
				}

			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {

					if (br != null)
						br.close();

					if (fr != null)
						fr.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}

			}

			return readList;
		}

	public String getFile(String fileName){
		InputStream inputStream=null;
		
		try {

			inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
		
			if (inputStream != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				StringBuffer sb = new StringBuffer();
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);					
				}
				return sb.toString();
			} else {
				throw new FileNotFoundException("file '" + fileName + "' not found in resources");
			}

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return "";
		}
	
	
//	public List<String> getTickers(){
//		
//	}
	
	private Properties getProp(String propName){
		
		InputStream inputStream=null;
		Properties prop = new Properties();
		
		try {

			inputStream = getClass().getClassLoader().getResourceAsStream(propName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propName + "' not found in the classpath");
			}

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return prop;
		
	}
	
	public boolean isPaper(){
		return Boolean.parseBoolean(prop.get("paperMode").toString());
	}
	
	public String getAcctNo(){
		if(!isPaper()){
			return prop.get("liveAccountNo").toString();
		}else{
			return prop.get("paperAccountNo").toString();
		}
	}
	
//	public int getInt(String propName){
//		String x= getProp().getProperty(propName);
//		return Integer.parseInt(x);
//	}
//	
//	public double getDouble(String propName){
//		String x= getProp().getProperty(propName);
//		return Double.parseDouble(x);
//	}
//	
//	public boolean getBoolean(String propName){
//		String x= getProp().getProperty(propName);
//		return Boolean.parseBoolean(x);
//	}
	
	  
	  public static int delay(Integer interval) {
		  		  
	      final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
	      calendar.setTimeInMillis(System.currentTimeMillis());
	      int currentSec = calendar.get(Calendar.MINUTE) * 60 + calendar.get(Calendar.SECOND);
	      return interval - (currentSec % interval);
	  }
	  
	 	public synchronized static double roundTo2D(double val){
	 		val = Math.round(val*100);
	 		return val/100;
	 	}
	 	
	 	public synchronized static double roundTo3D(double val){
	 		val = Math.round(val*1000);
	 		return val/1000;
	 	}
	 	
	 	public static String formatTimeStamp(long timeStamp){
	 		Date date = new Date(timeStamp);
	 		DateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	 		String dateFormatted = formatter.format(date);
	 		return dateFormatted;
	 	}
	 	
	 	public static String getSimpleDate(String dateStr){
	 		Calendar cal = Calendar.getInstance();
	 		cal.setTime(new Date());
	 		if(dateStr.equals("yDay")) cal.add(Calendar.DATE, -1);
	 		DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	 		String dateFormatted = formatter.format(cal.getTime());
	 		return dateFormatted;
	 	}
	 	
	 	public static int rnd(int min,int max){
	 		Random rand = new Random();
	 		return rand.nextInt((max - min) + 1) + min;
	 	}
	 	
		public static Contract getContract(String ticker){
			   Contract contract = new Contract();
		       contract.symbol(ticker); 

		       contract.secType("STK");

		       contract.exchange("SMART"); 
		       
		       contract.currency("USD");

		       return contract;
		}
	 	

	    public static <K, V extends Comparable<? super V>> Map<K, V> 
	        sortByValue(Map<K, V> map) {
	        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
	        Collections.sort( list, new Comparator<Map.Entry<K, V>>() {
	            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
	                return (o2.getValue()).compareTo( o1.getValue() );
	            }
	        });

	        Map<K, V> result = new LinkedHashMap<K, V>();
	        for (Map.Entry<K, V> entry : list) {
	            result.put(entry.getKey(), entry.getValue());
	        }
	        return result;
	    }
	    
	    
	    public static String parseTime(long time){
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeZone(TimeZone.getTimeZone("America/New_York"));
			calendar.setTimeInMillis(time);
			Integer date = calendar.get(Calendar.DATE);
			Integer hr = calendar.get(Calendar.HOUR_OF_DAY);
	        Integer min = calendar.get(Calendar.MINUTE);
	        Integer sec = calendar.get(Calendar.SECOND);
	        String result = date + "/" +  hr + ":" + min;
	        if(!sec.equals(0)){
	        	result = result + "." + sec;
	        }
	        return result;
		}
	    
	    public static double getDoubleTime(){
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeZone(TimeZone.getTimeZone("America/New_York"));
			calendar.setTimeInMillis(System.currentTimeMillis());
			Integer hr = calendar.get(Calendar.HOUR_OF_DAY);
	        Integer min = calendar.get(Calendar.MINUTE);
	        
	        return Double.parseDouble(hr + "." + min);
		}

}
