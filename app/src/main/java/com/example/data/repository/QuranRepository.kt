package com.example.data.repository

import com.example.data.model.QuranSurah
import com.example.data.model.QuranVerse

/**
 * Repository providing the Holy Quran Surahs index and notable verified verses with Amharic translations.
 */
object QuranRepository {

    val PROMINENT_SURAHS: List<QuranSurah> = listOf(
        QuranSurah(
            number = 1,
            nameArabic = "الفَاتِحَة",
            nameTranslit = "Al-Fatihah",
            nameAmharic = "አል-ፋቲሓ (መክፈቻዋ)",
            englishMeaning = "The Opening",
            versesCount = 7,
            revelationType = "መካ (Mecca)",
            prominentVerses = listOf(
                QuranVerse(1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "ቢስሚላሂር-ረሕማኒር-ረሒም", "በአላህ ስም እጅግ በጣም ሩኅሩህ በጣም አዛኝ በሆነው።"),
                QuranVerse(2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "አልሐምዱ ሊላሂ ረብቢል ዓለሚን", "ምስጋና ለአላህ ይገባው የዓለማት ጌታ ለሆነው።"),
                QuranVerse(3, "الرَّحْمَٰنِ الرَّحِيمِ", "አር-ረሕማኒር-ረሒም", "እጅግ በጣም ሩኅሩህ በጣም አዛኝ፤"),
                QuranVerse(4, "مَالِكِ يَوْمِ الدِّينِ", "ማሊኪ የውሚድ-ዲን", "የፍርዱ ቀን ባለቤት (ንጉሥ) ለሆነው።"),
                QuranVerse(5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "ኢያከ ነዕቡዱ ወኢያከ ነስተዒን", "አንተን ብቻ እናመልካለን፤ አንተንም ብቻ እርዳታን እንለምናለን።"),
                QuranVerse(6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "ኢህዲነስ-ሲራጦል ሙስተቂም", "ቀጥተኛውን መንገድ ምራን።"),
                QuranVerse(7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "ሲራጦለዚነ አንዐምተ ዐለይሂም ገይሪል መግዱቢ ዐለይሂም ወለድ-ዷልሊን", "የእነዚያን በነርሱ ላይ በጎ የዋልክላቸውን መንገድ፤ በእነርሱ ላይ ያልተቆጣህባቸውንና ያልተሳሳቱትንም መንገድ (ምራን)።")
            )
        ),
        QuranSurah(
            number = 2,
            nameArabic = "البَقَرَة (آيَةُ الكُرْسِي)",
            nameTranslit = "Ayat al-Kursi (Al-Baqarah 255)",
            nameAmharic = "አያተል ኩርሲ (ታላቋ አንቀጽ)",
            englishMeaning = "The Throne Verse",
            versesCount = 286,
            revelationType = "መዲና (Medina)",
            prominentVerses = listOf(
                QuranVerse(
                    numberInSurah = 255,
                    arabic = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ",
                    transliteration = "አላሁ ላ ኢላሀ ኢልላ ሁወል ሐይዩል ቀይዩም፣ ላ ተእኹዙሁ ሲነቱን ወላ ነውም፣ ለሁ ማ ፊስ-ሰማዋቲ ወማ ፊል-አርድ...",
                    amharicTranslation = "አላህ፤ ከእርሱ በስተቀር ሌላ አምላክ የለም፤ ሕያው ራሱን ቻይ ነው፡፡ እንቅልፍም ሆነ ማሸለብ አትይዘውም፤ በሰማያትና በምድር ያለው ሁሉ የእርሱ ብቻ ነው፡፡ ያ እርሱ ዘንድ በፈቃዱ ቢሆን እንጅ የሚያማልድ ማነው? (ከፍጡሮች) በፊታቸው ያለውንና ከኋላቸው ያለውን ሁሉ ያውቃል፡፡ እርሱ ከሻው በስተቀር ከዕውቀቱ በምንም ነገር ላይ አያካብቡም፡፡ ዙፋኑ ሰማያትንና ምድርን ሰፋ፤ እነርሱንም መጠበቅ አያደክመውም፤ እርሱም የሁሉ በላይ ታላቅ ነው፡፡"
                ),
                QuranVerse(
                    numberInSurah = 285,
                    arabic = "آمَنَ الرَّسُولُ بِمَا أُنزِلَ إِلَيْهِ مِن رَّبِّهِ وَالْمُؤْمِنُونَ ۚ كُلٌّ آمَنَ بِاللَّهِ وَمَلَائِكَتِهِ وَكُتُبِهِ وَرُسُلِهِ لَا نُفَرِّقُ بَيْنَ أَحَدٍ مِّن رُّسُلِهِ ۚ وَقَالُوا سَمِعْنَا وَأَطَعْنَا ۖ غُفْرَانَكَ رَبَّنَا وَإِلَيْكَ الْمَصِيرُ",
                    transliteration = "አመነር-ረሱሉ ቢማ ኡንዚለ ኢለይሂ ሚን ረብቢሂ ወል-ሙእሚኑን...",
                    amharicTranslation = "መልእክተኛው ከጌታው ወደ እርሱ በተወረደው አመነ፤ ምእምናኖቹም (እንደዚሁ)፡፡ ሁሉም በአላህ፣ በመላእክቱም፣ በመጻሕፍቱም፣ በመልእክተኞቹም አመኑ፤ «ከመልእክተኞቹ በአንድም መካከል አንለይም» (አሉ)፡፡ «ሰማን ታዘዝንም፤ ጌታችን ሆይ! ምሕረትህን (እንሻለን)፤ መመለሻም ወዳንተ ብቻ ነው» አሉም፡፡"
                )
            )
        ),
        QuranSurah(
            number = 18,
            nameArabic = "الكَهْف",
            nameTranslit = "Al-Kahf",
            nameAmharic = "አል-ካህፍ (ዋሻው)",
            englishMeaning = "The Cave",
            versesCount = 110,
            revelationType = "መካ (Mecca)",
            prominentVerses = listOf(
                QuranVerse(1, "الْحَمْدُ لِلَّهِ الَّذِي أَنزَلَ عَلَىٰ عَبْدِهِ الْكِتَابَ وَلَمْ يَجْعَل لَّهُ عِوَجًا", "አልሐምዱ ሊላሂል-ለዚ አንዘለ ዐላ ዐብዲሂል ኪታበ ወለም የጅዐል ለሁ ዒወጃ", "ምስጋና በባሪያው ላይ መጽሐፉን ላወረደውና ለእርሱም ምንም መጣመም ላላደረገበት አላህ ይገባው፡፡"),
                QuranVerse(2, "قَيِّمًا لِّيُنذِرَ بَأْسًا شَدِيدًا مِّن لَّدُنْهُ وَيُبَشِّرَ الْمُؤْمِنِينَ الَّذِينَ يَعْمَلُونَ الصَّالِحَاتِ أَنَّ لَهُمْ أَجْرًا حَسَنًا", "ቀይዪመን ሊዩንዚረ በእሰን ሸዲደን ሚን ለዱንሁ...", "ቀጥተኛ ሲሆን ከእርሱ ዘንድ የኾነን ብርቱ ቅጣት ሊያስጠነቅቅ፤ እነዚያንም በጎ ሥራዎችን የሚሠሩትን ምእምናን ለእነርሱ መልካም ምንዳ ያላቸው መኾኑን ሊያበስር (አወረደው)፡፡")
            )
        ),
        QuranSurah(
            number = 36,
            nameArabic = "يس",
            nameTranslit = "Ya-Sin",
            nameAmharic = "ያሲን",
            englishMeaning = "Ya-Sin",
            versesCount = 83,
            revelationType = "መካ (Mecca)",
            prominentVerses = listOf(
                QuranVerse(1, "يس", "ያሲን", "ያ ሲን።"),
                QuranVerse(2, "وَالْقُرْآنِ الْحَكِيمِ", "ወል-ቁርኣኒል ሐኪም", "ጥበብ በሞላበት ቁርኣን እምላለሁ፡፡"),
                QuranVerse(3, "إِنَّكَ لَمِنَ الْمُرْسَلِينَ", "ኢንነከ ለሚነል ሙርሰሊን", "አንተ በእርግጥ ከመልእክተኞቹ ነህ፡፡"),
                QuranVerse(4, "عَلَىٰ صِرَاطٍ مُّسْتَقِيمٍ", "ዐላ ሲራጢን ሙስተቂም", "በቀጥተኛው መንገድ ላይ (የኾንክ)፡፡")
            )
        ),
        QuranSurah(
            number = 67,
            nameArabic = "المُلْك",
            nameTranslit = "Al-Mulk",
            nameAmharic = "አል-ሙልክ (ሉዓላዊነት)",
            englishMeaning = "Sovereignty",
            versesCount = 30,
            revelationType = "መካ (Mecca)",
            prominentVerses = listOf(
                QuranVerse(1, "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ", "ተባረከል-ለዚ ቢየዲሂል ሙልኩ ወሁወ ዐላ ኩልሊ ሸይኢን ቀዲር", "ያ ንግሥናው በእጁ የሆነው አላህ ክብርና ላቀነቱ በዛ፤ እርሱም በነገሩ ሁሉ ላይ ቻይ ነው፡፡"),
                QuranVerse(2, "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا ۚ وَهُوَ الْعَزِيزُ الْغَفُورُ", "አልለዚ ኸለቀል መውተ ወል-ሐያተ ሊየብሉወኩም አይዩኩም አሕሰኑ ዐመላ...", "ያ የትኛችሁ ሥራው ያማረ መሆኑን ሊፈትናችሁ ሞትንና ሕይወትን የፈጠረ ነው፤ እርሱም አሸናፊው መሓሪው ነው፡፡")
            )
        ),
        QuranSurah(
            number = 112,
            nameArabic = "الإِخْلَاص",
            nameTranslit = "Al-Ikhlas",
            nameAmharic = "አል-ኢኽላስ (ጥራት)",
            englishMeaning = "Sincerity / Monotheism",
            versesCount = 4,
            revelationType = "መካ (Mecca)",
            prominentVerses = listOf(
                QuranVerse(1, "قُلْ هُوَ اللَّهُ أَحَدٌ", "ቁል ሁወላሁ አሐድ", "በል፡- «እርሱ አላህ አንድ ነው፡፡"),
                QuranVerse(2, "اللَّهُ الصَّمَدُ", "አላሁስ-ሶመድ", "አላህ (የሁሉ) መጠጊያ ነው፡፡"),
                QuranVerse(3, "لَمْ يَلِدْ وَلَمْ يُولَدْ", "ለም የሊድ ወለም ዩለድ", "አልወለደም፤ አልተወለደምም፡፡"),
                QuranVerse(4, "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ", "ወለም የኩን ለሁ ኩፉወን አሐድ", "ለእርሱም አንድም አምሳያ የለውም፡፡»")
            )
        ),
        QuranSurah(
            number = 113,
            nameArabic = "الفَلَق",
            nameTranslit = "Al-Falaq",
            nameAmharic = "አል-ፈለቅ (ጎህ)",
            englishMeaning = "The Daybreak",
            versesCount = 5,
            revelationType = "መካ (Mecca)",
            prominentVerses = listOf(
                QuranVerse(1, "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", "ቁል አዑዙ ቢረብቢል ፈለቅ", "በል፡- «በተፈልቃቂው (ንጋት) ጌታ እጠበቃለሁ፡፡"),
                QuranVerse(2, "مِن شَرِّ مَا خَلَقَ", "ሚን ሸርሪ ማ ኸለቅ", "ከፈጠረው ፍጡር ሁሉ ክፋት፤"),
                QuranVerse(3, "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ", "ወሚን ሸርሪ ጋሲቂን ኢዛ ወቀብ", "ከጨለማም ክፋት በገባ ጊዜ፤"),
                QuranVerse(4, "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ", "ወሚን ሸርሪን-ነፍፋሳቲ ፊል ዑቀድ", "በቋጠሮዎችም ላይ ተፊዎች ከሆኑት (ደጓሚዎች) ክፋት፤"),
                QuranVerse(5, "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ", "ወሚን ሸርሪ ሐሲዲን ኢዛ ሐሰድ", "ከምቀኛም ክፋት በምቀኘ ጊዜ (እጠበቃለሁ)፡፡»")
            )
        ),
        QuranSurah(
            number = 114,
            nameArabic = "النَّاس",
            nameTranslit = "An-Nas",
            nameAmharic = "አን-ናስ (ሰዎች)",
            englishMeaning = "Mankind",
            versesCount = 6,
            revelationType = "መካ (Mecca)",
            prominentVerses = listOf(
                QuranVerse(1, "قُلْ أَعُوذُ بِرَبِّ النَّاسِ", "ቁል አዑዙ ቢረብቢን-ናስ", "በል፡- «በሰዎች ጌታ እጠበቃለሁ፤"),
                QuranVerse(2, "مَلِكِ النَّاسِ", "መሊኪን-ናስ", "በሰዎች ንጉሥ፤"),
                QuranVerse(3, "إِلَٰهِ النَّاسِ", "ኢላሂን-ናስ", "በሰዎች አምላክ፤"),
                QuranVerse(4, "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ", "ሚን ሸርሪል ወስዋሲል ኸንናስ", "ከዚያ አፈግፋጊ ከኾነው ጎትጓች ክፋት (እጠበቃለሁ)፤"),
                QuranVerse(5, "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ", "አልለዚ ዩወስዊሱ ፊ ሱዱሪን-ናስ", "ያ በሰዎች ልቦች ውስጥ የሚጎተጉተው፤"),
                QuranVerse(6, "مِنَ الْجِنَّةِ وَالنَّاسِ", "ሚነል ጂንነቲ ወን-ናስ", "ከጋኔንም ከሰውም ከኾነው፡፡»")
            )
        )
    )

    val ALL_SURAHS: List<QuranSurah> = listOf(
        QuranSurah(1, "الفَاتِحَة", "Al-Fatihah", "አል-ፋቲሓ", "The Opening", 7, "መካ"),
        QuranSurah(2, "البَقَرَة", "Al-Baqarah", "አል-በቀራህ", "The Cow", 286, "መዲና"),
        QuranSurah(3, "آل عِمْرَان", "Ali 'Imran", "አሊ-ዒምራን", "Family of Imran", 200, "መዲና"),
        QuranSurah(4, "النِّسَاء", "An-Nisa", "አን-ኒሳእ", "The Women", 176, "መዲና"),
        QuranSurah(5, "المَائِدَة", "Al-Ma'idah", "አል-ማኢዳህ", "The Table Spread", 120, "መዲና"),
        QuranSurah(6, "الأَنْعَام", "Al-An'am", "አል-አንዓም", "The Cattle", 165, "መካ"),
        QuranSurah(7, "الأَعْرَاف", "Al-A'raf", "አል-አዕራፍ", "The Heights", 206, "መካ"),
        QuranSurah(8, "الأَنْفَال", "Al-Anfal", "አል-አንፋል", "The Spoils of War", 75, "መዲና"),
        QuranSurah(9, "التَّوْبَة", "At-Tawbah", "አት-ተውባህ", "The Repentance", 129, "መዲና"),
        QuranSurah(10, "يُونُس", "Yunus", "ዩኑስ", "Jonah", 109, "መካ"),
        QuranSurah(11, "هُود", "Hud", "ሁድ", "Hud", 123, "መካ"),
        QuranSurah(12, "يُوسُف", "Yusuf", "ዩሱፍ", "Joseph", 111, "መካ"),
        QuranSurah(13, "الرَّعْد", "Ar-Ra'd", "አር-ረዕድ", "The Thunder", 43, "መዲና"),
        QuranSurah(14, "إِبْرَاهِيم", "Ibrahim", "ኢብራሂም", "Abraham", 52, "መካ"),
        QuranSurah(15, "الحِجْر", "Al-Hijr", "አል-ሒጅር", "The Rocky Tract", 99, "መካ"),
        QuranSurah(16, "النَّحْل", "An-Nahl", "አን-ነሕል", "The Bee", 128, "መካ"),
        QuranSurah(17, "الإِسْرَاء", "Al-Isra", "አል-ኢስራእ", "The Night Journey", 111, "መካ"),
        QuranSurah(18, "الكَهْف", "Al-Kahf", "አል-ካህፍ", "The Cave", 110, "መካ"),
        QuranSurah(19, "مَرْيَم", "Maryam", "መርየም", "Mary", 98, "መካ"),
        QuranSurah(20, "طه", "Ta-Ha", "ጣሀ", "Ta-Ha", 135, "መካ"),
        QuranSurah(21, "الأَنْبِيَاء", "Al-Anbiya", "አል-አንቢያእ", "The Prophets", 112, "መካ"),
        QuranSurah(22, "الحَجّ", "Al-Hajj", "አል-ሐጅ", "The Pilgrimage", 78, "መዲና"),
        QuranSurah(23, "المُؤْمِنُون", "Al-Mu'minun", "አል-ሙእሚኑን", "The Believers", 118, "መካ"),
        QuranSurah(24, "النُّور", "An-Nur", "አን-ኑር", "The Light", 64, "መዲና"),
        QuranSurah(25, "الفُرْقَان", "Al-Furqan", "አል-ፉርቃን", "The Criterion", 77, "መካ"),
        QuranSurah(26, "الشُّعَرَاء", "Ash-Shu'ara", "አሽ-ሹዐራእ", "The Poets", 227, "መካ"),
        QuranSurah(27, "النَّمْل", "An-Naml", "አን-ነምል", "The Ant", 93, "መካ"),
        QuranSurah(28, "القَصَص", "Al-Qasas", "አል-ቀሶስ", "The Stories", 88, "መካ"),
        QuranSurah(29, "العَنْكَبُوت", "Al-'Ankabut", "አል-ዐንከቡት", "The Spider", 69, "መካ"),
        QuranSurah(30, "الرُّوم", "Ar-Rum", "አር-ሩም", "The Romans", 60, "መካ"),
        QuranSurah(31, "لُقْمَان", "Luqman", "ሉቅማን", "Luqman", 34, "መካ"),
        QuranSurah(32, "السَّجْدَة", "As-Sajdah", "አስ-ሰጅዳህ", "The Prostration", 30, "መካ"),
        QuranSurah(33, "الأَحْزَاب", "Al-Ahzab", "አል-አሕዛብ", "The Combined Forces", 73, "መዲና"),
        QuranSurah(34, "سَبَأ", "Saba", "ሰበእ", "Sheba", 54, "መካ"),
        QuranSurah(35, "فَاطِر", "Fatir", "ፋጢር", "The Originator", 45, "መካ"),
        QuranSurah(36, "يس", "Ya-Sin", "ያሲን", "Ya-Sin", 83, "መካ"),
        QuranSurah(37, "الصَّافَّات", "As-Saffat", "አስ-ሷፍፋት", "Those Ranks", 182, "መካ"),
        QuranSurah(38, "ص", "Sad", "ሷድ", "Sad", 88, "መካ"),
        QuranSurah(39, "الزُّمَر", "Az-Zumar", "አዝ-ዙመር", "The Troops", 75, "መካ"),
        QuranSurah(40, "غَافِر", "Ghafir", "ጋፊር", "The Forgiver", 85, "መካ"),
        QuranSurah(41, "فُصِّلَت", "Fussilat", "ፉስሲለት", "Explained in Detail", 54, "መካ"),
        QuranSurah(42, "الشُّورَى", "Ash-Shura", "አሽ-ሹራ", "The Consultation", 53, "መካ"),
        QuranSurah(43, "الزُّخْرُف", "Az-Zukhruf", "አዝ-ዙኽሩፍ", "The Ornaments of Gold", 89, "መካ"),
        QuranSurah(44, "الدُّخَان", "Ad-Dukhan", "አድ-ዱኻን", "The Smoke", 59, "መካ"),
        QuranSurah(45, "الجَاثِيَة", "Al-Jathiyah", "አል-ጃሲያህ", "The Crouching", 37, "መካ"),
        QuranSurah(46, "الأَحْقَاف", "Al-Ahqaf", "አል-አሕቃፍ", "The Dunes", 35, "መካ"),
        QuranSurah(47, "مُحَمَّد", "Muhammad", "ሙሐመድ", "Muhammad", 38, "መዲና"),
        QuranSurah(48, "الفَتْح", "Al-Fath", "አል-ፈትሕ", "The Victory", 29, "መዲና"),
        QuranSurah(49, "الحُجُرَات", "Al-Hujurat", "አል-ሑጁራት", "The Dwellings", 18, "መዲና"),
        QuranSurah(50, "ق", "Qaf", "ቃፍ", "Qaf", 45, "መካ"),
        QuranSurah(51, "الذَّارِيَات", "Adh-Dhariyat", "አዝ-ዛሪያት", "The Winnowing Winds", 60, "መካ"),
        QuranSurah(52, "الطُّور", "At-Tur", "አጥ-ጡር", "The Mount", 49, "መካ"),
        QuranSurah(53, "النَّجْم", "An-Najm", "አን-ነጅም", "The Star", 62, "መካ"),
        QuranSurah(54, "القَمَر", "Al-Qamar", "አል-ቀመር", "The Moon", 55, "መካ"),
        QuranSurah(55, "الرَّحْمَٰن", "Ar-Rahman", "አር-ረሕማን", "The Beneficent", 78, "መዲና"),
        QuranSurah(56, "الوَاقِعَة", "Al-Waqi'ah", "አል-ዋቂዐህ", "The Inevitable", 96, "መካ"),
        QuranSurah(57, "الحَدِيد", "Al-Hadid", "አል-ሐዲድ", "The Iron", 29, "መዲና"),
        QuranSurah(58, "المُجَادِلَة", "Al-Mujadila", "አል-ሙጃዲላህ", "The Pleading Woman", 22, "መዲና"),
        QuranSurah(59, "الحَشْر", "Al-Hashr", "አል-ሐሽር", "The Exile", 24, "መዲና"),
        QuranSurah(60, "المُمْتَحَنَة", "Al-Mumtahanah", "አል-ሙምተሐናህ", "The Examined One", 13, "መዲና"),
        QuranSurah(61, "الصَّفّ", "As-Saff", "አስ-ሶፍ", "The Ranks", 14, "መዲና"),
        QuranSurah(62, "الجُمُعَة", "Al-Jumu'ah", "አል-ጁሙዐህ", "The Congregation", 11, "መዲና"),
        QuranSurah(63, "المُنَافِقُون", "Al-Munafiqun", "አል-ሙናፊቁን", "The Hypocrites", 11, "መዲና"),
        QuranSurah(64, "التَّغَابُن", "At-Taghabun", "አት-ተጋቡን", "Mutual Disillusion", 18, "መዲና"),
        QuranSurah(65, "الطَّلَاق", "At-Talaq", "አጥ-ጦላቅ", "The Divorce", 12, "መዲና"),
        QuranSurah(66, "التَّحْرِيم", "At-Tahrim", "አት-ተሕሪም", "The Prohibition", 12, "መዲና"),
        QuranSurah(67, "المُلْك", "Al-Mulk", "አል-ሙልክ", "The Sovereignty", 30, "መካ"),
        QuranSurah(68, "القَلَم", "Al-Qalam", "አል-ቀለም", "The Pen", 52, "መካ"),
        QuranSurah(69, "الحَاقَّة", "Al-Haqqah", "አል-ሓቅቃህ", "The Inevitable Reality", 52, "መካ"),
        QuranSurah(70, "المَعَارِج", "Al-Ma'arij", "አል-መዓሪጅ", "The Ascending Stairways", 44, "መካ"),
        QuranSurah(71, "نُوح", "Nuh", "ኑሕ", "Noah", 28, "መካ"),
        QuranSurah(72, "الجِنّ", "Al-Jinn", "አል-ጂን", "The Jinn", 28, "መካ"),
        QuranSurah(73, "المُزَّمِّل", "Al-Muzzammil", "አል-ሙዘምሚል", "The Enshrouded One", 20, "መካ"),
        QuranSurah(74, "المُدَّثِّر", "Al-Muddaththir", "አል-ሙደስሲር", "The Cloaked One", 56, "መካ"),
        QuranSurah(75, "القِيَامَة", "Al-Qiyamah", "አል-ቂያማህ", "The Resurrection", 40, "መካ"),
        QuranSurah(76, "الإِنْسَان", "Al-Insan", "አል-ኢንሳን", "Man", 31, "መዲና"),
        QuranSurah(77, "المُرْسَلَات", "Al-Mursalat", "አል-ሙርሰላት", "The Emissaries", 50, "መካ"),
        QuranSurah(78, "النَّبَأ", "An-Naba", "አን-ነበእ", "The Tidings", 40, "መካ"),
        QuranSurah(79, "النَّازِعَات", "An-Nazi'at", "አን-ናዚዓት", "Those Who Drag Forth", 46, "መካ"),
        QuranSurah(80, "عَبَسَ", "'Abasa", "ዐበሰ", "He Frowned", 42, "መካ"),
        QuranSurah(81, "التَّكْوِير", "At-Takwir", "አት-ተክዊር", "The Overthrowing", 29, "መካ"),
        QuranSurah(82, "الانْفِطَار", "Al-Infitar", "አል-ኢንፊጣር", "The Cleaving", 19, "መካ"),
        QuranSurah(83, "المُطَفِّفِين", "Al-Mutaffifin", "አል-ሙጠፍፊፊን", "Those Who Deal in Fraud", 36, "መካ"),
        QuranSurah(84, "الانْشِقَاق", "Al-Inshiqaq", "አል-ኢንሺቃቅ", "The Splitting Asunder", 25, "መካ"),
        QuranSurah(85, "البُرُوج", "Al-Buruj", "አል-ቡሩጅ", "The Great Constellations", 22, "መካ"),
        QuranSurah(86, "الطَّارِق", "At-Tariq", "አጥ-ጣሪቅ", "The Night-Comer", 17, "መካ"),
        QuranSurah(87, "الأَعْلَى", "Al-A'la", "አል-አዕላ", "The Most High", 19, "መካ"),
        QuranSurah(88, "الغَاشِيَة", "Al-Ghashiyah", "አል-ጋሺያህ", "The Overwhelming Event", 26, "መካ"),
        QuranSurah(89, "الفَجْر", "Al-Fajr", "አል-ፈጅር", "The Dawn", 30, "መካ"),
        QuranSurah(90, "البَلَد", "Al-Balad", "አል-በለድ", "The City", 20, "መካ"),
        QuranSurah(91, "الشَّمْس", "Ash-Shams", "አሽ-ሸምስ", "The Sun", 15, "መካ"),
        QuranSurah(92, "اللَّيْل", "Al-Layl", "አል-ለይል", "The Night", 21, "መካ"),
        QuranSurah(93, "الضُّحَى", "Ad-Duha", "አድ-ዱሓ", "The Morning Hours", 11, "መካ"),
        QuranSurah(94, "الشَّرْح", "Ash-Sharh", "አሽ-ሸርሕ", "The Consolation", 8, "መካ"),
        QuranSurah(95, "التِّين", "At-Tin", "አት-ቲን", "The Fig", 8, "መካ"),
        QuranSurah(96, "العَلَق", "Al-'Alaq", "አል-ዐለቅ", "The Clot", 19, "መካ"),
        QuranSurah(97, "القَدْر", "Al-Qadr", "አል-ቀድር", "The Night of Decree", 5, "መካ"),
        QuranSurah(98, "البَيِّنَة", "Al-Bayyinah", "አል-በይዪናህ", "The Clear Evidence", 8, "መዲና"),
        QuranSurah(99, "الزَّلْزَلَة", "Az-Zalzalah", "አዝ-ዘልዘላህ", "The Earthquake", 8, "መዲና"),
        QuranSurah(100, "العَادِيَات", "Al-'Adiyat", "አል-ዓዲያት", "The Charging Steeds", 11, "መካ"),
        QuranSurah(101, "القَارِعَة", "Al-Qari'ah", "አል-ቃሪዐህ", "The Striking Hour", 11, "መካ"),
        QuranSurah(102, "التَّكَاثُر", "At-Takathur", "አት-ተካሱር", "Rivalry in World Increase", 8, "መካ"),
        QuranSurah(103, "العَصْر", "Al-'Asr", "አል-ዐስር", "The Time", 3, "መካ"),
        QuranSurah(104, "الهُمَزَة", "Al-Humazah", "አል-ሁመዛህ", "The Slanderer", 9, "መካ"),
        QuranSurah(105, "الفِيل", "Al-Fil", "አል-ፊል", "The Elephant", 5, "መካ"),
        QuranSurah(106, "قُرَيْش", "Quraysh", "ቁረይሽ", "Quraysh", 4, "መካ"),
        QuranSurah(107, "المَاعُون", "Al-Ma'un", "አል-ማዑን", "Small Kindnesses", 7, "መካ"),
        QuranSurah(108, "الكَوْثَر", "Al-Kawthar", "አል-ከውሰር", "Abundance", 3, "መካ"),
        QuranSurah(109, "الكَافِرُون", "Al-Kafirun", "አል-ካፊሩን", "The Disbelievers", 6, "መካ"),
        QuranSurah(110, "النَّصْر", "An-Nasr", "አን-ነስር", "The Divine Support", 3, "መዲና"),
        QuranSurah(111, "المَسَد", "Al-Masad", "አል-መሳድ", "The Palm Fiber", 5, "መካ"),
        QuranSurah(112, "الإِخْلَاص", "Al-Ikhlas", "አል-ኢኽላስ", "Sincerity", 4, "መካ"),
        QuranSurah(113, "الفَلَق", "Al-Falaq", "አል-ፈለቅ", "The Daybreak", 5, "መካ"),
        QuranSurah(114, "النَّاس", "An-Nas", "አን-ናስ", "Mankind", 6, "መካ")
    )
}
