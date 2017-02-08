package com.duyuan.photoview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Display photos, support multi photos
 * Created by duyuan797 on 17/2/3.
 */
public class PhotoActivity extends AppCompatActivity {
    private static final String EXTRA_PHOTOS = "photos";
    private static final String[] PERMISSIONS = { Manifest.permission.WRITE_EXTERNAL_STORAGE };
    private static final int PERMISSION_CODE = 100;

    private ViewPager mViewPager;
    private TextView mPageTv;
    private TextView mSaveTv;
    private List<DPhotoView> mPhotoViews;
    private List<String> mPhotos;

    /**
     * start PhotoActivity
     *
     * @param photos photos to be display
     */
    public static void actionStart(Context context, ArrayList<String> photos) {
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putStringArrayListExtra(EXTRA_PHOTOS, photos);
        if (photos != null && photos.size() != 0) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "No photo", Toast.LENGTH_SHORT).show();
        }
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        initView();
        initData();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.vp_photo);
        mViewPager.setOffscreenPageLimit(1);
        mPageTv = (TextView) findViewById(R.id.tv_page);
        mSaveTv = (TextView) findViewById(R.id.tv_save);
        mSaveTv.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (checkPermission()) {
                    new SavePhotoTask(mPhotos.get(mViewPager.getCurrentItem())).execute();
                }
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset,
                    int positionOffsetPixels) {
                mPageTv.setText(String.valueOf(position + 1) + "/" + mPhotos.size());
            }

            @Override public void onPageSelected(int position) {

            }

            @Override public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        mPhotos = getIntent().getStringArrayListExtra(EXTRA_PHOTOS);
        mPhotoViews = new ArrayList<>(mPhotos.size());

        for (String photo : mPhotos) {
            final DPhotoView photoView = (DPhotoView) View.inflate(this, R.layout.view_photo, null);
            Picasso.with(this)
                    .load(photo)
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(photoView);
            photoView.setPhotoViewListener(new DPhotoView.IPhotoViewListener() {
                @Override public void onTap() {
                    if (mPageTv.getVisibility() == View.VISIBLE) {
                        mPageTv.setVisibility(View.GONE);
                    } else {
                        mPageTv.setVisibility(View.VISIBLE);
                    }
                    if (mSaveTv.getVisibility() == View.VISIBLE) {
                        mSaveTv.setVisibility(View.GONE);
                    } else {
                        mSaveTv.setVisibility(View.VISIBLE);
                    }
                }

                @Override public void onDismiss() {
                    finish();
                }
            });
            mPhotoViews.add(photoView);
        }

        PhotoAdapter adapter = new PhotoAdapter();
        mViewPager.setAdapter(adapter);
    }

    class PhotoAdapter extends PagerAdapter {
        @Override public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mPhotoViews.get(position));
            return mPhotoViews.get(position);
        }

        @Override public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mPhotoViews.get(position));
        }

        @Override public int getCount() {
            return mPhotoViews.size();
        }

        @Override public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    class SavePhotoTask extends AsyncTask<String, Integer, Void> {
        private String photoUrl;

        public SavePhotoTask(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        @Override protected Void doInBackground(String... strings) {
            savePhoto(photoUrl);
            return null;
        }

        @Override protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(PhotoActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Save photo
     */
    private void savePhoto(String photoUrl) {
        OutputStream outputStream = null;
        HttpURLConnection httpURLConnection = null;
        URL url = null;
        Bitmap bitmap = null;
        String fileName = Environment.getExternalStorageDirectory()
                + "/"
                + Environment.DIRECTORY_PICTURES
                + "/"
                + System.currentTimeMillis()
                + ".jpg";
        try {
            url = new URL(photoUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(6000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);
            InputStream inputStream = httpURLConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

            File file = new File(fileName);
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bitmap.recycle();
        }
    }

    /**
     * check permission
     */
    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, PERMISSIONS[0])
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_CODE);
            return false;
        }
        return true;
    }

    @Override public void onRequestPermissionsResult(int requestCode, String permissions[],
            int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new SavePhotoTask(mPhotos.get(mViewPager.getCurrentItem())).execute();
            }
            return;
        }
    }
}
