import 'package:flutter/material.dart';
import 'package:simple_paypal/environment_paypal.dart';
import 'package:simple_paypal/simple_paypal.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    SimplePaypal.instance.initPaypal(
      clientId:
          'AaJe8QETzPffm2W9z2JPyRWq5pTpTht7A0g7ExOCarL7oud-61j8eg6YeNnFqmJxr1qW6gHy5xHutX47',
      environment: EnvironmentPaypal.sandbox,
    );
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              const SizedBox(height: 16),
              ElevatedButton(
                onPressed: () => _clickPaypal(),
                child: const Text('Paypal'),
              )
            ],
          ),
        ),
      ),
    );
  }

  _clickPaypal() async {}
}
