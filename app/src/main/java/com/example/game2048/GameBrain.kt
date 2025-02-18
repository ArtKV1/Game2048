package com.example.game2048

import kotlin.math.absoluteValue
import kotlin.random.Random

class GameBrain
{
    companion object
    {

        private var matrixNumbers = Array(4) { IntArray(4) }

        //Переделывание массива в матрицу для удобства
        fun doMatrix()
        {
            var index = 0
            for (i in 0 until 4) {
                for (j in 0 until 4) {
                    matrixNumbers[i][j] = GameActivity.masOfNumbers[index]
                    index++
                }
            }
        }

        fun doMassive()
        {
            var index = 0
            for (i in 0 until 4) {
                for (j in 0 until 4) {
                    GameActivity.masOfNumbers[index] = matrixNumbers[i][j]
                    index++
                }
            }
        }

        //Тестовая генерация
        fun testGeneration()
        {
            for (i in 0..15)
            {
                    GameActivity.masOfNumbers[i] = 2
            }
        }

        //Генерация двух рандомных кнопок, которые будут заполнены
        fun getTwoRandomNumbersForButtons() {
            val random = Random
            val list = mutableSetOf(random.nextInt(1, 15), random.nextInt(1, 15)).apply {
                while (size < 2) add(random.nextInt(1, 15))
            }.toList()

            list.forEachIndexed { index, buttonIndex ->
                GameActivity.masOfNumbers[buttonIndex] = if (random.nextInt(100) < 90) 2 else 4
            }
        }

        fun getRandomNumberForButton(): Int?
        {
            val random = Random
            var randomI: Int? = null

            var f: Boolean = false

            for (i in 0..15)
            {
                if (GameActivity.masOfNumbers[i] == 0) {
                    f = true
                    break
                }
            }
            if (f == true)
                while(true)
                {
                    randomI = random.nextInt(0, 15)
                    if (GameActivity.masOfNumbers[randomI] == 0) {
                        GameActivity.masOfNumbers[randomI] = if (random.nextInt(100) < 90) 2 else 4
                        return randomI
                    }
                }

            return randomI
        }


        //ПРОВЕРКА МОЖНО ЛИ СДЕЛАТЬ ХОД КУДА-ЛИБО

        fun isGameOver(): Boolean {
            doMatrix()
            return canTurnLeft() || canTurnRight() || canTurnUp() || canTurnDown()
        }


        fun canTurnLeft(): Boolean
        {
            val size = matrixNumbers.size
            for (i in 0 until size) {
                var canMove = false
                for (j in 1 until size) {
                    if (matrixNumbers[i][j] != 0) {
                        for (k in j - 1 downTo 0) {
                            if (matrixNumbers[i][k] == 0 || matrixNumbers[i][k] == matrixNumbers[i][j]) {
                                canMove = true
                                break
                            } else {
                                break
                            }
                        }
                        if (canMove) {
                            break
                        }
                    }
                }
                if (canMove) {
                    return true
                }
            }
            return false
        }

        fun canTurnRight(): Boolean
        {
            val size = matrixNumbers.size
            for (i in 0 until size) {
                var canMove = false
                for (j in size - 2 downTo 0) {
                    if (matrixNumbers[i][j] != 0) {
                        for (k in j + 1 until size) {
                            if (matrixNumbers[i][k] == 0 || matrixNumbers[i][k] == matrixNumbers[i][j]) {
                                canMove = true
                                break
                            } else {
                                break
                            }
                        }
                        if (canMove) {
                            break
                        }
                    }
                }
                if (canMove) {
                    return true
                }
            }
            return false
        }

        fun canTurnUp(): Boolean
        {
            val size: Int = matrixNumbers.size
            for (j in 0 until size) {
                var canMove = false
                for (i in 1 until size) {
                    if (matrixNumbers.get(i).get(j) !== 0) {
                        for (k in i - 1 downTo 0) {
                            if (matrixNumbers.get(k).get(j) === 0 || matrixNumbers.get(k)
                                    .get(j) === matrixNumbers.get(i).get(j)
                            ) {
                                canMove = true
                                break
                            } else {
                                break
                            }
                        }
                        if (canMove) {
                            break
                        }
                    }
                }
                if (canMove) {
                    return true
                }
            }
            return false
        }

        fun canTurnDown(): Boolean {
            val size = matrixNumbers.size
            for (j in 0 until size) {
                var canMove = false
                for (i in size - 2 downTo 0) {
                    if (matrixNumbers[i][j] != 0) {
                        for (k in i + 1 until size) {
                            if (matrixNumbers[k][j] == 0 || matrixNumbers[k][j] == matrixNumbers[i][j]) {
                                canMove = true
                                break
                            } else {
                                break
                            }
                        }
                        if (canMove) {
                            break
                        }
                    }
                }
                if (canMove) {
                    return true
                }
            }
            return false
        }

        fun turnArrayRight() {
            doMatrix()

            for (row in 0 until 4) {
                val currentRow = IntArray(4) { 0 } // Временный массив для строки
                val originalIndices = IntArray(4) { -1 } // Запоминаем исходные индексы элементов
                var index = 3 // Начинаем с конца строки

                // Первый проход: сдвигаем числа вправо, пропуская нули
                for (col in 3 downTo 0) {
                    if (matrixNumbers[row][col] != 0) {
                        currentRow[index] = matrixNumbers[row][col]
                        originalIndices[index] = row * 4 + col // Запоминаем исходную позицию
                        index--
                    }
                }

                // Второй проход: объединяем одинаковые числа
                for (col in 3 downTo 1) {
                    if (currentRow[col] != 0 && currentRow[col] == currentRow[col - 1]) {
                        currentRow[col] *= 2
                        GameActivity.score += currentRow[col]
                        currentRow[col - 1] = 0
                        if (originalIndices[col - 1] != -1) GameActivity.masOfAnimationCount[originalIndices[col - 1]]++
                        originalIndices[col - 1] = -1
                    }
                }

                // Третий проход: повторно сдвигаем после объединения
                index = 3
                val finalRow = IntArray(4) { 0 }
                for (col in 3 downTo 0) {
                    if (currentRow[col] != 0) {
                        finalRow[index] = currentRow[col]
                        index--
                    }
                }


                // Третий проход: снова сдвигаем после объединения
                index = 3
                val newRow = IntArray(4) { 0 }
                val newIndices = IntArray(4) { -1 }
                for (col in 3 downTo 0) {
                    if (currentRow[col] != 0) {
                        newRow[index] = currentRow[col]
                        newIndices[index] = originalIndices[col] // Сохраняем индекс
                        index--
                    }
                }

                // Записываем в матрицу и считаем финальные сдвиги
                for (col in 0 until 4) {
                    matrixNumbers[row][col] = newRow[col]

                    if (newIndices[col] != -1) {
                        GameActivity.masOfAnimationCount[newIndices[col]] += (col - (newIndices[col] % 4)).absoluteValue
                    }
                }
            }

            // Выводим матрицу после сдвига
            println("Матрица после сдвига:")
            for (row in 0 until 4) {
                for (col in 0 until 4) {
                    print("${matrixNumbers[row][col]}\t")
                }
                println()
            }

            // Выводим одномерный массив сдвигов
            println("Количество сдвигов:")
            for (i in 0 until 16) {
                print("${GameActivity.masOfAnimationCount[i]}\t")
                if ((i + 1) % 4 == 0) println() // Перенос строки для удобства
            }

            doMassive()
        }

        fun turnArrayLeft() {
            doMatrix()

            for (row in 0 until 4) {
                val currentRow = IntArray(4) { 0 }
                val originalIndices = IntArray(4) { -1 }
                var index = 0

                // Сдвиг чисел влево
                for (col in 0 until 4) {
                    if (matrixNumbers[row][col] != 0) {
                        currentRow[index] = matrixNumbers[row][col]
                        originalIndices[index] = row * 4 + col
                        index++
                    }
                }

                // Объединение одинаковых чисел
                for (col in 0 until 3) {
                    if (currentRow[col] != 0 && currentRow[col] == currentRow[col + 1]) {
                        currentRow[col] *= 2
                        GameActivity.score += currentRow[col]
                        currentRow[col + 1] = 0
                        if (originalIndices[col + 1] != -1) GameActivity.masOfAnimationCount[originalIndices[col + 1]]++
                        originalIndices[col + 1] = -1
                    }
                }

                // Сдвиг чисел после объединения
                index = 0
                val finalRow = IntArray(4) { 0 }
                for (col in 0 until 4) {
                    if (currentRow[col] != 0) {
                        finalRow[index] = currentRow[col]
                        index++
                    }
                }

                // Переносим в матрицу
                val newIndices = IntArray(4) { -1 }
                for (col in 0 until 4) {
                    matrixNumbers[row][col] = finalRow[col]
                    if (originalIndices[col] != -1) {
                        GameActivity.masOfAnimationCount[originalIndices[col]] += (col - (originalIndices[col] % 4)).absoluteValue
                    }
                }
            }

            println("Матрица после сдвига влево:")
            for (row in 0 until 4) {
                for (col in 0 until 4) {
                    print("${matrixNumbers[row][col]}\t")
                }
                println()
            }

            println("Количество сдвигов:")
            for (i in 0 until 16) {
                print("${GameActivity.masOfAnimationCount[i]}\t")
                if ((i + 1) % 4 == 0) println()
            }

            doMassive()
        }

        fun turnArrayUp() {
            doMatrix()

            for (col in 0 until 4) {
                val currentCol = IntArray(4) { 0 }
                val originalIndices = IntArray(4) { -1 }
                var index = 0

                // Сдвиг чисел вверх
                for (row in 0 until 4) {
                    if (matrixNumbers[row][col] != 0) {
                        currentCol[index] = matrixNumbers[row][col]
                        originalIndices[index] = row * 4 + col
                        index++
                    }
                }

                // Объединение одинаковых чисел
                for (row in 0 until 3) {
                    if (currentCol[row] != 0 && currentCol[row] == currentCol[row + 1]) {
                        currentCol[row] *= 2
                        GameActivity.score += currentCol[row]
                        currentCol[row + 1] = 0
                        if (originalIndices[row + 1] != -1) GameActivity.masOfAnimationCount[originalIndices[row + 1]]++
                        originalIndices[row + 1] = -1
                    }
                }

                // Сдвиг чисел после объединения
                index = 0
                val finalCol = IntArray(4) { 0 }
                for (row in 0 until 4) {
                    if (currentCol[row] != 0) {
                        finalCol[index] = currentCol[row]
                        index++
                    }
                }

                // Переносим в матрицу
                val newIndices = IntArray(4) { -1 }
                for (row in 0 until 4) {
                    matrixNumbers[row][col] = finalCol[row]
                    if (originalIndices[row] != -1) {
                        GameActivity.masOfAnimationCount[originalIndices[row]] += (row - (originalIndices[row] / 4)).absoluteValue
                    }
                }
            }

            println("Матрица после сдвига вверх:")
            for (row in 0 until 4) {
                for (col in 0 until 4) {
                    print("${matrixNumbers[row][col]}\t")
                }
                println()
            }

            println("Количество сдвигов:")
            for (i in 0 until 16) {
                print("${GameActivity.masOfAnimationCount[i]}\t")
                if ((i + 1) % 4 == 0) println()
            }

            doMassive()
        }


        fun turnArrayDown() {
            doMatrix()

            for (col in 0 until 4) {
                val currentCol = IntArray(4) { 0 }
                val originalIndices = IntArray(4) { -1 }
                var index = 3

                // Сдвиг чисел вниз
                for (row in 3 downTo 0) {
                    if (matrixNumbers[row][col] != 0) {
                        currentCol[index] = matrixNumbers[row][col]
                        originalIndices[index] = row * 4 + col
                        index--
                    }
                }

                // Объединение одинаковых чисел
                for (row in 3 downTo 1) {
                    if (currentCol[row] != 0 && currentCol[row] == currentCol[row - 1]) {
                        currentCol[row] *= 2
                        GameActivity.score += currentCol[row]
                        currentCol[row - 1] = 0
                        if (originalIndices[row - 1] != -1) GameActivity.masOfAnimationCount[originalIndices[row - 1]]++
                        originalIndices[row - 1] = -1
                    }
                }

                // Сдвиг чисел после объединения
                index = 3
                val finalCol = IntArray(4) { 0 }
                for (row in 3 downTo 0) {
                    if (currentCol[row] != 0) {
                        finalCol[index] = currentCol[row]
                        index--
                    }
                }

                // Переносим в матрицу
                val newIndices = IntArray(4) { -1 }
                for (row in 0 until 4) {
                    matrixNumbers[row][col] = finalCol[row]
                    if (originalIndices[row] != -1) {
                        GameActivity.masOfAnimationCount[originalIndices[row]] += (row - (originalIndices[row] / 4)).absoluteValue
                    }
                }
            }

            println("Матрица после сдвига вниз:")
            for (row in 0 until 4) {
                for (col in 0 until 4) {
                    print("${matrixNumbers[row][col]}\t")
                }
                println()
            }

            println("Количество сдвигов:")
            for (i in 0 until 16) {
                print("${GameActivity.masOfAnimationCount[i]}\t")
                if ((i + 1) % 4 == 0) println()
            }

            doMassive()
        }






    }
}