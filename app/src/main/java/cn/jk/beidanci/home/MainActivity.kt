package cn.jk.beidanci.home


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import cn.jk.beidanci.BaseActivity
import cn.jk.beidanci.R
import cn.jk.beidanci.choosebook.ChooseBookActivity
import cn.jk.beidanci.data.Constant
import cn.jk.beidanci.data.model.DbWord
import cn.jk.beidanci.data.model.DbWord_Table
import cn.jk.beidanci.myword.MyWordActivity
import cn.jk.beidanci.searchword.SearchWordActivity
import cn.jk.beidanci.utils.ThemeUtil
import cn.jk.beidanci.wordlist.ShowWordListHelper
import com.afollestad.aesthetic.Aesthetic
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.raizlabs.android.dbflow.kotlinextensions.select
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

/**
 * app首页,包括 主页,复习,设置三个功能.
 */
class MainActivity : BaseActivity() {
    internal lateinit var homeFragment: HomeFragment
    internal lateinit var reviewFragment: ReviewFragment
    internal lateinit var settingFragment: SettingFragment
    internal var currentFragment: androidx.fragment.app.Fragment? = null

    companion object {
        //暂时用不到
        var needRefreshdata = false

        fun setNeedRefreshData() {
            needRefreshdata = true
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Aesthetic.isFirstTime) {
            ThemeUtil.changeTheme(primaryColor = R.color.colorPrimary)
        }
        setContentView(R.layout.activity_main)

        val currentBookName: String = prefs[Constant.CURRENT_BOOK, ""] //getter
        if (currentBookName.isEmpty()) {
            startActivity<ChooseBookActivity>()
            finish()
        } else {
            dealFragment(savedInstanceState)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.searchWordView -> {
                startActivity<SearchWordActivity>()
                return true
            }
            R.id.myWordView -> {
                val dbWords = select.from(DbWord::class.java).where(DbWord_Table.collect.eq(true)).queryList().map { it as DbWord }
                ShowWordListHelper.useDefault("收藏" + dbWords.size, dbWords)
                startActivity<MyWordActivity>()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)


        return true
    }

    private fun dealFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null || supportFragmentManager.findFragmentByTag("homeFragment") == null) {
            homeFragment = HomeFragment()
            reviewFragment = ReviewFragment()
            settingFragment = SettingFragment()
            supportFragmentManager.beginTransaction().add(R.id.main_content, homeFragment, "homeFragment").commit()
            supportFragmentManager.beginTransaction().add(R.id.main_content, reviewFragment, "reviewFragment").commit()
            supportFragmentManager.beginTransaction().add(R.id.main_content, settingFragment, "settingFragment").commit()

        } else {
            homeFragment = supportFragmentManager.findFragmentByTag("homeFragment") as HomeFragment
            reviewFragment = supportFragmentManager.findFragmentByTag("reviewFragment") as ReviewFragment
            settingFragment = supportFragmentManager.findFragmentByTag("settingFragment") as SettingFragment


        }
        supportFragmentManager.beginTransaction().hide(homeFragment).hide(reviewFragment).hide(settingFragment).commit()
        showFragment(homeFragment)
        navigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var showFragment: androidx.fragment.app.Fragment = homeFragment
            when (item.itemId) {
                R.id.navigation_home -> showFragment = homeFragment
                R.id.navigation_review -> showFragment = reviewFragment
                R.id.navigation_setting -> showFragment = settingFragment
            }
            showFragment(showFragment)
            true
        })
    }

    fun showFragment(fragment: androidx.fragment.app.Fragment) {
        if (currentFragment === fragment) {
            return
        }
        var title = getString(R.string.app_name)
        if (currentFragment != null) {
            supportFragmentManager.beginTransaction().hide(currentFragment as androidx.fragment.app.Fragment).commit()
        }
        supportFragmentManager.beginTransaction().show(fragment).commit()
        currentFragment = fragment
        when {
            fragment === homeFragment -> title = getString(R.string.app_name)
            fragment === reviewFragment -> title = getString(R.string.title_review)
            fragment === settingFragment -> title = getString(R.string.title_setting)
        }
        if (supportActionBar != null) {
            supportActionBar!!.title = title
        }

    }


}

