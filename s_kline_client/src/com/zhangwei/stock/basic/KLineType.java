package com.zhangwei.stock.basic;

public enum  KLineType{
	UnKnow,
	LHLR, //LEFT-HIGH-LOW-RIGHT
	LLHR, //LEFT-LOW-HIGH-RIGHT

	HLR, //HIGH-LOW-RIGHT --- best.sub
	LHR, //LOW-HIGH-RIGHT --- best.main
	LLH, //LEFT-LOW-HIGH
	LHL, //LEFT-HIGH-LOW

	HL,  //HIGH-LOW
	LH   //LOW-HIGH
}
