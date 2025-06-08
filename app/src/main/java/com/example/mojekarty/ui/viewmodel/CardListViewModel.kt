package com.example.mojekarty.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mojekarty.data.StorageManager
import com.example.mojekarty.model.Card
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * ViewModel pre správu zoznamu kariet v aplikácii mojeKarty.
 *
 * Tento ViewModel implementuje architektúru s unidirectional data flow:
 * - Zoznam kariet sa drží cards (MutableStateFlow).
 * - UI komponenty (screeny) pozorujú stav kariet cez cards a automaticky sa aktualizujú.
 * - Pri vykonaní akcie (pridanie, zmena, vymazanie) sa aktualizuje zoznam kariet
 *
 * Dôvod: stav kariet prežije otočenie obrazovky.
 * Zdroj: https://developer.android.com/kotlin/flow/stateflow-and-sharedflow
 *
 * @param application Kontext aplikácie (potrebný na prístup k StorageManager)
 */
class CardListViewModel(application: Application) : AndroidViewModel(application) {

    var autoSaveEnabled: Boolean = true
    val cards = MutableStateFlow<List<Card>>(emptyList())

    init {
        loadCards()
    }

    /**
     * Načíta uložené karty zo StorageManager a nastaví ich do cards.
     */
    private fun loadCards() {
        cards.value = StorageManager.loadCardsFromFile(getApplication())
    }

    /**
     * Pridá novú kartu do zoznamu a uloží zmeny.
     *
     * @param card Nová karta, ktorá sa pridá do zoznamu
     */
    fun addCard(card: Card) {
        val updated = cards.value.toMutableList().apply { add(card) }
        cards.value = updated
        if (autoSaveEnabled) {
            StorageManager.saveCardsToFile(getApplication(), updated)
        }
    }

    /**
     * Vymaže kartu zo zoznamu a uloží zmeny.
     *
     * @param card Karta, ktorá sa má vymazať
     */
    fun removeCard(card: Card) {
        val updated = cards.value.toMutableList().apply { remove(card) }
        cards.value = updated
        if (autoSaveEnabled) {
            StorageManager.saveCardsToFile(getApplication(), updated)
        }
    }

    /**
     * Aktualizuje existujúcu kartu v zozname a uloží zmeny.
     *
     * @param updatedCard Aktualizovaná karta
     */
    fun updateCard(updatedCard: Card) {
        val updated = cards.value.map { if (it.id == updatedCard.id) updatedCard else it }
        cards.value = updated
        if (autoSaveEnabled) {
            StorageManager.saveCardsToFile(getApplication(), updated)
        }
    }
}
