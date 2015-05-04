package edu.zeebo.sp15.cosc6390.btree

/**
 * User: Eric
 * Date: 4/30/2015
 */
class BTree<K, V> {

	BTreeNode<K> root

	BTree() {
		root = BTreeNode.leaf
	}

	def add(K key, V value) {
		if (!root.add(key, value)) {
			root = root.split()
		}
	}

	def search(K key) {

	}

	public static void main(String[] args) {
		new BTree()
	}
}
