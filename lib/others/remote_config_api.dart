import 'package:firebase_remote_config/firebase_remote_config.dart';

class RemoteConfigApi {

  static RemoteConfigApi _singleton = RemoteConfigApi._internal();

  RemoteConfigApi._internal();

  factory RemoteConfigApi.instance() {
    return _singleton;
  }

  RemoteConfig rc;

  Future<void> initialize() async {
    rc = await RemoteConfig.instance;

    await rc.fetch(expiration: Duration(minutes: 10));
    await rc.activateFetched();
  }

  // ------------------------------------------ SERVER ------------------------------------------

  String get serverUrl => rc.getString('server_url');

  // ------------------------------------------ LOAN REQUIRING ------------------------------------------

  double get minLoanAmount => rc.getDouble('min_loan_amount');

  double get lowInterest => rc.getDouble('loan_interest');

  double get salaryFraction => rc.getDouble('salary_fraction');

  // ------------------------------------------ PAYMENT ------------------------------------------

  String get bankCBU => rc.getString('bank_cbu');

  String get bankAlias => rc.getString('bank_alias');

  String get mpEmail => rc.getString('mp_email');

  // ------------------------------------------ CONTACT ------------------------------------------

  String get contactEmail => rc.getString('contact_email');

  // ------------------------------------------ OTHERS ------------------------------------------

  String get lastMandatoryVersion => rc.getString('last_mandatory_version');

}