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

public class MultiTenantConnectionProvider extends AbstractMultiTenantConnectionProvider {

    final private Logger LOG = LoggerFactory.getLogger(MultiTenantConnectionProvider.class);

    private final Map<String, ConnectionProvider> connectionProviderMap = new HashMap<>();

//    @Autowired
//    DataSource dataSource;

    public MultiTenantConnectionProvider() {
        addTenantConnectionProvider(TenantContext.DEFAULT_TENANT_IDENTIFIER);
    }

    private void addTenantConnectionProvider(String tenantId) {
        LOG.info(">>> Initializing tenant with Id: " + tenantId);
        Properties properties = new Properties();
        try {
            //LOG.info(">>> DataSource class name: " + dataSource.getClass().getName());
            if( tenantId.equals(TenantContext.DEFAULT_TENANT_IDENTIFIER)) {
                properties.load(getClass().getResourceAsStream("/hibernate-db-main.properties"));
            } else {
                properties.load(getClass().getResourceAsStream("/hibernate-db-user.properties"));
                String url = String.format(properties.getProperty("hibernate.connection.url"), tenantId);
                properties.put("hibernate.connection.url",  url);

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
        LOG.info(">>> selectConnectionProvider for: " + tenantIdentifier);
        // Kind of a lazy loading: initialize on request.
        if(!connectionProviderMap.containsKey(tenantIdentifier)) {
            addTenantConnectionProvider(tenantIdentifier);
        }
        return connectionProviderMap.get(tenantIdentifier);
    }
}
