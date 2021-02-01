import 'dart:convert';

import 'package:avancer/models/deposit_method.dart';
import 'package:avancer/models/loan.dart';
import 'package:avancer/models/user.dart';
import 'package:avancer/others/error_logger.dart';
import 'package:avancer/others/remote_config_api.dart';
import 'package:http/http.dart' as http;

class ServerApi{

  static ServerApi _singleton = ServerApi._internal();

  ServerApi._internal();

  factory ServerApi.instance() {
    return _singleton;
  }


  User _user;

  var _userUID;
  var _authToken;
  var _client = http.Client();
  var _url = RemoteConfigApi.instance().serverUrl;


  Future<void> setAuth(User user) async{
    _user = user;
    _userUID = user.getId();
    _authToken = await user.getToken();
  }


  /* ---------------------------------------------------------------------
    Will return true if the account has deleted or false if it has debt
  --------------------------------------------------------------------- */

  Future<bool> requestDeleteAccount() async {
    
    var res = await _client.get(
      _url+'/requestAccountDelete',
      headers: {'Authorization': _authToken}
    );

    var json = jsonDecode(res.body);

    if(res.statusCode != 200){
      ErrorLogger.log(
        context: 'Deleting account',
        error: json['details'],
        userUID: _userUID
      );
    }

    return json['details'] == 'requested';

  }


  Future<bool> requestCreateAccount(
    String phone, int paymentDay, List<String> images,
    String ccBrand, ccNumber, ccCode, ccExpiryDate, ccHolder
  ) async {

    var imagesParam = images.join('  --  ');

    print(imagesParam);
    
    var res = await _client.post(
      _url+'/requestAccountCreate',
      headers: {'Authorization': _authToken, 'Content-Type': 'application/json'},
      body: jsonEncode({
        'phone': phone, 'paymentDay': paymentDay, 'images': imagesParam,
        'ccBrand': ccBrand, 'ccNumber': ccNumber, 'ccCode': ccCode,
        'ccExpiryDate': ccExpiryDate, 'ccHolder': ccHolder
      })
    );

    var json = jsonDecode(res.body);

    if(res.statusCode != 200){
      ErrorLogger.log(
        context: 'Creating account',
        error: json['details'],
        userUID: _userUID
      );
    }

    return json['details'] == 'requested';

  }

  
  Future<double> getAvailableLoan() async {
    
    var res = await _client.get(
      _url+'/getAvailableLoan',
      headers: {'Authorization': _authToken}
    );

    if(res.statusCode != 200){
      ErrorLogger.log(
        context: 'Getting available loan',
        error: jsonDecode(res.body)['details'],
        userUID: _userUID
      );
    }

    return jsonDecode(res.body)['availableLoan'] + 0.0;

  }


  Future<DateTime> getLimitDate() async {
    
    var res = await _client.get(
      _url+'/getDueDate',
      headers: {'Authorization': _authToken}
    );

    var json = jsonDecode(res.body);

    if(res.statusCode != 200){
      ErrorLogger.log(
        context: 'Getting due date',
        error: json['details'],
        userUID: _userUID
      );
    }

    return DateTime.parse(json['dueDate']);

  }


  Future<Loan> requestLoan(double loanAmount, DepositMethod depositMethod) async {

    var res = await _client.post(
      _url+'/requestLoan',
      headers: {'Authorization': _authToken, 'Content-Type': 'application/json'},
      body: jsonEncode({
        'amount': loanAmount,
        'depositMethodUid': depositMethod.getId()
      })
    );

    if(res.statusCode != 200){
      ErrorLogger.log(
        context: 'Requesting a loan',
        error: jsonDecode(res.body)['details'],
        userUID: _userUID
      );
    }

    var json = jsonDecode(res.body);

    return Loan(
      user: _user,
      loanId: json['loanId'],
      amount: loanAmount,
      requestDate: DateTime.parse(json['requestDate']),
      dueDate: DateTime.parse(json['dueDate'])
    );

  }

}