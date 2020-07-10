package com.kderyabin.web.storage.multitenancy;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MultiTenantConnectionProvider extends AbstractMultiTenantConnectionProvider {

    private final Map<String, ConnectionProvider> connectionProviderMap = new HashMap<>();

    public MultiTenantConnectionProvider() {
        addTenantConnectionProvider(TenantProfile.MAIN);
    }

    Map<String, ConnectionProvider> getConnectionProviderMap() {
        return connectionProviderMap;
    }
/*
    private void addTenantConnectionProvider(String tenantId, DataSource tenantDataSource, Properties properties) {

        DatasourceConnectionProviderImpl connectionProvider = new DatasourceConnectionProviderImpl();
        connectionProvider.setDataSource(tenantDataSource);
        connectionProvider.configure(properties);

        MultiTenantConnectionProvider.INSTANCE
                .getConnectionProviderMap()
                .put(tenantId, connectionProvider);
    }
*/
    private void addTenantConnectionProvider(String tenantId) {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream(
                    String.format("/hibernate-database-%s.properties", tenantId)));
            DriverManagerConnectionProviderImpl connectionProvider = new DriverManagerConnectionProviderImpl();
            connectionProvider.configure(properties);
            connectionProviderMap.put(tenantId, connectionProvider);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ConnectionProvider getAnyConnectionProvider() {
        return connectionProviderMap.get(TenantContext.DEFAULT_TENANT_IDENTIFIER);
    }

    @Override
    protected ConnectionProvider selectConnectionProvider(String tenantIdentifier) {
        return connectionProviderMap.get(tenantIdentifier);
    }
}
