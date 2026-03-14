package com.mappings

interface ObjectMapper<TSource, TTarget> {
    fun map(source: TSource): TTarget
}