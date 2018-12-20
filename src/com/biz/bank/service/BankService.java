package com.biz.bank.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.biz.bank.vo.BankVO;

public class BankService {

	List<BankVO> bankList;
	String balFile;
	Scanner scan;
	
	public BankService(String balFile) {
		bankList = new ArrayList();
		this.balFile = "src/com/biz/bank/iolist/";
		scan = new Scanner(System.in);
	}
	
	public void readBalance() {
		
		String FileName = "src/com/biz/bank/BankBalance.txt";
		
		FileReader fr;
		BufferedReader buffer;
		
		try {
			fr = new FileReader(FileName);
			buffer = new BufferedReader(fr);
			while(true) {
				String read = buffer.readLine();
				if(read == null) break;
				String[] readLine = read.split(":");
				BankVO b = new BankVO();
				b.setStrID(readLine[0]);
				b.setIntBalance(Integer.valueOf(readLine[1]));
				b.setStrLastDate(readLine[2]);
				bankList.add(b);
			}
			buffer.close();
			fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void bankMenu() {
		
		System.out.println("==================================================");
		System.out.println("1. 입금 / 2. 출금 / 3. 계좌조회 / 0. 종료");
		System.out.println("==================================================");
		System.out.print("원하시는 업무를 선택해주세요 >> ");
		String strChoice = scan.nextLine();
		int intChoice = Integer.valueOf(strChoice);
		
		if(intChoice == 0) {
				System.out.println("GOOD BYE!!!"); // 만약 다른 상황에서 추가 업무를 수행해야 한다면
				return;							 // 0을 눌렀을때 그 업무들을 실행하지 않기 위해 0을 가장 위에 배치함
			}
			
			if(intChoice == 1) this.bankInput();
			if(intChoice == 2) this.bankOutput();
			if(intChoice == 3) System.out.println("계좌조회");
			
		
		
	}
	
	public void bankInput() {
		System.out.print("계좌 번호를 입력해주세요 >>");
		String strId = scan.nextLine();
		BankVO vo = bankFindId(strId);
		if(vo == null) return;
		
		// 계좌번호가 정상이고, vo에는 해당 계좌번호의 정보가 담겨있다.
		
		System.out.print("입금액 >> ");
		String strIO = scan.nextLine();
		int intIO = Integer.valueOf(strIO);
		
		// 새로운 코드
		// 입금일 경우 vo.strIO에 "입금"문자열 저장
		// vo.intIOCash에 입금 금액을 저장하고
		// vo.intBalance에 +입금액을 저장한다.
		
		vo.setStrIO("입금");
		vo.setIntIOCash(intIO);
		vo.setIntBalance(vo.getIntBalance() + intIO);
		
		// old java 코드로 현재 날짜 가져오기
		// SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
		// Date curDate = new Date();
		// String strDate = sm.format(curDate);
		
		// New Java(1.8이상)코드로 현재 날짜 가져오기
		LocalDate ld = LocalDate.now();
		String strDate = ld.toString(); 
		vo.setStrLastDate(strDate);
		bankList.add(vo);
		this.bankIoWrite(vo);
		System.out.println("입금처리 완료!");
		
	}

	public void bankOutput() {
		System.out.print("계좌 번호를 입력해주세요 >>");
		String strId = scan.nextLine();
		BankVO vo = bankFindId(strId);
		if(vo == null) return;
		
		System.out.print("출금액 >> ");
		String strIO = scan.nextLine();
		int intIO = Integer.valueOf(strIO);
		
		if(intIO > vo.getIntBalance()) {
			System.out.println("예금 잔액 부족");
			return;
		}

		vo.setStrIO("출금");
		vo.setIntIOCash(intIO);
		vo.setIntBalance(vo.getIntBalance() - intIO);
		
		LocalDate ld = LocalDate.now();
		String strDate = ld.toString(); 
		vo.setStrLastDate(strDate);
		bankList.add(vo);
		this.bankIoWrite(vo);
		
		System.out.println("출금처리 완료!");
	}
	
	public void bankIoWrite(BankVO v) {
			
			FileWriter fw;
			PrintWriter pw;
			String thisId = v.getStrID(); // 계좌번호
			String strIO = v.getStrIO();
			int intIO = v.getIntIOCash();
			
			try {
				// 2번째 매개변수 true : 파일을 Append Mode로 열어라
				fw = new FileWriter(balFile + thisId, true);
				pw = new PrintWriter(fw);
				
				pw.print(v.getStrID() + ":");
				pw.print(v.getStrLastDate()+ ":");
				pw.print(v.getStrIO()+ ":");
				
				if(strIO.equals("입금")) {
					pw.print(intIO);
					pw.print(0);
				} else {
					pw.print(0);
					pw.print(strIO);
				}
				pw.println(":" + v.getIntBalance());
				
				pw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}			
	
	/*
	 * 계좌번호를 매개변수로 받아서
	 * bankList에서 계좌를 조회하고 bankList에 계좌가 있으면
	 * 찾은 vo를 return 하고, 없으면 null을 return 하도록 한다.
	 */
	public BankVO bankFindId(String strId) {
		for(BankVO b : bankList) {
			if(b.getStrID().equals(strId)) {
				return b;
			}
		}
		return null;
		
	}
	
}
