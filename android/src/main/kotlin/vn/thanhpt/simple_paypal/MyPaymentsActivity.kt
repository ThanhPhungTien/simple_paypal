package vn.thanhpt.simple_paypal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.paypal.android.corepayments.CoreConfig
import com.paypal.android.corepayments.Environment
import com.paypal.android.corepayments.PayPalSDKError
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutClient
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutFundingSource
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutListener
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutRequest
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutResult

class MyPaymentsActivity : FragmentActivity(), PayPalWebCheckoutListener {

    val TAG = "MyPaymentsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val clientId = intent.getStringExtra("ClientId") ?: ""
        val orderId = intent.getStringExtra("OrderId") ?: ""

        val config = CoreConfig(
            clientId = clientId,
            environment = Environment.SANDBOX,
        )

        Log.d(TAG, "onCreate: $clientId $orderId")

        val payPalWebCheckoutClient = PayPalWebCheckoutClient(
            activity = this,
            configuration = config,
            urlScheme = "simple_paypal://paypalpay",
        )

        val request = PayPalWebCheckoutRequest(
            orderId = orderId,
            fundingSource = PayPalWebCheckoutFundingSource.PAYPAL,
        )

        payPalWebCheckoutClient.listener = this

        payPalWebCheckoutClient.start(request)

    }


    override fun onNewIntent(newIntent: Intent?) {
        super.onNewIntent(intent)
        intent = newIntent
    }

    override fun onPayPalWebCanceled() {
        Log.d(TAG, "onPayPalWebCanceled: ")
        setResult(RESULT_OK)
        finish()
    }

    override fun onPayPalWebFailure(error: PayPalSDKError) {
        Log.d(TAG, "onPayPalWebFailure: $error")
    }

    override fun onPayPalWebSuccess(result: PayPalWebCheckoutResult) {
        Log.d(TAG, "onPayPalWebSuccess: $result")
    }

}