package com.messet.main

import com.lanistae.core.Lanistae
import com.messet.core.Messet

class Main {
	static Scanner scanner = new Scanner(System.in)

	static Messet vm = new Messet()
	
	static main(args) {
		println "Welcome to Messet"
		def goalVM = new Messet()
		goalVM.mgpa.data = 5
		goalVM.mgpb.data = 10
		goalVM.mgpc.data = 15
		goalVM.mgpd.data = 20
		Lanistae lan = new Lanistae(20, goalVM, null, 20, (0.1), (0.2))
		lan.randomInitialize()
		while(true) {
			lan.evolve();
			println "Leading Score:" + lan.currentGeneration[0].score
			if(lan.currentGeneration[0].score == 0) {
				println "Winner!"
				lan.currentGeneration[0].programMemory.each {
					println it	
				}
				System.exit(0);
			}
			
		}
	}
	
	void runTestProgram() {
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
