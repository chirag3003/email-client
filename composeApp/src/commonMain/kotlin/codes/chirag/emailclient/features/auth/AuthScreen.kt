package codes.chirag.emailclient.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import codes.chirag.emailclient.core.ui.theme.EditorialColors
import codes.chirag.emailclient.core.ui.theme.AppTypography
import codes.chirag.emailclient.shared.model.User

@Composable
fun AuthScreen(
    onAuthenticated: (User) -> Unit
) {
    var isSignIn by remember { mutableStateOf(true) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val nameFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    val onAuthSubmit = {
        onAuthenticated(User(if (isSignIn) "User" else name, email, true))
    }

    LaunchedEffect(isSignIn) {
        if (isSignIn) {
            emailFocusRequester.requestFocus()
        } else {
            nameFocusRequester.requestFocus()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(EditorialColors.Background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(400.dp)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isSignIn) "Sign In" else "Create Account",
                style = AppTypography.headlineSmall,
                color = EditorialColors.TextPrimary,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            if (!isSignIn) {
                AuthTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Name",
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .focusRequester(nameFocusRequester)
                )
            }

            AuthTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email,
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .focusRequester(emailFocusRequester)
            )

            AuthTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                isPassword = true,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(onDone = { onAuthSubmit() }),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
                    .focusRequester(passwordFocusRequester)
            )

            Button(
                onClick = onAuthSubmit,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EditorialColors.Primary,
                    contentColor = EditorialColors.Background
                ),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = if (isSignIn) "Sign In" else "Sign Up",
                    style = AppTypography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (isSignIn) "Need an account? Sign Up" else "Already have an account? Sign In",
                style = AppTypography.bodyMedium.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif),
                color = EditorialColors.TextMuted,
                modifier = Modifier.clickable { isSignIn = !isSignIn }
            )
        }
    }
}

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    imeAction: ImeAction = ImeAction.Default,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    
    Column(modifier = modifier) {
        Text(
            text = label.uppercase(),
            style = AppTypography.labelSmall,
            color = EditorialColors.TextMuted,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = AppTypography.bodyLarge.copy(color = EditorialColors.TextPrimary),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            cursorBrush = SolidColor(EditorialColors.Primary),
            keyboardOptions = KeyboardOptions(
                imeAction = imeAction,
                keyboardType = keyboardType
            ),
            keyboardActions = keyboardActions,
            modifier = Modifier
                .fillMaxWidth()
                .background(EditorialColors.Background)
                .border(1.dp, EditorialColors.Border, RoundedCornerShape(4.dp))
                .onPreviewKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
                        if (keyEvent.isShiftPressed) {
                            focusManager.moveFocus(FocusDirection.Up)
                        } else {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                        true
                    } else {
                        false
                    }
                }
                .padding(12.dp),
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = "Enter $label",
                            style = AppTypography.bodyLarge,
                            color = EditorialColors.TextMuted.copy(alpha = 0.5f)
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}
