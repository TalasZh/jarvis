package org.safehaus.confluence.client;


import java.util.List;
import java.util.Set;
import org.safehaus.confluence.model.LabelResult;
import org.safehaus.confluence.model.Page;
import org.safehaus.confluence.model.Space;


public interface ConfluenceManager
{
	/**
	 * Searches for pages, for now only spaceKey and title can be given
	 * 
	 * @param page
	 * @return List of pages matching given parameters
	 * @throws ConfluenceManagerException
	 */
	List<Page> findPages( Page page ) throws ConfluenceManagerException;


	/**
	 * lists pages in specified space, can be iterated giving start and limit
	 * parameters.
	 * 
	 * @param spaceKey
	 * @param start
	 * @param limit
	 * @return
	 * @throws ConfluenceManagerException
	 */
	Set<Page> listPages( String spaceKey, Integer start, Integer limit ) throws ConfluenceManagerException;


	/**
	 * Returns a piece of Content.
	 * 
	 * @param pageId
	 *            the id of the content
	 * @return
	 * @throws ConfluenceManagerException
	 */
	Page getPage( String pageId ) throws ConfluenceManagerException;


	/**
	 * Creates new page
	 * 
	 * @param page
	 * @return
	 * @throws ConfluenceManagerException
	 */
	Page createNewPage( Page page ) throws ConfluenceManagerException;


	/**
	 * Updates a piece of Content, or restores if it is trashed.
	 * 
	 * The body contains the representation of the content. Must include the new
	 * version number.
	 * 
	 * @param page
	 * @return
	 * @throws ConfluenceManagerException
	 */
	Page updatePage( Page page ) throws ConfluenceManagerException;


	/**
	 * Deletes the page by specified id
	 * 
	 * @param page
	 * @throws ConfluenceManagerException
	 */
	void deletePage( Page page ) throws ConfluenceManagerException;


	/**
	 * Creates new space. The incoming Space does not include an id, but must
	 * include a Key and Name, and should include a Description.
	 * 
	 * @param space
	 * @return
	 * @throws ConfluenceManagerException
	 */
	Space createNewSpace( Space space ) throws ConfluenceManagerException;


	/**
	 * Updates a Space. Currently only the Space name, description and homepage
	 * can be updated.
	 * 
	 * @param space
	 *            Space to be updated, key property must be set
	 * @throws ConfluenceManagerException
	 */
	void updateSpace( Space space ) throws ConfluenceManagerException;


	/**
	 * Returns information about a space, with description.plain expanded
	 * 
	 * @param key
	 *            Key of space
	 * @return Space for given key
	 * @throws ConfluenceManagerException
	 */
	Space getSpace( String key ) throws ConfluenceManagerException;


	/**
	 * Finds all spaces.
	 * 
	 * @return
	 * @throws ConfluenceManagerException
	 */
	List<Space> getAllSpaces() throws ConfluenceManagerException;


	/**
	 * Deletes a Space.
	 * 
	 * The space is deleted in a long running task, so the space cannot be
	 * considered deleted when this resource returns. Clients can follow the
	 * status link in the response and poll it until the task completes.
	 * 
	 * @param space
	 *            Space with key property set
	 * @throws ConfluenceManagerException
	 */
	void deleteSpace( Space space ) throws ConfluenceManagerException;


	/**
	 * Returns the list of labels on a piece of Content.
	 * 
	 * @param pageId
	 * @return
	 * @throws ConfluenceManagerException
	 */
	LabelResult getPageLabels( String pageId ) throws ConfluenceManagerException;


	/**
	 * Adds a list of labels to the specified page.
	 * 
	 * @param pageId
	 * @param labelNames
	 * @return
	 * @throws ConfluenceManagerException
	 */
	LabelResult addPageLabels( String pageId, Set<String> labelNames ) throws ConfluenceManagerException;


	/**
	 * Deletes a labels of the specified page
	 * 
	 * @param pageId
	 *            Id of page
	 * @param labelName
	 *            Name of label to delete, null to delete all.
	 * @throws ConfluenceManagerException
	 */
	void deletePageLabels( String pageId, String labelName ) throws ConfluenceManagerException;


	String getBaseUrl();
}
