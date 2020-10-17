package damayanti.evi.arunates.commons

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import damayanti.evi.arunates.R


open class BaseActivity: RxBaseActivity(){

    fun changeFragment(f: Fragment, cleanStack: Boolean, tag: String) {
        val ft = supportFragmentManager.beginTransaction()
        if(cleanStack)
            clearBackStack(supportFragmentManager)
        ft.replace(R.id.main_content, f, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss()
    }

    private fun clearBackStack(fm: FragmentManager) {
        if (fm.backStackEntryCount > 0){
            val first = fm.getBackStackEntryAt(0)
            fm.popBackStack(first.id, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }



}