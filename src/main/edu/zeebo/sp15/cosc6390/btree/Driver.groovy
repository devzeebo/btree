package edu.zeebo.sp15.cosc6390.btree

/**
 * User: Eric
 * Date: 5/9/2015
 */
class Driver {

	static enum Mode {
		INSERT, SEARCH, LIST, UPDATE, DELETE, SNAPSHOT
	}

	static def main(String[] args) {
		Scanner scan = new Scanner(new File(args[args.findIndexOf { it == '-f' } + 1]))

		BTree<String, String> tree = new BTree<>()
		Mode mode

		String updateKey

		while (scan.hasNextLine()) {
			String originalCommand = scan.nextLine().toLowerCase()
			String command = originalCommand.toLowerCase()

//			println '\n\n\n'
//			println tree.printTree()
			println command

			if (command[0] == '*') {
				mode = Mode.valueOf command.substring(1).toUpperCase()

				switch (mode) {
					case Mode.SNAPSHOT:
						println "Number of records: ${tree.size}"
						println "Number of blocks: ${tree.manager.blocks.size()}"
						println "Depth: ${tree.depth}"
						println "Node graph:\n${tree.pointersString}"
						break
					case Mode.LIST:
						println tree.list().join('\n')
						break
					case Mode.UPDATE:
						updateKey = null
						break
				}
			}
			else {
				try {
					switch (mode) {
						case Mode.INSERT:
							tree.add command, null
							break
						case Mode.SEARCH:
							def value
							if ((value = tree.search(command))) {
								println value.value
							} else {
								println "Key not found: $command"
							}
							break
						case Mode.UPDATE:
							if (updateKey) {
								def value = tree.search(updateKey)
								if (value) {
									value.value = originalCommand
								}
								else {
									println "Error: $updateKey not in tree"
								}
							} else {
								updateKey = command
							}
							break
						case Mode.DELETE:
							tree.delete command
							break
					}
				}
				catch (Exception e) {
					println "Error: $e.message"
					e.printStackTrace()
				}
			}

//			println tree.list()
			def checker = ''
			tree.list().each { String it ->
				assert checker < it
				checker = it
			}

//			println tree.printTree()
		}
	}
}
