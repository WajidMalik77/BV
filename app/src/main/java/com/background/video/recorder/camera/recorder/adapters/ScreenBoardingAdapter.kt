package com.background.video.recorder.camera.recorder.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.background.video.recorder.camera.recorder.model.ViewPagerModel
import com.background.video.recorder.camera.recorder.onboarding.Screen1
import com.background.video.recorder.camera.recorder.onboarding.Screen2
import com.background.video.recorder.camera.recorder.onboarding.Screen3

class ScreenBoardingAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity)  {
    val list = generateViewPagerList()

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position].fragment
    }

    private fun generateViewPagerList(): MutableList<ViewPagerModel> {
        val list = mutableListOf<ViewPagerModel>()
        list.add(ViewPagerModel(Screen1(), "Screen1"))
        list.add(ViewPagerModel(Screen2(), "Screen2"))
        list.add(ViewPagerModel(Screen3(), "Screen3"))
        return list
    }
}