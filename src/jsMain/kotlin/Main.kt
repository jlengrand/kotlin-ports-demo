import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import kotlin.js.Promise

external interface AppUser{
    val email: String
    val uid: String
    val accessToken: String
}

external interface AppMessage{
    val id: String
    val content: String
}

@JsModule("@jlengrand/firebase-ports")
@JsNonModule
external object FirebasePorts{
    fun logIn() : Promise<AppUser>
    fun logOut()

    fun saveMessage(uid: String, message: String)
    fun getMessages(uid: String) : Promise<Array<AppMessage>>

    fun syncMessages(uid:String, callback: (Array<AppMessage>?) -> Unit)
}

@Composable
fun LineBreak(){
    return P(){
        Text("---------------")
    }
}

fun main() {

    var user : AppUser? by mutableStateOf(null)
    var error : String? by mutableStateOf(null)
    var message : String by mutableStateOf("")
    var messages : Array<AppMessage>? by mutableStateOf(null)

    renderComposable(rootElementId = "root") {

        H1 {
            Text(value = "Jetpack Compose Firebase demo")
        }

        if(user == null) {
            Div({ id("login")}) {
                Button(attrs = {
                    onClick {
                        error = null
                        FirebasePorts.logIn()
                            .then { user = it;

                                // Subscribes to receiving new messages
                                FirebasePorts.syncMessages(it.uid) { newMessages -> messages = newMessages }
                            }
                            .catch { error = it.message }
                    }
                }) {
                    Text("Login!")
                }
            }
        }
        else {
            Div({ id("logout")}) {
                Button(attrs = {
                    onClick {
                        error = null

                        FirebasePorts.logOut()
                        user = null
                    }
                }) {
                    Text("Logout!")
                }
            }

            Div(){
                LineBreak()

                H2 {
                    Text(value = "You are logged in! Here is the info I have about you")
                }

                P(){
                    Text(JSON.stringify(user))
                }
            }

            Div({ id("message")}){
                LineBreak()

                H2 {
                    Text(value = "Write a new message to your message log")
                }

                TextArea(value = message,
                    attrs = {
                    onInput {
                        message = it.value
                    }
                })

                Button(attrs = {
                    onClick {
                        error = null
                        FirebasePorts.saveMessage(user!!.uid, message)
                        message = ""
                    }
                }) {
                    Text("Save!")
                }
            }

            Div({ id("messages")}) {
                LineBreak()

                H2 {
                    Text(value = "Messages are updated automatically, but you still can refetch them manually with that button")
                }

                Button(attrs = {
                    onClick {
                        error = null
                        FirebasePorts.getMessages(user!!.uid)
                            .then { messages = it }
                            .catch { error = it.message }
                    }
                }) {
                    Text("Retrieve messages manually!")
                }

                H2 {
                    Text(value = "Your message log : ")
                }

                if (messages == null){
                    P(){
                        Text("No messages found")
                    }
                }
                else{
                    Ul(){
                        for (m in messages!!){
                            Li(){
                                Text("${m.content} with id : ${m.id}")
                            }
                        }
                    }
                }

                LineBreak()
            }
        }

        if (error != null) {
            Div({ id("error")}){
                LineBreak()

                H2{
                    Text("Error log. If empty, everything went fine :)")
                }

                P() {
                    Text("Error: ")
                    Text(error!!)
                }
            }
        }
    }
}