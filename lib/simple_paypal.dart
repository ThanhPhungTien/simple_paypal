import 'simple_paypal_platform_interface.dart';

class SimplePaypal {
  Future<void> openPaypal({
    required String orderId,
    required String clientId,
  }) async {
    return SimplePaypalPlatform.instance.openPaypal(
      clientId: clientId,
      orderId: orderId,
    );
  }
}
