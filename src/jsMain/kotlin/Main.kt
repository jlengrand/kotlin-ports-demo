import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
    var error : String? by mutableStateOf(null)

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
                        error = null
                        FirebasePorts.logIn()
                            .then { user = it }
                            .catch { error = it.message }
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
                        error = null

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

        if (error != null) {
            Div({ style { padding(25.px) } }){
                P() {
                    Text("Error: ")
                    Text(error!!)
                }
            }
        }
    }
}

