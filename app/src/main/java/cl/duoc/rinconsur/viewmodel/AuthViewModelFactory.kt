package cl.duoc.rinconsur.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cl.duoc.rinconsur.data.repository.UserRepository

class AuthViewModelFactory(
    private val repository: UserRepository): ViewModelProvider.Factory{
        override fun <T: ViewModel>
                create(modelClass: Class<T>): T {
            if (modelClass == AuthViewModel::class.java){
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(repository) as T
            }
            throw IllegalArgumentException("Clase ViewModel desconocida")
        }
    }
