package com.ivanov.denis.rss_reader.constants;

public interface Constants {

    enum Fields {id, title, link, fulltext, favorite, date, readed}
    String LOG_TAG = "MyLogs";
    String TABLE_NAME = "feeds";
    String BROADCAST_ACTION = "com.package com.ivanov.denis.rss_reader";
    String FAVORITE = "FAVORITE";
    String AUTO_UPDATE = "AUTO_UPDATE";

    String STATUS_DOWNLOADING = "STATUS_DOWNLOADING";
    int ERROR_DOWNLOADING = 0;
    int START_DOWNLOADING = 1;
    int DOWNLOADING = 2;
    int END_DOWNLOADING = 3;

}
