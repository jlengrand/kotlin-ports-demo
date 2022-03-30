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
)

@JsModule("@jlengrand/firebase-ports")
@JsNonModule
external object FirebasePorts{
    fun logIn() : Promise<FirebaseUser>
    fun logOut()
}

fun main() {
    var user : FirebaseUser? by mutableStateOf(null)

    renderComposable(rootElementId = "root") {

        Div(){
            H1 {
                Text(value = "Jetpack Compose Firebase demo")
            }
        }

        if(user == null) {
            Div({ style { padding(25.px) } }) {

                Button(attrs = {
                    onClick {
                        GlobalScope.launch {
                            user = FirebasePorts.logIn().await()
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
}

