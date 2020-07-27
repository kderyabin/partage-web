package com.kderyabin.web.storage.multitenancy;

import org.springframework.web.context.annotation.RequestScope;

/**
 * Allows to set a tenant ID (database name) which is used to access main database or user database.
 * Main database name is unchanged. 
 * User database name is calculated by the application.
 * @see com.kderyabin.web.storage.AccountManager#getUserWorkspaceName(String) 
 */
@RequestScope
public class TenantContext {
    /**
     * Default tenant ID (main database)
     */
    public static final String DEFAULT_TENANT_IDENTIFIER = "main";
    /**
     * Tenant storage
     */
    public static final ThreadLocal<String> TENANT_IDENTIFIER = new ThreadLocal<>();

    /**
     * Sets tenant ID for the current thread.
     * @param tenantIdentifier Tenant id
     */
    public static void setTenant(String tenantIdentifier) {
        TENANT_IDENTIFIER.set(tenantIdentifier);
    }

    /**
     * Get current tenant ID
     * @return ID
     */
    public static String getId(){
        return TENANT_IDENTIFIER.get();
    }
}