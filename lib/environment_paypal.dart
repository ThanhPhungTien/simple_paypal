enum EnvironmentPaypal { sandbox, live }

extension EnvironmentPaypalExtension on EnvironmentPaypal {
  String get value {
    switch (this) {
      case EnvironmentPaypal.sandbox:
        return 'SANDBOX';
      case EnvironmentPaypal.live:
        return 'LIVE';
    }
  }
}
