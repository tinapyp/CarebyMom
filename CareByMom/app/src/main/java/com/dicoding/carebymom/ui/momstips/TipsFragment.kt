package com.dicoding.carebymom.ui.momstips

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.carebymom.R
import com.dicoding.carebymom.adapter.ListTipsAdapter
import com.dicoding.carebymom.data.Tips
import com.dicoding.carebymom.databinding.FragmentTipsBinding
import com.dicoding.carebymom.ui.detail.DetailTipsActivity

class TipsFragment : Fragment() {
    private var _binding: FragmentTipsBinding? = null
    private lateinit var rvTips: RecyclerView
    private var list = ArrayList<Tips>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTipsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvTips = view.findViewById(R.id.rv_list_tips)
        rvTips.setHasFixedSize(true)

        list.addAll(getListTips())
        showRecyclerList()
    }

    private fun showRecyclerList() {
        rvTips.layoutManager = LinearLayoutManager(requireActivity())
        val listTipsAdapter = ListTipsAdapter(list)
        rvTips.adapter = listTipsAdapter
        listTipsAdapter.setOnItemClickCallback(object : ListTipsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Tips) {
                val intentDetail = Intent(requireActivity(), DetailTipsActivity::class.java)
                intentDetail.putExtra("DATA",data)
                startActivity(intentDetail)
            }
        })
    }

    private fun getListTips(): ArrayList<Tips> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val listTips = ArrayList<Tips>()
        for (i in dataName.indices) {
            val food = Tips(dataName[i], dataDescription[i], dataPhoto.getResourceId(i, -1))
            listTips.add(food)
        }
        return listTips
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}