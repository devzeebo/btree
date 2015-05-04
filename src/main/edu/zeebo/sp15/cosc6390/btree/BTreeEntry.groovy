package edu.zeebo.sp15.cosc6390.btree

/**
 * User: Eric
 * Date: 4/30/2015
 */
class BTreeEntry<K, V> implements Comparable<BTreeEntry>{
	K key
	V value

	BTreeEntry() {}
	BTreeEntry(K k, V v) { key = k; value = v }

	String toString() { "$key: $value" }

	int compareTo(BTreeEntry other) {
		if (!key) {
			return 1
		}
		if (!other.key) {
			return -1
		}
		return key <=> other.key
	}
}
