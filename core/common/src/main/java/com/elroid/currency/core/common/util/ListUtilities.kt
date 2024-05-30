package com.elroid.currency.core.common.util

fun <E> MutableList<E>.addUnique(item: E): MutableList<E> {
    if (!contains(item))
        add(item)
    return this
}