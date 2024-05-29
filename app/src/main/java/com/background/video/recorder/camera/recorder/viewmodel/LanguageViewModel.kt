package com.background.video.recorder.camera.recorder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.background.video.recorder.camera.recorder.model.LanguageModel
import java.util.ArrayList

class LanguageViewModel : ViewModel() {
    @JvmField
    var  langList: MutableLiveData<ArrayList<LanguageModel>>? = null
    private val languageList: Unit
        get() {
            if (langList == null) {
                langList = MutableLiveData()
                loadLanguages()
            }
        }

    private fun loadLanguages() {
        val languageModels = ArrayList<LanguageModel>()
        languageModels.add(LanguageModel("Arabic", "ar", "SA",false))
        languageModels.add(LanguageModel("Chinese", "zh", "ZH",false))
        languageModels.add(LanguageModel("English", "en", "US",false))
        languageModels.add(LanguageModel("French", "fr", "FR",false))
        languageModels.add(LanguageModel("German", "de", "DE",false))
        languageModels.add(LanguageModel("Hindi", "hi", "IN",false))
        languageModels.add(LanguageModel("Indonesian", "in", "ID",false))
        languageModels.add(LanguageModel("Italian", "it", "IT",false))
        languageModels.add(LanguageModel("Japanese", "ja", "JP",false))
        languageModels.add(LanguageModel("Korean", "ko", "KO",false))
        languageModels.add(LanguageModel("Portuguese", "pt", "PT",false))
        languageModels.add(LanguageModel("Russian", "ru", "RU",false))
        languageModels.add(LanguageModel("Spanish", "es", "ES",false))
        languageModels.add(LanguageModel("Turkish", "tr","TR", false))
        languageModels.add(LanguageModel("Vietnamese", "vi","VN", false))
        langList!!.postValue(languageModels)
    }

    init {
        languageList
    }
}