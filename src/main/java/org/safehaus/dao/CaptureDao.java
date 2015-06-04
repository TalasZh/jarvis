package org.safehaus.dao;


import java.util.List;

import org.safehaus.model.Capture;
import org.safehaus.model.CaptureNotFoundException;
import org.safehaus.model.Session;
import org.safehaus.model.SessionNotFoundException;
import org.springframework.transaction.annotation.Transactional;


public interface CaptureDao extends GenericDao<Capture, Long>
{
    Capture saveCapture( Capture capture);

    Capture getCapture( String captureId ) throws CaptureNotFoundException;
}
