package com.agents.app.demo.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape

// Container
val containerShapeDefault = RoundedCornerShape(size = defaultRadius)
val containerShapeMedium = RoundedCornerShape(size = mediumRadius)
val containerShapeSmall = RoundedCornerShape(size = smallRadius)

// Image/Icon
val filledIconButtonShape = RoundedCornerShape(size = iconButtonRadius)

// View
val bottomSheetShapeDefault = RoundedCornerShape(
    topStart = bottomSheetRadius,
    topEnd = bottomSheetRadius
)
val buttonShapeDefault = RoundedCornerShape(size = btnCornerRadius)
val textFieldShapeDefault = RoundedCornerShape(size = inputRadius)
val alertShapeDefault = RoundedCornerShape(size = alertContainerRadius)

// Calendar
val eventAccentStripShape = RoundedCornerShape(size = eventAccentStripRadius)
