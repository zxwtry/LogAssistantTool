package main;

/*
 * 	text file encoding : gbk
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;


public class Main {
	static String pathBase = "C:/Users/Administrator/Desktop";
	static ArrayList<String> allRings = null;
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		allRings = getArrayListOfAllRings();
		int ringNumberInt = getRingNumber(scanner);
		writeToLog(scanner, ringNumberInt);
	}
	private static void writeToLog(Scanner scanner, int ringNumberInt) {
		String[] split = allRings.get(ringNumberInt).split("-");
		String name = null;
		if (split.length <= 2) {
			name = "";
		} else {
			name = split[2];
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
		stringBuilder.append(year+"/"+String.format("%02d", month)+
				"/"+String.format("%02d", day)+"  "+String.format("%02d", hour)
				+":"+String.format("%02d", minute) +"  Name: " +name+ "\r\n");
		while(true) {
			logString = scanner.nextLine();
			if ("##".equals(logString)) {
				break;
			}
			stringBuilder.append(logString+"\r\n");
		}
		scanner.close();
		String selPath = pathBase + "/" + allRings.get(ringNumberInt);
		File toWritePath = new File(selPath);
		if (! toWritePath.exists()) {
			toWritePath.mkdirs();
		}
		File toWriteFile = new File(selPath + "/log.txt");
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
			fileOutputStream = new FileOutputStream(selPath + "/log.txt", true);
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

	private static int getRingNumber(Scanner scanner) {
		printRingsMsg( 0, false);
		int ringNumberInt = 0;
		String ringNumber = scanner.nextLine().trim();
		while (true) {
			try {
				ringNumberInt = Integer.parseInt(ringNumber); 
				if (ringNumberInt >= 0 && ringNumberInt <= allRings.size()) {
					break;
				} else if (ringNumberInt == -1) {
					// 增加一个ring
					System.out.println("输入新增加的ring的功能 : ");
					addAnRing(allRings.size(), "-" + scanner.nextLine().trim());
					allRings = getArrayListOfAllRings();
					printRingsMsg( 0, true);
					ringNumber = scanner.nextLine().trim();
				} else if (ringNumberInt == -2) {
					modifyAnRing( scanner);
					allRings = getArrayListOfAllRings();
					printRingsMsg( 0, true);
					ringNumber = scanner.nextLine().trim();
				} else {
					printRingsMsg(1, true);
					ringNumber = scanner.nextLine().trim();
				}
			} catch (NumberFormatException e) {
				printRingsMsg( 2, true);
				ringNumber = scanner.nextLine().trim();
			}
		}
		return ringNumberInt;
	}

	private static void modifyAnRing(Scanner scanner) {
		System.out.println("需要修改的ring号 : ");
		int selectRing = 0;
		while (true) {
			String sel = scanner.nextLine().trim();
			try {
				selectRing = Integer.parseInt(sel);
				if (selectRing < 0 || selectRing >= allRings.size()) {
					System.out.println("ring号超界，重新输入");
				} else {
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("ring号不是数字，重新输入");
			}
		}
		System.out.println("输入新功能 : ");
		String fuct = "-" + scanner.nextLine().trim();
		File file = new File(pathBase + "/" + allRings.get(selectRing));
		if (! file.exists()) {
			System.exit(-1);
		}
		file.renameTo(new File(pathBase + "/ring-" + selectRing + fuct));
	}
	private static void printRingsMsg(int sign, boolean isModified) {
		if (isModified)
		System.out.println();
		switch (sign) {
		case 1:
			System.out.println("输入的ring号超界");
			break;
		case 2:
			System.out.println("输入的ring号不是数字");
			break;
		case 3:
			break;
		default:
			break;
		}
		for (int index = 0; index < allRings.size(); index ++) {
			System.out.println(allRings.get(index));
		}
		System.out.println("请输入需要修改的ring号：(-1增加-2修改)");
	}

	private static void addAnRing(int size, String msg) {
		// 增加一个ring
		String filePathString = pathBase + "/ring-" + size + msg;
		File filePath = new File(filePathString);
		if (filePath.exists()) {
			System.out.println("请检查目录　：" + pathBase);
			System.exit(-1);
		}
		filePath.mkdirs();
	}

	private static ArrayList<String> getArrayListOfAllRings() {
		File filePath = new File(pathBase);
		String[] allFiles = filePath.list();
		ArrayList<String> re = new ArrayList<String>();
		for (String string : allFiles) {
			if (string == null || string.length() < 5)	continue;
			if (string.substring(0, 5).equals("ring-")) {
				re.add(string);
			}
		}
		return re;
	}
}