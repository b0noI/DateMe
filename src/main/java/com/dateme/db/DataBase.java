package com.dateme.db;

import com.dateme.common.Connection;
import com.dateme.common.UserProfile;

/**
 * Created with IntelliJ IDEA.
 * User: o.kozlovskiy
 * Date: 2/13/13
 * Time: 7:45 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataBase {

    public void updateProfile(UserProfile profile, String sicretKey);
    public void removeProfile(long id, String secretKey);
    public UserProfile getProfile(long id, String secretKey);
    public void updateConnection(Connection conn,  String secretKey);
    public void removeConnection(Connection conn, String secretKey);
    public boolean isNotified(long id);
    public void setNotified(long id);
    public void log(String msg);

}
