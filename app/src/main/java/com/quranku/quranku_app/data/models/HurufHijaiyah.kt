package com.quranku.quranku_app.data.models

data class HurufHijaiyah(
    val id: Int,
    val huruf: String,
    val kondisiKelas: Map<String, String>
)

val hurufHijaiyahList = listOf(
    HurufHijaiyah(1, "ا", mapOf("fathah" to "01. alif_fathah", "kasroh" to "02. alif_kasroh", "dommah" to "03. alif_dommah")),
    HurufHijaiyah(2, "ب", mapOf("fathah" to "04. ba_fathah", "kasroh" to "05. ba_kasroh", "dommah" to "06. ba_dommah")),
    HurufHijaiyah(3, "ت", mapOf("fathah" to "07. ta_fathah", "kasroh" to "08. ta_kasroh", "dommah" to "09. ta_dommah")),
    HurufHijaiyah(4, "ث", mapOf("fathah" to "10. tsa_fathah", "kasroh" to "11. tsa_kasroh", "dommah" to "12. tsa_dommah")),
    HurufHijaiyah(5, "ج", mapOf("fathah" to "13. jim_fathah", "kasroh" to "14. jim_kasroh", "dommah" to "15. jim_dommah")),
    HurufHijaiyah(6, "ح", mapOf("fathah" to "16. hah_fathah", "kasroh" to "17. hah_kasroh", "dommah" to "18. hah_dommah")),
    HurufHijaiyah(7, "خ", mapOf("fathah" to "19. kha_fathah", "kasroh" to "20. kha_kasroh", "dommah" to "21. kha_dommah")),
    HurufHijaiyah(8, "د", mapOf("fathah" to "22. dal_fathah", "kasroh" to "23. dal_kasroh", "dommah" to "24. dal_dommah")),
    HurufHijaiyah(9, "ذ", mapOf("fathah" to "25. dzal_fathah", "kasroh" to "26. dzal_kasroh", "dommah" to "27. dzal_dommah")),
    HurufHijaiyah(10, "ر", mapOf("fathah" to "28. ra_fathah", "kasroh" to "29. ra_kasroh", "dommah" to "30. ra_dommah")),
    HurufHijaiyah(11, "ز", mapOf("fathah" to "31. zay_fathah", "kasroh" to "32. zay_kasroh", "dommah" to "33. zay_dommah")),
    HurufHijaiyah(12, "س", mapOf("fathah" to "34. sin_fathah", "kasroh" to "35. sin_kasroh", "dommah" to "36. sin_dommah")),
    HurufHijaiyah(13, "ش", mapOf("fathah" to "37. shin_fathah", "kasroh" to "38. shin_kasroh", "dommah" to "39. shin_dommah")),
    HurufHijaiyah(14, "ص", mapOf("fathah" to "40. sad_fathah", "kasroh" to "41. sad_kasroh", "dommah" to "42. sad_dommah")),
    HurufHijaiyah(15, "ض", mapOf("fathah" to "43. dad_fathah", "kasroh" to "44. dad_kasroh", "dommah" to "45. dad_dommah")),
    HurufHijaiyah(16, "ط", mapOf("fathah" to "46. tah_fathah", "kasroh" to "47. tah_kasroh", "dommah" to "48. tah_dommah")),
    HurufHijaiyah(17, "ظ", mapOf("fathah" to "49. zah_fathah", "kasroh" to "50. zah_kasroh", "dommah" to "51. zah_dommah")),
    HurufHijaiyah(18, "ع", mapOf("fathah" to "52. ain_fathah", "kasroh" to "53. ain_kasroh", "dommah" to "54. ain_dommah")),
    HurufHijaiyah(19, "غ", mapOf("fathah" to "55. ghaiin_fathah", "kasroh" to "56. ghaiin_kasroh", "dommah" to "57. ghaiin_dommah")),
    HurufHijaiyah(20, "ف", mapOf("fathah" to "58. fa_fathah", "kasroh" to "59. fa_kasroh", "dommah" to "60. fa_dommah")),
    HurufHijaiyah(21, "ق", mapOf("fathah" to "61. qaf_fathah", "kasroh" to "62. qaf_kasroh", "dommah" to "63. qaf_dommah")),
    HurufHijaiyah(22, "ك", mapOf("fathah" to "64. kaf_fathah", "kasroh" to "65. kaf_kasroh", "dommah" to "66. kaf_dommah")),
    HurufHijaiyah(23, "ل", mapOf("fathah" to "67. lam_fathah", "kasroh" to "68. lam_kasroh", "dommah" to "69. lam_dommah")),
    HurufHijaiyah(24, "م", mapOf("fathah" to "70. mim_fathah", "kasroh" to "71. mim_kasroh", "dommah" to "72. mim_dommah")),
    HurufHijaiyah(25, "ن", mapOf("fathah" to "73. nun_fathah", "kasroh" to "74. nun_kasroh", "dommah" to "75. nun_dommah")),
    HurufHijaiyah(26, "ه", mapOf("fathah" to "76. Ha_fathah", "kasroh" to "77. Ha_kasroh", "dommah" to "78. Ha_dommah")),
    HurufHijaiyah(27, "و", mapOf("fathah" to "79. waw_fathah", "kasroh" to "80. waw_kasroh", "dommah" to "81. waw_dommah")),
    HurufHijaiyah(28, "ي", mapOf("fathah" to "82. ya_fathah", "kasroh" to "83. ya_kasroh", "dommah" to "84. ya_dommah"))
)