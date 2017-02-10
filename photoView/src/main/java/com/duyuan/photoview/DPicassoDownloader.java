package com.duyuan.photoview;

import android.net.Uri;
import android.support.annotation.IntRange;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.squareup.picasso.Downloader;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 自定义Picasso downloader
 * Created by duyuan797 on 2017/2/10.
 */

public final class DPicassoDownloader implements Downloader {

    private final Call.Factory client;

    public DPicassoDownloader(final ProgressListener listener) {
        OkHttpClient.Builder builder =
                new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
                    @Override public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Response response = chain.proceed(chain.request());
                        return response.newBuilder()
                                .body(new DPicassoDownloader.ProgressResponseBody(response.body(),
                                        response.request().url().url().toString(), listener))
                                .build();
                    }
                });
        this.client = builder.build();
    }

    @Override public Response load(Uri uri, int networkPolicy) throws IOException {

        Request.Builder php_builder = new Request.Builder().url(uri.toString());

        okhttp3.Response php_response = client.newCall(php_builder.build()).execute();
        return new Response(php_response.body().byteStream(), false,
                php_response.body().contentLength());
    }

    @Override public void shutdown() {

    }

    public interface ProgressListener {
        @WorkerThread void onProgress(@IntRange(from = 0, to = 100) int percent);
    }

    public static class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private final ProgressListener progressListener;
        private BufferedSource bufferedSource;

        public ProgressResponseBody(ResponseBody responseBody, String url,
                ProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override public long contentLength() {
            return responseBody.contentLength();
        }

        @Override public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {

            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    Log.i("dd", "total :" + totalBytesRead);
                    if (progressListener != null) {
                        progressListener.onProgress(
                                ((int) ((100 * totalBytesRead) / responseBody.contentLength())));
                    }
                    return bytesRead;
                }
            };
        }
    }
}