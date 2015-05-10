package edu.zeebo.sp15.cosc6340.btree

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
			return Integer.MAX_VALUE
		}
		if (!other.key) {
			return Integer.MIN_VALUE
		}
		return key <=> other.key
	}
}
