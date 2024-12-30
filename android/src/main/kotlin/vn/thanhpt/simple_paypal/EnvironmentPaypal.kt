package vn.thanhpt.simple_paypal

import com.paypal.android.corepayments.Environment

enum class EnvironmentPaypal(val value: String) {
    SANDBOX("SANDBOX"),
    LIVE("LIVE");

    companion object {
        private fun fromString(name: String): EnvironmentPaypal {
            return values().find { it.value.equals(name, ignoreCase = true) } ?: SANDBOX
        }

        fun toPaypalEnvironment(name: String): Environment {

            val env = fromString(name)

            return when (env) {
                SANDBOX -> Environment.SANDBOX
                LIVE -> Environment.LIVE
            }
        }
    }
}
