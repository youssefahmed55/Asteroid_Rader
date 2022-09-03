package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidrader.R
import com.udacity.asteroidrader.adapters.AsteroidsRecyclerAdapter
import com.udacity.asteroidrader.databinding.FragmentMainBinding
private const val TAG = "MainFragment"
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private val asteroidsRecyclerAdapter: AsteroidsRecyclerAdapter by lazy { AsteroidsRecyclerAdapter() }
    private lateinit var binding: FragmentMainBinding
    private val weekDates = getNextSevenDaysFormattedDates()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding =  DataBindingUtil.inflate(inflater,R.layout.fragment_main, container, false)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)
        render()                  //Render
        setOnClickOnRecyclerItem()//Set On Click On Recylcer Item
        return binding.root
    }

    private fun setOnClickOnRecyclerItem() {
        asteroidsRecyclerAdapter.setOnItemClickListener(object  : AsteroidsRecyclerAdapter.OnClickOnItem{
            override fun onClick1(asteroid: Asteroid) {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToDetailFragment(asteroid)) //Navigate To Details Fragment With Selected Asteroid Data
            }


        })
    }

    private fun setAdapter(arraylist : MutableList<Asteroid>){
        asteroidsRecyclerAdapter.setArraylist(arraylist)            //Set Array To Adapter
        binding.asteroidRecycler.adapter = asteroidsRecyclerAdapter //Set Adapter To Recycler View

    }

    private fun render() {

        lifecycleScope.launchWhenStarted {
            viewModel.states.collect {
                when (it) {
                    is MainViewStates.Idle ->
                    {
                        viewModel.reduceIntent(weekDates.first(),weekDates.last()) //This Function To Get List Asteroid Of Week From Api Then Get Them From Room DataBase
                    }
                    is MainViewStates.ShowAsteroid -> {
                        if (it.asteroidMutableList != null) {
                            setAdapter(it.asteroidMutableList)                //Set Adapter
                            binding.statusLoadingWheel.visibility = View.GONE //Make visibility Gone To ProgressBar
                        }
                    }
                    is MainViewStates.Error -> {
                        Log.d(TAG, "render: "+it.error)
                        if (it.error == "Unable to resolve host \"api.nasa.gov\": No address associated with hostname"){
                            Toast.makeText(requireActivity(), getString(R.string.Please_Check_Internet_Connection_Problem_Get_Data_From_Host), Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(requireActivity(), it.error, Toast.LENGTH_LONG).show()
                        }
                        viewModel.getPictureOfDay()                                     //Get Picture Of Day From DataBase
                        viewModel.getAllAsteroidWeek(weekDates.first(),weekDates.last())//Get List Asteroid Of Week From DataBase
                        binding.statusLoadingWheel.visibility = View.GONE               //Make visibility Gone To ProgressBar
                    }
                }
            }

        }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val weekDates = getNextSevenDaysFormattedDates()
        lifecycleScope.launchWhenStarted {
            when(item.itemId){
                //Get List Asteroid Of Week From DataBase
                R.id.show_all_menu -> {viewModel.getAllAsteroidWeek(weekDates.first(),weekDates.last()) }
                //Get List Asteroid Of Today From DataBase
                R.id.show_rent_menu-> {viewModel.getAllAsteroidByDay(weekDates.first()) }
                //Get All List Asteroid From DataBase
                R.id.show_buy_menu-> {viewModel.getAllAsteroids(); }
            }

        }

        return true
    }
}
