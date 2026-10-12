package edu.uph.learn.taskreminder.ui.dashboard

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.uph.learn.taskreminder.data.assignment.AssignmentDatabase
import edu.uph.learn.taskreminder.data.assignment.AssignmentRepository
import edu.uph.learn.taskreminder.data.assignment.AssignmentService
import edu.uph.learn.taskreminder.domain.assignment.AssignmentField
import edu.uph.learn.taskreminder.domain.assignment.AssignmentValidator
import edu.uph.learn.taskreminder.ui.component.FormField
import edu.uph.learn.taskreminder.ui.component.InternalHeader
import edu.uph.learn.taskreminder.ui.theme.ColdBackground
import edu.uph.learn.taskreminder.ui.theme.Danger
import edu.uph.learn.taskreminder.ui.theme.Ink
import edu.uph.learn.taskreminder.ui.theme.MutedText
import edu.uph.learn.taskreminder.ui.theme.PrimaryTeal
import edu.uph.learn.taskreminder.ui.theme.SoftMint
import edu.uph.learn.taskreminder.ui.theme.interFamily
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAssignmentScreen(
    onBack: () -> Unit,
    onCreated: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    val viewModel: AssignmentViewModel = viewModel(
        factory = remember(context) {
            val database = AssignmentDatabase.getInstance(context)
            val repository = AssignmentRepository(database.assignmentDao())
            val service = AssignmentService(repository)

            AssignmentViewModelFactory(service)
        }
    )

    // Form fields
    var title by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var dueDateTime by remember {
        mutableLongStateOf(defaultDueDateTime())
    }

    // Form state
    var fieldErrors by remember {
        mutableStateOf(mapOf<AssignmentField, String>())
    }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val errorMessage by viewModel.errorMessage.collectAsState()
    val isCreating by viewModel.isCreating.collectAsState()

    val dateFormatter = remember {
        SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
    }
    val timeFormatter = remember {
        SimpleDateFormat("HH:mm", Locale.getDefault())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(ColdBackground)
            .padding(24.dp)
    ) {
        InternalHeader(
            title = "New Assignment",
            subtitle = "Capture the details now; stay ahead later.",
            onBackClick = onBack
        )

        Spacer(Modifier.height(24.dp))

        FormField(
            label = "Assignment Name",
            value = title,
            onValueChange = {
                title = it
                fieldErrors = fieldErrors - AssignmentField.TITLE
                viewModel.clearError()
            },
            placeholder = "e.g. Mobile App Proposal",
            errorMessage = fieldErrors[AssignmentField.TITLE]
        )

        Spacer(Modifier.height(12.dp))

        FormField(
            label = "Course / Subject",
            value = course,
            onValueChange = {
                course = it
                fieldErrors = fieldErrors - AssignmentField.COURSE
                viewModel.clearError()
            },
            placeholder = "e.g. Mobile Development",
            errorMessage = fieldErrors[AssignmentField.COURSE]
        )

        Spacer(Modifier.height(12.dp))

        FormField(
            label = "Notes (optional)",
            value = notes,
            onValueChange = {
                notes = it
                fieldErrors = fieldErrors - AssignmentField.DESCRIPTION
                viewModel.clearError()
            },
            placeholder = "Add instructions or things to remember",
            minLines = 3,
            maxLines = 5
        )

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Due Date
            Column(modifier = Modifier.weight(1.4f)) {
                Text(
                    text = "Due Date",
                    color = Ink,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    fontFamily = interFamily
                )

                Spacer(Modifier.height(6.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = dateFormatter.format(Date(dueDateTime)),
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            color = Ink,
                            fontSize = 14.sp,
                            fontFamily = interFamily
                        ),
                        isError = fieldErrors[AssignmentField.DUE_DATE] != null,
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = SoftMint,
                            focusedBorderColor = PrimaryTeal,
                            errorBorderColor = Danger,
                            unfocusedTextColor = Ink,
                            focusedTextColor = Ink,
                            unfocusedPlaceholderColor = MutedText,
                            focusedPlaceholderColor = MutedText
                        )
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable {
                                showDatePicker = true
                                Log.d(
                                    "CreateAssignmentScreen",
                                    "Clicked showDatePicker"
                                )
                            }
                    )
                }
            }

            // Due Time
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Due Time",
                    color = Ink,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    fontFamily = interFamily
                )

                Spacer(Modifier.height(6.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = timeFormatter.format(Date(dueDateTime)),
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            color = Ink,
                            fontSize = 14.sp,
                            fontFamily = interFamily
                        ),
                        isError = fieldErrors[AssignmentField.DUE_DATE] != null,
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = SoftMint,
                            focusedBorderColor = PrimaryTeal,
                            errorBorderColor = Danger,
                            unfocusedTextColor = Ink,
                            focusedTextColor = Ink,
                            unfocusedPlaceholderColor = MutedText,
                            focusedPlaceholderColor = MutedText
                        )
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable {
                                showTimePicker = true
                                Log.d(
                                    "CreateAssignmentScreen",
                                    "Clicked showTimePicker"
                                )
                            }
                    )
                }
            }
        }

        fieldErrors[AssignmentField.DUE_DATE]?.let {
            Spacer(Modifier.height(6.dp))
            Text(
                text = it,
                color = Danger,
                fontSize = 12.sp,
                fontFamily = interFamily
            )
        }

        errorMessage?.let {
            Spacer(Modifier.height(12.dp))
            Text(
                text = it,
                color = Danger,
                fontSize = 14.sp,
                fontFamily = interFamily
            )
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                val errors = AssignmentValidator.validateCreate(
                    title = title,
                    course = course,
                    dueDate = dueDateTime
                )

                fieldErrors = errors
                viewModel.clearError()

                if (errors.isEmpty() && !isCreating) {
                    viewModel.createAssignment(
                        title = title,
                        course = course,
                        description = notes,
                        dueDate = dueDateTime,
                        onSaved = onCreated
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryTeal
            ),
            enabled = !isCreating
        ) {
            if (isCreating) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
                Spacer(Modifier.width(8.dp))
                Text("Saving Assignment...")
            } else {
                Text("Save Assignment")
            }
        }
    }

    // Material 3 Compose date picker
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = dueDateTime.toUtcDateMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { utcMillis ->
                            dueDateTime = dueDateTime.withLocalDate(utcMillis)
                            fieldErrors = fieldErrors - AssignmentField.DUE_DATE
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Material 3 Compose time picker
    if (showTimePicker) {
        val currentTime = Calendar.getInstance().apply {
            timeInMillis = dueDateTime
        }

        val timePickerState = rememberTimePickerState(
            initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
            initialMinute = currentTime.get(Calendar.MINUTE),
            is24Hour = true
        )

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Select Due Time") },
            text = {
                TimePicker(state = timePickerState)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        dueDateTime = dueDateTime.withLocalTime(
                            hour = timePickerState.hour,
                            minute = timePickerState.minute
                        )
                        fieldErrors = fieldErrors - AssignmentField.DUE_DATE
                        showTimePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

private fun defaultDueDateTime(): Long {
    return Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, 1)
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

/**
 * Converts the local calendar date to the UTC-based milliseconds
 * expected by Material 3 DatePicker.
 */
private fun Long.toUtcDateMillis(): Long {
    val local = Calendar.getInstance().apply {
        timeInMillis = this@toUtcDateMillis
    }

    return Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
        clear()
        set(
            local.get(Calendar.YEAR),
            local.get(Calendar.MONTH),
            local.get(Calendar.DAY_OF_MONTH)
        )
    }.timeInMillis
}

/**
 * Applies the DatePicker's UTC calendar date to the existing local
 * date/time, preserving the selected hour and minute.
 */
private fun Long.withLocalDate(utcDateMillis: Long): Long {
    val selectedUtc = Calendar.getInstance(
        TimeZone.getTimeZone("UTC")
    ).apply {
        timeInMillis = utcDateMillis
    }

    return Calendar.getInstance().apply {
        timeInMillis = this@withLocalDate
        set(Calendar.YEAR, selectedUtc.get(Calendar.YEAR))
        set(Calendar.MONTH, selectedUtc.get(Calendar.MONTH))
        set(Calendar.DAY_OF_MONTH, selectedUtc.get(Calendar.DAY_OF_MONTH))
    }.timeInMillis
}

private fun Long.withLocalTime(hour: Int, minute: Int): Long {
    return Calendar.getInstance().apply {
        timeInMillis = this@withLocalTime
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}
