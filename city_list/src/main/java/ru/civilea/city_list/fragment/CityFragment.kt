package ru.civilea.city_list.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_city_list.view.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.civilea.city_list.CityViewModel
import ru.civilea.city_list.R
import ru.civilea.city_list.adapter.CityRecyclerAdapter
import ru.civilea.common.models.City
import ru.civilea.common.models.CreateCityDto
import ru.civilea.core.model.getMainNavController

class CityFragment : Fragment(R.layout.fragment_city_list) {

    val viewModel by sharedViewModel<CityViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        viewModel.loadingDataEvent.observe(viewLifecycleOwner, Observer {
            getAdapter()?.setList(it)
        })
        view.addCityBTN.setOnClickListener {
            getCityFromView()?.let {
                viewModel.addCity(it)
            }
        }
        viewModel.downloadData()
    }


    private fun initRecycler() {
        view?.cityRecycler?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = CityRecyclerAdapter(::onCityItemClick)
        }
    }

    private fun getAdapter() = view?.cityRecycler?.adapter as? CityRecyclerAdapter


    private fun onCityItemClick(city: City, view: View) {
        when (view.id) {
            R.id.editBTN -> {
                viewModel.showEditDialog(findNavController(),city)
            }
            R.id.deleteBTN -> {
                viewModel.deleteCity(city.id)
            }

            R.id.cityLinearContainer -> {
                getMainNavController()?.let {
                    viewModel.goToCityWeatherDetail(
                        navigator = get(),
                        city = city,
                        navController = it
                    )
                }
            }

        }
    }

    private fun getCityFromView(): CreateCityDto? {
        val city = CreateCityDto()
        val name = view?.cityNameET?.text.toString()
        val degree = view?.weatherET?.text.toString()
        return try {
            city.copy(name = name, weatherDegree = degree.toInt())
        } catch (e: Exception) {
            Toast.makeText(context, resources.getString(R.string.emptyField), Toast.LENGTH_LONG)
                .show()
            null
        }

    }
}