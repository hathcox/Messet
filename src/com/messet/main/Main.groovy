package com.messet.main

import com.messet.core.Messet

class Main {
	static Scanner scanner = new Scanner(System.in)

	def static vm = new Messet()
	
	static main(args) {
		println "Welcome to Messet"
		def instructions = new File('test/easy.ms').readLines()
		vm.loadInstructions(instructions)
		vm.run()
		vm.displayMemory()
	}
	
	void consolePromptLoop() {
		while(true) {
			print "Enter a command: [execute] [display]:\n"
			def input = scanner.nextLine()
			
			if(input == 'execute') {
				print "Enter a vm Instruction:\n"
				def executeString = scanner.nextLine()
				vm.execute(executeString)
			}
			else if(input == 'display') {
				print "Enter a register to display:\n"
				def register = scanner.nextLine()
				vm.displayRegister(register)
			}
			else {
				println "Invalid command!\n"
			}
		}
	}

}
