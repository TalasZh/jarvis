package org.safehaus.util;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * Created by tzhamakeev on 6/4/15.
 */
public class SecurityUtil
{
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";


    public static boolean hasRole( String roleName )
    {
        for ( GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities() )
        {
            if ( authority.getAuthority().equals( roleName ) )
            {
                return true;
            }
        }
        return false;
    }
}
