package org.safehaus.service;


import java.util.List;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.safehaus.Constants;
import org.safehaus.model.User;
import org.safehaus.service.api.RoleManager;
import org.safehaus.service.api.UserManager;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@Ignore
@Transactional(Transactional.TxType.NOT_SUPPORTED)
public class UserManagerTest extends BaseManagerTestCase {
    private Log log = LogFactory.getLog(UserManagerTest.class);
    @Autowired
    private UserManager mgr;
    @Autowired
    private RoleManager roleManager;

    @Before
    public void before() throws Exception {
        User user = new User();

        // call populate method in super class to populate test data
        // from a properties file matching this class name
        user = (User) populate(user);

        user.addRole(roleManager.getRole(Constants.USER_ROLE));

        user = mgr.saveUser(user);
        assertEquals("john", user.getUsername());
        assertEquals(1, user.getRoles().size());
    }

    @After
    public void after() {
        User user = mgr.getUserByUsername("john");
        mgr.removeUser(user.getId().toString());

        try {
            mgr.getUserByUsername("john");
            fail("Expected 'Exception' not thrown");
        } catch (Exception e) {
            log.debug(e);
            assertNotNull(e);
        }
    }

    @Test
    public void testGetUser() throws Exception {
        User user = mgr.getUserByUsername("john");
        assertNotNull(user);
        assertEquals(1, user.getRoles().size());
    }

    @Test
    public void testSaveUser() throws Exception {
        User user = mgr.getUserByUsername("john");
        user.setPhoneNumber("303-555-1212");

        log.debug("saving user with updated phone number: " + user);

        user = mgr.saveUser(user);
        assertEquals("303-555-1212", user.getPhoneNumber());
        assertEquals(1, user.getRoles().size());
    }

    @Test
    public void testGetAll() throws Exception {
        List<User> found = mgr.getAll();
        log.debug("Users found: " + found.size());
        // don't assume exact number so tests can run in parallel
        assertFalse(found.isEmpty());
    }
}
