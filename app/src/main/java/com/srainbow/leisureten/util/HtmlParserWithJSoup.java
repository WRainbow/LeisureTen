package com.srainbow.leisureten.util;

import android.util.Log;

import com.srainbow.leisureten.data.jsoupdata.ImgWithAuthor;
import com.srainbow.leisureten.data.jsoupdata.TagDetail;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SRainbow on 2017/4/9.
 */

public class HtmlParserWithJSoup {
    private volatile static HtmlParserWithJSoup instance = null;

    private HtmlParserWithJSoup(){}

    public static HtmlParserWithJSoup getInstance(){
        if(instance == null){
            synchronized (HtmlParserWithJSoup.class){
                if(instance == null){
                    instance = new HtmlParserWithJSoup();
                }
            }
        }
        return instance;
    }

    public List<TagDetail> parserHtmlForTags(Document doc){
        List<TagDetail> urlList = new ArrayList<>();
        if(doc != null){
            Element tags = doc.select("div.footer_tags").first().select("div.container").first();
            Elements tag = tags.select("a");
            for(Element ele : tag){
                String tagUrl = ele.attr("href");
                String tagName = ele.text();
                urlList.add(new TagDetail(tagName, tagUrl));
            }
        }
        return urlList;
    }

    public List<TagDetail> parserHtmlForTags(String url){
        List<TagDetail> urlList = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            if(doc != null){
                Element tags = doc.select("div.footer_tags").first().select("div.container").first();
                Elements tag = tags.select("a");
                for(Element ele : tag){
                    String tagUrl = ele.attr("href");
                    String tagName = ele.text();
                    urlList.add(new TagDetail(tagName, tagUrl));
                }
            }else{
                Log.e("msg", "null");
            }
        } catch (IOException e) {
            urlList.add(new TagDetail("error", e.getMessage()));
        }
        Log.e("msg", "parserHtmlForTags Over");
        return urlList;
    }

    public List<ImgWithAuthor> parserHtmlForImgWithAuthor(String url) {
        List<ImgWithAuthor> imgWithAuthorList = new ArrayList<>();
        int pre = 0, next = 0;
        String preUrl = "", nextUrl = "";
        if (url != null) {
            try {
                Document doc = Jsoup.connect(url).get();
                Element content = doc.select("div.container").select("div.sticky_wrap")
                        .select("div.content").first();
                Elements items = content.select("div.item_wrap");
                Element navigation = content.select("div.navigation").first();
                //判断是否存在上一页
                if(navigation.select("div.previous").first().text() == null ||
                        navigation.select("div.previous").first().text().length() == 0){
                    pre = 0;
                    Log.e("msg", "nonPre");
                }else{
                    pre = 1;
                    preUrl = navigation.select("div.previous").first().select("a").attr("href");
                    Log.e("msg", preUrl);
                }
                //判断是否存在下一页
                if(navigation.select("div.next").first().text() == null ||
                        navigation.select("div.next").first().text().length() == 0){
                    next = 0;
                    Log.e("msg", "nonNext");
                }else{
                    next = 1;
                    nextUrl = navigation.select("div.next").first().select("a").attr("href");
                    Log.e("msg", nextUrl);
                }
                for (Element ele : items) {
                    String imgUrl = ele.getElementsByClass("img_wrap").select("a").select("img").attr("src");
                    String author = ele.select("p").select("a").text();
                    ImgWithAuthor imgWithAuthor = new ImgWithAuthor(imgUrl, author, next, pre);
                    imgWithAuthor.setPreUrl(preUrl);
                    imgWithAuthor.setNextUrl(nextUrl);
                    imgWithAuthorList.add(imgWithAuthor);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        Log.e("msg", "parserHtmlForImgWithAuthor Over");
        return imgWithAuthorList;
    }

    public List<ImgWithAuthor> getNextPageImgWithAuthor(){
        return parserHtmlForImgWithAuthor("");
    }

}
