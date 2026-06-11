package com.ai.calendar.demo.agent.client

private const val TOOL_CREATE_EVENT = "create_event"
private const val TOOL_LIST_EVENTS = "list_events"
private const val TOOL_FIND_NEAREST = "find_nearest_event"
private const val EMPTY_LIST = "[]"

fun formatSuccessResponse(toolName: String, result: String): String = when (toolName) {
    TOOL_CREATE_EVENT -> formatCreateEventResult(result)
    TOOL_LIST_EVENTS -> formatListEventsResult(result)
    TOOL_FIND_NEAREST -> formatFindNearestResult(result)
    else -> result
}

private fun formatCreateEventResult(result: String): String = result
    .replace("created successfully.", "")
    .replace("Event", "I created")
    .trim().trimEnd('.')
    .plus(".")

private fun formatListEventsResult(result: String): String =
    if (result == EMPTY_LIST) "You have no events this month." else "Here are your events:\n$result"

private fun formatFindNearestResult(result: String): String =
    if (result.contains("No upcoming")) result else "Your nearest event:\n$result"
