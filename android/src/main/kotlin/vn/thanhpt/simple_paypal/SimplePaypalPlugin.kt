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


    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "simple_paypal")
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {

            Method.INIT_PAYPAL.value -> {
                Log.d("SimplePaypalPlugin", "onMethodCall: initPaypal")
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
                Log.d("SimplePaypalPlugin", "onMethodCall: openPaypal")

                if (call.arguments == null) {
                    result.error("INVALID_ARGUMENTS", "Invalid arguments", null)
                    return
                }

                val orderId = call.arguments as String

                val intent = Intent(context, MyPaymentsActivity::class.java)
                intent.putExtra("ClientId", initDataPaypal.clientId)
                intent.putExtra("Environment", initDataPaypal.environment)
                intent.putExtra("OrderId", orderId)
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity?.startActivityForResult(intent, 1001)

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
        activity = binding.activity
        binding.addActivityResultListener(this)
    }

    override fun onDetachedFromActivityForConfigChanges() {
        activity = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activity = binding.activity
        binding.addActivityResultListener(this)
    }

    override fun onDetachedFromActivity() {
        activity = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        Log.d(tag, "onActivityResult: $resultCode $data $requestCode")
        when (requestCode) {
            1001 -> {
                if (resultCode == Activity.RESULT_OK){
                    Log.d(tag, "onActivityResult: RESULT_OK")
                } else if (resultCode == Activity.RESULT_CANCELED){
                    Log.d(tag, "onActivityResult: RESULT_CANCELED")
                }
            }
        }
        return false
    }


}
