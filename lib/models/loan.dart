import 'dart:async';
import 'package:avancer/others/server_api.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:avancer/models/deposit_method.dart';

enum LoanState{REQUESTED, GRANTED, REPAID}

class Loan implements Comparable{

  // ------------------------------------------------ PROPERTIES ------------------------------------------------

  String _loanId;

  double _amount;
  LoanState _state;

  DateTime _requestDate;
  DateTime _dueDate;
  
  double _dueAmount;


  // ------------------------------------------------ BUILDERS ------------------------------------------------


  Loan({
    String loanId,
    double amount,
    double dueAmount,
    DateTime requestDate,
    DateTime dueDate,
    LoanState state,
  }){
    this._loanId = loanId;

    this._amount = amount;
    this._dueAmount = dueAmount;

    this._requestDate = requestDate;
    this._dueDate = dueDate;

    this._state = state ?? LoanState.REQUESTED;
  }


  static Loan fromSnapshot(DocumentSnapshot snap){
    return new Loan(
      loanId: snap.documentID,
      amount: snap.data['amount'] + 0.0,
      dueAmount: snap.data['dueAmount'] + 0.0,
      requestDate: snap.data['requestDate'].toDate(),
      dueDate: snap.data['dueDate'].toDate(),
      state: strToLoanState(snap.data['state']),
    );
  }


  // ------------------------------------------------ METHODS ------------------------------------------------


  @override
  int compareTo(other) {

    if(!(other is Loan))
      throw ArgumentError("Can't compare with non Loan object");

    return (other as Loan)._requestDate.compareTo(_requestDate);

  }

  static Future<Loan> request(double amount, DepositMethod depositMethod) async{
    return await ServerApi.instance().requestLoan(amount, depositMethod);
  }


  // ------------------------------------------------ GETTERS ------------------------------------------------


  String getLoanId() => _loanId;
  

  LoanState getState() => _state;


  DateTime getRequestDate() => _requestDate;


  double getAmount() => _amount;


  FutureOr<DateTime> getDueDate() {
    if(_dueDate == null){
      return ServerApi.instance().getLoanRequestConditions(_amount).then((res){
        _dueDate = res['dueDate'];
        return _dueDate;
      });
    }

    return _dueDate;
  }


  FutureOr<double> getDueAmount() {
    if(_dueAmount == null){
      return ServerApi.instance().getLoanRequestConditions(_amount).then((res){
        _dueAmount = res['dueAmount'];
        return _dueAmount;
      });
    }

    return _dueAmount;
  }


  // ------------------------------------------------ OTHERS ------------------------------------------------

  static LoanState strToLoanState(String str) => LoanState.values.firstWhere((value) => value.toString() == 'LoanState.'+str.toUpperCase());

}