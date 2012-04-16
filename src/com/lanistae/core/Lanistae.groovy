package com.lanistae.core

import com.messet.core.Instruction
import com.messet.core.Messet


/** 
 * This class is used to train and release programs that will run on Messet
 * @author haddaway
 *
 */
class Lanistae {
	def goals
	Messet goalVM
	int populationSize
	float mutationChance
	float mutationFactor
	float elitism
	int generationCount = 0
	ArrayList<Messet> currentGeneration
	int maxInstructionSize
	
	public Lanistae(int maxInstructionSize, Messet goalVM, def goals, int populationSize, float mutationChance, float mutationFactor, float elitism) {
		this.goals = goals
		this.populationSize = populationSize
		this.mutationChance = mutationChance
		this.mutationFactor = mutationFactor
		this.elitism = elitism
		this.goalVM = goalVM
		currentGeneration = new ArrayList<Messet>(populationSize)
		this.maxInstructionSize = maxInstructionSize
	}
	
	void randomInitialize() {
		for(vm in 0..(populationSize-1)) {
			def newVM = new Messet()
			def randomInstructionList = generateRandomInstructionList()
			newVM.loadInstructions(randomInstructionList)
			currentGeneration[vm] = newVM
		}
	}
	
	List<String> generateRandomInstructionList() {
		Random random = new Random()
		def instructionList = []
		def randomSize = random.nextInt(maxInstructionSize)
		for(index in 0..(randomSize-1)) {
			def tempInstruction = new Instruction()
			tempInstruction.randomize()
			instructionList.add(tempInstruction.data)
		}
		println instructionList
		return instructionList
	}
	
	void runCurrentGeneration() {
		for(vm in currentGeneration) {
			//TODO: Run these on threads, make sure that we dont hit a halting situation!
			vm.run()
			vm.score(goalVM)
		}
	}
	
	void sortCurrentGeneration() {
		this.currentGeneration.sort {
			it.score
		}
	}
	
	void evolve() {
		'''
		This is the meat of the program!
		'''
		//Temporary variable to work with
		ArrayList<Messet> nextGeneration = new ArrayList<Messet>()
		
		//Run the current Generation
		runCurrentGeneration()
		
		//Sort the current Generation based on scores
		sortCurrentGeneration()
		
		//Grab a batch of the most fit solutions (based on the elitismRate)
		int numberOfElite = populationSize * elitism
		for(index in 0..(numberOfElite-1))
			nextGeneration.add(currentGeneration[index])
		
		//Mutate the parents (based on the mutationChance, and mutationFactor)
		for (solution in nextGeneration)
			solution.mutate(mutationChance, mutationFactor)
		
		//Fill back to the populationSize with random new Solutions
		while( len(nextGeneration) < self.populationSize)
			nextGeneration.append(Solution.Solution(self.validCoins, self.numberOfCoins, self.targetValue))
		
		//Update the Generation
		currentGeneration = nextGeneration
		generationCount += 1
	}

}
