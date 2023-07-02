package com.novsu.frature_auth_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.novsu.core_firebase_auth_api.FirebaseAuthApi
import com.novsu.core_local_storage_api.LocalStorageApi
import com.novsu.core_local_storage_api.LocalUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class AuthScreenViewModel(
    private val firebaseAuth: FirebaseAuthApi,
    private val localData: LocalStorageApi
):ViewModel() {

    val user = firebaseAuth.getUser()

    fun signIn(){
        viewModelScope.launch(Dispatchers.IO) {
            firebaseAuth.signIn()
            user.collect{
                if(it != null){
                    localData.saveUser(LocalUser(it.uid, it.name))
                }
            }
        }
    }


    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val firebaseAuth: Provider<FirebaseAuthApi>,
        private val localData: Provider<LocalStorageApi>
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthScreenViewModel(firebaseAuth.get(),localData.get()) as T
        }
    }
}

