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
	BTreeNode<K> leftSibling
	BTreeNode<K> rightSibling

	BTreeNode parent

	def getCount() { pointers.size() }

	def getSmallestKey() {
		isLeaf ? pointers[0].key : pointers[0].value.smallestKey
	}

	def getPointerIndex(K key) {
		pointers.findIndexOf { it.key == null || it.key > key }
	}

	def split() {
		// Create sibling node
		BTreeNode<K> sib = isLeaf ? leaf : node

		// Assign the parent
		sib.parent = parent

		// Split our pointer set
		sib.pointers = pointers.subList(0, count / 2 as int) as LinkedList
		// Reassign the parent and clear the last key
		if (!isLeaf) {
			sib.pointers*.value*.parent = sib
			sib.pointers[-1].key = null
		}
		pointers = pointers.subList(count / 2 as int, count) as LinkedList

		// Reassign the sibling hierarchy
		leftSibling?.rightSibling = sib
		sib.leftSibling = leftSibling
		sib.rightSibling = this
		leftSibling = sib

		// Add sibling to our parent
		parent.addDirect(new BTreeEntry(this.smallestKey, sib))
	}

	def addDirect(BTreeEntry entry) {

		if (entry.key == null) {
			pointers.add(entry)
		}
		else {
			int idx = getPointerIndex(entry.key)
			idx >= 0 ? pointers.add(idx, entry) : pointers << entry

			// if we assign a new lowest key
			if (parent != null && idx == 0) {
				if ((idx = parent.pointers.findIndexOf { it.value == this } - 1) > 0) {
					parent.pointers[idx].key = this.smallestKey
				}
			}
		}

		if (count > size) {
			split()
		}
	}

	def add(K key, value) {

		if (isLeaf) {
			addDirect(new BTreeEntry(key, value))
		}
		else {
			(pointers[getPointerIndex(key)].value as BTreeNode).add(key, value)
		}
	}

	def search(K key) {
		isLeaf ? pointers.find { it.key == key } : pointers[getPointerIndex(key)].value.search(key)
	}

	def deleteDirect(K key) {
		pointers.remove ( pointers.find { it.key == key } )
	}

	def delete(K key) {
		if (isLeaf) {
			deleteDirect(key)
		}
		else {
			(pointers[getPointerIndex(key)].value as BTreeNode).delete(key)
		}

		if (parent != null && count == 0) {
			parent.deleteDirect( parent.pointers.find { it.value == this }.key )
		}
	}

	def snapshot() {
		if (isLeaf) {
			def elements = []
			BTreeNode node = this

			while (node != null) {
				elements << node.pointers*.key
				node = node.rightSibling
			}
			return elements.flatten()
		}
		else {
			pointers[0].value.snapshot()
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
