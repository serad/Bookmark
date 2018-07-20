package com.aplication.sergio.bookmark.fragments

import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_list.view.*
import com.aplication.sergio.bookmark.R
import android.support.v4.app.*
import android.support.v4.view.ViewPager
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.*
import android.view.MenuInflater

/**
 * A simple [Fragment] subclass.
 *
 */
class ListFragment : Fragment() {

    private val NUM_PAGES = 5

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private var mPager: ViewPager? = null

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private var mPagerAdapter: PagerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_list, container, false)
        setHasOptionsMenu(true)
        // Instantiate a ViewPager and a PagerAdapter.
        val mPager = v.pager as ViewPager
        val args = arguments
        var top = 0
        if(args != null)
        {
            Log.i("PASO", "PASO")
            top = args!!.getInt("top", 0)
            Log.i("PASO", top.toString())
        }

        if(top == 1)
        {
            mPagerAdapter = FavoritePagerAdapter(activity!!.supportFragmentManager)
        }
        else
        {
            mPagerAdapter = CustomPagerAdapter(activity!!.supportFragmentManager)
        }



        mPager.adapter = mPagerAdapter

        return v
    }

    fun onBackPressed() {
        if (mPager!!.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.

        } else {
            // Otherwise, select the previous step.
            mPager!!.currentItem = mPager!!.currentItem - 1
        }
    }

  inner class FavoritePagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter (fm) {

      override fun getItem(position: Int): Fragment {
          var args = Bundle()
          args.putInt("position", -1)
          Log.i("POSICION", position.toString())
          var fragment = BookListFragment()
          fragment.arguments = args
          return fragment
        }

        override fun getCount(): Int {
            return 1
        }

        @Override
        override  fun getPageTitle(position: Int): CharSequence {
            when (position) {
                1 -> {
                    return getString(R.string.favourites)
                }
            }
            return "Top Favourites"
        }
    }
    inner class CustomPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter (fm) {

        override fun getItem(position: Int): Fragment {
            var args = Bundle()
            args.putInt("position", position)
            Log.i("POSICION", position.toString())
            var fragment = BookListFragment()
            fragment.arguments = args
            return fragment
        }

        override fun getCount(): Int {
            return 3
        }

        @Override
        override  fun getPageTitle(position: Int): CharSequence {
            when (position) {
                0 -> {
                    return getString(R.string.favourites)
                }
                1 -> {
                    return getString(R.string.discarded)
                }
                2 -> {
                    return getString(R.string.read)
                }
            }
            return "Page " + (position + 1)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_navigation, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.navigation_favourites) {
            true
        } else super.onOptionsItemSelected(item)
    }
    companion object {
        fun newInstance(): ListFragment = ListFragment()
    }

}
