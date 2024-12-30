import 'package:flutter_test/flutter_test.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:simple_paypal/environment_paypal.dart';
import 'package:simple_paypal/simple_paypal_method_channel.dart';
import 'package:simple_paypal/simple_paypal_platform_interface.dart';

class MockSimplePaypalPlatform
    with MockPlatformInterfaceMixin
    implements SimplePaypalPlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');

  @override
  Future<void> openPaypal({required String orderId}) {
    // TODO: implement openPaypal
    throw UnimplementedError();
  }

  @override
  Future<void> initPaypal({
    required String clientId,
    required EnvironmentPaypal environment,
  }) {
    // TODO: implement initPaypal
    throw UnimplementedError();
  }
}

void main() {
  final SimplePaypalPlatform initialPlatform = SimplePaypalPlatform.instance;

  test('$MethodChannelSimplePaypal is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelSimplePaypal>());
  });

  test('getPlatformVersion', () async {
    MockSimplePaypalPlatform fakePlatform = MockSimplePaypalPlatform();
    SimplePaypalPlatform.instance = fakePlatform;
  });
}
