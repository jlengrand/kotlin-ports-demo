import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import kotlin.js.Promise

external interface FirebaseUser{
    val email: String
    val uid: String
    val accessToken: String
}

external interface FirestoreMessage{
    val id: String
    val content: String
}

@JsModule("@jlengrand/firebase-ports")
@JsNonModule
external object FirebasePorts{
    fun logIn() : Promise<FirebaseUser>
    fun logOut()

    fun saveMessage(uid: String, message: String)
    fun getMessages(uid: String) : Promise<Array<FirestoreMessage>>
}

fun main() {
    var user : FirebaseUser? by mutableStateOf(null)
    var error : String? by mutableStateOf(null)
    var message : String by mutableStateOf("")
    var messages : Array<FirestoreMessage>? by mutableStateOf(null)

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

            Div({ style { padding(25.px) } }){
                P(){
                    Text("---------------")
                }

                TextArea(value = message,
                    attrs = {
                    onInput {
                        console.log(it.value)
                        message = it.value
                    }
                })

                Button(attrs = {
                    onClick {
                        error = null
                        console.log(JSON.stringify(user))
                        console.log(user)
                        console.log(user!!.uid)
                        FirebasePorts.saveMessage(user!!.uid, message)
                        message = ""
                    }
                }) {
                    Text("Save!")
                }
            }

            Div({ style { padding(25.px) } }) {
                P(){
                    Text("---------------")
                }

                Button(attrs = {
                    onClick {
                        error = null
                        FirebasePorts.getMessages(user!!.uid)
                            .then { messages = it }
                            .catch { error = it.message }
                    }
                }) {
                    Text("Retrieve messages!")
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

                P(){
                    Text("---------------")
                }
            }
        }

        if (error != null) {
            Div({ style { padding(25.px) } }){
                P(){
                    Text("---------------")
                }

                P() {
                    Text("Error: ")
                    Text(error!!)
                }
            }
        }
    }
}

