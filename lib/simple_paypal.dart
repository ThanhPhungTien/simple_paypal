
import 'simple_paypal_platform_interface.dart';

class SimplePaypal {
  Future<String?> getPlatformVersion() {
    return SimplePaypalPlatform.instance.getPlatformVersion();
  }
}
