package com.community.backend.common.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
public class DataSourceRoutingAspect {

	@Before("@annotation(transactional)")
	public void setDataSource(Transactional transactional) {
		if (transactional.readOnly()) {
			DataSourceContextHolder.setDataSourceType("slave");
		} else {
			DataSourceContextHolder.setDataSourceType("master");
		}
	}
}