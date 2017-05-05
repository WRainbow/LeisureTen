package com.srainbow.leisureten.util;

import android.util.Log;

import com.srainbow.leisureten.data.APIData.ImgWithAuthor;
import com.srainbow.leisureten.data.APIData.TagDetail;
import com.srainbow.leisureten.data.APIData.TagData;

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
        Log.e("msg", "parserHtml Over");
        return urlList;
    }

    public List<ImgWithAuthor> parserHtmlForImgWithAuthor(String url) {
        List<ImgWithAuthor> imgWithAuthorList = new ArrayList<>();
        if (url != null) {
            try {
                Document doc = Jsoup.connect(url).get();
                Element content = doc.select("div.container").select("div.sticky_wrap")
                        .select("div.content").first();
                Elements items = content.select("div.item_wrap");
                for (Element ele : items) {
                    String imgUrl = ele.getElementsByClass("img_wrap").select("a").select("img").attr("src");
                    String author = ele.select("p").select("a").text();
                    imgWithAuthorList.add(new ImgWithAuthor(imgUrl, author));
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return imgWithAuthorList;
    }
}
