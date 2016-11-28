package com.cspinformatique.kubik.common.mysql;

import java.sql.Types;

import org.hibernate.dialect.MySQLDialect;

public class MySQLCustomDialect extends MySQLDialect {
	public MySQLCustomDialect(){
		super();
        registerColumnType(Types.CLOB, "text");
	}
}
