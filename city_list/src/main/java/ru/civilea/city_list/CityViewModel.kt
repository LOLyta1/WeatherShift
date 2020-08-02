package ru.civilea.city_list

import android.app.Application
import android.arch.lifecycle.SingleLiveEvent
import android.os.Handler
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import ru.civilea.core.model.Navigator
import ru.civilea.core.model.Repository
import ru.civilea.weathershift.model.City

class CityViewModel(
    application: Application,
    private val repository: Repository<City>
) : AndroidViewModel(application) {

    private var data = listOf<City>()
    val loadingDataEvent = SingleLiveEvent<List<City>>()

    fun getData() = data

    fun downloadData() {
        data = repository.getAll()
        loadingDataEvent.postValue(data)
    }

    fun goToCityWeatherDetail(
        navigator: Navigator,
        navController: NavController,
        city: City
    ) {
        navigator.navigateToWeatherInfo(navController,city)
    }

}