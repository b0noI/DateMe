package com.dateme.common;

import com.dateme.db.DBHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: o.kozlovskiy
 * Date: 2/15/13
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class Connection{

    private final long id;

    private final long connector_id;

    private Set<Status> statuses = new HashSet<>();

    public Connection(long id, long  connector_id){
        this.id = id;
        this.connector_id = connector_id;
    }

    public Connection(long id, long connector_id, Status status){
        this.id = id;
        this.connector_id = connector_id;
        addStatus(status);
    }

    public long getConnectorId(){
        return connector_id;
    }

    public void addStatus(Status status){
        statuses.add(status);
    }

    public long getId(){
        return id;
    }

    public Set<Status> getStatuses(){
        return statuses;
    }

    public void delStatus(Status status) {
        statuses.remove(status);
    }

    public boolean isMatch() {
        DBHelper dbHelper = DBHelper.getInstance();
        if (dbHelper.getProfile(getId()).connectedTo(getConnectorId()) &&
                dbHelper.getProfile(getConnectorId()).connectedTo(getId()))
            return true;
        return false;
    }

}
