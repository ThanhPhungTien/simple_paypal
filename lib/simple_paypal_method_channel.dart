import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'environment_paypal.dart';
import 'simple_paypal_platform_interface.dart';

/// An implementation of [SimplePaypalPlatform] that uses method channels.
class MethodChannelSimplePaypal extends SimplePaypalPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('simple_paypal');

  @override
  Future<void> initPaypal({
    required String clientId,
    required EnvironmentPaypal environment,
  }) async =>
      await methodChannel
          .invokeMethod('initPaypal', [clientId, environment.value]);

  @override
  Future<void> openPaypal({required String orderId}) async =>
      await methodChannel.invokeMethod<String>('openPaypal', orderId);
}
