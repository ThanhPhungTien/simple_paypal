import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'simple_paypal_method_channel.dart';

abstract class SimplePaypalPlatform extends PlatformInterface {
  /// Constructs a SimplePaypalPlatform.
  SimplePaypalPlatform() : super(token: _token);

  static final Object _token = Object();

  static SimplePaypalPlatform _instance = MethodChannelSimplePaypal();

  /// The default instance of [SimplePaypalPlatform] to use.
  ///
  /// Defaults to [MethodChannelSimplePaypal].
  static SimplePaypalPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [SimplePaypalPlatform] when
  /// they register themselves.
  static set instance(SimplePaypalPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
