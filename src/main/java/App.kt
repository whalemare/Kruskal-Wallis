import java.io.File

/**
 * @since 2019
 * @author Anton Vlasov - whalemare
 */
class App : Runnable {

    private fun println(matrix: List<List<Number>>) {
        matrix.forEach { row ->
            println(row.joinToString(separator = ", "))
        }
    }

    override fun run() {
        val matrixRaw = readMatrix()
        println("Используется матрица")
        println(matrixRaw)
        println()

        val matrixFlattenSorted = matrixRaw.flatten().sorted()
        println("Матрица отсортирована и представлена в виде вектора")
        println(matrixFlattenSorted)
        println()

        val matrixRanks = matrixRaw.map { row -> row.map { matrixFlattenSorted.indexOf(it) + 1 } }
        println("Преобразована в ранги")
        println(matrixRanks)
        println()

        val matrixRawSum = matrixRanks.map { ranks -> ranks.sum() }
        println("Сумма строк")
        println(matrixRawSum)
        println()

        val n = matrixFlattenSorted.size
        println("n = $n")
        val first = 12.0 / (n.toDouble() * (n.toDouble() + 1.toDouble()))
        println("first = $first")
        val second = matrixRawSum.mapIndexed { index, it ->
            Math.pow(it.toDouble(), 2.0) / matrixRaw[index].size
        }.sum()
        println("second = $second")

        val third = 3 * (n + 1)
        println("third = $third")

        val H_emp = (first * second) - third
        println("Найденное эмперическое значение критерия = $H_emp")
        println()

        val H_kp_005 = 5.780
        val H_kp_001 = 8.000
        if (H_emp > H_kp_005 && H_emp > H_kp_001) {
            println("\uD835\uDC3Bэмп > \uD835\uDC3B кр (0.05) и \uD835\uDC3Bэмп > \uD835\uDC3Bкр (0.01)." +
                    "\nНулевая гипотеза \uD835\uDC3B0 отвергается, хотя бы одно рабочее место отличается от других.")
        } else if (H_emp <= H_kp_005) {
            println("\uD835\uDC3Bэмп <= \uD835\uDC3Bкр(0.05). \nНулевая гипотеза принимается. Группы являются сравнительно одинаковыми.")
        }

        println()
        println("Рейтинг защищенности:")
        val ratingRaw = matrixRaw.mapIndexed { index, raw -> raw to matrixRawSum[index] }
        val ratingSorted = ratingRaw.sortedBy { it.second }

        val output = ratingSorted.map { (raw, rank) -> "Индекс: #${ratingRaw.indexOf(raw to rank)}; $raw => $rank" }
        output.forEach { println(it) }
    }

    private fun readMatrix(): List<List<Double>> {
        val test =  listOf(
            listOf(0.00150, 0.01050, 0.03120, 0.00900, 0.00062),
            listOf(0.03100, 0.06900, 0.05700, 0.05709, 0.01280),
            listOf(0.00080, 0.00990, 0.00012, 0.00031, 0.00002)
        )
        println("Введите ссылку на файл с матрицей (либо нажмите Enter чтобы использовать тестовые данные): ")
        val filePath = readLine()
        if (filePath.isNullOrBlank()) {
            println("Вы выбрали тестовые данные")
            return test
        }

        println("В качестве разделителя используется строка ', '")
        println("Если требуется, введите новый разделить (если нет, нажмите Enter)")
        var delimeter = readLine()
        if (delimeter.isNullOrBlank()) delimeter = ", "

        val file = File(filePath)
        val text = file.readText()
        val rows = text.split("\n")
        return rows.map { row -> row.split(delimeter).map { number -> number.toDouble() } }
    }
}
