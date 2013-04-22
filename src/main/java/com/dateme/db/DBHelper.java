package com.dateme.db;

import com.dateme.common.Connection;
import com.dateme.common.Status;
import com.dateme.common.UserProfile;
import com.dateme.db.settings.Settings;
import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: o.kozlovskiy
 * Date: 2/13/13
 * Time: 7:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBHelper implements DataBase {

    private DBCollection profiles;
    private DBCollection connections;
    private DBCollection notified;
    private DBCollection log;
    private MongoClient mongo;

    private DBHelper(){
        try{
            initDB();
        }catch (UnknownHostException e){
            e.printStackTrace();
        }
    }

    public static DBHelper getInstance() {
        return InstanceHolder.getInstance();
    }

    public void closeDB(){
        mongo.close();
    }

    @Override
    public void updateProfile(UserProfile profile, String sicretKey) {

        BasicDBObject query = new BasicDBObject("id" , profile.getProfileID());

        try(DBCursor cursor = profiles.find(query);){
            if(cursor.hasNext()){
                System.out.println("errore " + cursor.next());
            }else{
                inputNewprofile(profile);
            }
        }
    }
    @Override
    public void removeProfile(long id, String sicretKey) {
        UserProfile userProfile = getProfile(id);
        if (userProfile != null && userProfile.getProfileConnections() != null)
            for (Connection connection : userProfile.getProfileConnections())
                removeConnection(connection, userProfile.getKey());
        BasicDBObject removeQuery = new BasicDBObject("id", id);
        profiles.remove(removeQuery);
    }


    @Override
    public UserProfile getProfile(long id, String sicretKey) {
        BasicDBObject getQuery = new BasicDBObject("id", id);
        try(DBCursor c = profiles.find(getQuery);){
            UserProfile p;
            if(c.hasNext() && c.count() == 1){

                DBObject out = c.next();
                p = new UserProfile((Long)out.get("id"), (String)out.get("secretKey"),
                        (String)out.get("mail"), new Locale((String)out.get("locale")));

                try(DBCursor cur = connections.find(new BasicDBObject("id", p.getProfileID()));){
                    while(cur.hasNext()){
                        DBObject it = cur.next();
                        DBObject statuses = (DBObject)it.get("statuses");
                        p.getProfileConnections().add(getConnection(p.getProfileID(), (Long)it.get("connector_id")));
                    }
                }
                return p;
            }else
                return null;
        }

    }

    @Override
    public void updateConnection(Connection conn, String secretKey) {
        Connection connInDB = getConnection(conn.getId(), conn.getConnectorId());
        if(connInDB != null){
            removeConnection(connInDB, secretKey);
            putConnectionToDB(conn);
        }else{
            putConnectionToDB(conn);
        }
    }

    @Override
    public void removeConnection(Connection conn, String secretKey) {
        BasicDBObject removeQuery = new BasicDBObject("id", conn.getId()).append("connector_id", conn.getConnectorId());
        connections.remove(removeQuery);
    }

    @Override
    public boolean isNotified(long id) {
        BasicDBObject query = new BasicDBObject("id" , id);

        try(DBCursor cursor = notified.find(query);){
            return cursor.hasNext();
        }
    }

    @Override
    public void setNotified(long id) {
        BasicDBObject idDB = new BasicDBObject("id", id);
        notified.insert(idDB);
    }

    @Override
    public void log(String msg) {
        BasicDBObject msgLog = new BasicDBObject("msg", msg);
        log.insert(msgLog);
    }

    public UserProfile getProfile(long id) {
        BasicDBObject getQuery = new BasicDBObject("id", id);
        try(DBCursor c = profiles.find(getQuery);){
            UserProfile p;
            if(c.hasNext() && c.count() == 1){

                DBObject out = c.next();
                p = new UserProfile((Long)out.get("id"), (String)out.get("secretKey"),
                        (String)out.get("mail"), new Locale((String)out.get("locale")));

                try(DBCursor cur = connections.find(new BasicDBObject("id", p.getProfileID()));){
                    while(cur.hasNext()){
                        DBObject it = cur.next();
                        DBObject statuses = (DBObject)it.get("statuses");
                        p.getProfileConnections().add(getConnection(p.getProfileID(), (Long)it.get("connector_id")));
                    }
                }
                return p;
            }else
                return null;
        }

    }

    Connection getConnection(long id, long conn_id){
        Connection conn;
        try(DBCursor c = connections.find(new BasicDBObject("id", id).append("connector_id", conn_id));){
            if(c.hasNext()){
                conn = new Connection(id, conn_id);
                while(c.hasNext()){
                    DBObject it = c.next();
                    DBObject statuses = (DBObject)it.get("statuses");
                    int i = 0;
                    while(statuses.get(""+i) != null){
                        conn.getStatuses().add(Status.valueOf(statuses.get(""+i).toString()));
                        i++;
                    }

                }
                return conn;
            }
            return null;
        }
    }

    private void initDB() throws UnknownHostException{

        mongo = new MongoClient(Settings.DB_ADRESS, Settings.DB_PORT);
        DB db = mongo.getDB(Settings.DB_NAME);
        db.authenticate(Settings.DB_LOGIN, Settings.DB_PASSWORD);

        profiles = db.getCollection(Settings.PROFILE_COLLECTION_NAME);
        connections = db.getCollection(Settings.CONNECTION_COLLECTION_NAME);
        notified = db.getCollection(Settings.NOTIFIED_COLLECTION_NAME);
        log = db.getCollection(Settings.LOGS_COLLECTION_NAME);

        if(profiles ==  null){
            System.out.println("profile collection creating");
            profiles = db.createCollection(Settings.PROFILE_COLLECTION_NAME, null);
        }
        if(connections == null){
            System.out.println("connection collection creating");
            connections = db.createCollection(Settings.CONNECTION_COLLECTION_NAME, null);
        }
        if(notified == null){
            System.out.println("notified collection creating");
            notified = db.createCollection(Settings.NOTIFIED_COLLECTION_NAME, null);
        }
        if(log == null){
            System.out.println("log collection creating");
            log = db.createCollection(Settings.LOGS_COLLECTION_NAME, null);
        }
    }

    private void inputNewprofile(UserProfile p){
        BasicDBObject inputQuery = new BasicDBObject("id", p.getProfileID())
                .append("secretKey", p.getKey())
                .append("mail", p.mail)
                .append("locale", p.locale.getLanguage());
        profiles.insert(inputQuery);
        inputProfileConnections(p);
    }

    private void inputProfileConnections(UserProfile p){

        List<Connection> list = p.getProfileConnections();

        for(int i = 0; i < list.size(); i++){
            putConnectionToDB(list.get(i));
        }
    }

    private void putConnectionToDB(Connection conn){

            BasicDBObject inputCon = new BasicDBObject("id", conn.getId()).
                    append("connector_id", conn.getConnectorId());


            Set<Status> set = conn.getStatuses();
            BasicDBObject statuses = new BasicDBObject();
            Iterator it = set.iterator();
            int i = 0;
            while(it.hasNext()){
                String str = ""+i;
                statuses = statuses.append(str, it.next().toString());
                i++;
            }

            inputCon.append("statuses", statuses);
            connections.insert(inputCon);
    }

    private static class InstanceHolder {

        private static DBHelper INSTANCE = new DBHelper();

        public static DBHelper getInstance() {
            return INSTANCE;
        }

    }

}
