package com.nutz.framework.utils.tools;
/*package com.nutz.framework.utils;


import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;
import dao.DB;
import dao.Remoteserver;
import file.FileVO;
import file.file;
import function.Ini;
import i18n.Messages;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import log.log;
import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.Client;
import org.codehaus.xfire.client.XFireProxy;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;
import sys.OnlineList;
import sys.ServerInfo;
import test.IServerService;
import user.userImpl;

public class CommonUtil
{
  public static final String FILE_BACKUP_PATH = "D:/tomcat 5.0/bin";
  public static final int OPT_BIG = 0;
  public static final int OPT_SMALL = 1;
  public static final int OPT_EQUAL = 2;
  public static final int OPT_BIGEQUAL = 3;
  public static final int OPT_SMALLEQUAL = 4;
  public static final int OPT_NOTEQUAL = 5;
  public static final int SAME_ID_SAME_IP = 1;
  public static final int SAME_ID_NOT_SAME_IP = 2;
  public static final int NO_ALL = 3;
  private static final int CPUTIME = 30;
  private static final int PERCENT = 100;
  private static final int FAULTLENGTH = 10;

  public static void showMessageDialog(HttpServletResponse response, String strMessage, String strJsp, boolean bBack)
    throws IOException
  {
    PrintWriter out = response.getWriter();
    if (bBack)
      out.println("<script>alert('" + strMessage + 
        "');location='javascript:history.go(-1);'</script>");
    else if (strJsp.equals(""))
      out.println("<script>alert('" + strMessage + "');</script>");
    else if (strMessage.equals(""))
      out.println("<script>parent.location.href='" + strJsp + 
        "';</script>");
    else
      out.println("<script>alert('" + strMessage + 
        "');document.location='" + strJsp + "';</script>");
  }

  public static Remoteserver remoteLoginValid(String ip, String name, String password)
  {
    try {
      if (pingHost(ip, 10000)) break label42;
      System.out.println(ip + " time out! 10s");
      label42: return null;
    }
    catch (IOException srvcModel) {
      e1.printStackTrace();

      Service srvcModel = new ObjectServiceFactory().create
        (IServerService.class);

      XFireProxyFactory factory = new XFireProxyFactory(
        XFireFactory.newInstance().getXFire());

      String helloWorldURL = "http://" + ip + "/services/ServerLogin";
      try
      {
        IServerService srvc = (IServerService)factory.create(srvcModel, 
          helloWorldURL);
        Client client = ((XFireProxy)Proxy.getInvocationHandler(srvc)).getClient
          ();

        client.setProperty("gzip.response.enabled", 
          Boolean.TRUE);

        client.setProperty("http.timeout", "0");
        ServerInfo si = srvc.getServersInfo(ip, name, password);
        if ((si != null) && (!("-1".equals(si.getChannelcount())))) {
          String[] status = si.getStatus();
          writeIntoDB(ip, status, si.getStrChannelAlias(), 
            si.getChannelcount());
          Remoteserver server = new Remoteserver();
          server.setChannelcount(si.getChannelcount());
          int intchannelcount = 
            Integer.parseInt(server.getChannelcount());
          int[] intChannelstatus = new int[intchannelcount];

          for (int i = 0; i < intchannelcount; ++i)
            if (status[i] != null)
              intChannelstatus[i] = Integer.parseInt(status[i]);
            else
              intChannelstatus[i] = 0;


          server.setChannelStatus(intChannelstatus);
          server.setConnected(true);
          server.setRemark(si.getRemark());
          server.setChannelAlias(si.getStrChannelAlias());

          return server;
        }
        return null;
      }
      catch (MalformedURLException e)
      {
        return null; } catch (Exception e) {
      }
    }
    return null;
  }

  public static boolean pingRemoteServer(int serverid)
  {
    String sql = "select isconnected from remoteserver where id =" + 
      serverid;
    Connection con = null;
    try {
      con = DB.getConnection();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }

    Statement st = null;
    try {
      st = con.createStatement();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }

    ResultSet rs = DB.query(sql, st);
    if (DB.next(rs)) {
      boolean iscon = DB.getBool(rs, "isconnected");
      try
      {
        st.close();
      }
      catch (SQLException e) {
        e.printStackTrace();
      }
      try {
        con.close();
      }
      catch (SQLException e) {
        e.printStackTrace();
      }

      return iscon;
    }
    try {
      st.close();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    try {
      con.close();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }

    return false;
  }

  public static String CheckIP(String strIP)
  {
    String[] str = strIP.split("\\.", 5);
    int nLen = str.length;

    if (nLen != 4)
      return "";

    if (!(IsNum(str[0])))
      return "";

    if (!(IsNum(str[1])))
      return "";

    if (!(IsNum(str[2])))
      return "";

    if (!(IsNum(str[3])))
      return "";

    int nSub = 0;
    nSub = Integer.parseInt(str[0]);
    if ((nSub <= 0) || (nSub > 255))
      return "";

    String strResult = Integer.toString(nSub);
    strResult = strResult + ".";

    nSub = Integer.parseInt(str[1]);
    if ((nSub < 0) || (nSub > 255))
      return "";

    strResult = strResult + Integer.toString(nSub);
    strResult = strResult + ".";

    nSub = Integer.parseInt(str[2]);
    if ((nSub < 0) || (nSub > 255))
      return "";

    strResult = strResult + Integer.toString(nSub);
    strResult = strResult + ".";

    nSub = Integer.parseInt(str[3]);
    if ((nSub <= 0) || (nSub >= 255))
      return "";

    strResult = strResult + Integer.toString(nSub);

    return strResult;
  }

  public static boolean IsNum(String str) {
    if (str.equals(""))
      return false;

    for (int i = 0; i < str.length(); ++i)
      if ((str.charAt(i) < '0') || (str.charAt(i) > '9'))
        return false;

    return true;
  }

  public static String getLocalhostAddress() {
    try {
      return String.valueOf(InetAddress.getLocalHost().getHostAddress());
    }
    catch (UnknownHostException e) {
      e.printStackTrace(); }
    return "";
  }

  public static String separateStr(String str)
  {
    if (str.trim().length() == 0)
      return "";
    int lenght = 10;
    int i = str.length() / lenght;
    if (i == 0)
      return str;
    String s = str.substring(0, lenght);
    s = s + "<br>";

    for (int j = 1; j < i; ++j)
      if (j + 1 < i) {
        s = s + str.substring(j * lenght, (j + 1) * lenght);
        s = s + "<br>";
      } else {
        s = s + str.substring(j * lenght, str.length());
      }

    return s;
  }

  public static String getLocalhostName() {
    try {
      return String.valueOf(InetAddress.getLocalHost().getHostName());
    }
    catch (UnknownHostException e) {
      e.printStackTrace(); }
    return "";
  }

  public static int getYear(Calendar cal)
  {
    return cal.get(1);
  }

  public static int getMonth(Calendar cal) {
    return (cal.get(2) + 1);
  }

  public static int getDay(Calendar cal) {
    return cal.get(5);
  }

  public static int getHour(Calendar cal) {
    return cal.get(11);
  }

  public static int getMinute(Calendar cal) {
    return cal.get(12);
  }

  public static int getSecond(Calendar cal) {
    return cal.get(13);
  }

  public static String GetCurTime() {
    Calendar calNow = Calendar.getInstance();
    return formatCalendar(calNow, "yyyy-MM-dd HH:mm:ss");
  }

  public static String getCurTime(String strFormat) {
    Calendar calNow = Calendar.getInstance();
    return formatCalendar(calNow, strFormat);
  }

  public static String formatCalendar(Calendar cal, String strFormat) {
    SimpleDateFormat m = new SimpleDateFormat(strFormat);
    return m.format(cal.getTime());
  }

  public static String getFileCreateDate(File _file) {
    File file = _file;
    try {
      String str = "cmd.exe /c dir \"" + file.getAbsolutePath() + 
        "\" /tc";
      Process ls_proc = Runtime.getRuntime().exec(str);
      DataInputStream in1 = new DataInputStream(ls_proc.getInputStream());
      InputStreamReader in2 = new InputStreamReader(in1);
      BufferedReader in = new BufferedReader(in2);

      for (int i = 0; i < 5; ++i) {
        in.readLine();
      }

      String stuff = in.readLine();
      if (stuff == null)
        stuff = in.readLine();

      if (stuff == null)
        return "";
      StringTokenizer st = new StringTokenizer(stuff);
      if (st == null)
        return "";
      String dateC = st.nextToken();
      String time = st.nextToken();
      if (time.indexOf(":") == -1)
        time = st.nextToken();

      if (time.indexOf(97) != -1)
        time = time.replace('a', ' ');

      if (time.indexOf(112) != -1)
        time = time.replace('p', ' ');

      dateC = dateC + " " + time.trim() + ":00";
      in.close();
      return dateC; } catch (Exception e) {
    }
    return null;
  }

  public static boolean compareCalendar(String strBegin, String strEnd)
  {
    Calendar begin = getCalendar(strBegin);
    Calendar end = getCalendar(strEnd);
    return begin.before(end);
  }

  public static boolean compareCalendar(String strBegin, String strEnd, String format)
  {
    Calendar begin = getCalendar(strBegin, format);
    Calendar end = getCalendar(strEnd, format);
    return begin.before(end);
  }

  public static Calendar getCalendar(String str) {
    SimpleDateFormat myFormatter = new SimpleDateFormat(
      "yyyy-MM-dd HH:mm:ss");
    Calendar calNow = Calendar.getInstance();
    try {
      calNow.setTime(myFormatter.parse(str));
    } catch (ParseException ex) {
      ex.printStackTrace(System.err);
      return null;
    }

    return calNow;
  }

  public static boolean equalCalendar(String strBegin, String strEnd) {
    if ((strBegin.trim().length() == 0) && (strEnd.trim().length() != 0))
      return false;

    Calendar begin = getCalendar(strBegin);
    Calendar end = getCalendar(strEnd);
    if (begin.before(end))
      return false;

    return (!(end.before(begin)));
  }

  public static String calendar2String(Calendar cal2)
  {
    int date = cal2.get(5);
    int month = cal2.get(2) + 1;
    int year = cal2.get(1);
    int hour = cal2.get(11);
    int minute = cal2.get(12);
    int second = cal2.get(13);
    String time = year + "-" + month + "-" + date + " " + hour + ":" + 
      minute + ":" + second;
    return time;
  }

  public static boolean checkRight(int nRight, int nOpt, HttpServletRequest request, HttpServletResponse response)
    throws IOException
  {
    HttpSession session = request.getSession(true);
    if (session == null) {
      logOut(false, request, response);
      return false;
    }

    userImpl ur = (userImpl)session.getAttribute("userinfo");
    if (ur == null) {
      logOut(false, request, response);
      return false;
    }

    switch (nOpt)
    {
    case 0:
      if (ur.getRight() <= nRight) {
        logOut(false, request, response);
        return false;
      }

    case 1:
      if (ur.getRight() >= nRight) {
        logOut(false, request, response);
        return false;
      }

    case 2:
      if (ur.getRight() != nRight) {
        logOut(false, request, response);
        return false;
      }

    case 3:
      if (ur.getRight() < nRight) {
        logOut(false, request, response);
        return false;
      }

    case 4:
      if (ur.getRight() > nRight) {
        logOut(false, request, response);
        return false;
      }

    case 5:
      if (ur.getRight() == nRight) {
        logOut(false, request, response);
        return false;
      }
    }

    return true;

    return true;
  }

  public static userImpl logOut(boolean bLogin, HttpServletRequest request, HttpServletResponse response) throws IOException
  {
    HttpSession session = request.getSession(false);
    userImpl ur = null;
    if (session != null) {
      ur = (userImpl)session.getAttribute("userinfo");
      if (ur != null)
        if (bLogin)
          log.AddLog(Messages.getString("top.logout"), 
            2, session);

      else if (bLogin)
        ur = new userImpl();

      if (!(bLogin)) {
        log.AddLog(Messages.getString("top.logout"), 2, 
          session);

        session.removeAttribute("userinfo");
        session.invalidate();
      }
    }

    if (!(bLogin))
      showMessageDialog(response, "", "Login_out", false);
    return ur;
  }

  public static boolean checkFileName(String strFileName) {
    if (strFileName.indexOf("\\") != -1)
      return false;

    if (strFileName.indexOf("/") != -1)
      return false;

    if (strFileName.indexOf(":") != -1)
      return false;

    if (strFileName.indexOf("*") != -1)
      return false;

    if (strFileName.indexOf("\"") != -1)
      return false;

    if (strFileName.indexOf("<") != -1)
      return false;

    if (strFileName.indexOf(">") != -1)
      return false;

    if (strFileName.indexOf("|") != -1)
      return false;

    if (strFileName.indexOf("'") != -1)
      return false;

    if (strFileName.indexOf("?") != -1)
      return false;

    if (strFileName.indexOf("#") != -1)
      return false;

    if (strFileName.indexOf("%") != -1) {
      return false;
    }

    return (strFileName.indexOf(";") == -1);
  }

  public static boolean pingHost(String ip, int timeout)
    throws IOException
  {
    return true;
  }

  public static void writeIntoDB(String ip, String[] status, String[] channelAlias, String channelNumber)
  {
    if (ip == null) {
      throw new IllegalArgumentException("ip is null!");
    }

    if (status == null) {
      throw new IllegalArgumentException("status is null");
    }

    for (int i = 0; i < status.length; ++i)
    {
      String sql;
      if (status[i] != null) {
        sql = "update remoteserver set channal" + i + "='" + 
          status[i] + "', channalCount= " + channelNumber + 
          "  where ip = '" + ip + "'";
        System.out.println(sql);

        DB.execute(sql);
      } else {
        sql = "update remoteserver set channal" + i + "='" + 0 + 
          "' where ip = '" + ip + "'";
        DB.execute(sql);
      }

      if (channelAlias[i] != null) {
        sql = "update remoteserver set channel" + i + "alias='" + 
          channelAlias[i] + "' " + " where ip = '" + ip + "'";
        DB.execute(sql);
      }
    }
  }

  public static boolean CheckCalendar(String str1, String str2, int nMs)
  {
    if (!(checkCalendar(str1, "yyyy-MM-dd HH:mm:ss")))
      return false;
    if (!(checkCalendar(str2, "yyyy-MM-dd HH:mm:ss")))
      return false;

    Calendar cal1 = getCalendar(str1);
    Calendar cal2 = getCalendar(str2);
    long n = cal1.getTimeInMillis() - cal2.getTimeInMillis();

    return ((n >= -nMs) && (n <= nMs));
  }

  public static boolean checkCalendar(String str, String strFormat)
  {
    SimpleDateFormat myFormatter = new SimpleDateFormat(strFormat);
    Calendar calNow = Calendar.getInstance();
    try {
      calNow.setTime(myFormatter.parse(str));
    } catch (ParseException ex) {
      ex.printStackTrace(System.err);
      return false;
    }

    return true;
  }

  public static boolean synTime(String strClient)
  {
    Calendar cal = getCalendar(strClient, "yyyy年MM月dd日 HH:mm:ss");
    if (cal == null)
      cal = getCalendar(strClient, "yyyy-MM-dd HH:mm:ss");
    if (cal == null)
      cal = getCalendar(strClient, "MM/dd/yyyy HH:mm:ss");
    if (cal == null)
      cal = getCalendar(strClient, "yyyy MM dd HH:mm:ss");
    if (cal == null)
      cal = getCalendar(strClient, "dddd yyyy MM dd HH:mm:ss");

    int nYear = getYear(cal);
    int nMonth = getMonth(cal);
    int nDay = getDay(cal);
    int nHour = getHour(cal);
    int nMin = getMinute(cal);
    int nSec = getSecond(cal);

    return (file.SynSystemTime(nYear, nMonth, nDay, nHour, nMin, nSec));
  }

  public static Calendar getCalendar(String str, String strFormat)
  {
    SimpleDateFormat myFormatter = new SimpleDateFormat(strFormat);
    Calendar calNow = Calendar.getInstance();
    try {
      calNow.setTime(myFormatter.parse(str));
    } catch (ParseException ex) {
      ex.printStackTrace(System.err);
      return null;
    }

    return calNow;
  }

  public static void zipFile(String[] filenames, String outFilename)
  {
    byte[] buf = new byte[1024];
    try
    {
      ZipOutputStream out = new ZipOutputStream(
        new FileOutputStream(outFilename));

      for (int i = 0; i < filenames.length; ++i) {
        int len;
        FileInputStream in = new FileInputStream(filenames[i]);

        out.putNextEntry(new ZipEntry(filenames[i]));

        while ((len = in.read(buf)) > 0) {
          out.write(buf, 0, len);
        }

        out.closeEntry();
        in.close();
      }

      out.close();
    } catch (IOException e) {
      System.out.println(e.toString());
      log.AddSystemLog(e.getMessage(), 5);
    }
  }

  public static void upzipFile(String inFilename)
  {
    ZipInputStream in;
    try
    {
      in = new ZipInputStream(
        new FileInputStream(inFilename));

      ZipEntry entry = null;
      while ((entry = in.getNextEntry()) != null) {
        int len;
        String name = entry.getName();

        String outFilename = "D:/tomcat 5.0/bin/" + name;
        OutputStream out = new FileOutputStream(outFilename);

        byte[] buf = new byte[1024];

        while ((len = in.read(buf)) > 0) {
          out.write(buf, 0, len);
        }

        out.close();
      }
      in.close();
    } catch (IOException e) {
      System.out.println(e.toString());
    }
  }

  public static void restartSys(String ip)
  {
    Service srvcModel = new ObjectServiceFactory().create
      (IServerService.class);

    XFireProxyFactory factory = new XFireProxyFactory(
      XFireFactory.newInstance().getXFire());

    String helloWorldURL = "http://" + ip + "/services/ServerLogin";
    try
    {
      IServerService srvc = (IServerService)factory.create(srvcModel, 
        helloWorldURL);
      Client client = ((XFireProxy)Proxy.getInvocationHandler(srvc)).getClient
        ();

      client.setProperty("gzip.response.enabled", 
        Boolean.TRUE);

      client.setProperty("http.timeout", "0");
      srvc.restartSys();
    }
    catch (MalformedURLException e) {
      e.printStackTrace();
      Throwable t = e.getCause();
      if (t == null) return;
      System.out.println(t.getMessage());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void netpageCapt(String url, String ip) throws IOException
  {
    String execstr = "D:\\netpagecapt\\IECapt.exe " + url + 
      " D:\\netpagecapt\\" + ip + ".jpg";

    Runtime.getRuntime().exec(execstr);
  }

  public static void copyFile(String src, String target)
    throws Exception
  {
    File in = new File(src);
    if (in.exists()) {
      File out = new File(target);
      FileInputStream fis = new FileInputStream(in);
      FileOutputStream fos = new FileOutputStream(out);
      byte[] buf = new byte[1024];
      int i = 0;
      while ((i = fis.read(buf)) != -1)
        fos.write(buf, 0, i);

      fis.close();
      fos.close();
    }
  }

  public static BufferedImage resize(BufferedImage source, int targetW, int targetH)
  {
    int type = source.getType();
    BufferedImage target = null;
    double sx = targetW / source.getWidth();
    double sy = targetH / source.getHeight();

    if (sx > sy) {
      sx = sy;
      targetW = (int)(sx * source.getWidth());
    } else {
      sy = sx;
      targetH = (int)(sy * source.getHeight());
    }
    if (type == 0) {
      ColorModel cm = source.getColorModel();
      WritableRaster raster = cm.createCompatibleWritableRaster(targetW, 
        targetH);
      boolean alphaPremultiplied = cm.isAlphaPremultiplied();
      target = new BufferedImage(cm, raster, alphaPremultiplied, null);
    } else {
      target = new BufferedImage(targetW, targetH, type); }
    Graphics2D g = target.createGraphics();

    g.setRenderingHint(RenderingHints.KEY_RENDERING, 
      RenderingHints.VALUE_RENDER_QUALITY);
    g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
    g.dispose();
    return target;
  }

  public static void saveImageAsJpg(String fromFileStr, String saveToFileStr, int width, int hight)
    throws IOException
  {
    String imgType = "JPEG";
    if (fromFileStr.toLowerCase().endsWith(".png")) {
      imgType = "PNG";
    }

    File saveFile = new File(saveToFileStr);
    File fromFile = new File(fromFileStr);
    if (!(fromFile.exists()))
      return;
    BufferedImage srcImage = ImageIO.read(fromFile);
    if ((width > 0) || (hight > 0))
      srcImage = resize(srcImage, width, hight);

    ImageIO.write(srcImage, imgType, saveFile);
  }

  public static void cutImageJpg(String srcpath, String subpath, int x, int y, int width, int height) throws IOException
  {
    FileInputStream is = null;
    ImageInputStream iis = null;
    File f = new File(srcpath);
    if (!(f.exists())) return;

    f = new File(subpath);
    if (f.exists()) return;
    try
    {
      is = new FileInputStream(srcpath);

      Iterator it = ImageIO.getImageReadersByFormatName("jpg");
      ImageReader reader = (ImageReader)it.next();

      iis = ImageIO.createImageInputStream(is);

      reader.setInput(iis, true);

      ImageReadParam param = reader.getDefaultReadParam();

      Rectangle rect = new Rectangle(x, y, width, height);

      param.setSourceRegion(rect);

      BufferedImage bi = reader.read(0, param);

      ImageIO.write(bi, "jpg", new File(subpath));
    } finally {
      if (is != null)
        is.close();
      if (iis != null)
        iis.close();
    }
  }

  public static int checkLogin(int nId, String strIp)
  {
    for (int i = 0; i < OnlineList.getCount(); ++i) {
      userImpl ur = OnlineList.getListUser(i);
      if (ur == null)
        break;

      if (ur.getId() == nId) {
        if (ur.getIp() == strIp)
          return 1;

        HttpSession session = OnlineList.GetList(i);
        log.AddLog(Messages.getString("fUser") + ur.getName() + 
          Messages.getString("logoutOtherWhere") + 
          ur.getIp() + Messages.getString("newIP") + strIp, 
          2, session);
        session.removeAttribute("userinfo");
        session.invalidate();
        OnlineList.RemoveSession(session);
        session = null;
        return 2;
      }
    }

    return 3;
  }

  public static String getFreeDiskSpace(String dirName)
  {
    String str = file.GetDiskInfo(dirName);
    return str;
  }

  public static String getStrFreddSpace(String disk) {
    String size = getFreeDiskSpace(disk);

    return "UNKNOWN";
  }

  public static String getDiskPercentEx() {
    String strDiskInfo = "";
    strDiskInfo = file.GetDiskInfo(Ini.getFileSaveDisk() + ":\\");
    String strPercent = getDiskPercent(strDiskInfo);
    float fPercent = Float.parseFloat(strPercent.substring(0, 
      strPercent.length() - 1));
    if (fPercent - 10.0F < 0F)
      return "<span class=\"style1\">" + strPercent + 
        Messages.getString("alartwarning") + "</span>";

    return "<strong>" + strPercent + "</strong>";
  }

  public static boolean lessThanDiskPercentEx() {
    String strDiskInfo = "";
    strDiskInfo = file.GetDiskInfo(Ini.getFileSaveDisk() + ":\\");
    String strPercent = getDiskPercent(strDiskInfo);
    float fPercent = Float.parseFloat(strPercent.substring(0, 
      strPercent.length() - 1));

    return (fPercent - 10.0F < 0F);
  }

  public static String getDiskPercent(String strDiskInfo)
  {
    if (strDiskInfo.equals(""))
      return "";
    String[] str = strDiskInfo.split(" ");
    return str[2];
  }

  public static boolean isARECServer(String ip) throws SQLException {
    Connection con = DB.getConnection();
    Statement stm = con.createStatement();
    String sql = "select 1 from remoteserver where ip ='" + ip + "'";
    ResultSet rs = DB.query(sql, stm);
    if (DB.next(rs)) {
      if (stm != null)
        stm.close();

      if (stm != null)
        con.close();

      return true;
    }
    if (stm != null)
      stm.close();

    if (stm != null)
      con.close();

    return false;
  }

  public static String getRemoteServerRemark(String ip) throws SQLException {
    Connection con = DB.getConnection();
    Statement stm = con.createStatement();
    String sql = "select verinfo from remoteserver where ip ='" + ip + "'";
    ResultSet rs = DB.query(sql, stm);

    if (DB.next(rs)) {
      String remark = DB.GetString(rs, "verinfo");
      if (stm != null)
        stm.close();

      if (stm != null)
        con.close();

      return remark;
    }
    if (stm != null)
      stm.close();

    if (stm != null)
      con.close();

    return "";
  }

  public static void restartLocalServer() {
    try {
      Runtime.getRuntime().exec("D:\\Data\\restartsys.exe");
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static int getUserId(String username) {
    String sql = "select id from user where name='" + username + "'";
    Connection conn = null;
    try {
      conn = DB.getConnection();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    Statement st = null;
    try {
      st = conn.createStatement();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    ResultSet rs = DB.query(sql, st);
    if (DB.next(rs)) {
      int id = DB.getInt(rs, "id");
      return id;
    }
    return -1;
  }

  public static String getUserName(String userid) {
    String sql = "select name from user where id='" + userid + "'";
    Connection conn = null;
    try {
      conn = DB.getConnection();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    Statement st = null;
    try {
      st = conn.createStatement();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    ResultSet rs = DB.query(sql, st);
    if (DB.next(rs)) {
      String name = DB.GetString(rs, "name");
      DB.closeAll(st);
      return name;
    }
    DB.closeAll(st);
    return "";
  }

  public static boolean isNum(String str) {
    if (str.equals(""))
      return false;

    for (int i = 0; i < str.length(); ++i)
      if ((str.charAt(i) < '0') || (str.charAt(i) > '9'))
        return false;


    return true;
  }

  public static List getFtpServerFileList(String ip, String port, String username, String password)
    throws IOException, FTPException, ParseException
  {
    List list = new ArrayList();
    FTPClient f = new FTPClient();

    f.setRemoteHost(ip);

    f.setRemotePort(Integer.parseInt(port));

    f.setControlEncoding(Messages.getString("charset"));

    f.connect();

    f.setTimeout(4000);

    f.login(username, password);

    f.setConnectMode(FTPConnectMode.ACTIVE);

    f.setType(FTPTransferType.BINARY);

    String[] files = f.dir(".", false);

    for (int i = 0; i < files.length; ++i) {
      String name = files[i];
      System.out.println(f.fileDetails(name));
      FileVO vo = new FileVO();
      vo.setFilename(files[i]);
      list.add(vo);
    }

    f.quit();

    return null;
  }

  public static double getCpuRatioForWindows()
  {
    String procCmd;
    try
    {
      procCmd = System.getenv("windir") + 
        "\\system32\\wbem\\wmic.exe process get Caption,CommandLine,KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";

      long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
      Thread.sleep(30L);
      long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
      if ((c0 != null) && (c1 != null)) {
        long idletime = c1[0] - c0[0];
        long busytime = c1[1] - c0[1];
        return Double.valueOf(
          100L * busytime / (busytime + idletime)).doubleValue
          ();
      }
      return 0D;
    }
    catch (Exception ex) {
      ex.printStackTrace(); }
    return 0D;
  }

  private static long[] readCpu(Process proc)
  {
    long[] retn = new long[2];
    try {
      long[] arrayOfLong1;
      proc.getOutputStream().close();
      InputStreamReader ir = new InputStreamReader(proc.getInputStream());
      LineNumberReader input = new LineNumberReader(ir);
      String line = input.readLine();
      if ((line == null) || (line.length() < 10))
        return null;

      int capidx = line.indexOf("Caption");
      int cmdidx = line.indexOf("CommandLine");
      int rocidx = line.indexOf("ReadOperationCount");
      int umtidx = line.indexOf("UserModeTime");
      int kmtidx = line.indexOf("KernelModeTime");
      int wocidx = line.indexOf("WriteOperationCount");
      long idletime = 1180347477836955648L;
      long kneltime = 1180347477836955648L;
      long usertime = 1180347477836955648L;
      while ((line = input.readLine()) != null) {
        if (line.length() < wocidx) {
          continue;
        }

        String caption = Bytes.substring(line, capidx, cmdidx - 1).trim
          ();
        String cmd = Bytes.substring(line, cmdidx, kmtidx - 1).trim();
        if (cmd.indexOf("wmic.exe") >= 0)
          continue;

        String s1 = Bytes.substring(line, kmtidx, rocidx - 1).trim();
        String s2 = Bytes.substring(line, umtidx, wocidx - 1).trim();
        if ((caption.equals("System Idle Process")) || 
          (caption.equals("System"))) {
          if (s1.length() > 0)
            idletime += Long.valueOf(s1).longValue();
          if (s2.length() > 0)
            idletime += Long.valueOf(s2).longValue();
        }
        else {
          if (s1.length() > 0)
            kneltime += Long.valueOf(s1).longValue();
          if (s2.length() > 0)
            usertime += Long.valueOf(s2).longValue(); 
        }
      }
      retn[0] = idletime;
      retn[1] = (kneltime + usertime);
      return retn;
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        proc.getInputStream().close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public static boolean isCGISystem(String sysInfo)
  {
    return (sysInfo.indexOf("Type:(HDR-CGI)") != -1);
  }

  public static void startupRmServer()
  {
    try
    {
      Runtime.getRuntime().exec("");
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void shutdownRmServer() {
    try {
      Runtime.getRuntime().exec("");
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void setRmElement()
  {
  }

  public static final String MD5(String s) {
    char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
      'A', 'B', 'C', 'D', 'E', 'F' };
    try {
      byte[] btInput = s.getBytes();
      MessageDigest mdInst = MessageDigest.getInstance("MD5");
      mdInst.update(btInput);
      byte[] md = mdInst.digest();
      int j = md.length;
      char[] str = new char[j * 2];
      int k = 0;
      for (int i = 0; i < j; ++i) {
        byte byte0 = md[i];
        str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
        str[(k++)] = hexDigits[(byte0 & 0xF)];
      }
      return new String(str); } catch (Exception e) {
    }
    return null;
  }
}*/