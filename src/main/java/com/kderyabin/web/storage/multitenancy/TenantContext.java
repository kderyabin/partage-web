package com.kderyabin.web.storage.multitenancy;

import org.springframework.web.context.annotation.RequestScope;

@RequestScope
public class TenantContext {

    public static final String DEFAULT_TENANT_IDENTIFIER = "main";

    public static final ThreadLocal<String> TENANT_IDENTIFIER = new ThreadLocal<>();

    public static void setTenant(String tenantIdentifier) {
        TENANT_IDENTIFIER.set(tenantIdentifier);
    }

    public static void reset(String tenantIdentifier) {
        TENANT_IDENTIFIER.remove();
    }

    public static String getId(){
        return TENANT_IDENTIFIER.get();
    }
}