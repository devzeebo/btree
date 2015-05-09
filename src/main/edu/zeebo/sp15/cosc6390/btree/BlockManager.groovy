package edu.zeebo.sp15.cosc6390.btree

/**
 * User: Eric
 * Date: 5/8/2015
 */
class BlockManager<V> {

	class Block<V> {

		BlockManager manager

		BlockElement[] elements = new BlockElement[4]
		int count

		Block(BlockManager manager) {
			(0..3).each {
				elements[it] = new BlockElement(block: this)
			}
			this.manager = manager
		}

		def getAt(int idx) { elements[idx] }
		def putAt(int idx, V value) { elements[idx].value = value }

		class BlockElement<V> {
			V value
			Block<V> block

			def delete() {
				value = null
				if (block.elements.count { it.value != null } == 0) {
					block.manager.blocks.remove block
				}
			}
		}
	}

	def blocks = []

	Block.BlockElement<V> getElement() {
		// add a new block if this one is full
		if (blocks.size() == 0 || blocks[-1].count == 4) {
			blocks << new Block<V>(this)
		}
		// get the next index in the last block
		return blocks[-1][blocks[-1].count++]
	}
}
