package com.duyuan.photoview;

import android.content.Context;
import com.squareup.picasso.Picasso;

/**
 * 自定义Picasso,支持获取图片下载进度
 * Created by duyuan797 on 17/2/10.
 */

public class DPicasso {

    private static Picasso mPicasso;

    public static Picasso getPicasso(Context context, DPicassoDownloader.ProgressListener listener) {
        DPicassoDownloader downloader = new DPicassoDownloader(listener);
        mPicasso = new Picasso.Builder(context).downloader(downloader).build();
        return mPicasso;
    }
}
