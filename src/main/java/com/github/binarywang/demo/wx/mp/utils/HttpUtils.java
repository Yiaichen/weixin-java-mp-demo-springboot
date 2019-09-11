package com.github.binarywang.demo.wx.mp.utils;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * http请求工具类
 *
 * @author vayi
 * @date 2019/9/10
 * @since 0.0.1
 */
public class HttpUtils {

    public static final String URL = "https://www.ciliwang.club/list.html?keyword=";

    /**
     * 发起http请求获取返回结果
     * @param req_url 请求地址
     * @return
     */
    public static String httpRequest(String req_url) {
        StringBuffer buffer = new StringBuffer();
        try {
            URL url = new URL(req_url);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

            httpUrlConn.setDoOutput(false);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);

            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.connect();

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return buffer.toString();
    }

    public static String parseHtml(String html) {
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isNotEmpty(html)) {
            // html解析
            Document document = Jsoup.parse(html);
            Element postList = document.getElementById("list_cont");
            Elements postItems = postList.getElementsByClass("single_cont");
            stringBuilder.append("*********************************************************************\n");
            for (Element postItem : postItems) {
                Elements titleEle = postItem.select(".font_blue a");
                stringBuilder.append("文件名：" + titleEle.attr("title") + "\n");
                Elements elements = postItem.getElementsByClass("data_text");
                for (Element element : elements) {
                    stringBuilder.append(element.text() + "\n");
                }
                stringBuilder.append("磁力链接：" + titleEle.attr("data-src") + "\n");
                stringBuilder.append("*********************************************************************\n");
            }
        }
        return stringBuilder.toString().substring(0, stringBuilder.length()-2);
    }

//    public static void main(String[] args) {
//        String url = "https://www.ciliwang.club/list.html?keyword=%E6%B3%A2%E5%A4%9A%E9%87%8E%E7%BB%93%E8%A1%A3";
//        String content = httpRequest(url);
//        System.out.println(parseHtml(content));
//    }
}
