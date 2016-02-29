package com.example.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GlideActivity extends AppCompatActivity {

    private ImageView firstImg;
    private ImageView secondImg;
    private ImageView thirdImg;
    private ImageView fourImg;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);

        firstImg = (ImageView) findViewById(R.id.img_first);
        secondImg = (ImageView) findViewById(R.id.img_second);
        thirdImg = (ImageView) findViewById(R.id.img_third);
        fourImg = (ImageView) findViewById(R.id.img_four);

        url = "https://www.baidu.com/img/bdlogo.png";
        Glide.with(this).load(url).asBitmap().into(new BitmapImageViewTarget(firstImg));
        Glide.with(this).load(url).asBitmap().into(new ImageViewTarget<Bitmap>(secondImg) {
            @Override
            protected void setResource(Bitmap bitmap) {
                secondImg.setImageBitmap(bitmap);
            }

            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                super.onResourceReady(resource, glideAnimation);
                thirdImg.setImageBitmap(resource);

            }
        });

        Glide.with(this).load(url).asBitmap().listener(new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(Exception e, String s, Target<Bitmap> target, boolean b) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap bitmap, String s, Target<Bitmap> target, boolean b, boolean b1) {
                fourImg.setImageBitmap(bitmap);
                return true;
            }
        }).preload();

//        startImg();

    }

    private void startImg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = getHttpBitmap(url);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fourImg.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();
    }

    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return bitmap;

    }
}
