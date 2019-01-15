package Common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpSession;

import Dao.LocalizationDao;
import Model.User;

public class Util {

	private final static DateFormat yyyyMMddFormat = new SimpleDateFormat("yyyy/MM/dd");

	public static boolean StringEquals(String val1, String val2) {
		if (val1 == null) {
			return false;
		}
		if (val2 == null) {
			return false;
		}
		return val1.equals(val2);
	}

	public static boolean StringEqualsUpper(String val1, String val2) {
		if (val1 == null) {
			return false;
		}
		if (val2 == null) {
			return false;
		}
		return val1.toUpperCase().equals(val2.toUpperCase());
	}

	public static boolean StringIsEmptyOrNull(String val) {
		if (val == null) {
			return true;
		}
		if (val.trim().length() == 0) {
			return true;
		}
		return false;
	}

	public static String localization(String name, HttpSession session) {
		User user = (User) session.getAttribute(Define.USER_SESSION_NAME);
		if (user == null) {
			return name;
		} else {
			return FactoryDao.getDao(LocalizationDao.class).getLocalization(name, user.getLanguaueType().getCode());
		}
	}

	public static Date getDateFromString(String pDate) {
		try {
			return yyyyMMddFormat.parse(pDate);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date getNow() {
		return Calendar.getInstance().getTime();
	}

	public static int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	public static int getMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH) + 1;
	}

	public static int getDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
}
