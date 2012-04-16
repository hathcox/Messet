package com.messet.core

/**
 * Wrapper for instructions loaded into memory,
 * 	This is easyier to use than MemoryWord's
 * @author haddaway
 *
 */
class Instruction {

	/**
	 * Location in programMemory
	 */
	long address
	
	/**
	 * The string instruction
	 */
	String data
}
