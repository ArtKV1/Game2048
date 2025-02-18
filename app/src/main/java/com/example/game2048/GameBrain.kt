package com.example.game2048

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

        //Генерация двух рандомных кнопок, которые будут заполнены
        fun getTwoRandomNumbersForButtons()
        {
            val list: MutableList<Int> = MutableList<Int>(0) { 0 }


            while (list.count() != 2)
            {
                val random: Random = Random
                val randomNumber: Int = random.nextInt(1, 15)
                if (list.count() == 0)
                    list.add(randomNumber)
                else if (list[0] != randomNumber)
                    list.add(randomNumber)
            }

            var numbersCount: Int = 0

            while(true)
            {
                val random: Random = Random
                val randomNumber: Int = random.nextInt(100)

                if (randomNumber < 90)
                    GameActivity.masOfNumbers[list[numbersCount]] = 2
                else
                    GameActivity.masOfNumbers[list[numbersCount]] = 4

                numbersCount++

                if (numbersCount == 2)
                    break
            }
        }

        //ПРОВЕРКА МОЖНО ЛИ СДЕЛАТЬ ХОД КУДА-ЛИБО

        fun canTurnLeft(): Boolean
        {
            doMatrix()

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
    }
}