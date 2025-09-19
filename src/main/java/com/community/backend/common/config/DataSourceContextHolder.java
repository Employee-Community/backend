package com.community.backend.common.config;

public class DataSourceContextHolder {
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

	public static void setDataSourceType(String dsType) {
		contextHolder.set(dsType);
	}

	public static String getDataSourceType() {
		return contextHolder.get();
	}

	public static void clear() {
		contextHolder.remove();
	}
}
