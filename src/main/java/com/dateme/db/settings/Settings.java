package com.dateme.db.settings;

/**
 * Created with IntelliJ IDEA.
 * User: o.kozlovskiy
 * Date: 2/14/13
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class Settings {

    public static final String DB_LOGIN = "dateme";
    public static final char[] DB_PASSWORD = {'q', 'w', 'e', 'r', 't', 'y'};
    public static final String DB_ADRESS = "mongodb-dateme.j.rsnx.ru";//"89.253.237.250";//"mongodb-dateme.j.rsnx.ru";
    public static final int DB_PORT = 27017;
    public static final String DB_NAME = "DateMe";

    public static final String PROFILE_COLLECTION_NAME = "profiles";
    public static final String CONNECTION_COLLECTION_NAME = "conection";
    public static final String NOTIFIED_COLLECTION_NAME = "notified";
    public static final String LOGS_COLLECTION_NAME = "log";
}
