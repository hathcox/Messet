package com.messet.main

class Test {

	static main(args) {
		def list = []
		list.add(1)
		println list.get(0)
		list.remove(0)
		try {
		println list.get(0)
		} catch(IndexOutOfBoundsException e) {
			println null
		}
	}

}
