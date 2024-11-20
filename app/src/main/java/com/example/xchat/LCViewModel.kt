package com.example.xchat

import android.icu.util.Calendar
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.xchat.data.CHATS
import com.example.xchat.data.ChatData
import com.example.xchat.data.ChatUser
import com.example.xchat.data.Event
import com.example.xchat.data.MESSAGE
import com.example.xchat.data.Message
import com.example.xchat.data.STATUS
import com.example.xchat.data.Status
import com.example.xchat.data.USER_NODE
import com.example.xchat.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LCViewModel @Inject constructor(
    val auth: FirebaseAuth,
    var db : FirebaseFirestore,
    var storage : FirebaseStorage
): ViewModel()
{
    // ui state
    var inprogess = mutableStateOf(false)
    var signIn = mutableStateOf(false)

    //chats and messages
    var inProcessChats = mutableStateOf(false)
    val chats = mutableStateOf<List<ChatData>>(listOf())
    val chatMessage = mutableStateOf<List<Message>>(listOf())
    var inProgressChatMessage = mutableStateOf(false)
    var currentChatMessageListener : ListenerRegistration?=null

    // event handling
    var eventMutableState = mutableStateOf<Event<String>?>(null)

    //user data
    private val _userData = MutableLiveData<UserData?>(null)
    var userData: LiveData<UserData?> = _userData



    //status
    private val _status = MutableStateFlow<List<Status>>(emptyList())
    val status: StateFlow<List<Status>> = _status
    var inProgressStatus = mutableStateOf(false)


    init {

        // it check it current user available or not
        // if not it will get from auth.currenuser
        // passed it into signIn value
        // if not current user will get it form getuserdata
        val currentUser = auth.currentUser
        signIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
        }
    }

    fun onSendReply(chatID:String, message : String){
        val time = Calendar.getInstance().time.toString()
        val msg = Message(userData.value?.userId, message, time)
        db.collection(CHATS).document(chatID).collection(MESSAGE).document().set(msg)
    }
    fun populateMessages(chatID: String){
        inProgressChatMessage.value = true
        currentChatMessageListener = db.collection(CHATS).document(chatID).collection(MESSAGE).addSnapshotListener{
            value, error->
            if(error != null) {
                handleExecption(error)
            }
            if (value != null){
                chatMessage.value = value.documents.mapNotNull {
                    it.toObject<Message>()
                }.sortedBy {
                    it.timestamp
                }
                inProgressChatMessage.value = false
            }

        }
    }

    fun depopulateMessage(){
        chatMessage.value = listOf()
        currentChatMessageListener = null
    }
    fun populateChats(){
        inProcessChats.value = true
        db.collection(CHATS).where(Filter.or(
            Filter.equalTo("user1.userId",userData.value?.userId),
            Filter.equalTo("user2.userId",userData.value?.userId),
        )).addSnapshotListener{
            value,error->
                if(error != null){
                    handleExecption(error)
                }
            if(value!=null){
                chats.value = value.documents.mapNotNull{
                    it.toObject<ChatData>()
                }
                inProcessChats.value = false
            }
        }
    }

    fun signUp(name: String, number: String, email: String, password: String) {

        if (name.isEmpty() or number.isEmpty() or email.isEmpty() or password.isEmpty()) {
            Log.d("TAG", "signUp: user not logged in ")
            handleExecption(customMessage = "Please fill all fields")
            return
        }
        inprogess.value = true
        db.collection(USER_NODE).whereEqualTo("number", number)
            .get()
            .addOnSuccessListener { documents ->
                Log.d("TAG", "Documents retrieved: ${documents.size()}")

                if (documents.isEmpty) {
                    Log.d("TAG","if condition running")
                    Log.d("TAG", "register user email: $email")
                    Log.d("TAG", "register user email: $password")
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.d("TAG","succesfull")
                            signIn.value = true
                            createOrUpdateProfle(name, number)
                            Log.d("TAG", "signUp: user logged in ")
                        } else {
                            Log.d("TAG", "not succesfull")
                            handleExecption(it.exception, customMessage = "Sign Up failed")
                            }
                        inprogess.value = false
                    }

                }else{
                    Log.d("TAG", "else condtion running")
//                Toast.makeText(, "", Toast.LENGTH_SHORT).show()
                handleExecption(customMessage = "Number Already Exist")
                inprogess.value = false
            }
        }.addOnFailureListener { exception ->
            Log.w("TAG", "Error checking existing number: ", exception)
            handleExecption(customMessage = "Error occurred while checking the number")
            inprogess.value = false
        }
    }

    fun loginIn(email: String, password: String){
        if (email.isEmpty() or password.isEmpty()){
            Log.d("TAG", "loginIn: please fill all fields")
            handleExecption(customMessage = "Please fill all the fields")
            return
        }
        else{
            inprogess.value  = true
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {task->
                    if(task.isSuccessful){
                        signIn.value = true
                        inprogess.value = false

                        // getcurrentuser function is called here to get data
                        auth.currentUser?.uid?.let {
                            getUserData(it)
                        }
                    }
                    else{
                        Log.d("TAG", "loginIn: failed")
                        handleExecption(task.exception, "Logined Failed")
                    }
                }

        }
    }

    fun createOrUpdateProfle(name: String?= null, number: String?= null, imageUrl : String?= null) {

        var uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            name = name?: userData.value?.name,
            number = number?: userData.value?.number,
            imageUrl = imageUrl?: userData.value?.imageUrl

        )


        // when uid is null or simple we can say when user first time create id
        // we will use  uid?.let{}
        // in this block of code we will get user data and stored it in firestore
        uid?.let {
            inprogess.value = true
            // for getting user data use .addOnSuccessListener{}
            db.collection(USER_NODE).document(uid).get().addOnSuccessListener {
                if(it.exists()){
                    db.collection(USER_NODE).document(uid).update(
                        mapOf(
                            "name" to userData.name,
                            "number" to userData.number,
                            "imageUrl" to userData.imageUrl
                        )
                    ).addOnSuccessListener {

                        Log.d("TAG", "createOrUpdateProfle: updated ")
                        getUserData(uid)
                        inprogess.value = false
                    }.addOnFailureListener {exception->
                        inprogess.value = false
                        Log.d("TAG", "createOrUpdateProfle: not updated")
                        handleExecption(exception, "failed user update data")
                    }

                    // update user data
                }else{
                    // getting userdata form document of having uid form collection usernode
                    db.collection(USER_NODE).document(uid).set(userData).addOnSuccessListener {
                        getUserData(uid)
                        inprogess.value = false
                    }.addOnFailureListener {exception->
                        inprogess.value = false
                        handleExecption(exception, "Failed to create profile")
                    }
//                    inprogess.value = false
//                    // after getting userdata
//                    // update userdata
//                    getUserData(uid)
                }

            }
            // for any failure using handle exceptional
                .addOnFailureListener {
                    inprogess.value = false

                    handleExecption(it,"Cannot Retrive User")
                }
        }

    }

    fun getUserData(uid : String) {
        inProgressStatus.value = true
        inprogess.value = true
        db.collection(USER_NODE).document(uid).addSnapshotListener{
            value, error->
            if (error != null){
                inProgressStatus.value = false
                inprogess.value = false
                handleExecption(error, "Cannot Retrive User")
            }
            if (value!= null){
                // value not null it will inserted in usedata.value and turning inprogress again false
                // using on inprogress to stop loading baar after insertion of data in userdata
                // till the time when when data loaded in userdata progress baar showing

                var user = value.toObject<UserData>()
                _userData.value = user
                inProgressStatus.value = false
                inprogess.value = false
                populateChats()
                populateStatuses()
            }
        }
    }

    fun handleExecption(execption : Exception?= null, customMessage : String= ""){
        // function handles the execption
        Log.e("LiveChatApp", "live chat execption: ", execption)
        // utility of execption
        execption?.printStackTrace()
        val errorMsg = execption?.localizedMessage?:""
        // if custommsg is true
        val message = if(customMessage.isNullOrEmpty()) errorMsg else customMessage
        eventMutableState.value = Event(message)
        inprogess.value = false
        inProgressStatus.value = false

    }

    fun uploadProfileImage(uri: Uri){
        uploadImage(uri){
            createOrUpdateProfle(imageUrl = it.toString())
        }
    }
    fun logout(){
        auth.signOut()
        signIn.value = false
        _userData.value = null
        depopulateMessage()
        currentChatMessageListener = null
        eventMutableState.value = Event("Logged Out")
    }

    fun onAddChat(number : String) {
        if(number.isEmpty() or !number.isDigitsOnly()){
            handleExecption(customMessage = "Number must be contain digits only")
        }else{
            db.collection(CHATS).where(Filter.or(
                Filter.and(
                    Filter.equalTo("user1.number", number),
                    Filter.equalTo("user2.number",userData.value?.number)
                ),

                Filter.and(
                    Filter.equalTo("user1.number", userData.value?.number),
                    Filter.equalTo("user2.number",number)
                )
            )).get().addOnSuccessListener {task->
                if (task.isEmpty){
                    db.collection(USER_NODE).whereEqualTo("number", number).get().addOnSuccessListener {
                        if (it.isEmpty){
                            handleExecption(customMessage = "number not found")
                        }else{
                            val chatPartner = it.toObjects<UserData>()[0]
                            val id = db.collection(CHATS).document().id
                            val chat = ChatData(
                                chatId = id,
                                ChatUser(userData.value?.userId,
                                    userData.value?.name,
                                    userData.value?.imageUrl,
                                    userData.value?.number),
                                ChatUser(chatPartner.userId,
                                    chatPartner.name,
                                    chatPartner.imageUrl,
                                    chatPartner.number)
                            )
                            db.collection(CHATS).document(id).set(chat)
                        }
                    }
                        .addOnFailureListener {
                            Log.d("TAG", "onAddChat:issues with number ")
                            handleExecption(it)
                        }

                }
                else{
                    Log.d("TAG", "onAddChat: Chat already exists ")
                    handleExecption(customMessage = "Chat already exists")
                }
            }.addOnFailureListener{
                Log.d("TAG","onAddChat: number is not added to firebase")
            }
        }
    }

    fun uploadImage(uri: Uri,onSuccess:(Uri) ->Unit) {
        inprogess.value = true
        val storageref=storage.reference
        val uuid = UUID.randomUUID()
        val imageref = storageref.child("images/$uuid")
        val uploadTask = imageref.putFile(uri)
        uploadTask.addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl?.addOnSuccessListener {downloaduri->
                onSuccess(downloaduri)
                inprogess.value = false
            }

        }
            .addOnFailureListener {
                inprogess.value = false
                Log.d("TAG", "uploadImage: image not uploaded")
                handleExecption(it)
            }


    }

    fun uploadStatus(uri: Uri) {
        uploadImage(uri){
            createStatus(it.toString())

        }
    }
    fun createStatus(imageurl :String){
        val newStatus = Status(
            ChatUser(
                userData.value?.userId,
                userData.value?.name,
                userData.value?.imageUrl,
                userData.value?.number
            )
            ,imageurl,
            System.currentTimeMillis()
        )
        db.collection(STATUS).document().set(newStatus).addOnSuccessListener {
            populateStatuses()
        }
    }
    fun populateStatuses(){
        val timeDelta = 24L * 60 * 60 * 1000
        val cutOff = System.currentTimeMillis()- timeDelta
        inProgressStatus.value = true

        val userId = userData.value?.userId
        if (userId == null) {
            Log.e("LCViewModel", "User ID is null, cannot fetch statuses.")
            inProgressStatus.value = false
            return
        }


        db.collection(CHATS).where(
            Filter.or(
                Filter.equalTo("user1.userId",userId),
                Filter.equalTo("user2.userId",userId)
            )
        ).addSnapshotListener {value, error ->
            if (error !=null){
                handleExecption(error)
                inProgressStatus.value = false
                return@addSnapshotListener
            }
            value?.let {
                val currentConnectons = arrayListOf(userId)

                val chats = it.toObjects<ChatData>()
                chats.forEach{chat->
                    val user1Id = chat.user1.userId
                    val user2Id = chat.user2.userId

                    //Add both user1 and user2 connections to currentConnections
                    if (user1Id == userId) {
                        user2Id?.let {currentConnectons.add(it)}
                    } else if (user2Id == userId) {
                        user1Id?.let {currentConnectons.add(it) }
                    }
                }

                Log.d("LCViewModel", "Current Connections: $currentConnectons")

                // Query the statues based on the connections

                db.collection(STATUS)
                    .whereGreaterThan("timestamp",cutOff)
                    .whereIn("user.userId",currentConnectons)
                    .addSnapshotListener{
                        value, error->
                        inProgressStatus.value = false

                        if (error!=null){
                            handleExecption(error)
                            return@addSnapshotListener
                        }
                        value?.let {
//                            _status.value = it.toObjects()
                            val statuses = it.toObjects<Status>()
                            _status.value = statuses
                            Log.d("LCViewModel", "Fetched ${statuses.size} statuses")
                        }
                    }
            }
        }
    }
}