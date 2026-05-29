package com.models

import kotlinx.serialization.Serializable

interface Name {
    fun getValue() : String
}

interface PersonalName : Name
interface ProjectName : Name
interface MiscellaneousName : Name

@Serializable
abstract class DetailedNameImpl<TName : Name>(
    open val primaryName : TName,
    open val aliases : List<TName>? = null
) : Name {
    override fun getValue(): String {
        return if (aliases == null) primaryName.getValue() else "$primaryName (a.k.a.) ${getStringFromAliases()}"
    }

    private fun getStringFromAliases(): String {
        val elements : List<String>? = aliases?.map { it.getValue() }
        return elements?.joinToString(", ") ?: String()
    }
}

class DetailedPersonalNameImpl(
    override val primaryName : PersonalName,
    override val aliases : List<PersonalName>? = null
) : DetailedNameImpl<PersonalName>(
    primaryName,
    aliases
), PersonalName

@Serializable
class LocalizedNameImpl<TName>(
    val name : TName,
    val localizedName : String
) : Name {
    override fun getValue(): String = "$name ($localizedName)"
}

@Serializable
class FullNameImpl(
    val parts: List<PartialPersonalName>
) : PersonalName {
    override fun getValue(): String {
        return parts.joinToString(
            separator = " "
        ) { part ->
            if (part.section == PersonalNameSection.EPITHET) {
                "'${part.value}'"
            } else {
                part.value
            }
        }
    }
}

@Serializable
class PartialPersonalName(
    val value: String,
    val section: PersonalNameSection
)

enum class PersonalNameSection {
    FIRST_NAME,
    MIDDLE_NAME,
    LAST_NAME,
    EPITHET,
    OTHER
}

@Serializable
class StageNameImpl(
    val value: String
) : PersonalName {
    override fun getValue(): String = value
}

class DetailedProjectNameImpl(
    override val primaryName : ProjectName,
    override val aliases : List<ProjectName>? = null
) : DetailedNameImpl<ProjectName>(
    primaryName,
    aliases
), ProjectName

@Serializable
class ProjectNameImpl(
    val value: String
) : ProjectName {
    override fun getValue(): String = value
}

@Serializable
abstract class MiscellaneousNameImpl(
    open val value: String
) : MiscellaneousName {
    override fun getValue(): String = value
}

class LabelNameImpl(
    override val value: String
) : MiscellaneousNameImpl(value)

class ReleaseNameImpl(
    override val value: String
) : MiscellaneousNameImpl(value)