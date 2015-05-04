package edu.zeebo.sp15.cosc6390.btree

/**
 * User: Eric
 * Date: 4/30/2015
 */
class BTreeNode<K> {

	static final int MAX_KEYS = 10
	static final int MAX_ENTRIES = 4

	// Create a new node-type node: BTreeNode.node
	static BTreeNode<K> getNode() { new BTreeNode<K>(isLeaf: false, size: MAX_KEYS) }
	// Create a new leaf-type node: BTreeNode.leaf
	static BTreeNode<K> getLeaf() { new BTreeNode<K>(isLeaf: true, size: MAX_ENTRIES) }

	boolean isLeaf
	int size

	def pointers = new LinkedList<BTreeEntry>()
	// The 11th pointer
	BTreeNode<K> sibling

	BTreeNode parent

	def getCount() { pointers.size() }

	def getSmallestKey() {
		isLeaf ? pointers[0].key : pointers[0].value.smallestKey
	}

	def split() {
		// Create sibling node
		BTreeNode<K> sib = isLeaf ? leaf : node

		// Assign the parent
		sib.parent = parent

		// Split our pointer set
		sib.pointers = pointers.subList(0, count / 2 as int) as LinkedList
		// Reassign the parent
		if (!isLeaf) {
			sib.pointers*.value*.parent = sib
		}
		pointers = pointers.subList(count / 2 as int, count) as LinkedList

		// Reassign the sibling hierarchy
		sibling = sib

		// Add sibling to our parent
		parent.addDirect(new BTreeEntry(this.smallestKey, sib))
	}

	def addDirect(BTreeEntry entry) {

		if (entry.key == null) {
			pointers.add(entry)
		}
		else {
			int idx = pointers.findLastIndexOf { it.key < entry.key } + 1
			pointers.add(idx, entry)

			// Keep the keys sorted
			pointers.sort(true)

			// if we assign a new lowest key
			if (parent != null && idx == 0) {
				if ((idx = parent.pointers.findIndexOf { it.value == this } - 1) > 0) {
					parent.pointers[idx].key = pointers[0].key
				}
			}
		}

		if (count > size) {
//			BTree.instance.printTree()
			split()
//			BTree.instance.printTree()
//			println()
		}
	}

	def add(K key, value) {

		if (isLeaf) {
			addDirect(new BTreeEntry(key, value))
		}
		else {
			(pointers[pointers.findIndexOf{ it.key > key }].value as BTreeNode).add(key, value)
		}
	}

	def printTree(int indent = 0) {
		print '\t' * indent
		println pointers*.key.join(',')

		if (!isLeaf) {
			pointers*.value*.printTree(indent + 1)
		}
	}
}
