package org.safehaus.sonar.client;


import java.util.Date;
import java.util.Set;

import org.safehaus.sonar.model.ComplexityStats;
import org.safehaus.sonar.model.DuplicationStats;
import org.safehaus.sonar.model.QuantitativeStats;
import org.safehaus.sonar.model.TimeUnitTestStats;
import org.safehaus.sonar.model.UnitTestStats;
import org.safehaus.sonar.model.ViolationStats;


/**
 * This manager provides means to execute a most commonly used subset of Sonar API.
 *
 * All methods are read-only, they do not perform amy mutator operations on Sonar.
 *
 * All methods return Sonar metrics of some resource, i.e project or sub-project. New metrics can be easily implemented
 * upon need, see <a href="http://docs.sonarqube.org/display/SONAR/Metric+definitions">Sonar Metrics</a>
 *
 * @see <a href="https://sonar.subutai.io/api_documentation">Sonar Rest Api</a>.
 */
public interface SonarManager
{
    public UnitTestStats getUnitTestStats( String resourceId ) throws SonarManagerException;

    public ViolationStats getViolationStats( String resourceId ) throws SonarManagerException;

    public ComplexityStats getComplexityStats( String resourceId ) throws SonarManagerException;

    public DuplicationStats getDuplicationStats( String resourceId ) throws SonarManagerException;

    public QuantitativeStats getQuantitativeStats( String resourceId ) throws SonarManagerException;


    public Set<TimeUnitTestStats> getTimeUnitTestStats( String resourceId, Date fromDate, Date toDate )
            throws SonarManagerException;
}
