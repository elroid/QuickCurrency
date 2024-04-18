package com.elroid.currency.data.common

fun <E> MutableList<E>.addUnique(item: E): MutableList<E> {
    if (!contains(item))
        add(item)
    return this
}