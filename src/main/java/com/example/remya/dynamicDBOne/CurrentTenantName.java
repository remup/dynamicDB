package com.example.remya.dynamicDBOne;

public class CurrentTenantName {

	private static ThreadLocal<String> currentTenant = new ThreadLocal<>();

	public static String getCurrentTenant() {
		return currentTenant.get();
	}

	public static void setCurrentTenant(String currentTenantName) {
		currentTenant.set(currentTenantName);
	}

	
	
	 
}
