package com.dateme.common;

import com.dateme.db.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * User: b0noI
 * Date: 10.02.13
 * Time: 23:00
 */
public class UserProfile {

    public String mail;

    public Locale locale;

    public final List<Connection> followingIDs = new ArrayList<>();

    private final long id;

    private String sicretKey;

    private String nick;

    public UserProfile(long id, String sicretKey, String mail, Locale locale){
        this.id = id;
        this.sicretKey = sicretKey;
        this.mail = mail;
        this.locale = locale;
    }

    public long getProfileID(){
        return this.id;
    }

    public List<Connection> getProfileConnections(){
        return followingIDs;
    }

    public void addConnection(long id, Status s){
        followingIDs.add(new Connection(this.id, id, s));
    }

    public void addConnection(Connection conn){
        followingIDs.add(conn);
    }

    public String getKey(){
        return sicretKey;
    }

    public String getNick() {
        return nick;
    }

    public boolean connectedTo(long id) {
        for (Connection connection : getProfileConnections() ){
            if (connection.getConnectorId() == id)
                return true;
        }
        return false;
    }

}

