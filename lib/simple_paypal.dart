import 'dart:developer';

import 'package:flutter/services.dart';
import 'package:simple_paypal/environment_paypal.dart';

import 'simple_paypal_platform_interface.dart';

class SimplePaypal {
  SimplePaypal._privateConstructor();

  static final SimplePaypal _instance = SimplePaypal._privateConstructor();

  static SimplePaypal get instance => _instance;

  Future<void> initPaypal({
    required String clientId,
    required EnvironmentPaypal environment,
  }) async =>
      SimplePaypalPlatform.instance.initPaypal(
        clientId: clientId,
        environment: environment,
      );

  /// Mở Paypal Web
  Future<void> openPaypal({
    required String orderId,
    required Function(String payerId) onSuccess,
    required Function(String errorMessage) onFailure,
  }) async {
    try {
      final result =
          await SimplePaypalPlatform.instance.openPaypal(orderId: orderId);
      onSuccess(result);
    } on PlatformException catch (e) {
      onFailure(e.message ?? '');
    }
  }
}
