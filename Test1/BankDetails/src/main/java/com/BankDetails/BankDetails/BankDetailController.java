package com.BankDetails.BankDetails;
/*	1. Validation done for card data, we can make use of if needed
 * 	2. sorting data based on expire date in descending order
 * 	3. Masking is done for card No
 *  4. Request mapping for EXCEL file upload and reading cell value and setting in entity
 *  5. Luhn Algorithm
 */
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BankDetailController {
	@RequestMapping("/bankDet")
	public String bankDetShow(BankDetailEn bnkDet, HttpServletRequest req) throws ParseException {
		Boolean flagNo = false;
		if (bnkDet.getCardNo() != null) {
			String ccNumber = bnkDet.getCardNo().replaceAll("[\\s-]+", "");
			
			//validation for card no using luhn algorithm if required  and use below algorithm if needed
			/*flagNo = validateCreditCardNumber(ccNumber);// valid card No that satisfy luhn alg : 4111-1111-1111-1111,5196-0818-8850-0645,4929-2489-8029-5542,4222-2222-22222,1234-5678-9035-55  
			if (flagNo == true) {*/
			
			// Maintain session object and if null creating new list of session object 
			@SuppressWarnings("unchecked")
			List<BankDetailEn> sessionBnkObjList = (List<BankDetailEn>) req.getSession().getAttribute("bnkDet");
			if (sessionBnkObjList == null) {
				sessionBnkObjList = new ArrayList<BankDetailEn>();
				req.getSession().setAttribute("bnkDet", sessionBnkObjList);
			}
			sessionBnkObjList.add(bnkDet);
			
			// Sorting session object based on expire date into descending order
			Collections.sort(sessionBnkObjList, new Comparator<BankDetailEn>() {
				public int compare(BankDetailEn o1, BankDetailEn o2) {
					Date dateObj1 = null;
					Date dateObj2 = null;
					try {
						dateObj1 = new SimpleDateFormat("MMM-yyyy", Locale.ENGLISH).parse(o1.getExpireDate());
						dateObj2 = new SimpleDateFormat("MMM-yyyy", Locale.ENGLISH).parse(o2.getExpireDate());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return dateObj2.compareTo(dateObj1);
				}
			});
			
			// Masking value of card no except the last four digits, with ‘X’ and return to session object
			final int STARTLENGTH = 0; 
			final int ENDLENGTH = 4;
			StringBuilder sb1 = new StringBuilder();
			for (int i = 0; i < sessionBnkObjList.size(); i++) {
				String cardNoMask=sessionBnkObjList.get(i).getCardNo();
				String[] splitmask = cardNoMask.split("-");
				if (!splitmask[0].equals("XXXX")) {
					for (int j = 0; j < cardNoMask.length() - 4; j++) {
						char ch = cardNoMask.charAt(j);
						if (ch != '-') {
							sb1.append("X");
						} else {
							sb1.append(ch);
						}
					}
				String sessMask= sessionBnkObjList.get(i).getCardNo();
				String maskedCard = sessMask.substring(0, STARTLENGTH) + sb1 + sessMask.substring(sessMask.length() - ENDLENGTH, sessMask.length());
				sessionBnkObjList.get(i).setCardNo(maskedCard);
				}
			}
			req.getSession().setAttribute("bnkDet", sessionBnkObjList);
			/*req.getSession().setAttribute("cardMessage", "");
			} else { 
				 req.getSession().setAttribute("cardMessage", bnkDet.getCardNo() + " is an invalid credit card number"); 
			}*/
			 
		}
		return "bankDetails";
	}
	
	/* Excel file upload Request Mapping */
	@RequestMapping("/import")
	public String mapReapExcelDatatoDB(@RequestParam("file") MultipartFile reapExcelDataFile, HttpServletRequest req) throws IOException, ParseException {
	    
	    @SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
	    XSSFSheet worksheet = workbook.getSheetAt(0);
	    
	    /* Reading cell value and setting in BankDetailEn entity and calling same functionality from excel*/
	    for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {
	    	BankDetailEn tempBankEn = new BankDetailEn();
	        XSSFRow row = worksheet.getRow(i);
	        SimpleDateFormat dateObjCon = new SimpleDateFormat("MMM-yyyy", Locale.ENGLISH);
	        String strDate = dateObjCon.format(row.getCell(2).getDateCellValue()); 
	        tempBankEn.setBankName(row.getCell(0).getStringCellValue());
	        tempBankEn.setCardNo(row.getCell(1).getStringCellValue());
	        tempBankEn.setExpireDate(strDate);
	        bankDetShow(tempBankEn,req);
	    }
	    return "bankDetails";
	}
	/* luhn algorithm 
	 * looping value from right and doubling every second digit, 
	 * if doubled value greater than 10 then do summation to get single digit and summation of all the digit, 
	 * if total modulo 10 is equal to 0 then returning true.
	 * 
	 * */
	private boolean validateCreditCardNumber(String str) {
		int[] ints = new int[str.length()];
		for (int i = 0; i < str.length(); i++) {
			ints[i] = Integer.parseInt(str.substring(i, i + 1));
		}
		for (int i = ints.length - 2; i >= 0; i = i - 2) {
			int j = ints[i];
			j = j * 2;
			if (j > 9) {
				j = j % 10 + 1;
			}
			ints[i] = j;
		}
		int sum = 0;
		for (int i = 0; i < ints.length; i++) {
			sum += ints[i];
		}
		if (sum % 10 == 0) {
			return true;
		} else {
			return false;
		}
	}

}
