import 'dart:convert';

import 'package:avancer/models/deposit_method.dart';
import 'package:avancer/models/loan.dart';
import 'package:avancer/others/error_logger.dart';
import 'package:avancer/others/remote_config_api.dart';
import 'package:http/http.dart' as http;

class ServerApi{

  static ServerApi _singleton = ServerApi._internal();

  ServerApi._internal();

  factory ServerApi.instance() {
    return _singleton;
  }


  var _userUID;
  var _authToken;
  var _client = http.Client();
  var _url = RemoteConfigApi.instance().serverUrl;


  void setAuth(String userUID, String token){
    _userUID = userUID;
    _authToken = token;
  }


  /* ---------------------------------------------------------------------
    Will return true if the account has deleted or false if it has debt
  --------------------------------------------------------------------- */

  Future<bool> deleteAccount() async {
    
    var res = await _client.get(
      _url+'/deleteAccount',
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

    return json['details'] == 'deleted';

  }

  
  Future<double> getAvailableLoan() async {
    
    var res = await _client.get(
      _url+'/availableLoan',
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


  Future<Map<String, dynamic>> getLoanRequestConditions(double amount) async {
    
    var res = await _client.get(
      _url+'/loanRequestConditions?amount=$amount',
      headers: {'Authorization': _authToken}
    );

    var json = jsonDecode(res.body);

    if(res.statusCode != 200){
      ErrorLogger.log(
        context: 'Getting loan interest',
        error: json['details'],
        userUID: _userUID
      );
    }

    return {
      'dueAmount': json['dueAmount'] + 0.0,
      'dueDate': DateTime.parse(json['dueDate'])
    };

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
      loanId: json['loanId'],
      amount: loanAmount,
      dueAmount: json['dueAmount'] + 0.0,
      requestDate: DateTime.parse(json['requestDate']),
      dueDate: DateTime.parse(json['dueDate'])
    );

  }

}