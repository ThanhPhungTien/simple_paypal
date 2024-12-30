import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:simple_paypal/simple_paypal_method_channel.dart';

void main() {
  MethodChannelSimplePaypal platform = MethodChannelSimplePaypal();
  const MethodChannel channel = MethodChannel('simple_paypal');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });
}
