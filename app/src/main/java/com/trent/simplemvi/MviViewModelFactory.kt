package com.trent.simplemvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trent.simplemvi.mvi.MviProcessorHolder
import com.trent.simplemvi.mvi.MviReducerHolder
import java.lang.reflect.InvocationTargetException

class MviViewModelFactory(
    private val processorHolder: MviProcessorHolder<*, *>,
    private val reducerHolder: MviReducerHolder<*, *>
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        try {
            return modelClass.getConstructor(
                MviProcessorHolder::class.java,
                MviReducerHolder::class.java
            ).newInstance(processorHolder, reducerHolder)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: InstantiationException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        }
    }
}