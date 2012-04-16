package com.messet.core

/**
 * Wrapper for instructions loaded into memory,
 * 	This is easier to use than MemoryWord's
 * @author haddaway
 *
 */
class Instruction {
	
	static final List<String> opCodes = [
		"jmp",
		"je",
		"add",
		"sub",
		"mul",
		"div",
		"cmp",
		"mov",
		"set"
	]
	
	static final List<String> useableRegisters = [
		"msp",
		"msp",
		"mbp",
		"mcr",
		"mgpa",
		"mgpb",
		"mgpc",
		"mgpd"
		]
	
	static final List<String> allRegisters = [
		"mip",
		"msp",
		"msp",
		"mbp",
		"mcr",
		"mgpa",
		"mgpb",
		"mgpc",
		"mgpd",
		"mer"
		]

	/**
	 * Location in programMemory
	 */
	long address
	
	/**
	 * The string instruction
	 */
	String data
	
	//Set's the data section of the instruction to a random *hopefully correct* instruction
	def randomize() {
		Random random = new Random()
		def randomOpCodeIndex = random.nextInt(opCodes.size())
		def randomRegisterIndex = random.nextInt(useableRegisters.size())
		def randomNumber = Math.abs(random.nextInt(32000))
		def diceRoll = random.nextInt(7)
		//Use a register
		if(diceRoll > 0) {
			def secondDice = random.nextInt(2)
			if(secondDice) {
				data = "${opCodes[randomOpCodeIndex]} ${useableRegisters[randomRegisterIndex]} $randomNumber"
			} else {
				data = "${opCodes[randomOpCodeIndex]} $randomNumber ${useableRegisters[randomRegisterIndex]}"
			}
		} else {
			def secondRandomNumber = Math.abs(random.nextInt(32000))
			data = "${opCodes[randomOpCodeIndex]} $secondRandomNumber $randomNumber"
		}
	}
}
