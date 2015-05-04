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

	def pointers = new LinkedList<BTreeEntry<K, ?>>()

	BTreeNode<K> sibling

	def getCount() { pointers.size() }

	def split() {
		// Create two new nodes
		BTreeNode<K> parent = node
		BTreeNode<K> sib = isLeaf ? leaf : node

		// Split our pointer set
		sib.pointers = pointers.subList(MAX_KEYS / 2 as int, MAX_KEYS) as LinkedList
		pointers = pointers.subList(0, MAX_KEYS / 2 as int) as LinkedList

		// Reassign the sibling hierarchy
		sib.sibling = sibling
		sibling = sib

		// Add ourselves to our parent
		parent.pointers << new BTreeEntry<K, Object>(pointers[0].key, this)
		parent.pointers << new BTreeEntry<K, Object>(sib.pointers[0].key, this)

		return parent
	}

	def add(K key, value) {
		if (isLeaf) {
			if (count >= size) {
				return false
			}

			pointers.add pointers.lastIndexOf{ it.key <= key }, new BTreeEntry(key, value)
		}
		else {
			if (!(pointers.lastIndexOf { it.key <= key } as BTreeNode).add(key, value)) {

			}
		}
	}
}
