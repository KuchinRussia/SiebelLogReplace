package com.sokolmn.siebellogreplacer;

import com.sokolmn.siebellogreplacer.domain.SQLStatement;
import com.sokolmn.siebellogreplacer.domain.SelectStatement;

import java.util.regex.Pattern;

class SqlBindReplacer {
	public static String replace(String sourceSql) {
		SQLStatement sqlStatement = getSqlStatement(sourceSql);
		return replace(sqlStatement);
	}

	private static SQLStatement getSqlStatement(String sourceSql) {
		Pattern selectPattern = Pattern.compile("SELECT\\n");
		SQLStatement sqlStatement;

		if (selectPattern.matcher(sourceSql).find()) {
			sqlStatement = new SelectStatement(sourceSql);
		} else {
			sqlStatement = new SQLStatement(sourceSql);
		}

		return sqlStatement;
	}

	private static String replace(SQLStatement sqlStatement) {
		sqlStatement.setKeyList();
		sqlStatement.setKeyValueTable();
		sqlStatement.removeFirstBind();
		sqlStatement.replaceLog();
		sqlStatement.cutObjBinds();
		return sqlStatement.getSiebelReplaceLog();
	}
}