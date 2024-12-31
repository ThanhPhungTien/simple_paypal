package vn.thanhpt.simple_paypal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

    var hasOpenedPaypal = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide the app bar
        supportActionBar?.hide()

        setContentView(R.layout.activity_my_payments)

        val buttonBack = findViewById<Button>(R.id.buttonBack)

        buttonBack.setOnClickListener {
            setResult(PaypalResultCode.CANCELED)
            finish()
        }


        val clientId = intent.getStringExtra(Constant.CLIENT_ID) ?: ""
        val orderId = intent.getStringExtra(Constant.ORDER_ID) ?: ""
        val environment = intent.getSerializableExtra(Constant.ENVIRONMENT) as Environment

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
                hasOpenedPaypal = false
                Log.d(tag, "onPayPalWebFailure: $error")
                val intent = Intent()
                intent.putExtra(Constant.CODE, error.code)
                intent.putExtra(Constant.ERROR_DESCRIPTION, error.errorDescription)
                intent.putExtra(Constant.CORRELATION_ID, error.correlationId)
                setResult(PaypalResultCode.FAILURE, intent)
                finish()
            }

            override fun onPayPalWebSuccess(result: PayPalWebCheckoutResult) {
                hasOpenedPaypal = false
                Log.d(tag, "onPayPalWebSuccess: $result")
                val intent = Intent()
                intent.putExtra(Constant.ORDER_ID, result.orderId)
                intent.putExtra(Constant.PAYER_ID, result.payerId)
                setResult(PaypalResultCode.SUCCESS, intent)
                finish()
            }
        }

        val request = PayPalWebCheckoutRequest(
            orderId = orderId,
            fundingSource = PayPalWebCheckoutFundingSource.PAYPAL,
        )

        paypalClient.start(request)

    }

    override fun onStop() {
        super.onStop()
        hasOpenedPaypal = true
        Log.d(tag, "onStop: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(tag, "onPause: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(tag, "onResume: $hasOpenedPaypal")
        if (hasOpenedPaypal) {
            Handler(Looper.getMainLooper()).postDelayed({
                if (hasOpenedPaypal) {
                    hasOpenedPaypal = false
                    setResult(PaypalResultCode.CANCELED)
                    finish()
                    Log.d(tag, "finished ")
                }
            }, 200)
        }
    }


    override fun onNewIntent(newIntent: Intent?) {
        super.onNewIntent(intent)
        intent = newIntent
    }

}