package edu.zeebo.sp15.cosc6390.btree

/**
 * User: Eric
 * Date: 4/30/2015
 */
class BTree<K, V> extends BTreeNode<K> {

	BTree() {
		isLeaf = true
		size = MAX_ENTRIES
	}

	def split() {

		println "Split Root"

		// create two new children
		BTreeNode<K> left = isLeaf ? leaf : node
		BTreeNode<K> right = isLeaf ? leaf : node
		// assign parent to this
		[left, right]*.parent = this

		// Assign the left and right pointer lists
		left.pointers = pointers.subList(0, count / 2 as int) as LinkedList
		right.pointers = pointers.subList(count / 2 as int, count) as LinkedList

		println "$isLeaf : left: $left.isLeaf -- right: $right.isLeaf"
		println "${left.pointers.collect { it.key }.join(',')}"
		println "${right.pointers.collect { it.key }.join(',')}"
		// reassign the parent node if not leafs
		if (!isLeaf) {
			[left, right].each { node -> node.pointers*.value*.parent = node }
		}

		// add the children to this
		pointers.clear()
		[left, right].each { addDirect(new BTreeEntry(it.pointers[0].key, it)) }

		// Transform into a node
		if (isLeaf) {
			isLeaf = false
			size = MAX_KEYS
		}
	}

	def search(K key) {

	}

	public static void main(String[] args) {
		BTree<String, String> tree = new BTree<>()

		('a'..'z').each {
			tree.add(it, it)
			tree.printTree()
		}
	}
}
