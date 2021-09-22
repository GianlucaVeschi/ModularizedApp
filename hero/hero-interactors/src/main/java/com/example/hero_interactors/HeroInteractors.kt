package com.example.hero_interactors

import com.example.hero_datasource.network.HeroService

data class HeroInteractors(
    val getHerosUseCase: GetHerosUseCase,
    // TODO(Add other hero interactors)
) {
    companion object Factory {
        fun build(): HeroInteractors{
            val service = HeroService.build()
            return HeroInteractors(
                getHerosUseCase = GetHerosUseCase(
                    service = service,
                ),
            )
        }
    }
}