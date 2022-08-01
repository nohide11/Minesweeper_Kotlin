import kotlin.random.Random
import kotlin.system.exitProcess

val random1 = Random
var row = arrayOf( -1, -1, -1, 0, 0, 1, 1, 1 )
var col = arrayOf( -1, 0, 1, -1, 1, -1, 0, 1 )

fun Check(field: MutableList<MutableList<Char>>, x: Int, y: Int, i: Int, j: Int): Boolean {

    if (x == i && y == j) return true
    for (k in 0 until 8) {
        if (field[x + row[k]][y + col[k]] == 'X') return true
    }
    return false
}

fun unit(field: MutableList<MutableList<Char>>, x: Int, y: Int) {
    for (i in 0 until 8) {
        if (field[x + row[i]][y + col[i]] == 'X' || field[x + row[i]][y + col[i]] == '*') continue
        else if (field[x + row[i]][y + col[i]] == '.') field[x + row[i]][y + col[i]] = '\u0031'
        else if (field[x + row[i]][y + col[i]] in '\u0031'..'\u0038') field[x + row[i]][y + col[i]]++
    }
}

fun placement(field: MutableList<MutableList<Char>>, mines: Int, x: Int, y: Int, fieldOpen: MutableList<MutableList<Char>>) {
    field[x][y] = '/'
    fieldOpen[x][y] = '/'
    var mines1 = mines
    var safe: Char = ' '
        for ( k in 0 until mines1) {
        var i = random1.nextInt(1, 10)
        var j = random1.nextInt(1, 10)
        if(field[i][j] != 'X') {
            safe = field[i][j]
            field[i][j] = 'X'
        } else {
            mines1++
            continue
        }
        if (Check(field, x, y, i, j)) {
            field[i][j] = safe
            mines1++
        } else unit(field, i, j)
    }
}

fun marcs(field: MutableList<MutableList<Char>>, fieldOpen: MutableList<MutableList<Char>>) {
    print("Set/unset mines marks or claim a cell as free: ")
    val coordinates = readLine()!!.split(' ').toMutableList()
    val y: Int = coordinates[0].toInt()
    val x: Int = coordinates[1].toInt()
    val z: String = coordinates[2]
    when (z) {
        "free" -> {
            if (field[x][y] == 'X') {
                println("You stepped on a mine and failed!")
                Lose(field, fieldOpen)
                output(field, fieldOpen)
                exitProcess(0)
            }
            if (field[x][y] in '\u0030' .. '\u0038') fieldOpen[x][y] = field[x][y]
            if (field[x][y] == '.') {
                field[x][y] = '/'
                fieldOpen[x][y] = '/'
                free(field, x, y)
            }
            output(field, fieldOpen)
        }
        "mine" -> {
            if (fieldOpen[x][y] == '*') fieldOpen[x][y] = '.' else fieldOpen[x][y] = '*'
            output(field, fieldOpen)
        }
    }
}

fun Lose(field: MutableList<MutableList<Char>>, fieldOpen: MutableList<MutableList<Char>>) {
    for (i in 1 until 10) {
        for (j in 1 until 10) {
            if (field[i][j] == 'X') fieldOpen[i][j] = field[i][j]
        }
    }
}

fun checkBesideNum(field: MutableList<MutableList<Char>>, x: Int, y: Int): Boolean  {
    var chet = 0
    for (i in 0 until 8) {
        if (field[x + row[i]][y + col[i]] == '/') chet++
    }
    return chet != 0
}

fun output(field: MutableList<MutableList<Char>>, fieldOpen: MutableList<MutableList<Char>>) {
    println("\n |123456789|")
    println("-|---------|")
    for (i in 1 until 10) {
        print("${i}|")
        for (j in 1 until 10) {
            if (field[i][j] == '/') {
                fieldOpen[i][j] = field[i][j]
                print(fieldOpen[i][j])
            } else if (field[i][j] in '\u0031' .. '\u0038' && checkBesideNum(field, i, j)) {
                fieldOpen[i][j] = field[i][j]
                print(fieldOpen[i][j])
            } else print(fieldOpen[i][j])
        }
        print("|")
        println()
    }
    println("-|---------|")
    println()
}

fun main() {
    print("How many mines do you want on the field? ")
    var mines: Int = readLine()!!.toInt()
    val field = MutableList(11) {
        MutableList(11) { '.' }
    }
    val fieldOpen = MutableList(11) {
        MutableList(11) { '.' }
    }
    outputField(field)
    do {
        print("Set/unset mines marks or claim a cell as free: ")
        val coordinates = readLine()!!.split(' ').toMutableList()
        val y: Int = coordinates[0].toInt()
        val x: Int = coordinates[1].toInt()
        val z: String = coordinates[2]
        when (z) {
            "free" -> {
                placement(field, mines, x, y, fieldOpen)
                free(field, x, y)
                output(field, fieldOpen)
            }
            "mine" -> {
                if (fieldOpen[x][y] == '*') fieldOpen[x][y] = field[x][y] else fieldOpen[x][y] = '*'
                output(field, fieldOpen)
            }
        }
    } while (z == "mine")
    while (winOrLose(field, fieldOpen, mines)) {
        marcs(field, fieldOpen)
    }
}

fun winOrLose(field: MutableList<MutableList<Char>>, fieldOpen: MutableList<MutableList<Char>>, mines: Int): Boolean {
    var minesDef: Int = 0
    var count: Int = 0
    for (i in 1 until 10) {
        for (j in 1 until 10) {
            if (field[i][j] == 'X' && fieldOpen[i][j] == '*') minesDef++
            if (fieldOpen[i][j] == '.') count++
        }
    }
    if (minesDef == mines) {
        println("Congratulations! You found all the mines!")
        return false
    }
    if (minesDef == 0 && count == mines) {
        println("Congratulations! You found all the mines!")
        return false
    }
    if (minesDef + count == mines) {
        println("Congratulations! You found all the mines!")
        return false
    }
    return true
}

fun outputField(field: MutableList<MutableList<Char>>) {
    println("\n |123456789|")
    println("-|---------|")
    for (i in 1 until 10) {
        print("${i}|")
        for (j in 1 until 10) {
            print(field[i][j])
        }
        print("|")
        println()
    }
    println("-|---------|")
}

val row1 = arrayOf(-1, 0, 0, 1)
val col1 = arrayOf(0, -1, 1, 0)
val row2 = arrayOf(-1, -1, 1, 1)
val col2 = arrayOf(-1, 1, -1, 1)

fun free(field: MutableList<MutableList<Char>>, x: Int, y: Int) {
    var myArray = mutableListOf(x, y)
    var i: Int
    var j: Int

    while (myArray.isNotEmpty()) {
        j = myArray.last()
        myArray.removeAt(myArray.lastIndex)
        i = myArray.last()
        myArray.removeAt(myArray.lastIndex)
        if (i < 10 && j < 10 && i > 0 && j > 0) {
            for (k in 0 until 4) {
                if (field[i + row1[k]][j + col1[k]] == '.') {
                    field[i + row1[k]][j + col1[k]] = '/'

                    myArray.add(i + row1[k])
                    myArray.add(j + col1[k])
                }
            }
                for ( k in 0 until 4) {
                    if (field[i + row2[k]][j] in '\u0030'..'\u0038' && field[i][j + col2[k]] in '\u0030'..'\u0038') {
                        if (field[i + row2[k]][j + col2[k]] == '.') {
                            field[i + row2[k]][j + col2[k]] = '/'

                            myArray.add(i + row2[k])
                            myArray.add(j + col2[k])
                        }
                    }
                }
        }
    }
}