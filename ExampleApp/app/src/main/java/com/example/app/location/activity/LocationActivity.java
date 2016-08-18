package com.example.app.location.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.app.R;

/**
 * @author LIUYAN
 * @date 2016/1/23 0023
 * @time 14:47
 *
 * 功能描述：
 * 百度地图API：http://lbsyun.baidu.com/index.php?title=android-locsdk/guide/v5-0
 *
 * */
public class LocationActivity extends Activity {

//    public LocationClient mLocationClient = null;
//    public BDLocationListener mBDLocationListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        initLocation();
    }

    public void initLocation() {
//        // LocationClient类是定位SDK的核心类
//        mLocationClient = new LocationClient(getApplicationContext());
//        mBDLocationListener = new MyLocationListener();
//        mLocationClient.registerLocationListener(mBDLocationListener);
//
//        LocationClientOption option = new LocationClientOption();
//        //可选，默认高精度
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        //设置返回的定位结果坐标系，三种坐标：
//        // wgs84 --GPS系统直接通过卫星定位获得的坐标.(最基础的坐标.)
//        // gcj02 --兲朝已安全原因为由,要求在中国使用的地图产品使用的都必须是加密后的坐标.这套加密后的坐标就是gcj02 google的中国地图.高德地图. 他们为中国市场的产品都是用这套坐标.
//        // bd09ll 百度又在gcj02的技术上将坐标加密就成了 bd09ll坐标.
//        // bd09ll只能百度地图用，bd09可以其他地图用，所以不让用户把百度地图的数据转成其他地图的数据
//        option.setCoorType("bd09ll");
//        option.setScanSpan(1000);// 设置发起定位请求的间隔需要大于等于1000ms才是有效的
//        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
//        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1s1次频率输出GPS结果
//        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
//
//        mLocationClient.setLocOption(option);
//        mLocationClient.start();
    }

    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            StringBuffer sb = new StringBuffer(256);
            sb.append("time: ").append(bdLocation.getTime())
                    .append("\nerror code: ").append(bdLocation.getLocType())
                    .append("\nlatitude: ").append(bdLocation.getLatitude())
                    .append("\nlontitude: ").append(bdLocation.getLongitude());
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                    sb.append("\naddress: ").append(bdLocation.getAddress())
                            .append("\ndescription: ").append(bdLocation.getLocationDescribe())
                            .append("\nGPS定位成功");
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("\naddress: ").append(bdLocation.getAddress())
                        .append("\ndescription: ").append(bdLocation.getLocationDescribe())
                        .append("\n网络定位成功");
            }

            Log.e("TAG", sb.toString());
//            mLocationClient.stop();
        }
    }
}
