package com.kderyabin.web.storage.multitenancy;

import org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Multitenancy connection provider implementation.
 */
public class MultiTenantConnectionProvider extends AbstractMultiTenantConnectionProvider {

    final private Logger LOG = LoggerFactory.getLogger(MultiTenantConnectionProvider.class);

    /**
     * Map of connection providers.
     */
    private final Map<String, ConnectionProvider> connectionProviderMap = new HashMap<>();

    /**
     * Constructor.
     * Sets default connection provider.
     */
    public MultiTenantConnectionProvider() {
        addTenantConnectionProvider(TenantContext.DEFAULT_TENANT_IDENTIFIER);
    }

    /**
     * Loads from the system connection settings.
     * Adjusts a database name when access to user database is required.
     * @param tenantId tenant ID
     */
    private void addTenantConnectionProvider(String tenantId) {
        LOG.info(">>> Initializing tenant with Id: " + tenantId);
        Properties properties = new Properties();
        try {

            if (tenantId.equals(TenantContext.DEFAULT_TENANT_IDENTIFIER)) {
                properties.load(getClass().getResourceAsStream("/hibernate-db-main.properties"));
            } else {
                properties.load(getClass().getResourceAsStream("/hibernate-db-user.properties"));
                String url = String.format(properties.getProperty("hibernate.connection.url"), tenantId);
                properties.put("hibernate.connection.url", url);

            }
            DriverManagerConnectionProviderImpl connectionProvider = new DriverManagerConnectionProviderImpl();

            connectionProvider.configure(properties);
            connectionProviderMap.put(tenantId, connectionProvider);
            LOG.info("Tenant initialized with success. Id : " + tenantId);
        } catch (Exception e) {
            LOG.warn("Error during tenant initialization: " + tenantId);
        }
    }

    @Override
    protected ConnectionProvider getAnyConnectionProvider() {
        return connectionProviderMap.get(TenantContext.DEFAULT_TENANT_IDENTIFIER);
    }

    @Override
    protected ConnectionProvider selectConnectionProvider(String tenantIdentifier) {
        LOG.debug(">>> selectConnectionProvider for: " + tenantIdentifier);
        // Kind of a lazy loading: initialize on request.
        if (!connectionProviderMap.containsKey(tenantIdentifier)) {
            addTenantConnectionProvider(tenantIdentifier);
        }
        return connectionProviderMap.get(tenantIdentifier);
    }
}
