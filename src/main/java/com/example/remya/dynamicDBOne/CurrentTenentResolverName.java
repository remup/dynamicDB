package com.example.remya.dynamicDBOne;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;




public class CurrentTenentResolverName implements CurrentTenantIdentifierResolver{
	private static final String DEFAULT_TENANT_ID = "dbo";
	@Override
	public String resolveCurrentTenantIdentifier() {
		System.out.println("current tenant is "+CurrentTenantName.getCurrentTenant());
		
		String schemaTenant = CurrentTenantName.getCurrentTenant();

		if (schemaTenant != null && !schemaTenant.isEmpty()) {

			return schemaTenant;

		}
		
		return DEFAULT_TENANT_ID;
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		// TODO Auto-generated method stub
		return true;
	}

	

}
