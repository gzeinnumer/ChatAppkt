package com.gzeinnumer.chatappkt

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.gzeinnumer.chatappkt.databinding.ActivityMainBinding
import com.gzeinnumer.chatappkt.fragment.CameraFragment
import com.gzeinnumer.chatappkt.fragment.ChatsFragment
import com.gzeinnumer.chatappkt.fragment.ProfileFragment
import com.gzeinnumer.chatappkt.fragment.UsersFragment
import com.gzeinnumer.chatappkt.model.User
import java.util.*

class MainActivity : AppCompatActivity() {
    //todo 28
    private lateinit var binding: ActivityMainBinding
    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //todo 29
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""

        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users_chat_app").child(firebaseUser!!.uid)

        //todo 31
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: User? = dataSnapshot.getValue<User>(User::class.java)
                user?.let {
                    binding.username.text = it.username
                    if (it.imageURL == "default") {
                        binding.profileImage.setImageResource(R.mipmap.ic_launcher)
                    } else {
                        Glide.with(this@MainActivity).load(it.imageURL)
                            .into(binding.profileImage)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        //todo 37
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)

        viewPagerAdapter.apply {
            addFragment(CameraFragment(), "Camera")
            addFragment(ChatsFragment(), "Chats")
            addFragment(UsersFragment(), "Users")
            //todo 70
            addFragment(ProfileFragment(), "Profile")
            //end todo 70
            with(binding){
                viewPager.adapter = viewPagerAdapter
                tabLayout.setupWithViewPager(viewPager)
            }
        }
    }


    //todo 33
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    //todo 34
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@MainActivity, StartActivity::class.java))
            finish()
        }
        return false
    }

    //todo 36
    internal class ViewPagerAdapter(
        fm: FragmentManager,
        behavior: Int
    ) : FragmentPagerAdapter(fm, behavior) {

        private val fragments: ArrayList<Fragment> = ArrayList()
        private val title: ArrayList<String> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        fun addFragment(
            fragment: Fragment,
            title: String
        ) {
            fragments.add(fragment)
            this.title.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return title[position]
        }
    }
}
