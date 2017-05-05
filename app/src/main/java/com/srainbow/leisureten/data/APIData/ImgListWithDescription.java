package com.srainbow.leisureten.data.APIData;

import java.util.List;

/**
 * Created by SRainbow on 2017/4/19.
 */

public class ImgListWithDescription {
    public List<ShowApiPicContentDetail.PicSizeWithUrl> sizeWithUrlList;
    public String imgDescription;

    public ImgListWithDescription() {
    }

    public ImgListWithDescription(List<ShowApiPicContentDetail.PicSizeWithUrl> imgUrl, String imgDescription) {
        this.sizeWithUrlList = imgUrl;
        this.imgDescription = imgDescription;
    }

    public List<ShowApiPicContentDetail.PicSizeWithUrl> getImgUrl() {
        return sizeWithUrlList;
    }

    public void setImgUrl(List<ShowApiPicContentDetail.PicSizeWithUrl> imgUrl) {
        this.sizeWithUrlList = imgUrl;
    }

    public void setImgDescription(String imgDescription) {
        this.imgDescription = imgDescription;
    }

    public String getImgDescription() {
        return imgDescription;
    }
}
