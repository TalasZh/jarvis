package org.safehaus.dao;


import java.util.List;

import org.safehaus.model.Capture;
import org.safehaus.model.CaptureNotFoundException;


public interface CaptureDao extends GenericDao<Capture, Long>
{
    List<Capture> getCapturesByUsername( String username );

    Capture saveCapture( Capture capture);

    Capture getCapture( String captureId ) throws CaptureNotFoundException;
}
