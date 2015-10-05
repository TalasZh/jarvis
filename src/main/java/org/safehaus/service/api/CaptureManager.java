package org.safehaus.service.api;


import java.util.List;

import org.safehaus.model.Capture;
import org.safehaus.model.CaptureNotFoundException;
import org.safehaus.service.GenericManager;


public interface CaptureManager extends GenericManager<Capture, Long>
{
    List<Capture> getCapturesByUsername( String username );

    Capture getCapture( String captureId ) throws CaptureNotFoundException;

    Capture saveCapture( Capture capture );

    void removeCapture( Capture capture );
}
