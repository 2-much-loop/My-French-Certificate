package com.maximeg.my_french_certificate.sqlite;

import android.provider.BaseColumns;

public class QRContract implements BaseColumns{
    private QRContract() {}

    public static final String TABLE_NAME = "QR_Table";
    public static final String COLUMN_NAME_FIRST_NAME = "first_name";
    public static final String COLUMN_NAME_LAST_NAME = "last_name";
    public static final String COLUMN_NAME_FILE_NAME = "file_name";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_TIME = "time";
}
