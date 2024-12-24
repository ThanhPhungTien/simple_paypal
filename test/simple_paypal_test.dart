import 'package:flutter_test/flutter_test.dart';
import 'package:simple_paypal/simple_paypal.dart';
import 'package:simple_paypal/simple_paypal_platform_interface.dart';
import 'package:simple_paypal/simple_paypal_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockSimplePaypalPlatform
    with MockPlatformInterfaceMixin
    implements SimplePaypalPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final SimplePaypalPlatform initialPlatform = SimplePaypalPlatform.instance;

  test('$MethodChannelSimplePaypal is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelSimplePaypal>());
  });

  test('getPlatformVersion', () async {
    SimplePaypal simplePaypalPlugin = SimplePaypal();
    MockSimplePaypalPlatform fakePlatform = MockSimplePaypalPlatform();
    SimplePaypalPlatform.instance = fakePlatform;

    expect(await simplePaypalPlugin.getPlatformVersion(), '42');
  });
}
