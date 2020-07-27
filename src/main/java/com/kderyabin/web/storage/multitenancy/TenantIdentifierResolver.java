package com.kderyabin.web.storage.multitenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

/**
 * TenantIdentifierResolver is used internally by Hibernate to choose an appropriate connection provider.
 */
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    @Override
    public String resolveCurrentTenantIdentifier() {
        String currentTenantId = TenantContext.getId();
        return currentTenantId != null ? currentTenantId : TenantContext.DEFAULT_TENANT_IDENTIFIER;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}