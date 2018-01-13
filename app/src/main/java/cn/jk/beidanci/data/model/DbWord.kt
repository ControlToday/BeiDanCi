package cn.jk.beidanci.data.model

import cn.jk.beidanci.data.source.AppDatabase
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

/**
 * Created by jack on 2018/1/12.
 */
@Table(database = AppDatabase::class)
class DbWord(
        @PrimaryKey
        var wordId: String = "",//GaoZhong_2_1
        @Column
        var bookId: String = "",//kaoyan_2
        @Column var rank: Int = 0, //顺序
        @Column var head: String = "",//英文单词
        @Column var content: String = "",//释义
        @Column(getterName = "getEasy") var easy: Boolean = false,
        @Column(typeConverter = WordStateConverter::class)
        var state: WordState = WordState.unlearned,
        @Column(getterName = "getImportant") var important: Boolean = false,
        @Column var knownCount: Int = 0,
        @Column var unknownCount: Int = 0

) {
    constructor(netWord: String, book: Book) : this() {
        val word = Gson().fromJson<Word>(netWord, Word::class.java)
        with(word) {
            wordId = contentOutOut.word.wordId
            bookId = book.id
            rank = wordRank!!.toInt()
            head = headWord!!
            content = contentOutOut.word.content.toString()
        }
    }

    private class Word(
            var wordRank: String?,
            var headWord: String?,
            @SerializedName("content")
            var contentOutOut: WordContentOutOut)

    class WordContentOutOut(var word: WordContentOut)
    class WordContentOut(var wordHead: String, var wordId: String, var content: Any)
}