package edu.zeebo.sp15.cosc6390.btree

/**
 * User: Eric
 * Date: 4/30/2015
 */
class BTreeBucket<K, V> {

	static final int MAX_ENTRIES = 4

	def entries = new LinkedList<BTreeEntry<K, V>>()

	BTreeNode<K> parent

	def getCount() { entries.size() }

	def add(BTreeEntry<K, V> entry) {
		if (count >= MAX_ENTRIES) {
			BTreeBucket<K, V> sibling = new BTreeBucket<>()
			sibling.entries = entries.subList(MAX_ENTRIES / 2 as int, MAX_ENTRIES) as LinkedList
			entries = entries.subList(0, MAX_ENTRIES / 2 as int) as LinkedList

			parent.add sibling.entries[0].key, sibling
		}
		else {
			entries << entry
		}
	}

	def add(K key, V value) {
		add(new BTreeEntry<K, V>(key: key, value: value))
	}
}
