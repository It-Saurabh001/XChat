package com.example.xchat

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent :: class)
class HiltModule {
    @Provides
    fun provideAuthentication(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides

    fun provideFirestore() : FirebaseFirestore = Firebase.firestore

    @Provides
    fun provideStorage() : FirebaseStorage = Firebase.storage

    // getting accessible of firestore by using provides annotation and using it in viewModel
}