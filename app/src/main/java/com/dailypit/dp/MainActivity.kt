package com.dailypit.dp

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.dailypit.dp.Adapter.AddOnServiceAdapter
import com.dailypit.dp.Fragment.*
import com.dailypit.dp.Fragment.Wallet.addPoints
import com.dailypit.dp.Interface.IOnBackPressed
import com.dailypit.dp.Interface.ServiceInterface
import com.dailypit.dp.Model.AddOnService.AddOnServiceResponse
import com.dailypit.dp.Model.AddOnService.AddOnServiceResponseData
import com.dailypit.dp.Model.AddOnService.AddOnServicesCnfResponse
import com.dailypit.dp.Utils.ApiClient
import com.dailypit.dp.Utils.DatabaseHandler
import com.dailypit.dp.Utils.PackageDB
import com.dailypit.dp.Utils.YourPreference
import com.facebook.FacebookSdk
import com.facebook.LoggingBehavior
import com.facebook.appevents.AppEventsLogger
import com.razorpay.PaymentResultListener
import com.sanojpunchihewa.updatemanager.UpdateManager
import com.sanojpunchihewa.updatemanager.UpdateManagerConstant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity(), PaymentResultListener {

    companion object MainObject {
        var isFromActivity = true
    }

    var databaseHandler: DatabaseHandler? = null
    var databaseHandler2: PackageDB? = null
    var doubleBackToExitPressedOnce = false
    var toolbar: Toolbar? = null
    var footer: View? = null
    var ishome: Boolean? = true
    var img_home: ImageView? = null
    var img_myOrder: ImageView? = null
    var img_profile: ImageView? = null
    var img_wallet: ImageView? = null
    var img_referAndEarn: ImageView? = null
    var mUpdateManager: UpdateManager? = null
    var main_nav_host: FrameLayout? = null
    var logger: AppEventsLogger? = null
    var addOnServiceResponseData: List<AddOnServiceResponseData> = ArrayList()
    var add_newServiceRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(application)
        FacebookSdk.setIsDebugEnabled(true)
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS)
        toolbar = findViewById(R.id.toolbar)
        footer = findViewById(R.id.footer)
        img_home = findViewById(R.id.img_home)
        img_myOrder = findViewById(R.id.img_myOrder)
        img_profile = findViewById(R.id.img_profile)
        img_wallet = findViewById(R.id.img_wallet)
        img_referAndEarn = findViewById(R.id.img_referAndEarn)
        main_nav_host = findViewById(R.id.main_nav_host)
        logger = AppEventsLogger.newLogger(this)

        setUpDB()
        setUppackageDB()

        val appBarConfiguration = AppBarConfiguration(
            setOf(

            )
        )

        mUpdateManager = UpdateManager.Builder(this).mode(UpdateManagerConstant.IMMEDIATE)
        mUpdateManager!!.start()

        val yourPrefrence = YourPreference.getInstance(applicationContext)
        yourPrefrence.saveData("COUPON", "0")
        yourPrefrence.saveData("TOTAL", "")
        yourPrefrence.saveData("COUPONCHARGE", "")
        yourPrefrence.saveData("COUPONCODE", "")
        databaseHandler!!.cartInterface().deleteall()
        databaseHandler2!!.packageInterface().deleteallpackage()
        val preferences = getSharedPreferences("cart", 0)
        val preferences2 = getSharedPreferences("package", 0)

        preferences.edit().clear().commit()
        preferences2.edit().clear().commit()

        getHome()


        if (intent.extras != null) {
            val o_id = intent.extras!!.getString("order_id")
            val addressType = intent.extras!!.getString("notificationType")
            if (addressType!!.equals("addon_confirmation", true)) {
                if (o_id != null) {
                    openAcceptanceDialog(o_id, addressType)
                }
            }
        }
    }

    private fun setUpDB() {
        databaseHandler = Room.databaseBuilder(this, DatabaseHandler::class.java, "cart").allowMainThreadQueries().build()
    }

    private fun setUppackageDB() {
        databaseHandler2 = Room.databaseBuilder(this, PackageDB::class.java, "package").allowMainThreadQueries().build()
    }

    override fun onBackPressed() {
        if (ishome == true) {
            val fragment = supportFragmentManager.findFragmentById(R.id.main_nav_host)
            if (fragment !is IOnBackPressed || !(fragment as IOnBackPressed).onBackPressed()) {
                if (doubleBackToExitPressedOnce) {
                    finishAffinity()
                    System.exit(0)

                }

                this.doubleBackToExitPressedOnce = true
                Toast.makeText(this, "Press again to close DailyPit", Toast.LENGTH_SHORT).show()
                Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
            }

        } else {
            ishome = true
            val fragmenthome = HomeFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_nav_host, fragmenthome)
            transaction.commitNow()

            img_profile!!.setImageDrawable(
                    ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_profile_inactive
                    )
            )
            img_wallet!!.setImageDrawable(
                    ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_wallet_inactive
                    )
            )
            img_home!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_home_active))
            img_referAndEarn!!.setImageDrawable(
                    ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_referand_earn
                    )
            )
            img_myOrder!!.setImageDrawable(
                    ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_orders_inactive
                    )
            )

        }
    }

    override fun onResume() {
        super.onResume()

        if (!isFromActivity) {
                val fragmenthome = MyOrderFragment()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.main_nav_host, fragmenthome)
                main_nav_host!!.removeAllViews()
                transaction.commitNow()
                ishome = false
                isFromActivity = true

                img_profile!!.setImageDrawable(
                        ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_profile_inactive
                        )
                )
                img_wallet!!.setImageDrawable(
                        ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_wallet_inactive
                        )
                )
                img_home!!.setImageDrawable(
                        ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_home_inactive
                        )
                )
                img_referAndEarn!!.setImageDrawable(
                        ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_referand_earn
                        )
                )
                img_myOrder!!.setImageDrawable(
                        ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_orders_active
                        )
                )
            }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        try {
            if (razorpayPaymentID!!.isEmpty()) {
                Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show()
            } else {
                val yourPrefrence = YourPreference.getInstance(FacebookSdk.getApplicationContext())
                val user_id = yourPrefrence.getData("USERID")
                addPoints(user_id, razorpayPaymentID)
            }
        } catch (e: Exception) {
            Log.e("cds", "Error in starting Razorpay Checkout", e)
        }
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show()
    }

    fun profile(view: View) {
        val fragmenthome = MyProfileFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_nav_host, fragmenthome)
        main_nav_host!!.removeAllViews()
        transaction.commitNow()
        ishome = false

        img_profile!!.setImageDrawable(
                ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_profile_active
                )
        )
        img_wallet!!.setImageDrawable(
                ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_wallet_inactive
                )
        )
        img_home!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_home_inactive))
        img_referAndEarn!!.setImageDrawable(
                ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_referand_earn
                )
        )
        img_myOrder!!.setImageDrawable(
                ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_orders_inactive
                )
        )

    }

    fun wallet(view: View) {
        val fragmenthome = Wallet()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_nav_host, fragmenthome)
        main_nav_host!!.removeAllViews()
        transaction.commitNow()
        ishome = false

        img_profile!!.setImageDrawable(
                ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_profile_inactive
                )
        )
        img_wallet!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_wallet_active))
        img_home!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_home_inactive))
        img_referAndEarn!!.setImageDrawable(
                ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_referand_earn
                )
        )
        img_myOrder!!.setImageDrawable(
                ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_orders_inactive
                )
        )

    }

    fun order(view: View) {
        val fragmenthome = MyOrderFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_nav_host, fragmenthome)
        main_nav_host!!.removeAllViews()
        transaction.commitNow()
        ishome = false

        img_profile!!.setImageDrawable(
                ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_profile_inactive
                )
        )
        img_wallet!!.setImageDrawable(
                ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_wallet_inactive
                )
        )
        img_home!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_home_inactive))
        img_referAndEarn!!.setImageDrawable(
                ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_referand_earn
                )
        )
        img_myOrder!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_orders_active))

    }

    fun referAndEarn(view: View) {
        val fragmenthome = RefundandEarnFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_nav_host, fragmenthome)
        main_nav_host!!.removeAllViews()
        transaction.commitNow()
        ishome = false

        img_profile!!.setImageDrawable(
                ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_profile_inactive
                )
        )
        img_wallet!!.setImageDrawable(
                ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_wallet_inactive
                )
        )
        img_home!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_home_inactive))
        img_referAndEarn!!.setImageDrawable(
                ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_referand_earn_active
                )
        )
        img_myOrder!!.setImageDrawable(
                ContextCompat.getDrawable(
                        this,

                        R.drawable.ic_orders_inactive
                )
        )
    }

    fun img_home(view: View) {
        val fragmenthome = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_nav_host, fragmenthome)
        main_nav_host!!.removeAllViews()
        transaction.commitNow()
        ishome = true
        img_profile!!.setImageDrawable(
                ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_profile_inactive
                )
        )
        img_wallet!!.setImageDrawable(
                ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_wallet_inactive
                )
        )
        img_home!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_home_active))
        img_referAndEarn!!.setImageDrawable(
                ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_referand_earn
                )
        )
        img_myOrder!!.setImageDrawable(
                ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_orders_inactive
                )
        )
    }

    private fun getHome() {
        val fragmenthome = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_nav_host, fragmenthome)
        main_nav_host!!.removeAllViews()
        transaction.commitNow()
        ishome = true
        img_profile!!.setImageDrawable(
                ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_profile_inactive
                )
        )
        img_wallet!!.setImageDrawable(
                ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_wallet_inactive
                )
        )
        img_home!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_home_active))
        img_referAndEarn!!.setImageDrawable(
                ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_referand_earn
                )
        )
        img_myOrder!!.setImageDrawable(
                ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_orders_inactive
                )
        )
    }

    fun openAcceptanceDialog(o_id: String, msg: String) {

        if (msg == "addon_confirmation") {
            val map = HashMap<String, String>()
            map["p_order_id"] = o_id
            val serviceInterface = ApiClient.getClient().create(ServiceInterface::class.java)
            val call = serviceInterface.addNewServices(map)
            call.enqueue(object : Callback<AddOnServiceResponse> {
                override fun onResponse(call: Call<AddOnServiceResponse>, response: Response<AddOnServiceResponse>) {
                    if (response.isSuccessful) {
                        val status = response.body()!!.status.toString()
                        if (status == "1") {
                            addOnServiceResponseData = response.body()!!.data
                            bindData(addOnServiceResponseData, response.body()!!.discountAmount, response.body()!!.netAmount, o_id)
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Something is wrong try again later", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AddOnServiceResponse>, t: Throwable) {
                    Log.d("ff", t.toString())
                }
            })
        }


    }

    private fun bindData(addOnServiceResponseData: List<AddOnServiceResponseData>, discountAmount: String, netAmount: String, o_id: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.add_on_confermation_layout)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        val txt_order_id = dialog.findViewById<TextView>(R.id.txt_order_id)
        val txt_discountMoney = dialog.findViewById<TextView>(R.id.txt_discountMoney)
        val txt_totalAMount = dialog.findViewById<TextView>(R.id.txt_totalAMount)
        val txt_yes = dialog.findViewById<TextView>(R.id.txt_yes)
        val txt_no = dialog.findViewById<TextView>(R.id.txt_no)
        txt_discountMoney.text = discountAmount
        txt_totalAMount.text = netAmount
        txt_order_id.text = "ORER ID: $o_id"
        add_newServiceRecyclerView = dialog.findViewById(R.id.add_newServiceRecyclerView)
        val adapter = AddOnServiceAdapter(addOnServiceResponseData)
        add_newServiceRecyclerView!!.setHasFixedSize(true)
        add_newServiceRecyclerView!!.setLayoutManager(LinearLayoutManager(FacebookSdk.getApplicationContext()))
        add_newServiceRecyclerView!!.setAdapter(adapter)
        adapter.notifyDataSetChanged()
        txt_yes.setOnClickListener {
            getConfirmation(o_id, "Confirmed", dialog)
        }
        txt_no.setOnClickListener {
            dialog.dismiss()
            getConfirmationNo(o_id, "Cancel", dialog)
        }
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.show()
    }

    private fun getConfirmation(o_id: String, status: String, dialog1: Dialog) {
        val map = HashMap<String, String>()
        map["p_order_id"] = o_id
        map["addon_status"] = status
        val serviceInterface = ApiClient.getClient().create(ServiceInterface::class.java)
        val call = serviceInterface.addNewServicesCnf(map)
        call.enqueue(object : Callback<AddOnServicesCnfResponse> {
            override fun onResponse(call: Call<AddOnServicesCnfResponse>, response: Response<AddOnServicesCnfResponse>) {
                if (response.isSuccessful) {
                    val status = response.body()!!.status.toString()
                    if (status == "1") {
                        dialog1.dismiss()

                        val dialog = Dialog(this@MainActivity)
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialog.setContentView(R.layout.add_on_services_confirmation)
                        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        dialog.setCancelable(false)

                        val btn_addonTextConfirmation = dialog.findViewById<TextView>(R.id.btn_addonTextConfirmation)
                        btn_addonTextConfirmation!!.setOnClickListener {

                            dialog.dismiss()
                            val fragmenthome = MyOrderFragment()
                            val transaction = supportFragmentManager.beginTransaction()
                            transaction.replace(R.id.main_nav_host, fragmenthome)
                            main_nav_host!!.removeAllViews()
                            transaction.commitNow()

                            ishome = false

                            img_profile!!.setImageDrawable(
                                    ContextCompat.getDrawable(
                                            this@MainActivity,
                                            R.drawable.ic_profile_inactive
                                    )
                            )
                            img_wallet!!.setImageDrawable(
                                    ContextCompat.getDrawable(
                                            this@MainActivity,
                                            R.drawable.ic_wallet_inactive
                                    )
                            )
                            img_home!!.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_home_inactive))
                            img_referAndEarn!!.setImageDrawable(
                                    ContextCompat.getDrawable(
                                            this@MainActivity,
                                            R.drawable.ic_referand_earn
                                    )
                            )
                            img_myOrder!!.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_orders_active))

                        }

                        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                        dialog.show()

                    }
                } else {
                    Toast.makeText(this@MainActivity, "Something is wrong try again later", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddOnServicesCnfResponse>, t: Throwable) {
                Log.d("ff", t.toString())
            }
        })
    }

    private fun getConfirmationNo(o_id: String, status: String, dialog1: Dialog) {
        val map = HashMap<String, String>()
        map["p_order_id"] = o_id
        map["addon_status"] = status
        val serviceInterface = ApiClient.getClient().create(ServiceInterface::class.java)
        val call = serviceInterface.addNewServicesCnf(map)
        call.enqueue(object : Callback<AddOnServicesCnfResponse> {
            override fun onResponse(call: Call<AddOnServicesCnfResponse>, response: Response<AddOnServicesCnfResponse>) {
                if (response.isSuccessful) {
                    val status = response.body()!!.status.toString()
                    if (status == "1") {
                        dialog1.dismiss()

                        val dialog = Dialog(this@MainActivity)
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialog.setContentView(R.layout.add_on_confirmation_no)
                        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        dialog.setCancelable(false)

                        val btn_addonTextConfirmation = dialog.findViewById<TextView>(R.id.btn_addonTextConfirmation)
                        btn_addonTextConfirmation!!.setOnClickListener {
                            dialog.dismiss()
                        }

                        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                        dialog.show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Something is wrong try again later", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddOnServicesCnfResponse>, t: Throwable) {
                Log.d("ff", t.toString())
            }
        })
    }

}

