package com.yls.mmb.mymediabrowser;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaDescription;
import android.media.browse.MediaBrowser;
import android.media.session.MediaSession;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * Created by mobvoi on 17-3-22.
 */

public class DataProvider {

    private static DataProvider dataProvider;
    private Context context;
    private ArrayList<MediaBrowser.MediaItem> data = new ArrayList<>();
    private OnQueryCompleteListener listener;

    private DataProvider(Context context) {
        this.context = context;
    }

    public static DataProvider getInstance(Context context) {
        synchronized (DataProvider.class){
            if (dataProvider == null) {
                dataProvider = new DataProvider(context);
            }
            return dataProvider;
        }
    }

    public void query() {
        ContentResolver cr = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String [] cols = new String[]{
                MediaStore.Audio.Media._ID,         //内部ID
                MediaStore.Audio.Media.TITLE,       //标题
                MediaStore.Audio.Media.DATA,        //路径
                MediaStore.Audio.Media.MIME_TYPE    //类型
        };
        Cursor cursor = cr.query(uri,cols,null,null,MediaStore.Audio.Media.TITLE);
        for (cursor.moveToFirst();cursor.moveToNext();) {
            MediaBrowser.MediaItem item = new MediaBrowser.MediaItem(new MediaDescription.Builder()
                    .setMediaId(""+cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID)))
                    .setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)))
                    .build(), MediaBrowser.MediaItem.FLAG_PLAYABLE);
            data.add(item);
        }
        //notify
        if (listener != null) {
            listener.onComplete(data);
        }
    }

    public void setQueryCompleteListener (OnQueryCompleteListener listener){
        this.listener = listener;
    }

    public static interface OnQueryCompleteListener {
        public void onComplete(ArrayList<MediaBrowser.MediaItem> data);
    }
}
