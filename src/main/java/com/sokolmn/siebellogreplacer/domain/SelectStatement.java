package com.sokolmn.siebellogreplacer.domain;

public class SelectStatement extends SQLStatement {

	public SelectStatement(String siebelString) {
		super(siebelString);
	}

	@Override
	public void removeFirstBind() {
		this.keyList.remove(0);
		this.keyValueTable.remove(":1");
		this.siebelLog = this.siebelLog.replaceAll(",\n.*:1", "");
	}
}
