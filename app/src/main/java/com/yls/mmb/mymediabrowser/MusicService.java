package com.yls.mmb.mymediabrowser;

import android.app.Service;
import android.content.Intent;
import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.os.IBinder;
import android.service.media.MediaBrowserService;
import android.support.annotation.Nullable;

import java.util.List;

public class MusicService extends MediaBrowserService {

    private static final String TAG = Utils.LOG_TAG;
    private static final String MUSIC_BROWSER_ROOT = "music_browser_root";

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(String clientPackageName, int clientUid, Bundle rootHints) {
        return new BrowserRoot(MUSIC_BROWSER_ROOT,null);
    }

    @Override
    public void onLoadChildren(String parentId, Result<List<MediaBrowser.MediaItem>> result) {
    }
}
