package org.safehaus.stash.model;


import java.util.Map;
import java.util.Set;

import org.safehaus.dao.entities.stash.Commit;
import org.safehaus.dao.entities.stash.Link;
import org.safehaus.dao.entities.stash.MinimalCommit;
import org.safehaus.stash.client.Page;

import com.google.common.base.Objects;


public class ChangeSet
{
    private MinimalCommit fromCommit;
    private Commit toCommit;
    private Page<Change> changes;
    private Link link;
    private Map<String, Set<Map<String, String>>> links;
    private Repository repository;


    public MinimalCommit getFromCommit()
    {
        return fromCommit;
    }


    public Commit getToCommit()
    {
        return toCommit;
    }


    public Page<Change> getChanges()
    {
        return changes;
    }


    public Link getLink()
    {
        return link;
    }


    public Map<String, Set<Map<String, String>>> getLinks()
    {
        return links;
    }


    public Repository getRepository()
    {
        return repository;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "fromCommit", fromCommit ).add( "toCommit", toCommit )
                      .add( "changes", changes ).add( "link", link ).add( "links", links )
                      .add( "repository", repository ).toString();
    }
}
