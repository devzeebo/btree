package edu.zeebo.sp15.cosc6390.btree

/**
 * User: Eric
 * Date: 4/30/2015
 */
class BTree<K> extends BTreeNode<K> {

	static BTree instance

	BTree() {
		instance = this
		isLeaf = true
		size = MAX_ENTRIES
	}

	def split() {

		// create two new children
		BTreeNode<K> left = isLeaf ? leaf : node
		BTreeNode<K> right = isLeaf ? leaf : node
		// assign parent to this
		[left, right]*.parent = this

		// Assign the left and right pointer lists
		left.pointers = pointers.subList(0, count / 2 as int) as LinkedList
		right.pointers = pointers.subList(count / 2 as int, count) as LinkedList

		// clear the rightmost left key
		if (!left.isLeaf) {
			left.pointers[-1].key = null
		}
		else {
			left.rightSibling = right
			right.leftSibling = left
		}

		// reassign the parent node if not leaves
		if (!isLeaf) {
			[left, right].each { node -> node.pointers*.value*.parent = node }
		}

		// add the children to this
		pointers.clear()
		addDirect(new BTreeEntry(right.smallestKey, left))
		addDirect(new BTreeEntry(null, right))

		// Transform into a node
		if (isLeaf) {
			isLeaf = false
			size = MAX_KEYS
		}
	}

	def add(K key, value) {
		if (search(key)) {
			throw new IllegalStateException("$key is already in the tree")
		}
		super.add key, value
	}

	def delete(K key) {
		super.delete key

		if (count == 0) {
			isLeaf = true
			size = MAX_ENTRIES
		}
	}

	public static void main(String[] args) {
		BTree<Integer, Integer> tree = new BTree<>()

		def letters = (1..100).toList()
		letters.sort { Math.random() }.each {
			println "Adding $it"
			tree.add(it, it * it)
		}

		def snapshot = tree.snapshot()
		println snapshot
		(1..100).each {
			if (snapshot[it - 1] != it) {
				throw new AssertionError("$it out of order")
			}
		}

		(0..<10).each { a ->
			(1..10).each {
				println "Remove ${a * 10 + it}"
				tree.delete(a * 10 + it)
			}

			tree.printTree()
			println()
		}

		letters.each {
			tree.add(it, it * it)
		}

		println tree.search(Math.random() * 100 + 1 as int)
	}
}
