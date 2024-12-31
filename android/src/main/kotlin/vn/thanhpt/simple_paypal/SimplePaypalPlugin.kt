package vn.thanhpt.simple_paypal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.paypal.android.corepayments.Environment
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry


/** SimplePaypalPlugin */
class SimplePaypalPlugin : FlutterPlugin, MethodCallHandler, ActivityAware,
    PluginRegistry.ActivityResultListener {

    private lateinit var channel: MethodChannel

    private lateinit var context: Context

    private var activity: Activity? = null

    private val tag = "SimplePaypalPlugin"


    private var initDataPaypal: InitDataPaypal = InitDataPaypal(
        clientId = "",
        environment = Environment.SANDBOX,
    )


    private var result: Result? = null


    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "simple_paypal")
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        this.result = result
        when (call.method) {

            Method.INIT_PAYPAL.value -> {
                if (call.arguments == null) {
                    result.error("INVALID_ARGUMENTS", "Invalid arguments", null)
                    return
                }
                val initData = call.arguments as List<*>
                initDataPaypal = InitDataPaypal(
                    clientId = initData[0] as String,
                    environment = EnvironmentPaypal.toPaypalEnvironment(initData[1] as String)
                )
            }

            Method.OPEN_PAYPAL.value -> {

                if (call.arguments == null) {
                    result.error("INVALID_ARGUMENTS", "Invalid arguments", null)
                    return
                }

                val orderId = call.arguments as String

                val intent = Intent(context, MyPaymentsActivity::class.java)
                intent.putExtra(Constant.CLIENT_ID, initDataPaypal.clientId)
                intent.putExtra(Constant.ENVIRONMENT, initDataPaypal.environment)
                intent.putExtra(Constant.ORDER_ID, orderId)
                activity?.startActivityForResult(intent, Constant.PAYPAL_REQUEST_CODE)

            }

            else -> {
                result.notImplemented()
            }
        }
    }


    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        binding.addActivityResultListener(this)
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        activity = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivity() {
        activity = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        Log.d(tag, "onActivityResult: $resultCode $data $requestCode")
        when (requestCode) {
            Constant.PAYPAL_REQUEST_CODE -> {
                when (resultCode) {
                    PaypalResultCode.SUCCESS -> {
                        val dataResult = mapOf(
                            Constant.ORDER_ID to data?.getStringExtra(Constant.ORDER_ID),
                            Constant.PAYER_ID to data?.getStringExtra(Constant.PAYER_ID)
                        )
                        Log.d(tag, "onActivityResult: ${result != null} $dataResult")

                        result?.success(data?.getStringExtra(Constant.PAYER_ID))
                    }

                    PaypalResultCode.FAILURE -> {
                        if (data != null) {
                            val code = data.getIntExtra(Constant.CODE, 0).toString()
                            val errorDescription = data.getStringExtra(Constant.ERROR_DESCRIPTION)
                            val correlationId = data.getStringExtra(Constant.CORRELATION_ID)
                            Log.d(tag, "onActivityResult: $code $errorDescription $correlationId")
                            result?.error(
                                code,
                                errorDescription,
                                correlationId,
                            )
                        }
                    }

                    PaypalResultCode.CANCELED -> {
                        result?.success(null)
                    }
                }
            }
        }
        return false
    }


}
