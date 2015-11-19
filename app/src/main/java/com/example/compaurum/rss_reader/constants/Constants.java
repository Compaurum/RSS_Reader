package com.example.compaurum.rss_reader.constants;

public interface Constants {

    enum Fields {id, title, link, fulltext, favorite, date, readed}
    final static String LOG_TAG = "MyLogs";
    final static String TABLE_NAME = "feeds";

    public final static int ERROR_DOWNLOADING = 0;
    public final static int START_DOWNLOADING = 1;
    public final static int DOWNLOADING = 2;
    public final static int END_DOWNLOADING = 3;

}
