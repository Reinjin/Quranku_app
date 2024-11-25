package com.quranku.quranku_app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.quranku.quranku_app.R
import com.quranku.quranku_app.ui.util.Visibility
import com.quranku.quranku_app.ui.util.Visibility_off
import com.quranku.quranku_app.ui.util.validateEmail
import com.quranku.quranku_app.ui.util.validatePassword
import com.quranku.quranku_app.ui.viewmodel.RegisterViewModel
import kotlinx.coroutines.launch
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RegisterScreen(navController: NavController = rememberNavController(), viewModel: RegisterViewModel = hiltViewModel()) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    var fullNameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val registerResponse by viewModel.registerResponse.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
        ) {
            // Layout utama aplikasi
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(80.dp))

                // Title
                Text(
                    text = "Register",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.blue_dark_light)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtitle
                Text(
                    text = "Silahkan buat akunmu terlebih dahulu!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Full Name TextField
                OutlinedTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                        fullNameError = if (fullName.isBlank()) "Full name is required" else null
                    },
                    label = { Text("Full Name") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Password Icon")
                    },
                    isError = fullNameError != null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (fullNameError != null) Color.Red else colorResource(id = R.color.blue_dark_light),
                        focusedLabelColor = if (fullNameError != null) Color.Red else colorResource(id = R.color.blue_dark_light)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                if (fullNameError != null) {
                    Text(
                        text = fullNameError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Email TextField
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = validateEmail(email)
                    },
                    label = { Text("Email") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Email, contentDescription = "Password Icon")
                    },
                    isError = emailError != null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (emailError != null) Color.Red else colorResource(id = R.color.blue_dark_light),
                        focusedLabelColor = if (emailError != null) Color.Red else colorResource(id = R.color.blue_dark_light)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                if (emailError != null) {
                    Text(
                        text = emailError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Password TextField
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = validatePassword(password)
                    },
                    label = { Text("Password") },
                    isError = passwordError != null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (passwordError != null) Color.Red else colorResource(id = R.color.blue_dark_light),
                        focusedLabelColor = if (passwordError != null) Color.Red else colorResource(id = R.color.blue_dark_light)
                    ),
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon")
                    },
                    trailingIcon = {
                        val image = if (showPassword) Visibility else Visibility_off
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(imageVector = image, contentDescription = if (showPassword) "Hide password" else "Show password")
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                if (passwordError != null) {
                    Text(
                        text = passwordError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Confirm Password TextField
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        confirmPasswordError = if (confirmPassword != password) "Passwords do not match" else null
                    },
                    label = { Text("Confirm Password") },
                    isError = confirmPasswordError != null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (confirmPasswordError != null) Color.Red else colorResource(id = R.color.blue_dark_light),
                        focusedLabelColor = if (confirmPasswordError != null) Color.Red else colorResource(id = R.color.blue_dark_light)
                    ),
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon")
                    },
                    trailingIcon = {
                        val image = if (showPassword) Visibility else Visibility_off
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(imageVector = image, contentDescription = if (showPassword) "Hide password" else "Show password")
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                if (confirmPasswordError != null) {
                    Text(
                        text = confirmPasswordError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Register Button
                Button(
                    onClick = {
                        fullNameError = if (fullName.isBlank()) "Full name is required" else null
                        emailError = validateEmail(email)
                        passwordError = validatePassword(password)
                        confirmPasswordError = if (confirmPassword != password) "Passwords do not match" else null

                        if (fullName.isNotBlank() && email.isNotBlank() && password.isNotBlank() && password == confirmPassword) {
                            scope.launch {
                                viewModel.registerUser(fullName, email, password)
                            }
                        } else {
                            Toast.makeText(context, "Please fill out all fields correctly", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(48.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.blue_dark),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Register", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.weight(1f)) // Push the ClickableText to the bottom

                // Already have an account? Clickable Text
                @Suppress("DEPRECATION")
                ClickableText(
                    text = androidx.compose.ui.text.AnnotatedString("Already have an account?"),
                    onClick = { navController.navigate("login"){ popUpTo("register") {inclusive = true} } },
                    style = TextStyle(
                        color = colorResource(id = R.color.blue_dark_light),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // ProgressBar overlay yang berada di tengah layar saat loadingState bernilai true
            if (loadingState) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x80000000)), // Overlay semi-transparent
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = colorResource(id = R.color.blue_dark_light)
                    )
                }
            }

            // Menampilkan Toast dan Navigasi ke Login saat registrasi berhasil
            LaunchedEffect(registerResponse) {
                registerResponse?.let { message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    if (message.contains("successfully", ignoreCase = true)) {
                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                    viewModel.resetRegisterResponse()
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}
