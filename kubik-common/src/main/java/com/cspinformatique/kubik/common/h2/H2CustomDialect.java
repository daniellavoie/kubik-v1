package com.cspinformatique.kubik.common.h2;

import java.sql.Types;

import org.hibernate.dialect.H2Dialect;

public class H2CustomDialect extends H2Dialect {
	public H2CustomDialect(){
		super();
        registerColumnType(Types.FLOAT, "double");
	}
}
