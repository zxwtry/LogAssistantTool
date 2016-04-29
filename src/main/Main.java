package main;

/*
 * 	text file encoding : gbk
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("请输入需要修改的ring号：");
		String ringNumber = scanner.nextLine().trim();
		int ringNumberInt = 0;
		while (true) {
			try {
				ringNumberInt = Integer.parseInt(ringNumber); 
				if (ringNumberInt >= 0 && ringNumberInt <= 3) {
					break;
				} else {
					System.out.println("WRONG:输入数字大于3\n请输入需要修改的ring号：");
					ringNumber = scanner.nextLine().trim();
				}
			} catch (NumberFormatException e) {
				System.out.println("WRONG:输入字符不是数字\n请输入需要修改的ring号：");
				ringNumber = scanner.nextLine().trim();
			}
		}
		System.out.println("请输入日志内容：(以##表示结束)");
		String logString = null;
		StringBuilder stringBuilder = new StringBuilder();
		Calendar calendar = new GregorianCalendar();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		stringBuilder.append(year+"/"+String.format("%02d", month)+"/"+String.format("%02d", day)+"  "+String.format("%02d", hour)+":"+String.format("%02d", minute) + "\r\n");
		while(true) {
			logString = scanner.nextLine();
			if ("##".equals(logString)) {
				break;
			}
			stringBuilder.append(logString+"\r\n");
		}
		scanner.close();
		File toWriteFile = new File("C:/Users/Administrator/Desktop/ring-"+ringNumberInt+"/log.txt");
		if (! toWriteFile.exists() ) {
			try {
				toWriteFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileOutputStream fileOutputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		try {
			fileOutputStream = new FileOutputStream("C:/Users/Administrator/Desktop/ring-"+ringNumberInt+"/log.txt", true);
//			outputStreamWriter = new OutputStreamWriter(fileOutputStream, "GBK");
//			outputStreamWriter.write(stringBuilder.toString().getBytes("GBK"));
			fileOutputStream.write(stringBuilder.toString().getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != outputStreamWriter) {
				try {
					outputStreamWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != fileOutputStream) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
