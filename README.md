# mojeKarty

Android aplikácia na správu osobných vernostných kariet.  
Umožňuje pridávať, upravovať, odstraňovať a skenovať karty s čiarovým kódom.  
Dáta sa ukladajú offline vo formáte JSON.

---

## Funkcionalita

- Zobrazenie zoznamu kariet (grid 1x / 2x podľa orientácie)
- Pridávanie novej karty:
  - názov firmy
  - číslo vernostnej karty
  - meno držiteľa (voliteľné)
  - výber farby
  - skenovanie čiarového kódu (ZXing)
- Úprava existujúcej karty
- Odstránenie karty
- Náhľad karty na celú obrazovku (s čiarovým kódom)
- ⚙Nastavenia aplikácie:
  - automatické/manuálne ukladanie
  - export/import kariet (JSON súbor)
  - vymazanie všetkých kariet
  - zobrazenie štatistík používania kariet (počítadlo zobrazení)
- Dáta sa ukladajú iba offline do interného úložiska (bez cloudu)

---

## 🛠️ Použité technológie a knižnice

- **[Kotlin](https://kotlinlang.org/)**  
- **[Jetpack Compose](https://developer.android.com/jetpack/compose)** (Material3)
- **[Navigation Compose](https://developer.android.com/jetpack/compose/navigation)** – navigácia medzi obrazovkami
- **[ViewModel + StateFlow](https://developer.android.com/topic/libraries/architecture/viewmodel)** – správa stavu aplikácie
- **[Gson](https://github.com/google/gson)** – serializácia/deserializácia dát (JSON)
- **[ZXing Android Embedded](https://github.com/journeyapps/zxing-android-embedded)** – skenovanie čiarového kódu
- **[Skydoves ColorPicker Compose](https://github.com/skydoves/ColorPickerCompose)** – výber farby karty
- **Android SharedPreferences** + interné úložisko (`cards.json`)

---

## Inštalácia a spustenie

1. Klonujte repozitár:

    ```bash
    git clone https://github.com/tvoj-username/mojeKarty.git
    ```

2. Otvorte projekt v **Android Studio**.

3. Nechajte Gradle stiahnuť závislosti.

4. Spustite aplikáciu na emulátore alebo fyzickom zariadení (Android 7.0 / API 24+).

### Povolenia

- 📷 Fotoaparát (pre skenovanie čiarových kódov)
- 📁 Prístup k súborom (pre export/import kariet)
