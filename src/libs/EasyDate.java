package libs;

import javax.swing.text.MaskFormatter;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by jgibson on 7/10/2014.
 */
public class EasyDate {
    private static final String MASK = "####-##-##";
    private static final String DECIMAL_MASK = "0.0";
    private static MaskFormatter mf;
    private java.util.Date today;
    private java.util.Date last_month;
    private Date sql_today;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdf_milis = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private Calendar this_month_cal;
    private Calendar last_month_cal;
    private String sdf_date_today;
    private String sdf_date_lastMonth;
    private int year;
    private int month;
    private int dayOfMonth;
    private int dayOfYear;
    private int weekOfMonth;
    private String string_date;
    private Date sql_date;
    private java.util.Date util_date;

    public EasyDate(String date) throws ParseException {
        this.string_date = date;
        if(string_date.length() > 10) {
            util_date = sdf_milis.parse(date);
        } else {
            util_date = sdf.parse(date);
        }

        sql_date = new Date(util_date.getTime());
        this_month_cal = new GregorianCalendar();
        setYMD();
    }

    public EasyDate(long date) {
        sql_date = new Date(date);
        this_month_cal = new GregorianCalendar();
        setYMD();
    }

    public EasyDate(LocalDate ld) throws ParseException {
        String date = new String(ld.getYear() + "-" + ld.getMonthValue() + "-" + ld.getDayOfMonth());
        util_date = sdf.parse(date);
        sql_date = new Date(util_date.getTime());
        this_month_cal = new GregorianCalendar();
        setYMD();
    }

    public EasyDate() {
        util_date = new java.util.Date(System.currentTimeMillis());
        string_date = sdf.format(util_date);
        sql_date = new Date(util_date.getTime());
        this_month_cal = new GregorianCalendar();
        setYMD();
    }

    public void setCalendar(int month_shift) {
        //Takes a signed integer that shifts the calendar by a set number of months
        this_month_cal.add(Calendar.MONTH, month_shift);
        setYMD();
    }

    private void setYMD() {
        year = this_month_cal.get(Calendar.YEAR);
        month = this_month_cal.get(Calendar.MONTH);
        dayOfMonth = this_month_cal.get(Calendar.DAY_OF_MONTH);
    }

    public void getBeginningOfCurrentMonth() {
        this_month_cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
    }

    public void getEndOfCurrentMonth() {
        this_month_cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    public String getISODate() {
        return sdf.format(this_month_cal.getTime());
    }

    public Long getDateAsLong() {
        return this_month_cal.getTimeInMillis();
    }

    public Long getSqlDateAsLong() {
        return this.util_date.getTime();
    }

    public LocalDate getDateAsLocalDate() {
        return sql_date.toLocalDate();
    }

    public void setDates(String date) throws ParseException {
        this.string_date = date;
        if(string_date.length() > 10) {
            util_date = sdf_milis.parse(date);
        } else {
            util_date = sdf.parse(date);
        }

        sql_date = new Date(util_date.getTime());
        this_month_cal = new GregorianCalendar();
        setYMD();
    }

    public void setDates(long date) {
        sql_date = new Date(date);
        this_month_cal = new GregorianCalendar();
        setYMD();
    }

    public void reset() {
        this_month_cal = new GregorianCalendar();
        setYMD();
    }

    public String getString_date() {
        return string_date;
    }

    public Date getSql_date() {
        return sql_date;
    }

    public java.util.Date getUtil_date() {
        return util_date;
    }

}
