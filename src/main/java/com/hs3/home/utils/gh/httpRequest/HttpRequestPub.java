package com.hs3.home.utils.gh.httpRequest;


import com.hs3.utils.StrUtils;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Get/Post
 *
 * @author Stephen Zhou
 */
public class HttpRequestPub {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestPub.class);
    //public static String URL = "http://115.28.191.62/web/control";

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        //HttpRequestPub.postEmail("support@sendgrid.com", "测试验证码", "您的验证码：135968");

        //String to = "15673751509";
        //String smsContent = "【凯胜科技】尊敬的用户，您的验证码为111666，5分钟输入有效！";
        // 提交请求
        //String result = postSMS(to, smsContent);
        //System.out.println("result:" + System.lineSeparator() + result);

    }

    public static String postSMSZ(String toPhone, String phoneCode) {

        String URL = "https://api.miaodiyun.com/20150822/industrySMS/sendSMS";

        JSONObject json = new JSONObject();
        json.put("accountSid", "81640e3a2cca4c51b17bd18a48257d28");
        json.put("smsContent", phoneCode);
        json.put("to", toPhone);
        json.put("timestamp", "20180421115700");
        json.put("sig", StrUtils.MD5("81640e3a2cca4c51b17bd18a48257d28" + "9e3501c3a3424782babdbc02ab27627b" + "20180421115700"));
        json.put("respDataType", "JSON");


        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(URL);

        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        String result = "";

        try {

            StringEntity s = new StringEntity(json.toString(), "utf-8");
            s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                    "application/json"));
            post.setEntity(s);

            // 发送请求
            HttpResponse httpResponse = client.execute(post);

            // 获取响应输入流
            InputStream inStream = httpResponse.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inStream, "utf-8"));
            StringBuilder strber = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
                strber.append(line + "\n");
            inStream.close();

            result = strber.toString();
            logger.info(result);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                logger.info("请求服务器成功，做相应处理");

            } else {

                logger.error("请求服务端失败, url :{}, data:{}", new Object[]{URL, json.toString()});

            }


        } catch (Exception e) {
            logger.error("请求异常");
            throw new RuntimeException(e);
        }

        return result;
    }

    public static String post(String URL, JSONObject json) {

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(URL);

        post.setHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Basic YWRtaW46");

        String result = "";

        try {

            StringEntity s = new StringEntity(json.toString(), "utf-8");
            s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                    "application/json"));
            post.setEntity(s);

            // 发送请求
            HttpResponse httpResponse = client.execute(post);

            // 获取响应输入流
            InputStream inStream = httpResponse.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inStream, "utf-8"));
            StringBuilder strber = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
                strber.append(line + "\n");
            inStream.close();

            result = strber.toString();
            logger.info(result);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                logger.info("请求服务器成功，做相应处理");

            } else {

                logger.error("请求服务端失败, url :{}, data:{}", new Object[]{URL, json.toString()});

            }


        } catch (Exception e) {
            logger.error("请求异常");
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * 发送POST请求
     *
     * @param url        目的地址
     * @param parameters 请求参数，Map类型。
     * @return 远程响应结果
     */
    public static String sendPost(String url, Map<String, String> parameters) {
        String result = "";// 返回的结果
        BufferedReader in = null;// 读取响应输入流
        PrintWriter out = null;
        StringBuffer sb = new StringBuffer();// 处理请求参数
        String params = "";// 编码之后的参数
        try {
            // 编码请求参数
            if (parameters.size() == 1) {
                for (String name : parameters.keySet()) {
                    sb.append(name).append("=").append(
                            java.net.URLEncoder.encode(parameters.get(name),
                                    "UTF-8"));
                }
                params = sb.toString();
            } else {
                for (String name : parameters.keySet()) {
                    sb.append(name).append("=").append(
                            java.net.URLEncoder.encode(parameters.get(name),
                                    "UTF-8")).append("&");
                }
                String temp_params = sb.toString();
                params = temp_params.substring(0, temp_params.length() - 1);
            }
            // 创建URL对象
            java.net.URL connURL = new java.net.URL(url);
            // 打开URL连接
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL
                    .openConnection();
            // 设置通用属性
            httpConn.setRequestProperty("Accept", "*/*");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
            // 设置POST方式
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            // 获取HttpURLConnection对象对应的输出流
            out = new PrintWriter(httpConn.getOutputStream());
            // 发送请求参数
            out.write(params);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应，设置编码方式
            in = new BufferedReader(new InputStreamReader(httpConn
                    .getInputStream(), "UTF-8"));
            String line;
            // 读取返回的内容
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                logger.info(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                logger.error("--> error", e2);
            }
        }
        return result;
    }

    /**
     * post请求
     *
     * @param url  功能和操作
     * @param body 要post的数据
     * @return
     * @throws IOException
     */
    public String postSMS(String to, String smsContent) {

        String operation = "/industrySMS/sendSMS";

        String accountSid = "d4421d8910174415b8541b85847cd728";

        String tmpSmsContent = null;
        try {
            tmpSmsContent = URLEncoder.encode(smsContent, "UTF-8");
        } catch (Exception e) {
        }

        // 时间戳
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());

        // 签名
        String sig = DigestUtils.md5Hex("d4421d8910174415b8541b85847cd728" + "7f9d58cf1067434ab5a6eb9db2047e28" + timestamp);

        String createCommonParam = "&timestamp=" + timestamp + "&sig=" + sig + "&respDataType=" + "json";

        String url = "https://api.miaodiyun.com/20150822" + operation;
        String body = "accountSid=" + accountSid + "&to=" + to + "&smsContent=" + tmpSmsContent
                + createCommonParam;

        //System.out.println("url:" + System.lineSeparator() + url);
        //System.out.println("body:" + System.lineSeparator() + body);

        String result = "";
        try {
            OutputStreamWriter out = null;
            BufferedReader in = null;
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();

            // 设置连接参数
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(20000);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 提交数据
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(body);
            out.flush();

            // 读取返回数据
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = "";
            boolean firstLine = true; // 读第一行不加换行符
            while ((line = in.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                } else {
                    result += System.lineSeparator();
                }
                result += line;
            }
        } catch (Exception e) {
            logger.error("--> error ,",e);
        }
        return result;
    }

    public String postEmail(String toEmail, String subject, String emailCode) {

        String URL = "https://api.sendgrid.com/v3/mail/send";

        String data = "{\"personalizations\": [{\"to\": [{\"email\": \"" + toEmail + "\"}]}],\"from\": {\"email\": \"kaisheng1280@gmail.com\"},\"subject\": \"" + subject + "\",\"content\": [{\"type\": \"text/plain\", \"value\": \"" + emailCode + "\"}]}";

        JSONObject json = JSONObject.fromObject(data);

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(URL);

        post.setHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Bearer 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");

        String result = "";

        try {

            StringEntity s = new StringEntity(json.toString(), "utf-8");
            s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                    "application/json"));
            post.setEntity(s);

            // 发送请求
            HttpResponse httpResponse = client.execute(post);

            // 获取响应输入流
            InputStream inStream = httpResponse.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inStream, "utf-8"));
            StringBuilder strber = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
                strber.append(line + "\n");
            inStream.close();

            result = strber.toString();
            logger.info(result);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                logger.info("请求服务器成功，做相应处理");

            } else {

                logger.error("请求服务端失败, url :{}, data:{}", new Object[]{URL, data});

            }


        } catch (Exception e) {
            logger.error("请求异常");
            throw new RuntimeException(e);
        }

        return result;
    }

}
