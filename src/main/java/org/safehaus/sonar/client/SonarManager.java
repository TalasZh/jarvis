package org.safehaus.sonar.client;


import org.safehaus.sonar.model.UnitTestStats;


/**
 * This manager provide means to execute a most commonly used subset of Sonar API.
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
}
