package com.tridev.observablepatterns

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(repository: Repository) : ViewModel() {

    /*
    * The ViewModel class is a business logic or screen level state holder.
    *  It exposes state to the UI and encapsulates related business logic.
    *  Its principal advantage is that it caches state and persists it through configuration changes.
    * */

    /*Livedata is a lifecycle aware observable data holder,it holds ui state in the end.
    *If any value in ui changes over time then that can be stored in liveData,
    otherwise changed value will be lost when configuration change happens.
     */

    private val _liveData = MutableLiveData("Hello World")
    val liveData: LiveData<String> = _liveData

    /*Stateflow is a state-holder observable flow that emits the current and new state updates to its collectors.
   *The current state value can also be read through its value property.
   * To update state and send it to the flow, assign a new value to the value property of the MutableStateFlow class.
   *It can also perform mapping with the emitted states.
   ***It triggers EVENT for the updated state every time activity is created.
   * */
    private val _stateFlow = MutableStateFlow("Hello World State flow")
    val stateFlow = _stateFlow.asStateFlow()


    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFLow = _sharedFlow.asSharedFlow()

    /*SharedFlow is a state-holder observable flow that emits the current and new state updates to its collectors.
       *The current state value can also be read through its value property.
       * To update state and send it to the flow, assign a new value to the value property of the MutableStateFlow class.
       *It can also perform mapping with the emitted states.
       ***It triggers only ONE TIME EVENT for the updated value unlike Stateflow
       */

    fun triggerLiveData() {
        /*
        *main thread always overrides the setValue()'s value with postValue().
        *_liveData.postValue("a")
        *_liveData.setValue("b")
        * a value overrides b value by the main thread.
        */
        _liveData.postValue("Live Data")
    }


    fun triggerStateFlow() {
        _stateFlow.value = "State Flow"
    }


    /*
    * Flow is not state holder,does not hold ui state or store values.
    * On configuration change value is lost.
    * FLow does not hold any value it just performs to emit values and returns Flow
    * */

    fun triggerFLow(): Flow<String> {
        return flow {
            repeat(5) {
                emit("Item $it")
                delay(1000L)
            }
        }
    }

    fun triggerSharedFlow() {
        viewModelScope.launch {
            _sharedFlow.emit("Shared flow")
        }
    }


}