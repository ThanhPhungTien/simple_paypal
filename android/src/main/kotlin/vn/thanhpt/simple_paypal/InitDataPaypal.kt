package vn.thanhpt.simple_paypal

import com.paypal.android.corepayments.Environment

/**
 * Init data
 * [clientId] : client id from paypal
 * [environment] [Environment.SANDBOX] or [Environment.LIVE]
 **/

data class InitDataPaypal(
    var clientId: String,
    var environment: Environment,
    )
