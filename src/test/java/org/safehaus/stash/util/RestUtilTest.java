package org.safehaus.stash.util;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.resource.ClientResource;
import org.safehaus.util.RestUtil;


@RunWith( MockitoJUnitRunner.class )
public class RestUtilTest
{

    @Mock
    ClientResource clientResource;

    RestUtil restUtil;


    @Before
    public void setUp() throws Exception
    {


    }


    @Test
    public void testGet() throws Exception
    {
        //TODO
    }
}
