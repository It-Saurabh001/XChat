package com.example.xchat.data

open class Event<out T> (val content : T) {

    var hasBeenHandled = false
    fun getContentOrNull(): T? {
        return if(hasBeenHandled) null
        else{

            // if execption is not handled it will handle and make hasbeenhandle true  and get content

            hasBeenHandled = true
            content
        }
    }
}