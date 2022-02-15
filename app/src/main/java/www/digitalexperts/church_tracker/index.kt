package www.digitalexperts.church_tracker

import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.MobileAds
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import github.com.st235.lib_expandablebottombar.navigation.ExpandableBottomBarNavigationUI
import kotlinx.android.synthetic.main.content_main.*
import www.digitalexperts.church_tracker.Utils.showPermissionRequestExplanation
import www.digitalexperts.church_traker.R
import www.digitalexperts.church_traker.databinding.ActivityIndexBinding

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.M)
class index : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private  lateinit var navController: NavController
     val TOPIC="Alertstwo"

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_index)
        val binding= ActivityIndexBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AudienceNetworkAds.initialize(this)
        MobileAds.initialize(this) { }

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)




        /*val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)*/
         val navView: NavigationView = findViewById(R.id.nav_view)
          navView.setItemIconTintList(null)

        val navHostFragment=supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController=navHostFragment.findNavController()
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.live, R.id.pdfs, R.id.teachings
            ), binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        ExpandableBottomBarNavigationUI.setupWithNavController(expandable_bottom_bar, navController);





        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.visitationa -> {
                    val c = "https://repentanceandholinessinfo.com/visitations.php"
                    navController.navigate(R.id.wvinfo, bundleOf("web" to c))
                }
                R.id.nav_jsl -> {
                    navController.navigate(R.id.live)
                }
                R.id.propheciesa -> {
                    val c = "https://repentanceandholinessinfo.com/prophecies.php"
                    navController.navigate(R.id.wvinfo, bundleOf("web" to c))
                }
                R.id.nav_twitter -> {
                    val c = "https://repentanceandholinessinfo.com/twitter.php"
                    navController.navigate(R.id.wvinfo, bundleOf("web" to c))
                }
                R.id.nav_fb -> {
                    val c = "https://web.facebook.com/jesusiscomingofficial/?_rdc=1&_rdr"
                    navController.navigate(R.id.wvinfo, bundleOf("web" to c))
                }
                R.id.nav_tools -> {
                    navController.navigate(R.id.contactz)
                }
                R.id.nav_insta -> {
                    val c = "https://www.instagram.com/jesusiscoming_2/"
                    navController.navigate(R.id.wvinfo, bundleOf("web" to c))
                }
                R.id.healings -> {
                    navController.navigate(R.id.healingsfrag)
                }
                R.id.nav_share -> {
                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        "Repentance and Holiness android app on playstore\n https://play.google.com/store/apps/details?id=www.digitalexperts.church_traker&hl=en"
                    )
                    sendIntent.type = "text/plain"
                    startActivity(sendIntent)
                }

                /*else -> false*/
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            //This is for maintaining the behavior of the Navigation view
            NavigationUI.onNavDestinationSelected(it, navController)
            true
        }
 //comfirm if  permission was given

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                 if (!granted) {
                     Toast.makeText(this, "Storage Permission NOT Granted", Toast.LENGTH_SHORT).show()
                     requestStoragePermission()
                }
            }
        requestStoragePermission()
    }
    //asking for permission
    private fun requestStoragePermission(){
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                // The permission is granted
                // you can go with the flow that requires permission here
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                // This case means user previously denied the permission
                // So here we can display an explanation to the user
                // That why exactly we need this permission
                showPermissionRequestExplanation(
                    getString(R.string.write_storage),
                    getString(R.string.permission_request)
                ) { requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) }
            }
            else -> {
                // Everything is fine you can simply request the permission
                requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.index, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=www.digitalexperts.church_traker")
            )
            val title = "Complete Action Using"
            val chooser = Intent.createChooser(intent, title)
            startActivity(chooser)
            return true
        }

        return super.onOptionsItemSelected(item)
    }
/*override fun onOptionsItemSelected(item: MenuItem): Boolean {
    val navController = findNavController(R.id.nav_host_fragment)
    return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
}*/

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}