package krisa.c.gcwebviewer.util;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @author Krisa.Chaijaroen
 * Thread Local for Simple Date Format
 */
public class DateTimeUtil {
	public static final ThreadLocal<SimpleDateFormat> SDF_DEFAULT_DATE_TIME_FORMAT = new ThreadLocal<SimpleDateFormat>() {
                @Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy/MM/dd H:mm:ss");
		}
	};
	public static void main(String[] args){
		System.out.println(DateTimeUtil.SDF_DEFAULT_DATE_TIME_FORMAT.get().format(new Date()));
		
	}
}
