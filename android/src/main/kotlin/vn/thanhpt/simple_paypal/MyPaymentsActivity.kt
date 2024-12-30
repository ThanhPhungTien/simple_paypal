package vn.thanhpt.simple_paypal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.paypal.android.corepayments.CoreConfig
import com.paypal.android.corepayments.Environment
import com.paypal.android.corepayments.PayPalSDKError
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutClient
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutFundingSource
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutListener
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutRequest
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutResult


class MyPaymentsActivity : AppCompatActivity() {

    private val tag = "MyPaymentsActivity"

    private lateinit var paypalClient: PayPalWebCheckoutClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide the app bar
        supportActionBar?.hide()

        setContentView(R.layout.activity_my_payments)

        val buttonBack = findViewById<Button>(R.id.buttonBack)

        buttonBack.setOnClickListener {
            finish()
        }


        val clientId = intent.getStringExtra("ClientId") ?: ""
        val orderId = intent.getStringExtra("OrderId") ?: ""
        val environment = intent.getSerializableExtra("Environment") as Environment

        val config = CoreConfig(
            clientId = clientId,
            environment = environment,
        )

        Log.d(tag, "onCreate: $clientId $orderId $environment")

        paypalClient = PayPalWebCheckoutClient(
            activity = this,
            configuration = config,
            urlScheme = "vn.thanhpt.simple.paypal",
        )

        paypalClient.listener = object : PayPalWebCheckoutListener {

            override fun onPayPalWebCanceled() {
                Log.d(tag, "onPayPalWebCanceled: ")

            }

            override fun onPayPalWebFailure(error: PayPalSDKError) {
                Log.d(tag, "onPayPalWebFailure: $error")
            }

            override fun onPayPalWebSuccess(result: PayPalWebCheckoutResult) {
                Log.d(tag, "onPayPalWebSuccess: $result")
                val intent = Intent()
                intent.putExtra("orderId", result.orderId)
                intent.putExtra("payerId", result.payerId)
                setResult(RESULT_OK, intent)
                finish()
            }


        }

        val request = PayPalWebCheckoutRequest(
            orderId = orderId,
            fundingSource = PayPalWebCheckoutFundingSource.PAYPAL,
        )

        paypalClient.start(request)

    }


    override fun onNewIntent(newIntent: Intent?) {
        super.onNewIntent(intent)
        intent = newIntent
    }

}