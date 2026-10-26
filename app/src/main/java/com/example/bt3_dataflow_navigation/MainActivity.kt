package com.example.bt3_dataflow_navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bt3_dataflow_navigation.ui.theme.Bt3_dataflow_navigationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Bt3_dataflow_navigationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PasswordResetApp()
                }
            }
        }
    }
}

class ResetPasswordViewModel : ViewModel() {
    var email by mutableStateOf("")
    var otp by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var currentScreen by mutableStateOf(Screen.ForgetPassword)
    var isSubmitted by mutableStateOf(false)
}

enum class Screen {
    ForgetPassword,
    VerifyCode,
    ResetPassword,
    Confirm
}

@Composable
fun PasswordResetApp(viewModel: ResetPasswordViewModel = viewModel()) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (viewModel.currentScreen) {
            Screen.ForgetPassword -> ForgetPasswordScreen(
                viewModel = viewModel,
                onNext = {
                    if (viewModel.email.isBlank()) {
                        viewModel.email = "uth@gmail.com"
                    }
                    viewModel.currentScreen = Screen.VerifyCode
                }
            )
            Screen.VerifyCode -> VerifyCodeScreen(
                viewModel = viewModel,
                onBack = { viewModel.currentScreen = Screen.ForgetPassword },
                onNext = {
                    if (viewModel.otp.isBlank()) {
                        viewModel.otp = "12345"
                    }
                    viewModel.currentScreen = Screen.ResetPassword
                }
            )
            Screen.ResetPassword -> ResetPasswordScreen(
                viewModel = viewModel,
                onBack = { viewModel.currentScreen = Screen.VerifyCode },
                onNext = {
                    if (viewModel.password.isBlank()) {
                        viewModel.password = "12345678"
                    }
                    viewModel.currentScreen = Screen.Confirm
                }
            )
            Screen.Confirm -> ConfirmScreen(
                viewModel = viewModel,
                onBack = { viewModel.currentScreen = Screen.ResetPassword },
                onSubmit = {
                    viewModel.isSubmitted = true
                    viewModel.currentScreen = Screen.ForgetPassword
                }
            )
        }
    }
}

@Composable
fun AppHeader(showBack: Boolean = false, onBack: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showBack) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF0D6EFD)
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "UTH",
                    color = Color(0xFF007A55),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "UNIVERSITY OF TRANSPORTATION HỒ CHÍ MINH CITY",
                    color = Color.Gray,
                    fontSize = 8.sp,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "SmartTasks",
            color = Color(0xFF0D6EFD),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}

@Composable
fun ForgetPasswordScreen(viewModel: ResetPasswordViewModel, onNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        AppHeader(showBack = false)

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Forget Password?",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Enter your Email, we will send you a verification code.",
            fontSize = 13.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = { Text("Your Email") },
            placeholder = { Text("uth@gmail.com") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD))
        ) {
            Text(text = "Next", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.weight(1f))

        if (viewModel.isSubmitted) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F0FE)),
                border = BorderStroke(1.dp, Color(0xFF2196F3).copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Data Flow Result ",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0D6EFD),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "email: ${viewModel.email}", color = Color.DarkGray, fontSize = 13.sp)
                    Text(text = "otp: ${viewModel.otp}", color = Color.DarkGray, fontSize = 13.sp)
                    Text(text = "pass: ${viewModel.password}", color = Color.DarkGray, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun VerifyCodeScreen(viewModel: ResetPasswordViewModel, onBack: () -> Unit, onNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        AppHeader(showBack = true, onBack = onBack)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Verify Code",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Enter the code we just sent you on your registered Email",
            fontSize = 13.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 0 until 5) {
                val char = viewModel.otp.getOrNull(i)?.toString() ?: ""
                OutlinedTextField(
                    value = char,
                    onValueChange = { newVal ->
                        if (newVal.length <= 1) {
                            val chars = viewModel.otp.toMutableList()
                            if (i < chars.size) {
                                if (newVal.isNotEmpty()) chars[i] = newVal[0] else chars.removeAt(i)
                            } else if (newVal.isNotEmpty()) {
                                chars.add(newVal[0])
                            }
                            viewModel.otp = chars.joinToString("").take(5)
                        }
                    },
                    modifier = Modifier
                        .width(50.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD))
        ) {
            Text(text = "Next", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun ResetPasswordScreen(viewModel: ResetPasswordViewModel, onBack: () -> Unit, onNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        AppHeader(showBack = true, onBack = onBack)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Create new password",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Your new password must be different from previously used password",
            fontSize = 13.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.confirmPassword,
            onValueChange = { viewModel.confirmPassword = it },
            label = { Text("Confirm Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD))
        ) {
            Text(text = "Next", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun ConfirmScreen(viewModel: ResetPasswordViewModel, onBack: () -> Unit, onSubmit: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        AppHeader(showBack = true, onBack = onBack)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Confirm",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "We are here to help you!",
            fontSize = 13.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { },
            readOnly = true,
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.otp,
            onValueChange = { },
            readOnly = true,
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { },
            readOnly = true,
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD))
        ) {
            Text(text = "Submit", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PasswordResetAppPreview() {
    Bt3_dataflow_navigationTheme {
        PasswordResetApp()
    }
}
