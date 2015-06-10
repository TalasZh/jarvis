package org.safehaus.stash.util;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.resource.ClientResource;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;


@RunWith( MockitoJUnitRunner.class )
public class RestUtilTest
{

    @Mock
    ClientResource clientResource;

    RestUtil restUtil;


    @Before
    public void setUp() throws Exception
    {
        restUtil = spy( new RestUtil() );
        doReturn( clientResource ).when( restUtil ).getClientResource( anyString() );
    }


    @Test
    public void testGet() throws Exception
    {
        //TODO
    }
}
