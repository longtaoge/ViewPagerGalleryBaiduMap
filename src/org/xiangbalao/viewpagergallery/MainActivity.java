package org.xiangbalao.viewpagergallery;

import java.util.ArrayList;
import java.util.List;

import org.xiangbalao.baidu.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;

public class MainActivity extends Activity {
	TextView pageid;

	/**
	 * 存放Viewpager中的子项
	 */
	private List<View> mPagersList;

	private ViewPager mViewPager;

	/**
	 * viewPager的容器，页面改变后需要刷新
	 */
	private RelativeLayout viewPagerContainer;

	private MapView mMapView;

	// 初始化全局 bitmap 信息，不用时及时 recycle
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_marka);

	// 地图
	private BaiduMap mBaiduMap;

	// 提示窗口
	private InfoWindow mInfoWindow;
	// 存放标注点的坐标系
	private ArrayList<LatLng> mLatLngs;

	// 存放标注点的集合
	private ArrayList<Marker> mMarkers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// 初始化坐标
		initLatLngList();
		// 初始化百度地图
		initBaiduMap();

		// 初始化Viewpager 相关数据
		initPagers();
		initViewPager();
	}

	private void initBaiduMap() {
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);
		mMarkers = new ArrayList<Marker>();
		// 添加覆盖物，这里是标注点
		initOverlay(mLatLngs);

		// TODO 设置点的监听，不用可以去掉
		setMarkerListener();

	}

	/**
	 * 设置标注点的监听
	 */
	private void setMarkerListener() {
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			public boolean onMarkerClick(final Marker marker) {

				View mView = View.inflate(MainActivity.this,
						R.layout.custom_text_view, null);
				OnInfoWindowClickListener listener = null;

				if (marker == mMarkers.get(0)) {
					// TODO 为标注列表的第一个标注点设置监听
					final LatLng ll = marker.getPosition();
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							// 挪动位置

							LatLng llNew = new LatLng(ll.latitude + 0.005,
									ll.longitude + 0.005);
							marker.setPosition(llNew);
							mBaiduMap.hideInfoWindow();
						}
					};

					// 添加 提示窗口
					mInfoWindow = new InfoWindow(BitmapDescriptorFactory
							.fromView(mView), ll, -47, listener);

					mBaiduMap.showInfoWindow(mInfoWindow);
				} else if (marker == mMarkers.get(1)) {
					// TODO 点击第二个点后添加提示窗口或其它动作

				}
				return true;
			}
		});
	}

	private void initViewPager() {
		// 容器
		viewPagerContainer = (RelativeLayout) findViewById(R.id.pager_layout);

		mViewPager = (ViewPager) findViewById(R.id.ii_viewpager);

		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		mViewPager.setAdapter(new MyPagerAdapter());

		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setPageMargin(getResources().getDimensionPixelSize(
				R.dimen.page_margin));

		mViewPager.setCurrentItem(0, true);
	}

	/**
	 * 座标的位置列表
	 */
	private void initLatLngList() {

		mLatLngs = new ArrayList<LatLng>();
		mLatLngs.add(new LatLng(39.942821, 116.369199));
		mLatLngs.add(new LatLng(39.939723, 116.425541));
		mLatLngs.add(new LatLng(39.906965, 116.401394));
		mLatLngs.add(new LatLng(39.963175, 116.400244));
		mLatLngs.add(new LatLng(39.939723, 116.425541));

	}

	/**
	 * 描述: 模似数据</br> 开发人员：longtaoge</br> 创建时间：2015-7-19</br>
	 */
	private void initPagers() {
		mPagersList = new ArrayList<View>();
		pageid = (TextView) findViewById(R.id.pageid);

		for (int i = 0; i < mLatLngs.size(); i++) {

			View mView = LayoutInflater.from(this).inflate(
					R.layout.xiaojinyin_zhangdan_item, null);

			TextView zhangdanjinenum = (TextView) mView
					.findViewById(R.id.zhangdanjinenum);

			zhangdanjinenum.setText(String.valueOf(i));
			mPagersList.add(mView);

		}

	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mPagersList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		public Object instantiateItem(ViewGroup container, int position) {

			container.addView(mPagersList.get(position));
			return mPagersList.get(position);

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			container.removeView(mPagersList.get(position));
		}

	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int position) {
			// 添加Popwindow

			addPopWindows(mLatLngs.get(position));
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			// to refresh frameLayout 刷新 布局
			if (viewPagerContainer != null) {
				viewPagerContainer.invalidate();
			}

			pageid.setText(position + "**" + positionOffset + "**"
					+ positionOffsetPixels);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	public void initOverlay(List<LatLng> mLatLngs) {

		// 把所有的红点加到地图上
		for (int i = 0; i < mLatLngs.size(); i++) {
			// 获取座标点
			LatLng mLatLng = mLatLngs.get(i);
			// 标注点的选项 可选其它图标
			MarkerOptions ooA = new MarkerOptions().position(mLatLng).icon(bdA)
					.zIndex(9).draggable(true);
			// 标注点
			Marker mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
			// 将标注点存放到集合中，设置监听时用
			mMarkers.add(mMarker);
		}
		// 添加弹出窗口,只给第一个点加提示窗口
		addPopWindows(mLatLngs.get(0));
	}

	/**
	 * 根据坐标添加窗口
	 * 
	 * @param mLatLng
	 */
	private void addPopWindows(final LatLng mLatLng) {

		// 隐藏已经弹出的窗口
		mBaiduMap.hideInfoWindow();
		// 设置缩放级别，地图中心点等
		MapStatus mMapStatus = new MapStatus.Builder().target(mLatLng).zoom(12)
				.build();
		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);
		// 改变地图状态 重置中心点
		mBaiduMap.setMapStatus(mMapStatusUpdate);

		// 从布局配置引入一个PopWindow 提示框
		View mView = View.inflate(MainActivity.this, R.layout.custom_text_view,
				null);

		// 提示窗口点击监听
		OnInfoWindowClickListener listener = new OnInfoWindowClickListener() {
			public void onInfoWindowClick() {

				// TODO 点击后隐藏
				mBaiduMap.hideInfoWindow();
			}
		};

		// 创建提示窗口 添加到 mLatLng 这个坐标的 位置上
		mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(mView),
				mLatLng, -47, listener);
		// 显示提示窗口
		mBaiduMap.showInfoWindow(mInfoWindow);

		// 标注点的拖拽监听
		mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {
			public void onMarkerDrag(Marker marker) {
			}

			public void onMarkerDragEnd(Marker marker) {
				Toast.makeText(
						MainActivity.this,
						"拖拽结束，新位置：" + marker.getPosition().latitude + ", "
								+ marker.getPosition().longitude,
						Toast.LENGTH_LONG).show();
			}

			public void onMarkerDragStart(Marker marker) {
			}
		});
	}

	@Override
	protected void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		mMapView.onDestroy();
		super.onDestroy();
		// 回收 bitmap 资源
		bdA.recycle();

	}
}
