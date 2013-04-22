package com.dateme.server;

import com.dateme.db.DBHelper;
import com.dateme.db.DataBase;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * User: b0noI
 * Date: 16.02.13
 * Time: 23:16
 */
class BaseClient {

    protected static final Gson GSON = new Gson();

    protected static final DataBase DB = DBHelper.getInstance();

}
