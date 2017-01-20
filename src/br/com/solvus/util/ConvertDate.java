package br.com.solvus.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertDate {

	private static final String dateType = "dd/MM/yyyy";
	
	public static Date convertStringToDate(String inputStringDate) {
		Date convertedDate = null;

		try {
			DateFormat formatter = null;

			formatter = new SimpleDateFormat(dateType);
			convertedDate = (Date) formatter.parse(inputStringDate);
			
		} catch (ParseException parse) {
			convertedDate = null;

		}

		return convertedDate;
	}

	
}
