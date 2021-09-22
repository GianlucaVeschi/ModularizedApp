package com.example.modularizedapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.domain.DataState
import com.example.core.domain.ProgressBarState
import com.example.core.domain.UIComponent
import com.example.hero_domain.Hero
import com.example.hero_interactors.HeroInteractors
import com.example.modularizedapp.ui.theme.ModularizedAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : ComponentActivity() {

    private val heros: MutableState<List<Hero>> = mutableStateOf(listOf())
    private val progressBarState: MutableState<ProgressBarState> =
        mutableStateOf(ProgressBarState.Idle)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val getHeros = HeroInteractors.build().getHerosUseCase
        getHeros.run().onEach { dataState ->
            when (dataState) {
                is DataState.Response -> {
                    when (dataState.uiComponent) {
                        is UIComponent.Dialog -> {
                            Log.d(TAG, "onCreate: ${(dataState.uiComponent as UIComponent.Dialog).description}")
                        }
                        is UIComponent.None -> {
                            Log.d(TAG, "onCreate: ${(dataState.uiComponent as UIComponent.None).message}")
                        }
                    }
                }
                is DataState.Data -> {
                    heros.value = dataState.data ?: listOf()
                }
                is DataState.Loading -> {
                    progressBarState.value = dataState.progressBarState
                }
            }
        }.launchIn(CoroutineScope(IO))


        setContent {
            ModularizedAppTheme {
                Greeting("Gianluca")
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn {
                        items(heros.value) { hero -> Text(hero.localizedName) }
                    }
                    if (progressBarState.value is ProgressBarState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
    companion object {
        private const val TAG = "MainActivity"
    }
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ModularizedAppTheme {
        Greeting("Android")
    }
}