package com.dataTransferring.mappings

interface ObjectMapper<TSource, TTarget> {
    fun map(source: TSource): TTarget
}