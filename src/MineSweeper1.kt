import kotlin.random.Random
import kotlin.reflect.jvm.internal.impl.protobuf.ByteString.Output
import kotlin.reflect.jvm.internal.impl.util.Check
import kotlin.system.exitProcess

fun units(x: Int, y: Int, field: MutableList<MutableList<Char>>) {
    if(field[x - 1][y] != 'X') field[x - 1][y]++
    if(field[x - 1][y + 1] != 'X') field[x - 1][y + 1]++
    if(field[x][y + 1] != 'X') field[x][y + 1]++
    if(field[x + 1][y + 1] != 'X') field[x + 1][y + 1]++
    if(field[x + 1][y] != 'X') field[x + 1][y]++
    if(field[x + 1][y - 1] != 'X') field[x + 1][y - 1]++
    if(field[x][y - 1] != 'X') field[x][y - 1]++
    if(field[x - 1][y - 1] != 'X') field[x - 1][y - 1]++
}


val random = Random

fun Placement(field: MutableList<MutableList<Char>>, mines: Int, z: Int, g: Int) {
    var min = mines
    for (i in 0 until mines) {
        var x = random.nextInt(2, 10)
        var y = random.nextInt(2, 10)
        if (field[x][y] != 'X' || (x != z && y != g)) {
            field[x][y] = 'X'
            units(x, y, field)
        }else min++
    }
    for (i in 1..9) {
        field[0][i + 1] = '\u0030' + i
        field[i + 1][0] = '\u0030' + i
    }
    for (i in 0 until  12) {
        field[1][i] = '—'
        field[11][i] = '—'
        field[i][1] = '│'
        field[i][11] = '│'
    }
}

fun Check(field: MutableList<MutableList<Char>>, x: Int, y: Int): Boolean {
    var chet = 0
    if(field[x - 1][y] == '/') chet++
    if(field[x - 1][y + 1] == '/') chet++
    if(field[x][y + 1] == '/') chet++
    if(field[x + 1][y + 1] == '/') chet++
    if(field[x + 1][y] == '/') chet++
    if(field[x + 1][y - 1] == '/') chet++
    if(field[x][y - 1] == '/') chet++
    if(field[x - 1][y - 1] == '/') chet++
    return chet != 0
}

fun Output(field: MutableList<MutableList<Char>>, open: MutableList<MutableList<String>>) {
    println()
    var chet = 0
    val myArray = arrayOf('1', '2', '3', '4', '5', '6', '7', '8')
    val array = arrayOf(1, 12, 0)
    for (i in 0 until  12) {
        for (j in 0 until 12) {
            if (field[i][j] == 'X') chet++
//                    || field[i][j] == 'X'
            if ((field[i][j] == '0'  || myArray.contains(field[i][j])) && (!array.contains(i) && !array.contains(j))) {
                if (myArray.contains(field[i][j]) && Check(field, i, j)) {
                    print(field[i][j])
                } else if (open[i][j] == "open") {
                    print(field[i][j])
                } else print('.')
            }else print(field[i][j])
        }
        println()
    }
    if (chet == 0) println("Congratulations! You found all the mines!")
}

fun OutputLoss(field: MutableList<MutableList<Char>>, open: MutableList<MutableList<String>>) {
    println()
    val myArray = arrayOf('1', '2', '3', '4', '5', '6', '7', '8')
    val array = arrayOf(1, 12, 0)
    for (i in 0 until  12) {
        for (j in 0 until 12) {
            if ((field[i][j] == '0' || myArray.contains(field[i][j])) && (!array.contains(i) && !array.contains(j))) {
                if (myArray.contains(field[i][j]) && Check(field, i, j)) {
                    print(field[i][j])
                } else if (open[i][j] == "open") {
                    print(field[i][j])
                } else print('.')
            }else print(field[i][j])
        }
        println()
    }
    println("You stepped on a mine and failed!")
}

fun Free(field: MutableList<MutableList<Char>>, x: Int, y: Int, open: MutableList<MutableList<String>>) {
//    field[x][y] = '/'
//    for (i in 1 until  9) {
//        if (field[x][y + i] == '0') field[x][y + i] = '/' else break
//    }
//    for (i in 1 until  9) {
//        if (field[x][y - i] == '0') field[x][y - i] = '/' else break
//    }
    if (field[x][y] == 'X') {
        OutputLoss(field, open)
    } else if (field[x][y] in '\u0031' .. '\u0039') {
        open[x][y] = "open"
        Output(field, open)
    } else {
        for (i in 0 until 9) {
            if (field[x + i][y] == '0' || field[x + i][y] == '/') {
                field[x + i][y] = '/'
                for (j in 1 until 9) {
                    if (field[x + i][y + j] == '0' || field[x + i][y + j] == '/') {
                        field[x + i][y + j] = '/'
                        for (k in 1 until 9) {
                            if (field[x + i - k][y + j] == '0' || field[x + i - k][y + j] == '/') {
                                field[x + i - k][y + j] = '/'
                                for (z in 1 until 9) {
                                    if (field[x + i - k][y + j - z] == '0' || field[x + i - k][y + j - z] == '/') field[x + i - k][y + j - z] =
                                        '/' else break
                                }
                            } else break
                        }
                        for (k in 1 until 9) {
                            if (field[x + i + k][y + j] == '0' || field[x + i + k][y + j] == '/') {
                                field[x + i + k][y + j] = '/'
                                for (z in 1 until 9) {
                                    if (field[x + i + k][y + j - z] == '0' || field[x + i + k][y + j - z] == '/') field[x + i + k][y + j - z] =
                                        '/' else break
                                }
                            } else break
                        }
                    } else break
                }
                for (j in 1 until 9) {
                    if (field[x + i][y - j] == '0' || field[x + i][y - j] == '/') {
                        field[x + i][y - j] = '/'
                        for (k in 1 until 9) {
                            if (field[x + i + k][y - j] == '0' || field[x + i + k][y - j] == '/') {
                                field[x + i + k][y - j] = '/'
                                for (z in 1 until 9) {
                                    if (field[x + i + k][y - j + z] == '0' || field[x + i + k][y - j + z] == '/') field[x + i + k][y - j + z] =
                                        '/' else break
                                }
                            } else break
                        }
                        for (k in 1 until 9) {
                            if (field[x + i - k][y - j] == '0' || field[x + i - k][y - j] == '/') {
                                field[x + i - k][y - j] = '/'
                                for (z in 1 until 9) {
                                    if (field[x + i - k][y - j + z] == '0' || field[x + i - k][y - j + z] == '/') field[x + i - k][y - j + z] =
                                        '/' else break
                                }
                            } else break
                        }
                    } else break
                }
            } else break
            if (field[x - i][y] == '0' || field[x - i][y] == '/') {
                field[x - i][y] = '/'
                for (j in 1 until 9) {
                    if (field[x - i][y + j] == '0' || field[x - i][y + j] == '/') {
                        field[x - i][y + j] = '/'
                        for (k in 1 until 9) {
                            if (field[x - i - k][y + j] == '0' || field[x - i - k][y + j] == '/') {
                                field[x - i - k][y + j] = '/'
                            } else break
                        }
                    } else break
                }
                for (j in 1 until 9) {
                    if (field[x - i][y - j] == '0' || field[x - i][y - j] == '/') {
                        field[x - i][y - j] = '/'
                        for (k in 1 until 9) {
                            if (field[x - i - k][y - j] == '0' || field[x - i - k][y - j] == '/') field[x - i - k][y - j] =
                                '/' else break
                        }
                    } else break
                }
            } else break
        }
        Output(field, open)
    }
}

fun MineCheck(field: MutableList<MutableList<Char>>, x: Int, y: Int, open: MutableList<MutableList<String>>) {
    if (field[x][y] == 'X') OutputLoss(field, open)
    else if (open[x][y] in "1".."8") field[x][y] = open[x][y].toInt().toChar()
    else if (field[x][y] == '*') field[x][y] = '.'
    else if (field[x][y] == '0') field[x][y] = '*'
    for (i in 0 until 9) {
        if (field[x][y] == '\u0031' + i) {
            open[x][y] = field[x][y].toString()
        }
    }
}

fun Marcs(field: MutableList<MutableList<Char>>, open: MutableList<MutableList<String>>) {
    print("Set/unset mines marks or claim a cell as free: ")
    val coordinates = readLine()!!.split(' ').toMutableList()
    val y: Int = coordinates[0].toInt() + 1
    val x: Int = coordinates[1].toInt() + 1
    val z: String = coordinates[2]
    if(field[x][y] in '\u0031' .. '\u0038')open[x][y] = "open"
    when (z) {
        "mine" -> MineCheck(field, x, y, open)
        "free" -> Free(field, x, y, open)
    }
//
//    if (field[x ][y] == '*') {
//        field[x][y] = '.'
//        Output (field)
//    }
//    if (field[x][y] == 'X') {
//        field[x][y] = '*'
//        Output (field)
//    }
//    else if (field[x][y] == '0') {
//        Output (field)
//    }
}

fun main() {
    print("How many mines do you want on the field? ")
    var mines: Int = readLine()!!.toInt()
    val field = MutableList(12) {
        MutableList(12) { '0' }
    }
    val open = MutableList(12) {
        MutableList(12) { "close" }
    }
    print("Set/unset mines marks or claim a cell as free: ")
    val coordinates = readLine()!!.split(' ').toMutableList()
    val y: Int = coordinates[0].toInt() + 1
    val x: Int = coordinates[1].toInt() + 1
    val z: String = coordinates[2]
    open[x][y] = "open"

    Placement(field, mines, x ,  y)
    Output(field, open)



    Marcs(field, open)

    var X = 0
    while (X == 0) {
        X = 1
        Marcs(field, open)
        for (i in 0 until  12) {
            for (j in 0 until 12) {
                if (field[i][j] == 'X') X = 0
            }
        }
    }
}