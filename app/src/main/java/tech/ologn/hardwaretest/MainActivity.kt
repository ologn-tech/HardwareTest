package tech.ologn.hardwaretest

import android.content.Intent
import android.content.IntentFilter
import android.os.*
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.tabs.TabLayoutMediator
import tech.ologn.hardwaretest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val checkReceiver = CheckReceiver()

    private lateinit var nameTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // register the actions
        if (Build.VERSION.SDK_INT >= 26){
            val intentFilter = IntentFilter()
            intentFilter.addAction(Intent.ACTION_POWER_CONNECTED)
            intentFilter.addAction(Intent.ACTION_HEADSET_PLUG)
            intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED)
            registerReceiver(checkReceiver,intentFilter)
        }

        nameTextView = binding.nameTV
        nameTextView.text = TestResultStore.getTesterName(this)
        // to attach between fragments and viewPager
        // i created the TabLayoutAdapter to view the fragment in viewPager
        val adapter = TabLayoutAdapter(this)
        binding.viewPager.adapter = adapter
        // to attach between tabLayout and viewPager and display names of fragments
        val tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
            when (position) {
                0 -> tab.text = "Tests"
//                1 -> tab.text = "Device Info"
                1 -> tab.text = "Result"
            }
        }
        // to start attaching
        tabLayoutMediator.attach()

//        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                binding.welcomeLayout.visibility = if (position ==0) View.VISIBLE else View.GONE
//            }
//        })

        val toolbar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(toolbar)
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(checkReceiver)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear -> {
                showClearConfirmDialog()
                true
            }
            R.id.action_edit ->{
                showEditNameDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showEditNameDialog() {
        val currentName = nameTextView.text

        val editText = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_TEXT
            setText(currentName)
            setSelection(currentName.length)
        }

        AlertDialog.Builder(this)
            .setTitle("Edit Name")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                val newName = editText.text.toString().trim()
                if (newName.isNotEmpty()) {
                    TestResultStore.saveTesterName(this, newName)
                    nameTextView.text = newName
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showClearConfirmDialog() {
        AlertDialog.Builder(this)
            .setTitle("Clear Data")
            .setMessage("Are you sure you want to clear all data?")
            .setPositiveButton("Yes") { dialog, _ ->
                TestResultStore.clearAll(this)

                dialog.dismiss()

                // Restart activity
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}
