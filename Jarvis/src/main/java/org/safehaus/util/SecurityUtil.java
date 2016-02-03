package org.safehaus.util;


import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


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


    public static UserDetails getUserDetails()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ( !( auth instanceof AnonymousAuthenticationToken ) )
        {
            UserDetails userDetails = ( UserDetails ) auth.getPrincipal();
            return userDetails;
        }
        else
        {
            return null;
        }
    }
}
