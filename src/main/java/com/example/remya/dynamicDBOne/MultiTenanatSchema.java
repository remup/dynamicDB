package com.example.remya.dynamicDBOne;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

//@SuppressWarnings("serial")
//public class MultiTenanatSchema  extends AbstractMultiTenantConnectionProvider{
//	private ConnectionProvider connectionProvider;
//	@Override
//	protected ConnectionProvider getAnyConnectionProvider() {
//		// TODO Auto-generated method stub
//		return connectionProvider;
//	}
//
//	@Override
//	protected ConnectionProvider selectConnectionProvider(String arg0) {
//		// TODO Auto-generated method stub
//		return connectionProvider;
//	}
//	
//	@Override
//    public Connection getConnection(String tenant)
//      throws SQLException {
//		System.out.println("current tenant inside multiconnectionProvider "+tenant);
//        Connection connection = super.getConnection(tenant);
//        if (tenant != null && tenant.equalsIgnoreCase("remya") ) {
//        connection.createStatement()
//          .execute(String.format("SET SCHEMA %s;", tenant));
//		}
//        return connection;
//    }
//	
//	
//
//
//}
@SuppressWarnings("serial")
public class MultiTenanatSchema implements MultiTenantConnectionProvider{
	@Autowired
    private DataSource dataSource;
//	@Autowired
//	DriverManagerDataSource ds;
	
	@Override
	public boolean isUnwrappableAs(Class unwrapType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> unwrapType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Connection getAnyConnection() throws SQLException {
		// TODO Auto-generated method stub
		return dataSource.getConnection();
	}

	@Override
	public void releaseAnyConnection(Connection connection) throws SQLException {
		connection.close();
		
	}

	@Override
	public Connection getConnection(String tenant) throws SQLException {
		System.out.println("current tenant inside multiconnectionProvider "+tenant);
		
      Connection connection = dataSource.getConnection();
      
      
	
     // Connection connection = DriverManager.getConnection(container.getJdbcUrl().concat("&" + "currentSchema=store"), properties);
     // connection.setSchema(tenant);
//      connection.createStatement()
//      .execute(String.format("SET SCHEMA %s;", tenant));
      
     
      System.out.println(connection.getMetaData());
//      if (tenant != null && tenant.equalsIgnoreCase("remya") ) {
    	  connection.setSchema("remya");
//    	  connection.createStatement()
//          .execute(String.format("SET SCHEMA %s;", tenant));
      
		//}
      //else {
    	  
    	  //connection.setSchema("dbo");
     // }
      System.out.println("final connection schema "+connection.getMetaData().getURL());
		return connection;
	}

	@Override
	public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
		connection.close();
		
	}

	@Override
	public boolean supportsAggressiveRelease() {
		// TODO Auto-generated method stub
		return false;
	}

}


