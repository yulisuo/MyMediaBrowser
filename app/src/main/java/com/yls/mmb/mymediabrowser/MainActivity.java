package com.yls.mmb.mymediabrowser;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.browse.MediaBrowser;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DataProvider.OnQueryCompleteListener,
        AdapterView.OnItemClickListener{

    private static final String TAG = Utils.LOG_TAG;
    //view
    private Button mPlayPause;
    private SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},200);
        }
        initData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 200){
            boolean ret = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if(! ret){
                Log.e(TAG,"permission not granted");
                finish();
            }
        }
    }

    private void initData() {
        Log.i(TAG,"initData");
        DataProvider dataProvider = DataProvider.getInstance(this);
        dataProvider.setQueryCompleteListener(this);
        dataProvider.query();
    }

    @Override
    public void onComplete(ArrayList<MediaBrowser.MediaItem> data) {
        Log.i(TAG,"query complete,data size:"+data.size());
        ListView lv = (ListView)findViewById(R.id.lv);
        lv.setAdapter(new MediaItemAdapter(MainActivity.this,data,R.layout.media_item));
        lv.setOnItemClickListener(this);
    }

    private void logData(ArrayList<MediaBrowser.MediaItem> data) {
        for (MediaBrowser.MediaItem item: data) {
            Log.i(TAG,"logData,title:"+item.getDescription().getTitle());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private class MediaItemAdapter extends BaseAdapter{

        ArrayList<MediaBrowser.MediaItem> data;
        int resource;
        Context context;

        class ViewHolder {
            TextView mTitleView;
        }

        public MediaItemAdapter(Context context, ArrayList<MediaBrowser.MediaItem> data, int resource) {
            this.data = data;
            this.resource = resource;
            this.context = context;
            Log.i(TAG,"new MediaItemAdapter");
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null){
                convertView = LayoutInflater.from(context).inflate(resource,null);
                holder = new ViewHolder();
                holder.mTitleView = (TextView)convertView.findViewById(R.id.tv_media_title);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            MediaBrowser.MediaItem item = (MediaBrowser.MediaItem) getItem(position);
            Log.i(TAG,"getView , title:"+item.getDescription().getTitle());
            holder.mTitleView.setText(item.getDescription().getTitle());
            return convertView;
        }
    }
}
