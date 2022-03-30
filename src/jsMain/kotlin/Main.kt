import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import kotlin.js.Promise


data class FirebaseUser(
    val email: String,
    val uid: String,
    val accessToken: String,
    val lastLogin: Long,
    val createdAt: Long
)

@JsModule("@jlengrand/firebase-ports")
@JsNonModule
external object FirebasePorts{

    fun signUp(email: String, password: String) : Promise<FirebaseUser>
    fun logIn(email: String, password: String) : Promise<FirebaseUser>
    fun logOut()

}

fun main() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var user : FirebaseUser? by mutableStateOf(null)

    renderComposable(rootElementId = "root") {

        Div(){
            H1 {
                Text(value = "Jetpack Compose Firebase demo")
            }
        }

        if(user == null) {
            Div({ style { padding(25.px) } }) {

                TextArea(value = email,
                    attrs = {
                        onInput { email = it.value }
                    })

                TextArea(value = password,
                    attrs = {
                        onInput { password = it.value }
                    })

                Button(attrs = {
                    onClick {
                        GlobalScope.launch {
                            user = FirebasePorts.signUp(email, password).await()
                        }
                    }
                }) {
                    Text("Sign Up!")
                }

                Button(attrs = {
                    onClick {
                        GlobalScope.launch {
                            user = FirebasePorts.logIn(email, password).await()
                        }
                    }
                }) {
                    Text("LogIn!")
                }
            }
        }
        else {
            Div({ style { padding(25.px) } }) {
                Button(attrs = {
                    onClick {
                        FirebasePorts.logOut()
                        user = null
                    }
                }) {
                    Text("LogOut!")
                }
            }
        }

        Div({ style { padding(25.px) } }){
            P(){
                Text("---------------")
            }

            P(){
                Text(JSON.stringify(user))
            }
        }
    }
}

