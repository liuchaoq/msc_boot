package com.msc.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.naming.NamingException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public abstract class DateUtil {

  private static final String myFmt4 = new String("yyyy-MM-dd HH:mm:ss:SSS");
  private static final String myFmt = new String("yyyy-MM-dd HH:mm:ss");
  private static final String myFmt2 = new String("yyyy-MM-dd");
  private static final String myFmt3 = new String("EEE MMM dd HH:mm:ss zzz yyyy");
  private static final String myFmt5 = new String("yyyyMMddHHmmss");
  private static final String myFmt6 = new String("yyyyMM");
  private static final String myFmt7 = new String("yyyy");
  private static final String myFmt8 = new String("yyyyMMdd");


  public static String Date8Str(Date date) {
	    return new SimpleDateFormat(myFmt8).format(date);
	  }
  public static String Date7Str(Date date) {
    return new SimpleDateFormat(myFmt7).format(date);
  }

  public static String Date6Str(Date date) {
    return new SimpleDateFormat(myFmt6).format(date);
  }

  public static String Date5Str(Date date) {
    return new SimpleDateFormat(myFmt5).format(date);
  }

  public static String Date4Str(Date date) {
    return new SimpleDateFormat(myFmt4).format(date);
  }

  public static String Date2Str(Date date) {
    return new SimpleDateFormat(myFmt).format(date);
  }

  public static String Date2Str2(Date date) {
    return new SimpleDateFormat(myFmt2).format(date);
  }

  public static Date Str2D(String str) {
    if (null == str) {
      return null;
    }
    try {
      if (str.length() > 10) {
        return new SimpleDateFormat(myFmt).parse(str);
      } else {
        return new SimpleDateFormat(myFmt2).parse(str);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Date Str2D2(String str) {
    try {
      return new SimpleDateFormat(myFmt3, java.util.Locale.ENGLISH).parse(str);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Date Str2D3(String str) {
    if (null == str) {
      return null;
    }
    try {
      return new SimpleDateFormat(myFmt8).parse(str);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Date createDate(int year, int month, int date) {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.clear();
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DAY_OF_MONTH, date);
    return calendar.getTime();
  }

  /*****
   * @author LIYE 字符串转时间
   * @param date
   * @return
   */
  public static Date Str2Date(String date) {
    try {
      SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
      if (date.length() > 10) {
        ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      }
      Date d = ft.parse(date);
      return new java.sql.Date(d.getTime());
    } catch (Exception ex) {
      return new Date(Calendar.getInstance().getTime().getTime());
    }
  }

  /**
   * 获取当前日期的前一天
   *
   * @author JJ
   *
   * @return String format is:yyyy-MM-dd
   */
  public static String getPrevDay() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new java.sql.Date(System.currentTimeMillis()));
    int day = calendar.get(Calendar.DATE);
    calendar.set(Calendar.DATE, day - 1);
    return new SimpleDateFormat(myFmt).format(calendar.getTime());
  }

  /**
   * 获取当前日期的后一天
   *
   * @author JJ
   *
   * @return String format is:yyyy-MM-dd
   */
  public static String getNextDay() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new java.sql.Date(System.currentTimeMillis()));
    int day = calendar.get(Calendar.DATE);
    calendar.set(Calendar.DATE, day + 1);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    return sdf.format(calendar.getTime());
  }

  /**
   * 获取当前日期
   *
   * @return String format is:yyyy-MM-dd
   */
  public static String getCurrentDay() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new java.sql.Date(System.currentTimeMillis()));

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    return sdf.format(calendar.getTime());
  }

  /**
   * 获取当前数据库日期
   *
   * @return String format is:yyyy-MM-dd HH:mm:ss（年月日时分秒）
   */
  public static Date getSysdate() {
    return new Timestamp(System.currentTimeMillis());
  }

  /**
   * 获取当前月
   *
   * @author tzs 2011.06.21 pm
   * @return month
   */
  public static int getCurrentMonth() {
    Calendar ca = Calendar.getInstance();
    int month = ca.get(Calendar.MONTH) + 1;// 获取月
    return month;
  }

  /**
   * //获取当前日期
   *
   * @author tzs
   */
  public static Timestamp getCurrentTimestamp() {
    Timestamp d = new Timestamp(System.currentTimeMillis());
    return d;
  }

  // 获取当前时间
  @SuppressWarnings("deprecation")
  public static Time getCurrentTime() {
    Calendar ca = Calendar.getInstance();
    // int year = ca.get(Calendar.YEAR);// 获取年份
    // int month = ca.get(Calendar.MONTH);// 获取月份
    // int day = ca.get(Calendar.DATE);// 获取日
    int minute = ca.get(Calendar.MINUTE);// 分
    int hour = ca.get(Calendar.HOUR_OF_DAY);// 小时
    int second = ca.get(Calendar.SECOND);// 秒
    // int WeekOfYear = ca.get(Calendar.DAY_OF_WEEK);

    Time ts = new Time(hour, minute, second);
    return ts;
  }

  /**
   * 获取两位日
   *
   * @return
   */
  public static String getDay() {
    SimpleDateFormat formatter;
    formatter = new SimpleDateFormat("dd");
    String ctime = formatter.format(new Date());
    return ctime;
  }

  /**
   * 获取两位小时
   *
   * @return
   */
  public static String getHour() {
    SimpleDateFormat formatter;
    formatter = new SimpleDateFormat("HH");
    String ctime = formatter.format(new Date());
    return ctime;
  }

  /**
   * 获取两位月份
   *
   * @return
   */
  public static String getMonth() {
    SimpleDateFormat formatter;
    formatter = new SimpleDateFormat("MM");
    String ctime = formatter.format(new Date());
    return ctime;
  }

  /**
   * @Description: 返回传入日期的月份 @param: @param date @param: @return @return: String @author: Chen Xinjie @date: 2018年9月30日 下午2:01:24 @throws
   */
  public static String getMonth(String date) {
    if (StringUtils.isNotBlank(date)) {
      Date now = Str2Date(date);
      SimpleDateFormat formatter;
      formatter = new SimpleDateFormat("MM");
      return formatter.format(now);
    }
    return null;
  }

  /**
   * 获取四位年份
   *
   * @return
   */
  public static String getYear() {
    SimpleDateFormat formatter;
    formatter = new SimpleDateFormat("yyyy");
    String ctime = formatter.format(new Date());
    return ctime;
  }

  /**
   * 获取两位年份
   *
   * @return
   */
  public static String getShortYear() {
    SimpleDateFormat formatter;
    formatter = new SimpleDateFormat("yy");
    String ctime = formatter.format(new Date());
    return ctime;
  }

  public static Date getCurrentDate() {
    return new Timestamp(System.currentTimeMillis());
    // Connection conn = null;
    // ResultSet rs = null;
    // Statement stmt = null;
    // Date date = null;
    // try {
    // conn = DateUtil.getJdbcConnection();
    // // conn.setAutoCommit(false);
    // String sql = "select SYSDATE() as count";
    // stmt = conn.createStatement();
    // rs = stmt.executeQuery(sql);
    // if (rs != null) {
    // while (rs.next()) {
    // date = rs.getDate("COUNT");
    // }
    // }
    // // conn.commit();
    // } catch (Exception e) {
    // // logger.errorT("/查询操作异常!SQL:" + sql);
    // e.printStackTrace();
    // } finally {
    // freeConnection(conn, stmt, rs);
    // }
    // return date;
  }

  public static Connection getJdbcConnection() throws NamingException, SQLException {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      return DriverManager.getConnection("jdbc:mysql://10.101.4.65:3306/sc_marketing", "bsdyun", "BSDyun$1976");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      throw new SQLException("未找到驱动oracle.jdbc.OracleDriver");
    }
    // return null;
  }

  public static void freeConnection(Statement stmt, ResultSet rs) {
    try {
      freeConnection(rs);
      freeConnection(stmt);
    } catch (Exception ex) {
      // log.error("释放连接时出错！",ex);
    }
  }

  public static void freeConnection(ResultSet rs) {
    try {
      if (rs != null)
        rs.close();
    } catch (SQLException ex) {
      // log.error("释放连接时出错！",ex);
    }
  }

  public static void freeConnection(Statement stmt) {
    try {
      if (stmt != null)
        stmt.close();
    } catch (SQLException ex) {
      // log.error("释放连接时出错！",ex);
    }
  }

  public static void freeConnection(Connection conn) {
    try {
      if (conn != null)
        conn.close();
    } catch (SQLException ex) {
      // log.error("释放连接时出错！",ex);
    }
  }

  public static void freeConnection(Connection conn, Statement stmt, ResultSet rs) {
    try {
      freeConnection(rs);
      freeConnection(stmt);
      freeConnection(conn);
    } catch (Exception ex) {
      // log.error("释放连接时出错！",ex);
    }
  }

  /**
   * 获取当前周数
   *
   * @author ShenLiang
   *
   * @return int
   */
  public static int getCurWeek() {
    int week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    int weekDay = 0;
    if (week == 1) {
      weekDay = 7;
    } else {
      weekDay = week - 1;
    }
    return weekDay;
  }

  /**
   * 获取当前财务年度（3月31日~4月1日为财务一整年度）
   *
   * @return
   */
  public static String getFiscalYear() {
    SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
    String year = yearFormatter.format(new Date());

    SimpleDateFormat mmformatter = new SimpleDateFormat("MM");
    String month = mmformatter.format(new Date());
    Integer imonth = Integer.valueOf(month);

    String fiscalYear = year;
    if (imonth < 4) {
      Integer i = Integer.valueOf(year) - 1;
      fiscalYear = String.valueOf(i);
    }

    return fiscalYear;
  }

  /**
   * 获取年度
   *
   * @return
   */
  public static String getClosingYearNo() {
    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    if (month >= 4) {
      return String.valueOf(year);
    } else {
      return String.valueOf(year - 1);
    }
  }

  /**
   * 日期比较
   *
   * @param beginDate
   * @param endDate
   * @return
   */
  public static boolean compare(String beginDate, String endDate) {
    Date b = Str2D(beginDate);
    Date e = Str2D(endDate);
    // Date类的一个方法，如果a早于b返回true，否则返回false
    if (b.before(e))
      return true;
    else
      return false;
  }

  public static boolean compare(Date beginDate, String endDate) {
    Date e = Str2D(endDate);
    // Date类的一个方法，如果a早于b返回true，否则返回false
    if (beginDate.before(e))
      return true;
    else
      return false;
  }

  /**
   * 传入生日年月日返回当年年月日 1987-01-01 返回 2018-01-01
   *
   * 12月份生日特殊处理 1987-12-01 生日2018年 12月份领返回 2018-12-01，2019年1月份领也返回2018-12-01
   *
   * @param sapDate
   * @return
   * @throws Exception
   */
  public static Date getBirthday(Date birthday) throws Exception {

    Calendar birthdayca = Calendar.getInstance();
    birthdayca.setTime(birthday);

    Calendar nowca = Calendar.getInstance();
    int year = nowca.get(Calendar.YEAR);
    // 如果十二月份生日会员1月份进来领券，取上一年度时间条件
    if (birthdayca.get(Calendar.MONTH) == 11 && nowca.get(Calendar.MONTH) == 0) {
      year = year - 1;
    }

    GregorianCalendar calendar = new GregorianCalendar();
    calendar.clear();
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, birthdayca.get(Calendar.MONTH));
    calendar.set(Calendar.DAY_OF_MONTH, birthdayca.get(Calendar.DATE));
    return calendar.getTime();
  }

  /**
   * 将八位的年月日格式化成10位带横杠的年月日如：20140404->2014-04-04
   *
   * @param sapDate
   * @return
   * @throws Exception
   */
  public static Date formatSAP8Date(String sapDate) throws Exception {
    boolean isInt = sapDate.matches("[0-9]*");
    if (isInt == false)
      throw new Exception("SAP日期格[" + sapDate + "]式校验错误,不符合8位数字类型日期格式。\n如:20140404");

    String year = sapDate.substring(0, 4);
    String month = sapDate.substring(4, 6);
    String day = sapDate.substring(6, 8);

    if (Integer.valueOf(month) > 12)
      throw new Exception("月份[" + month + "]式校验错误,月份不能大于12个月.");

    if (Integer.valueOf(day) > 31)
      throw new Exception("日期[" + day + "]式校验错误,日期不能大于31天.");

    return new SimpleDateFormat("yyyy-MM-dd").parse(year + "-" + month + "-" + day);
  }

  /**
   * @param beginDate
   *          开始时间
   * @param endDate
   *          结束时间
   * @param dateDiff
   *          时间间隔
   * @param ts
   * @param tsDiff
   *          ts间隔
   * @return
   */
  public static boolean isVaildDate(String beginDate, String endDate, int dateDiff, String ts, int tsDiff) {
    return isVaildDate(Str2D(beginDate), Str2D(endDate), dateDiff, Str2D(ts), tsDiff);
  }

  private final static long TO_DAY = 24 * 3600 * 1000;

  public static boolean isVaildDate(Date beginDate, Date endDate, int dateDiff, Date ts, int tsDiff) {
    if (dateDiff == 0 && tsDiff == 0)
      return false;
    if (beginDate != null && endDate != null) {
      if (((endDate.getTime() - beginDate.getTime()) / TO_DAY) < dateDiff) {
        return true;
      }
    } else if (ts != null) {
      if (((new Date().getTime() - ts.getTime()) / TO_DAY) < tsDiff) {
        return true;
      }
    }
    return false;
  }

  /**
   * @param offset
   *          开始位置
   * @param limit
   *          取行数
   * @param diff
   *          限制行数
   * @return
   */
  public static boolean isVaildLimit(String offset, String limit, int diff) {
    if (StringUtils.isNotBlank(offset) && StringUtils.isNotBlank(limit)) {
      return isVaildLimit(Integer.valueOf(offset), Integer.valueOf(limit), diff);
    }
    return false;
  }

  public static boolean isVaildLimit(int offset, int limit, int diff) {
    if (limit != 0 && limit <= diff && (limit - offset) <= diff) {
      return true;
    }
    return false;
  }

  /**
   * @Description: 两个日期差几天 @param: @param beginDate @param: @param endDate @param: @return @return: int @author: Chen Xinjie @date: 2018年7月12日 下午2:17:45 @throws
   */
  public static int getDayBySub(String beginDate, String endDate) {
    if (StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
      return 0;
    } else {
      return getDayBySub(Str2D(beginDate), Str2D(endDate));

    }

  }

  /**
   * @Description: 两个日期差几天 @param: @param beginDate @param: @param endDate @param: @return @return: int @author: Chen Xinjie @date: 2018年7月12日 下午2:18:20 @throws
   */
  public static int getDayBySub(Date beginDate, Date endDate) {
    if (beginDate == null || endDate == null) {
      return 0;
    } else if (beginDate.before(endDate)) {
      long difference = endDate.getTime() - beginDate.getTime();
      return Long.valueOf((difference / (3600 * 24 * 1000))).intValue();
    } else {
      long difference = beginDate.getTime() - endDate.getTime();
      return Long.valueOf((difference / (3600 * 24 * 1000))).intValue();
    }
  }

  /**
   * @Description 获取某个日期的年份
   * @param date
   * @return
   * @author TangYaJun
   * @date 2018年7月17日 下午3:41:09
   */
  public static int getYear(Date date) {
    Calendar ca = Calendar.getInstance();
    ca.setTime(date);
    int year = ca.get(Calendar.YEAR);// 获取年
    return year;
  }

  /**
   * @Description 获取某个日期的月份
   * @param date
   * @return
   * @author TangYaJun
   * @date 2018年7月17日 下午3:41:09
   */
  public static int getMonth(Date date) {
    Calendar ca = Calendar.getInstance();
    ca.setTime(date);
    int month = ca.get(Calendar.MONTH) + 1;// 获取月
    return month;
  }

  /**
   * @Description 获取某个日期的天数
   * @param date
   * @return
   * @author TangYaJun
   * @date 2018年7月17日 下午3:41:09
   */
  public static int getDays(Date date) {
    Calendar ca = Calendar.getInstance();
    ca.setTime(date);
    int days = ca.get(Calendar.DATE);// 获取天数
    return days;
  }

  /**
   * @Description 将现有时间重新排版
   * @param date
   * @return
   * @author liwen
   * @date 2018年8月23日14:23:30
   */
  public String newDate(String date) {
    String newdate = "";
    String str = date.replaceAll("/", "").replaceAll(":", "").replaceAll(" ", "");
    if (str.length() == 8) {
      newdate = str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6, 8);
    }
    if (str.length() > 8) {
      newdate = str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6, 8) + " " + str.substring(8, 10) + ":" + str.substring(10, 12) + ":" + str.substring(12, 14);
    }
    return newdate;
  }

  public static String fomateDateString(String dateStr1, String fmtOld, String fmtNew) throws ParseException {
    SimpleDateFormat formate1 = new SimpleDateFormat(fmtOld);
    SimpleDateFormat formate2 = new SimpleDateFormat(fmtNew);
    Date date1 = formate1.parse(dateStr1);
    String dateStr2 = formate2.format(date1);
    return dateStr2;
  }

  /**
   * 时间比较(间隔不能超过一天) timeComparison
   *
   * @param date
   * @param date2
   * @return boolean
   * @author geng saifei
   * @date 2018年10月9日
   */
  public static boolean timeComparison(Date date, Date date2) {
    boolean result = false;
    if (date2.getTime() - date.getTime() > 24 * 3600000) {// 相差的时间不能大于一天
      result = true;
    }
    return result;
  }

  /**
   * 获取 Unix时间戳
   *
   * @return
   * @throws Exception
   */
  public static String getUnixDate() throws Exception {
    Timestamp appointTime = Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    Date date = df.parse(String.valueOf(appointTime));
    long s = date.getTime();
    return String.valueOf(s).substring(0, 10);
  }

  /*
   * 将时间戳转换为时间
   */
  public static String stampToDate(String s) {
    String res;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    long lt = new Long(s);
    Date date = new Date(lt);
    res = simpleDateFormat.format(date);
    return res;
  }

  /**
   * 七天前的日期
   *
   * @return
   */
  public static String lastSevenDay() {
    SimpleDateFormat sdf = new SimpleDateFormat(myFmt);
    Calendar theCa;
    Date start;
    theCa = Calendar.getInstance();
    theCa.add(Calendar.DATE, -7);
    start = theCa.getTime();
    return sdf.format(start);
  }
  //获取yyyyMMdd
  public static String getSysDay() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new java.sql.Date(System.currentTimeMillis()));
    int day = calendar.get(Calendar.DATE);
    calendar.set(Calendar.DATE, day - 1);
    return new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
  }

  //日期加减
  public static Date dateAdd(Date date, String dateType, Integer interval) {
    if(date == null || StringUtils.isBlank(dateType) || interval == null)
      return new Date();
    if(!Arrays.asList(new String[]{"year","month","week","day","hour"}).contains(dateType)){
      return new Date();
    }
    Calendar ca = Calendar.getInstance();
    ca.setTime(date);
    if("year".equals(dateType)){
      ca.add(Calendar.YEAR,interval);
      return ca.getTime();
    } else if("month".equals(dateType)){
      ca.add(Calendar.MONTH,interval);
      return ca.getTime();
    } else if("week".equals(dateType)){
      ca.add(Calendar.WEEK_OF_MONTH,interval);
      return ca.getTime();
    } else if("day".equals(dateType)){
      ca.add(Calendar.DAY_OF_MONTH,interval);
      return ca.getTime();
    } else if("hour".equals(dateType)){
      ca.add(Calendar.HOUR,interval);
      return ca.getTime();
    }
    return new Date();
  }

  //获取今年第几周
  public static int weekOfYear(){
    Calendar calendar = Calendar.getInstance();
    calendar.setFirstDayOfWeek(Calendar.MONDAY);
    calendar.setMinimalDaysInFirstWeek(4);
    calendar.setTimeInMillis(System.currentTimeMillis());
    int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
    return weekOfYear;
  }
}
