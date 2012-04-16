package com.messet.core

/**
 * This is the meat baby!
 * @author haddaway
 *
 */
class Messet {

	/** Instruction Pointer */
	Register mip

	/** Stack Pointer */
	Register msp

	/** Base Pointer */
	Register mbp

	/** Counting Register */
	Register mcr

	/** This must be set for the program to finish execution */
	Register mer
	
	/**
	 * General Registers
	 */
	Register mgpa
	Register mgpb
	Register mgpc
	Register mgpd

	/** 
	 * Comparison Flag 
	 * 
	 * Defaults to false
	 * */
	boolean mcf

	/**
	 * Memory 
	 */
	ArrayList<Instruction> programMemory
	ArrayList<MemoryWord> dataMemory
	

	/**
	 * Stack
	 */
	MessetStack stack

	/**
	 * Constructors
	 */

	/**
	 * Default constructor
	 */

	Messet() {
		this.mbp = new Register()
		this.mcf = false
		this.mcr = new Register()
		this.programMemory = new ArrayList<Instruction>()
		this.dataMemory = new ArrayList<MemoryWord>()
		this.stack = new MessetStack()
		this.mgpa = new Register()
		this.mgpb = new Register()
		this.mip = new Register()
		this.mgpc = new Register()
		this.mgpd = new Register()
		this.msp = new Register()
		this.mer = new Register()
	}

	/**
	 *  Interface Functions
	 */

	/**
	 * Executes the line, throws an exception if there is invalid syntax
	 * @param line
	 * @return
	 */
	def execute(String line) {
		//def function = line.substring(0, line.indexOf(' ')).toLowerCase()
		def peices = line.split()
		println peices
		def function = null
		def destination = null
		def source = null
		
		if(peices.size() > 1) {
			function = peices[0]
			destination = peices[1]
			if(peices.size() > 2) {
				source = peices[2]
			}
		}
		
//		println "Function: [$function]"
//		println "Destination: [$destination]"
//		println "Source: [$source]"

		//Jump into dealing with the right Opp Code
		if (destination == null && source == null) {
			println "Error parse the destination or source"
			return
		}
		switch(function) {

			case "add":
				executeAdd(destination, source)
				break

			case "sub":
				executeSub(destination, source)
				break

			case "mul":
				executeMul(destination, source)
				break

			case "div":
				executeDiv(destination, source)
				break

			case "cmp":
				executeCmp(destination, source)
				break

			case "mov":
				executeMov(destination, source)
				break

			case "jmp":
				executeJmp(destination)
				break

			case "je":
				executeJe(destination)
				break

			case "set":
				executeSet(destination, source)
				break
				
			default:
				println "Unknown Opp Code [$function]"
		}
		
	}

	/**
	 * Displays all of the information that is currently set in the virtual machine
	 * @return
	 */
	def displayInfo() {
		println "Instruction Pointer: $mip.data"
		println "Base Pointer: $mbp.data"
		println "Stack Pointer: $msp.data"
		println "Counting Register: $mcr.data"
		println "Comparison Flag: $mcf"
		println "Exit Register: $mer"
		println "General Regiser A: $mgpa.data"
		println "General Register B: $mgpb.data"
		println "General Register C: $mgpc.data"
		println "General Register D: $mgpd.data"
		println "Program Memory:"
		println this.programMemory 
		println "Data Memory:"
		println this.dataMemory
		println "Stack:"
		println stack
	}
	
	/**
	 * Displays the current memory in the VM
	 * @return
	 */
	def displayMemory() {
		println "Memory Contents:"
		for(word in dataMemory) {
			println "[$word.address] : [$word.data]"
		}
	}

	def displayRegister(String register) {
		println "Register [$register]: " + getRegister(register)?.data
	}
	
	/**
	 * Score the VM, against a goal VM
	 */
	def score(Messet goal) {
		//The closer we are to zero, the better
		int score = 0
	}
	
	/**
	 * Executes any instructions already loaded into Program memory!
	 * @return
	 */
	def run() {
		/**
		 * Mer need to get set or the vm will hang. In the future we should attempt to detect this
		 */
		while(mer.data != 1) {
			if(mip.data < programMemory.size()) {
				execute(this.programMemory.get(mip.data as int).data)
				mip.data += 1
			} else {
				println "Attempted to enter an invalid instruction location"
				println "Shutting down the vm..."
				//Set the exit flag
				mer.data = 1
			}
		}
	}
	
	/**
	 * Given a list of strings, this will load
	 * each of them into memory one by one.
	 * 
	 * To run the program just call run()
	 * @param instructions
	 * 
	 * @return
	 */
	def loadInstructions(def instructions) {
		def memoryOffset = this.programMemory.size()
		for(index in 0..instructions.size()-1) {
			this.programMemory.add(new Instruction(address:(memoryOffset+index), data:instructions.get(index)))
		}
	}

	/**
	 * Clears EVERYTHING dealing with the virtual machine
	 * @return
	 */
	def clear() {
		this.mbp = new Register()
		this.mcf = false
		this.mcr = new Register()
		this.programMemory = new ArrayList<MemoryWord>()
		this.dataMemory = new ArrayList<MemoryWord>()
		this.stack = new MessetStack()
		this.mgpa = new Register()
		this.mgpb = new Register()
		this.mip = new Register()
		this.mgpc = new Register()
		this.mgpd = new Register()
		this.msp = new Register()
		this.mer = new Register()
	}

	/**
	 * Pre-Instruction Functions
	 */

	def getRegister(String test) {

		switch(test) {
			case "mip":
				return mip
			case "msp":
				return msp
			case "mbp":
				return mbp
			case "mcr":
				return mcr
			case "mgpa":
				return mgpa
			case "mgpb":
				return mgpb
			case "mgpc":
				return mgpc
			case "mgpd":
				return mgpd
			case "mer":
				return mer
			default:
				return null
		}
	}

	def executeMov(String destination, String source) {
		//Grab the registers if thats what we recieved
		def destReg = getRegister(destination)
		def sourceReg = getRegister(source)

		//We should have the a register as the first parameter
		if(destReg != null) {
			//If we got another register
			if(sourceReg != null) {
				mov(destReg, sourceReg.data)
			}
			//If we don't has a second register and we are int'y
			else if(source.isLong()) {
				mov(destReg, Long.parseLong(source))
			} else {
				println "Invalid Source inside Mov!"
			}
		} else {
			println "Mov's destination should be a register!"
		}
	}

	def executeAdd(String destination, String source) {
		//Grab the registers if thats what we recieved
		def destReg = getRegister(destination)
		def sourceReg = getRegister(source)

		//We should have the a register as the first parameter
		if(destReg != null) {
			//If we got another register
			if(sourceReg != null) {
				add(destReg, sourceReg.data)
			}
			//If we don't has a second register and we are int'y
			else if(source.isLong()) {
				add(destReg, Long.parseLong(source))
			} else {
				println "Invalid Source inside Add!"
			}
		} else {
			println "Add's destination should be a register!"
		}
	}

	def executeSub(String destination, String source) {
		//Grab the registers if thats what we recieved
		def destReg = getRegister(destination)
		def sourceReg = getRegister(source)

		//We should have the a register as the first parameter
		if(destReg != null) {
			//If we got another register
			if(sourceReg != null) {
				sub(destReg, sourceReg.data)
			}
			//If we don't has a second register and we are int'y
			else if(source.isLong()) {
				sub(destReg, Long.parseLong(source))
			} else {
				println "Invalid Source inside Sub!"
			}
		} else {
			println "Sub's destination should be a register!"
		}
	}

	def executeMul(String destination, String source) {
		//Grab the registers if thats what we recieved
		def destReg = getRegister(destination)
		def sourceReg = getRegister(source)

		//We should have the a register as the first parameter
		if(destReg != null) {
			//If we got another register
			if(sourceReg != null) {
				mul(destReg, sourceReg.data)
			}
			//If we don't has a second register and we are int'y
			else if(source.isLong()) {
				mul(destReg, Long.parseLong(source))
			} else {
				println "Invalid Source inside Mul!"
			}
		} else {
			println "Mul's destination should be a register!"
		}
	}
	
	def executeDiv(String destination, String source) {
		//Grab the registers if thats what we recieved
		def destReg = getRegister(destination)
		def sourceReg = getRegister(source)

		//We should have the a register as the first parameter
		if(destReg != null) {
			//If we got another register
			if(sourceReg != null) {
				div(destReg, sourceReg.data)
			}
			//If we don't has a second register and we are int'y
			else if(source.isLong()) {
				div(destReg, Long.parseLong(source))
			} else {
				println "Invalid Source inside Div!"
			}
		} else {
			println "Div's destination should be a register!"
		}
	}
	
	def executeCmp(String destination, String source) {
		//Grab the registers if thats what we recieved
		def destReg = getRegister(destination)
		def sourceReg = getRegister(source)

		//We should have the a register as the first parameter
		if(destReg != null) {
			//If we got another register
			if(sourceReg != null) {
				cmp(destReg, sourceReg.data)
			}
			//If we don't has a second register and we are int'y
			else if(source.isLong()) {
				cmp(destReg, Long.parseLong(source))
			} else {
				println "Invalid Source inside cmp!"
			}
		} else {
			println "cmp's destination should be a register!"
		}
	}
	
	def executeJmp(String destination) {
		//Grab the registers if thats what we recieved
		def destReg = getRegister(destination)

		//We might have the a register as the first parameter
		if(destReg != null) {
			jmp(destReg.data)
		} else if(destination.isLong()){
			jmp(Long.valueOf(destination))
		} else {
			println "Invalid imput for Jmp"
		}
	}
	
	def executeJe(String destination) {
		//Grab the registers if thats what we recieved
		def destReg = getRegister(destination)

		//We should have the a register as the first parameter
		if(destReg != null) {
			je(destReg.data)
		} else if(destination.isLong()) {
			je(Long.parseLong(destination))
		} else {
			println "Invalid target for je!"
		}
	}
	
	def executeSet(String destination, String source) {
		//Grab the registers if thats what we got
		def destReg = getRegister(destination)
		def sourceReg = getRegister(source)
		
		//Check to see if we have a register
		if(destReg != null) {
			if(sourceReg != null) {
				set(destReg.data, sourceReg.data)
			} else if (source.isLong()) {
				set(destReg.data, Long.parseLong(source))
			} else {
				println "Invalid source inside Set"
			}
		} else if (destination.isLong()) {
			if(sourceReg != null) {
				set(Long.parseLong(destination), sourceReg.data)
			} else if (source.isLong()) {
				set(Long.parseLong(destination), Long.parseLong(source))
			} else {
				println "Invalid source inside Set"
			}
		} else {
			println "Invalid destination inside Set"
		}
		
	}
	/**
	 * Instruction Functions
	 */

	/**
	 * Adds the source to the destination
	 * @param destination
	 * @param source
	 * @return
	 */
	def add(Register destination, long source) {
		destination.data += source
	}

	/**
	 * Subtracts the source to the destination
	 * @param destination
	 * @param source
	 * @return
	 */
	def sub(Register destination, long source) {
		destination.data -= source
	}

	/**
	 * Multiplies the destination by the source
	 * @param destination
	 * @param source
	 * @return
	 */
	def mul(Register destination, long source) {
		destination.data *= source
	}

	/**
	 * Divides the destination byt the source
	 * @param destination
	 * @param source
	 * @return
	 */
	def div(Register destination, long source) {
		destination.data /= source
	}

	/**
	 * Compares the two given values. If they are equvilant,
	 * then mcf is set to true
	 * 
	 * @param destination
	 * @param source
	 */
	def cmp(Register destination, long source) {
		if(destination.data == source) {
			mcf = true
		} else {
			mcf = false
		}
	}

	/**
	 * This sets a destination to the given source	
	 * @param destination
	 * @param source
	 * @return
	 */
	def mov(Register destination, long source) {
		destination.data = source
	}

	/**
	 * Set the instruction pointer to the destination
	 * @param destination
	 * @return
	 */
	def jmp(long destination) {
		//Subtract one to handle the fact the mip always increments
		mip.data = destination -1
	}

	/**
	 * Check to see if the comparison flag has been set, if so
	 * we set the instruction pointer to the destination
	 */
	def je(long destination) {
		//If the comparison flag has been set
		if(mcf) {
			//Subtract one to handle the fact the mip always increments
			mip.data = destination -1
		} else {
			//Do nothing
		}
	}
	
	/**
	 * This sets a location in memory to the given value,
	 * if the location doesn't exit, it is created
	 * 
	 * @param destination
	 * @param source
	 * @return
	 */
	def set(long destination, long source) {
		
		//If we dont have enough memory already allocated
		if(this.dataMemory.size() < destination) {
			def wordsToCreate = destination - dataMemory.size
			def offset = this.dataMemory.size
			for(word in 0..(wordsToCreate)) {
				def newWord = new MemoryWord()
				newWord.setAddress(offset + word)
				dataMemory.add(newWord)
			}
			
		}
		//Set our peice of memory
		dataMemory[destination as int].data = source
	}
}
