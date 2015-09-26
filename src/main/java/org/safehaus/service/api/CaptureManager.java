package org.safehaus.service.api;


import org.safehaus.model.Capture;
import org.safehaus.model.CaptureNotFoundException;
import org.safehaus.service.GenericManager;


public interface CaptureManager extends GenericManager<Capture, Long>
{
    Capture getCapture( String captureId ) throws CaptureNotFoundException;

    Capture saveCapture( Capture capture );
}
