package com.messet.core

class MessetStack {
	
	def stack = []
	
	/**
	 * Interface for removing the top element from the stack
	 * 
	 * This returns null if the stack is empty
	 * @return instruction
	 */
	def pop() {
		try {
			def word = stack.get(0)
			stack.remove(0)
			return word
		
		} catch (IndexOutOfBoundsException e) {
			return null
		}
	}
	
	/**
	* Interface for pushing an element to the stack
	*
	*/
   def push(MemoryWord word) {
	   stack.add(word)
   }
   
   /**
    * Returns the size of the stack
    * @return long
    */
   def size() {
	   return stack.size()
   }
	
	
}
