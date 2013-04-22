package com.dateme.db;

import com.dateme.common.Connection;
import com.dateme.common.Status;
import com.dateme.common.UserProfile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: o.kozlovskiy
 * Date: 2/14/13
 * Time: 4:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBHelperTest {


    private UserProfile user;
    private DBHelper dbH;

    @Before
    public void inti(){
        dbH = DBHelper.getInstance();
    }

    @After
    public void dropDB(){
        dbH = null;
    }

    @Test
    public void updateProfileTest(){
        user = new UserProfile(1, "l", "wsaf@asf.com", Locale.US);
        dbH.updateProfile(user, "lololo");
    }

    @Test
    public void Connectiontest(){
        user = new UserProfile(54, "l", "wsaf@asf.com", Locale.US);
        user.addConnection(1, Status.SEX);
        user.addConnection(33, Status.TALK);
        user.addConnection(12, Status.SEX);
        user.addConnection(12, Status.SEX);


        assertNotNull(user.getProfileConnections());
        dbH.updateProfile(user, "lalala");

        UserProfile Nuser = dbH.getProfile(54, "lo");

        assertEquals(Nuser.getProfileID(), user.getProfileID());
        assertNotNull(Nuser.getProfileConnections());
        for (int i = 0 ; i < Nuser.getProfileConnections().size(); i++){
            assertEquals(user.getProfileConnections().get(i).getId(), Nuser.getProfileConnections().get(i).getId());
            assertEquals(user.getProfileConnections().get(i).getConnectorId(), Nuser.getProfileConnections().get(i).getConnectorId());
            Set<Status> user_s = user.getProfileConnections().get(i).getStatuses();
            Set<Status> Nuser_s = Nuser.getProfileConnections().get(i).getStatuses();
            assertEquals(user_s, Nuser_s);
        }
    }

    @Test
    public void updateConnectionTest_NewConnection(){
        Connection inputCon = new Connection(1, 2, Status.RELATIONS);
        dbH.updateConnection(inputCon, "");

        Connection outCon = dbH.getConnection(1,2);

        assertEquals(outCon.getStatuses(), inputCon.getStatuses());
    }

    @Test
    public void updateConnectionTest(){
        Connection inputCon = new Connection(1, 2, Status.RELATIONS);
        dbH.updateConnection(inputCon, "");

        inputCon.addStatus(Status.SEX);
        dbH.updateConnection(inputCon, " ");

        Connection outCon = dbH.getConnection(1, 2);
        assertEquals(inputCon.getStatuses(), outCon.getStatuses());
    }

    @Test
    public void getNullConnectionTest(){
        Connection conn = dbH.getConnection(1, 2);
        assertNull(conn);
    }

    @Test
    public void removeConnectionTest(){
        Connection inputCon = new Connection(1, 2, Status.RELATIONS);
        dbH.updateConnection(inputCon, "");
        dbH.removeConnection(inputCon, "");
        Connection out = dbH.getConnection(inputCon.getId(), inputCon.getConnectorId());

        assertNull(out);
    }

    @Test
    public void removeProfileTest(){
        user = new UserProfile(1, "l", "wsaf@asf.com", Locale.US);
        dbH.updateProfile(user, "lololo");

        dbH.removeProfile(1, "");

        UserProfile nUser = dbH.getProfile(1, "lolo");

        assertNull(nUser);
    }


}
